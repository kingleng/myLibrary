package com.info.aegis.plugincorelibrary.plugIn;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.info.aegis.plugincorelibrary.PluginManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by leng on 2020/3/13.
 */
public class PluginManifestUtil {

    public static void setManifestInfo(Context context, String apkPath, PlugInfo info, String mPhoneInfo)
            throws XmlPullParserException, IOException {

        ZipFile zipFile = new ZipFile(new File(apkPath), ZipFile.OPEN_READ);
        ZipEntry manifestXmlEntry = zipFile.getEntry(XmlManifestReader.DEFAULT_XML);

        String manifestXML = XmlManifestReader.getManifestXMLFromAPK(zipFile, manifestXmlEntry);
        PackageInfo pkgInfo = context.getPackageManager().getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES
//                        | PackageManager.GET_RECEIVERS//
//                        | PackageManager.GET_PROVIDERS//
                        | PackageManager.GET_META_DATA//
                        | PackageManager.GET_SHARED_LIBRARY_FILES//
//                        | PackageManager.GET_SERVICES//
                // | PackageManager.GET_SIGNATURES//
        );
        info.setPackageInfo(pkgInfo);

        File libDir = PluginManager.getInstance().getPluginLibPath(info);
        try {
//            if (extractLibFile(zipFile, libDir,mPhoneInfo)) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//                    pkgInfo.applicationInfo.nativeLibraryDir = libDir.getAbsolutePath();
//                }
//            }
        } finally {
            zipFile.close();
        }
        setAttrs(info, manifestXML);
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
                case XmlPullParser.END_TAG: {
                    break;
                }
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
