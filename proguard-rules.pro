###########################以下是AndroidStudio自带的混淆配置协议###############################

#忽略警告
-ignorewarnings
##保证是独立的jar,没有任何项目引用,如果不写就会认为我们所有的代码是无用的,从而把所有的代码压缩掉,导出一个空的jar
-dontshrink
###泛型
-keepattributes Signature

#log
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# 表示混淆时不使用大小写混合类名
-dontusemixedcaseclassnames

# 表示不跳过library中的非public的类
-dontskipnonpubliclibraryclasses

# 打印混淆的详细信息
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 表示不进行校验,这个校验作用 在java平台上的
-dontpreverify

#使用注解需要添加
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
#指定不混淆所有的JNI方法
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
#所有View的子类及其子类的get、set方法都不进行混淆
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
# 不混淆Activity中参数类型为View的所有方法
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
# 不混淆Enum类型的指定方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 不混淆Parcelable和它的子类，还有Creator成员变量
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# 不混淆R类里及其所有内部static类中的所有static变量字段
-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
# 不提示兼容库的错误警告
-dontwarn android.support.**

-dontwarn com.markupartist.**

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class *{
      <init>(android.content.Context);
}

#####################################  乐播   ###########################################
###Lebo
-keep class com.hpplay.**{*;}
-keep class com.hpplay.**$*{*;}
-dontwarn com.hpplay.**