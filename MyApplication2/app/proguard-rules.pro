# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\leng\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-dontwarn com.alibaba.sdk.android.utils.**
#防止inline
-dontoptimize

-keepattributes Signature
    -keepattributes *Annotation*
    -keep class com.mintegral.** {*; }
    -keep interface com.mintegral.** {*; }
    -keep class android.support.v4.** { *; }
    -dontwarn com.mintegral.**
    -keep class **.R$* { public static final int mintegral*; }
    -keep class com.alphab.** {*; }
    -keep interface com.alphab.** {*; }
