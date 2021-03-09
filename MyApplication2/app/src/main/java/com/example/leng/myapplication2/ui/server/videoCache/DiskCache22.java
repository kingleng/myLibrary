package com.example.leng.myapplication2.ui.server.videoCache;

import com.example.leng.myapplication2.ui.server.videoCache.cache.BlockListFile;
import com.example.leng.myapplication2.ui.server.videoCache.cache.SegmentInfo;
import com.example.leng.myapplication2.ui.server.videoCache.utils.CloseUtil;
import com.example.leng.myapplication2.ui.server.videoCache.utils.Constant;
import com.example.leng.myapplication2.ui.tools.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by leng on 2021/1/21.
 */
public class DiskCache22 {

    private String cachePath;
    private final ExecutorService service = Executors.newFixedThreadPool(1);

    private final int maxSize;

    private final static float TRIM_FACTOR = 0.75f;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock rLock = rwLock.readLock();
    private final Lock wLock = rwLock.writeLock();
    private volatile int curTotalSize;
    private final String NAME_SEPARATOR = "_";

    private final int cacheSlice;

    public DiskCache22(String cachePath, int maxSize) {
        this(cachePath, maxSize, Constant.CACHE_SLICE_5MB);
    }

    public DiskCache22(String cachePath, int maxSize, int cacheSlice) {
        this.cachePath = cachePath;
        this.maxSize = maxSize;
        this.cacheSlice = cacheSlice;
    }

    /***************************** 请求返回头信息缓存 **************************************/

    //缓存 url请求返回的对应头信息
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void cacheHeaders(String host, String url, HttpResponse response) {
        wLock.lock();

        try {
            File file = new File(getHeaderParentFile(host), getTransformedString(url));
            if (file.exists()) {
                file.delete();
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                file.createNewFile();

                String string = response.getHeadText();
                fileOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            wLock.unlock();
        }

    }

    //获取 url请求返回的对应头信息
    public HttpResponse getCacheHeaders(String host, String url) {
        File file = new File(getHeaderParentFile(host), getTransformedString(url));
        if (!file.exists()) {
            return null;
        }

        rLock.lock();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return HttpResponse.parse(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            rLock.unlock();
        }
    }

    //清除缓存 url请求返回的对应头信息
    public void clearCacheHeaders(String host, String url) {
        wLock.lock();

        File file = new File(getHeaderParentFile(host), getTransformedString(url));
        if (file.exists()) {
            file.delete();
        }

        wLock.unlock();
    }

    /******************************  返回视频流数据缓存  *************************************/

    //缓存视频流到文件
    public BlockListFile put(SegmentInfo key, final InputStream inputStream) {
        wLock.lock();

        curTotalSize = getTotalSize(cachePath);
        checkToTrim(getContentRootFile());

        int pendingCacheLength = 0;
        try {
            checkToCombine(key);
            final List<SegmentInfo> keys = new ArrayList<>();
            // slice start from 0
            int diskRangeStartSlice = key.getStartByte() / cacheSlice;
            int diskRangeEndSlice = key.getEndByte() / cacheSlice;

            for (int i = diskRangeStartSlice; i <= diskRangeEndSlice; i++) {
                int sliceStartByte = i * cacheSlice;
                int sliceEndByte = sliceStartByte + cacheSlice - 1;
                SegmentInfo k = new SegmentInfo(key.getHost(),
                        key.getUrl(),
                        Math.max(sliceStartByte, key.getStartByte()),
                        Math.min(key.getEndByte(), sliceEndByte));
                if (curTotalSize + pendingCacheLength > maxSize) {
                    break;
                }
                pendingCacheLength += k.getLength();
                keys.add(k);
            }

            final BlockListFile blockList = new BlockListFile();
            blockList.setTotalLength(pendingCacheLength);
            service.submit(new Runnable() {
                @Override
                public void run() {
                    for (SegmentInfo info : keys) {
                        File f = writeToFile(inputStream, info);
                        if (f.length() != info.getEndByte() - info.getStartByte() + 1) {
                            f.delete();
                        }
                        blockList.server(f);
                    }
                    blockList.destroy();
                }
            });
            return blockList;
        } finally {
            wLock.unlock();
        }
    }

    //获取对应视频流的文件列表
    public List<CacheResult> get(SegmentInfo segmentInfo) {
        rLock.lock();
        try {
            File parentFile = getContentParentFile(segmentInfo.getHost(), segmentInfo.getUrl());
            if (!parentFile.exists()) {
                return null;
            }

            String[] localFiles = parentFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String[] split = name.split(NAME_SEPARATOR);
                    if (split.length < 2) {
                        return false;
                    }
                    return new File(dir, name).length() > 0;
                }
            });

