package com.cpsdna.app.mykotlinapplication.util.GLog;

import android.text.TextUtils;

import com.cpsdna.app.mykotlinapplication.util.GLog.logParser.BaseParser;
import com.cpsdna.app.mykotlinapplication.util.GLog.logParser.FileParser;
import com.cpsdna.app.mykotlinapplication.util.GLog.logParser.JsonParser;
import com.cpsdna.app.mykotlinapplication.util.GLog.logParser.ObjParser;
import com.cpsdna.app.mykotlinapplication.util.GLog.logParser.XmlParser;

import java.io.File;

/**
 * Created by gallon on 2017/5/18
 */
public class GLog {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";
    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "DefaultTag";
    private static final String SUFFIX = ".java";
    public static final int JSON_INDENT = 4;
    public static final int V = 0x1;
    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;
    private static final int JSON = 0x7;
    private static final int XML = 0x8;
    private static final int STACK_TRACE_INDEX = 5;
    private static String mGlobalTag;
    private static boolean mIsGlobalTagEmpty = true;
    public static void init() {}
    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }
    public static void v(Object msg) {
        printLog(V, null, msg);
    }
    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }
    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }
    public static void d(Object msg) {
        printLog(D, null, msg);
    }
    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }
    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }
    public static void i(Object msg) {
        printLog(I, null, msg);
    }
    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }
    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }
    public static void w(Object msg) {
        printLog(W, null, msg);
    }
    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }
    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }
    public static void e(Object msg) {
        printLog(E, null, msg);
    }
    public static void e(String tag, Object... objects) {
        if (objects == null || objects.length == 0) {
            printLog(E, null, tag);
        } else {
            printLog(E, tag, objects);
        }
    }
    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }
    public static void a(Object msg) {
        printLog(A, null, msg);
    }
    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }
    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }
    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }
    public static void xml(String xml) {
        printLog(XML, null, xml);
    }
    public static void xml(String tag, String xml) {
        printLog(XML, tag, xml);
    }
    public static void file(File targetDirectory, Object msg) {
        printFile(null, targetDirectory, null, msg);
    }
    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, null, msg);
    }
    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }
    private static void printLog(int type, String tagStr, Object... objects) {
        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                BaseParser.printDefault(type, tag, headString + msg);
                break;
            case JSON:
                JsonParser.printJson(tag, msg, headString);
                break;
            case XML:
                XmlParser.printXml(tag, msg, headString);
                break;
        }
    }
    private static void printFile(String tagStr, File targetDirectory, String fileName, Object objectMsg) {
        String[] contents = wrapperContent(tagStr, objectMsg);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        FileParser.printFile(tag, targetDirectory, fileName, headString, msg);
    }
    private static String[] wrapperContent(String tagStr, Object... objects) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }
        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        //无tag使用类名做tag,有就用自定义tag
        String tag = (tagStr == null ? className : tagStr);
        //如果设置全局tag，就使用全局
        if (mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            //无全局tag，自定义tag也无，使用默认tag
            tag = TAG_DEFAULT;
        } else if (!mIsGlobalTagEmpty) {
            //有全局tag，使用全局tag
            tag = mGlobalTag;
        }
        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[]{tag, msg, headString};
    }
    private static String getObjectsString(Object... objects) {
        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(ObjParser.parseObj(object)).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : ObjParser.parseObj(object);
        }
    }
}