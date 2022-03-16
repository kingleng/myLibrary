package util;

/**
 * Created by gallon on 2017/12/5.
 */

public class JniUtils {

    static {
        System.loadLibrary("NdkJniG1");
    }

    public static native String getG1();

    public static native String getG2();
}