            if (localFiles == null || localFiles.length <= 0) {
                return null;
            }

            List<SegmentInfo> keys = new ArrayList<>();
            List<CacheResult> results = new ArrayList<>();

            int curStartIndex = segmentInfo.getStartByte() / cacheSlice * cacheSlice;
            while (curStartIndex < segmentInfo.getEndByte()) {
                SegmentInfo key = new SegmentInfo(segmentInfo.getHost(), segmentInfo.getUrl(), curStartIndex, Math.min(curStartIndex + cacheSlice - 1, segmentInfo.getEndByte()));
                keys.add(key);
                curStartIndex += cacheSlice;
            }

            if (keys.size() == 0) {
                return null;
            }

            for (int i = 0; i < keys.size(); i++) {
                curStartIndex = keys.get(i).getStartByte() / cacheSlice * cacheSlice;
                if (i == 0) {
                    int skip = segmentInfo.getStartByte() - curStartIndex;
                    results.add(new CacheResult(keys.get(i), null, skip, Math.min(curStartIndex + cacheSlice - 1, segmentInfo.getEndByte()) - curStartIndex));
                } else {
                    results.add(new CacheResult(keys.get(i), null, 0, Math.min(curStartIndex + cacheSlice - 1, segmentInfo.getEndByte()) - curStartIndex));
                }
            }

            Map<SegmentInfo, File> localFilesMap = new HashMap<>();
            for (String localFile : localFiles) {
                DiskCache22.Range range = getFileRangeByName(localFile);
                localFilesMap.put(new SegmentInfo(segmentInfo.getHost(), segmentInfo.getUrl(), range.start, range.end), new File(parentFile, localFile));
            }

