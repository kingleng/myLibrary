package com.info.aegis.plugincore.plugIn;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.info.aegis.plugincore.utils.FileUtils;
import com.info.aegis.plugincore.PluginManager;
import com.info.aegis.plugincore.utils.PluginUtil;

import static com.info.aegis.plugincore.utils.DexUtil.NATIVE_DIR;

/**
 * Created by leng on 2020/3/13.
 */
public class PluginManifestUtil {

    public static void setManifestInfo(Context context, String apkPath, PlugInfo info, String mPhoneInfo)
            throws XmlPullParserException, IOException, Exception{

        ZipFile zipFile = new ZipFile(new File(apkPath), ZipFile.OPEN_READ);
        ZipEntry manifestXmlEntry = zipFile.getEntry(XmlManifestReader.DEFAULT_XML);

        String manifestXML = XmlManifestReader.getManifestXMLFromAPK(zipFile, manifestXmlEntry);
        PackageInfo pkgInfo = context.getPackageManager().getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES
                        | PackageManager.GET_RECEIVERS//
                        | PackageManager.GET_PROVIDERS//
                        | PackageManager.GET_META_DATA//
                        | PackageManager.GET_SHARED_LIBRARY_FILES//
                        | PackageManager.GET_SERVICES//
//                 | PackageManager.GET_SIGNATURES//
        );
        info.setPackageInfo(pkgInfo);

        File libDir = getDir(context, NATIVE_DIR);

//        File libDir = PluginManager.getInstance().getPluginLibPath(info);
        try {
            if (extractLibFile(zipFile, libDir,mPhoneInfo)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    pkgInfo.applicationInfo.nativeLibraryDir = libDir.getAbsolutePath();
                }
            }
        } finally {
            zipFile.close();
        }
        setAttrs(info, manifestXML);
    }

    public static File getDir(Context context, String name) {
        return context.getDir(name, Context.MODE_PRIVATE);
    }

    /**
     * copy so库文件
     * @param zip
     * @param tardir
     * @param mPhoneInfo
     * @return
     * @throws IOException
     */
    private static boolean extractLibFile(ZipFile zip, File tardir, String mPhoneInfo) throws IOException {
        if (!tardir.exists()) {
            tardir.mkdirs();
        }
        //TODO mPhoneInfo 指定copy哪个文件夹下的so库文件 重要
        String defaultArch = mPhoneInfo;
        Map<String, List<ZipEntry>> archLibEntries = new HashMap<String, List<ZipEntry>>();
        for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = e.nextElement();
            String name = entry.getName();
            if (name.startsWith("/")) {
                name = name.substring(1);
            }
            if (name.startsWith("lib/")) {
                if (entry.isDirectory()) {
                    continue;
                }
                int sp = name.indexOf('/', 4);
                String en2add;
                if (sp > 0) {
                    String osArch = name.substring(4, sp);
                    en2add = osArch.toLowerCase();
                } else {
                    en2add = defaultArch;
                }
                List<ZipEntry> ents = archLibEntries.get(en2add);
                if (ents == null) {
                    ents = new LinkedList<ZipEntry>();
                    archLibEntries.put(en2add, ents);
                }
                ents.add(entry);
            }
        }
        String arch = System.getProperty("os.arch");
        List<ZipEntry> libEntries = archLibEntries.get(arch.toLowerCase());
        if (libEntries == null) {
            libEntries = archLibEntries.get(defaultArch);
        }
        boolean hasLib = false;
        if (libEntries != null) {
            hasLib = true;
            if (!tardir.exists()) {
                tardir.mkdirs();
            }
            InputStream dataIns = null;
            for (ZipEntry libEntry : libEntries) {
                String ename = libEntry.getName();
                String pureName = ename.substring(ename.lastIndexOf('/') + 1);
                File target = new File(tardir, pureName);
                try {
                    dataIns = zip.getInputStream(libEntry);
                    FileUtils.writeToFile(dataIns, target);
                } finally {
                    if (null != dataIns){
                        dataIns.close();
                    }
                }
            }
        }
        return hasLib;
    }

    private static void setAttrs(PlugInfo info, String manifestXML)
            throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(manifestXML));
        int eventType = parser.getEventType();
        String namespaceAndroid = null;
        do {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT: {
                    break;
                }
                case XmlPullParser.START_TAG: {
                    String tag = parser.getName();
                    if (tag.equals("manifest")) {
                        namespaceAndroid = parser.getNamespace("android");
                    } else if ("activity".equals(parser.getName())) {
                        addActivity(info, namespaceAndroid, parser);
                    } else if("application".equals(parser.getName())){
                        parseApplicationInfo(info, namespaceAndroid, parser);
                    }
                    break;
                }
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);
    }

    private static void parseApplicationInfo(PlugInfo info,
                                             String namespace, XmlPullParser parser) throws XmlPullParserException, IOException{
        String applicationName = parser.getAttributeValue(namespace, "name");
        String packageName = info.getPackageInfo().packageName;
        ApplicationInfo applicationInfo = info.getPackageInfo().applicationInfo;
        applicationInfo.name = getName(applicationName, packageName);
    }

    private static void addActivity(PlugInfo info, String namespace,
                                    XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        String activityName = parser.getAttributeValue(namespace, "name");
        String packageName = info.getPackageInfo().packageName;
        activityName = getName(activityName, packageName);
        ResolveInfo act = new ResolveInfo();
        act.activityInfo = info.findActivityByClassNameFromPkg(activityName);
        if(act.activityInfo == null){
            return;
        }
        do {
            switch (eventType) {
                case XmlPullParser.START_TAG: {
                    String tag = parser.getName();
                    if ("intent-filter".equals(tag)) {
                        if (act.filter == null) {
                            act.filter = new IntentFilter();
                        }
                    } else if ("action".equals(tag)) {
                        String actionName = parser.getAttributeValue(namespace,
                                "name");
                        act.filter.addAction(actionName);
                    } else if ("category".equals(tag)) {
                        String category = parser.getAttributeValue(namespace,
                                "name");
                        act.filter.addCategory(category);
                    } else if ("data".equals(tag)) {
                        // TODO parse data
                    }
                    break;
                }
            }
            eventType = parser.next();
        } while (!"activity".equals(parser.getName()));
        //
        info.addActivity(act);
    }

    private static String getName(String nameOrig, String pkgName) {
        if (nameOrig == null) {
            return null;
        }
        StringBuilder sb;
        if (nameOrig.startsWith(".")) {
            sb = new StringBuilder();
            sb.append(pkgName);
            sb.append(nameOrig);
        } else if (!nameOrig.contains(".")) {
            sb = new StringBuilder();
            sb.append(pkgName);
            sb.append('.');
            sb.append(nameOrig);
        } else {
            return nameOrig;
        }
        return sb.toString();
    }

}