            for (CacheResult cacheResult : results) {
                SegmentInfo key = cacheResult.getKey();
                File f = localFilesMap.get(key);
                updateModifyTime(f);
                cacheResult.setCachedFile(f);
            }
            return results;
        } finally {
            rLock.unlock();
        }

    }

    /*******************************  文件处理  *******************************************/

    //缓存空间不足，删除一部分缓存数据
    private void checkToTrim(File parentFile) {
        if (curTotalSize > maxSize) {
            removeEmptyFile(parentFile);
            List<File> ret = new LinkedList<>();
            getAllFiles(ret, parentFile);
            Collections.sort(ret, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return Long.compare(o2.lastModified(), o1.lastModified());
                }
            });
            Iterator<File> iterator = ret.iterator();
            while (iterator.hasNext()) {
                File next = iterator.next();
                long length = next.length();
                if (curTotalSize + length > maxSize * TRIM_FACTOR) {
                    if (next.delete()) {
                        curTotalSize -= length;
                    }
                    iterator.remove();
                }
            }

        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File writeToFile(InputStream inputStream, SegmentInfo segmentKey) {
        File file = new File(getContentParentFile(segmentKey.getHost(), segmentKey.getUrl()), segmentKey.getStartByte() + NAME_SEPARATOR + segmentKey.getEndByte());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int totalLength = segmentKey.getEndByte() - segmentKey.getStartByte() + 1;
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] buf = new byte[1024 * 512];
            int length;
            while ((length = inputStream.read(buf, 0, Math.min(buf.length, totalLength))) > 0) {
                fileOutputStream.write(buf, 0, length);
                totalLength -= length;
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //更新修改时间
    private void updateModifyTime(File file) {
        try (RandomAccessFile accessFile = new RandomAccessFile(file, "rwd")) {
            if (file.exists()) {
                long now = System.currentTimeMillis();
                boolean modified = file.setLastModified(now);
                if (!modified) {
                    long size = file.length();
                    if (size == 0) {
                        file.delete();
                        return;
                    }

                    accessFile.seek(size - 1);
                    byte lastByte = accessFile.readByte();
                    accessFile.seek(size - 1);
                    accessFile.write(lastByte);
                    accessFile.close();
                }
            }
        } catch (Exception ignore) {

        }

    }

    //合并文件
    private File combineFile(File parentPathFile, DiskCache22.Range range1, DiskCache22.Range range2) {
        if (!range1.isIntersected(range2) && !range1.isContinuous(range2)) {
            return null;
        }

        int startRange = Math.min(range1.start, range2.start);
        int endRange = Math.max(range1.end, range2.end);

        DiskCache22.Range firstRange;
        DiskCache22.Range secondRange;
        if (range1.start < range2.start) {
            firstRange = range1;
            secondRange = range2;
        } else {
            firstRange = range2;
            secondRange = range1;
        }

        int skipLength = firstRange.end - secondRange.start + 1;

        File outFile = new File(parentPathFile, startRange + NAME_SEPARATOR + endRange);
        File firstFile = new File(parentPathFile, firstRange.start + NAME_SEPARATOR + firstRange.end);
        File secondFile = new File(parentPathFile, secondRange.start + NAME_SEPARATOR + secondRange.end);

        FileInputStream firstInputStream = null;
        RandomAccessFile outputStream = null;
        RandomAccessFile secondRandomFile = null;

        try {
            outputStream = new RandomAccessFile(outFile, "rwd");
            firstInputStream = new FileInputStream(firstFile);
            secondRandomFile = new RandomAccessFile(secondFile, "rwd");

            byte[] buf = new byte[1024 * 1024];
            int length;
            while ((length = firstInputStream.read(buf, 0, buf.length)) > 0) {
                outputStream.write(buf, 0, length);
            }

            secondRandomFile.seek(skipLength);

            while ((length = secondRandomFile.read(buf, 0, buf.length)) > 0) {
                outputStream.write(buf, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(firstInputStream);
            CloseUtil.close(outputStream);
            CloseUtil.close(secondRandomFile);
        }

        return null;
    }

    //清理重复，交叉缓存的文件
    private void checkToCombine(SegmentInfo segmentKey) {
        File parentPathFile = getContentParentFile(segmentKey.getHost(), segmentKey.getUrl());
        String[] list = parentPathFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String[] split = name.split(NAME_SEPARATOR);
                return split.length >= 2;
            }
        });

        if (list == null || list.length == 0) {
            return;
        }
        ArrayList<DiskCache22.Range> fileRanges = new ArrayList<>();

        for (String fileName : list) {
            DiskCache22.Range range = getFileRangeByName(fileName);
            if (range.length() > cacheSlice) {
                deleteFileByRange(range);
            } else {
                fileRanges.add(range);
            }
        }

        for (int i = 0; i < fileRanges.size(); i++) {
            DiskCache22.Range rangeI = fileRanges.get(i);
            if (rangeI.length() < cacheSlice) {
                for (int j = 0; j < fileRanges.size(); j++) {
                    DiskCache22.Range rangeJ = fileRanges.get(j);
                    if (i != j) {
                        if (rangeI.isIntersected(rangeJ)) {
                            if (rangeJ.contains(rangeI)) {
                                deleteFileByRange(rangeI);
                                fileRanges.remove(i);
                                i--;
                                break;
                            } else {
                                File file = combineFile(parentPathFile, rangeI, rangeJ);
                                int startRange = Math.min(rangeI.start, rangeJ.start);
                                int endRange = Math.max(rangeI.end, rangeJ.end);
                                DiskCache22.Range range = new DiskCache22.Range(startRange, endRange);
                                if (file != null) {
                                    if (file.exists() && file.length() == range.length()) {
                                        fileRanges.set(i, range);
                                        fileRanges.remove(j);
                                        deleteFileByRange(rangeI);
                                        deleteFileByRange(rangeJ);
                                        i--;
                                        break;
                                    } else {
                                        file.delete();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }


    /*******************************  工具方法  ************************************/

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    //获取缓存视频的大小
    private int getTotalSize(String cachePath) {
        int total = 0;
        List<File> ret = new ArrayList<>();
        getAllFiles(ret, new File(cachePath));
        for (File file : ret) {
            total += file.length();
        }
        return total;
    }

    private void getAllFiles(List<File> ret, File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        getAllFiles(ret, f);
                    }
                }
            } else {
                ret.add(file);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void removeEmptyFile(File parentFile) {
        File[] files = parentFile.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    removeEmptyFile(file);
                } else {
                    if (file.length() == 0) {
                        file.delete();
                    }
                }
            }
        } else {
            parentFile.delete();
        }
    }

    //获取服务器返回的包头数据 对应的host保存目录
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File getHeaderParentFile(String host) {
        File file = new File(cachePath + "/headers" + "/" + getTransformedString(host));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //获取服务器返回的包体数据 对应的url保存目录
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File getContentParentFile(String host, String url) {
        File file = new File(cachePath + "/content" + "/" + getTransformedString(host) + "/" + getTransformedString(url));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //获取
    private File getContentRootFile() {
        File file = new File(cachePath + "/content");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //获取对应的md5值
    private String getTransformedString(String string) {
//        return string.replaceAll("[/:.]", "_");
        try {
            return MD5.md5(string);
        } catch (Exception e) {
            return string;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteFileByRange(DiskCache22.Range range) {
        new File(range.start + NAME_SEPARATOR + range.end).delete();
    }

    private DiskCache22.Range getFileRangeByName(String name) {
        DiskCache22.Range range = new DiskCache22.Range();
        if (name != null && name.contains(NAME_SEPARATOR)) {
            try {
                String[] s = name.split(NAME_SEPARATOR);
                range.start = Integer.parseInt(s[0]);
                range.end = Integer.parseInt(s[1]);
            } catch (Exception ignore) {
            }

        }
        return range;
    }

    /*******************************  缓存内部类  ************************************/

    public static class CacheResult {
        private SegmentInfo key;
        private File cachedFile;
        private int startBytes;
        private int endBytes;

        public CacheResult(SegmentInfo key, File cachedFile, int startBytes, int endBytes) {
            this.key = key;
            this.startBytes = startBytes;
            this.cachedFile = cachedFile;
            this.endBytes = endBytes;
        }

        public boolean isCached() {
            return cachedFile != null && cachedFile.exists() && cachedFile.length() > 0;
        }

        public SegmentInfo getKey() {
            return key;
        }

        public void setKey(SegmentInfo key) {
            this.key = key;
        }

        public File getCachedFile() {
            return cachedFile;
        }

        public void setCachedFile(File cachedFile) {
            this.cachedFile = cachedFile;
        }

        public int getStartBytes() {
            return startBytes;
        }

        public void setStartBytes(int startBytes) {
            this.startBytes = startBytes;
        }

        public int getEndBytes() {
            return endBytes;
        }

        public void setEndBytes(int endBytes) {
            this.endBytes = endBytes;
        }

    }

    private static class Range {
        int start;
        int end;

        private boolean contains(DiskCache22.Range range) {
            return start <= range.start && end >= range.end;
        }

        private boolean isIntersected(DiskCache22.Range range) {
            int minStart = Math.min(start, range.start);
            int maxEnd = Math.max(end, range.end);
            return maxEnd - minStart + 1 < length() + range.length();
        }

        private boolean isContinuous(DiskCache22.Range range) {
            return start == range.end + 1 || end + 1 == range.start;
        }

        private int length() {
            return end - start + 1;
        }

        private Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        private Range() {
        }

    }

}
