-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.CheckReturnValue
-dontwarn com.google.errorprone.annotations.Immutable
-dontwarn com.google.errorprone.annotations.RestrictedApi
-dontwarn org.slf4j.impl.StaticLoggerBinder

-keep class com.vk.dto.common.id.UserId { *; }
-keep class * extends com.vk.id.sample.app.util.carrying.CarryingCallable { *; }
-keep class android.content.Context { *; }

# OneTapStyle.Companion methods's arguments and return types
-keep class com.vk.id.onetap.common.OneTapStyle { *; }
-keep class com.vk.id.onetap.common.OneTapStyle$* { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle { *; }
# OAuthListWidgetStyle.Companion methods's arguments and return types
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetStyle { *; }
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetStyle$* { *; }
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle { *; }
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle { *; }
# OneTapBottomSheetStyle.Companion methods's arguments and return types
-keep class com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle { *; }
-keep class com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle$* { *; }
-keep class com.vk.id.onetap.compose.onetap.sheet.style.OneTapSheetCornersStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle { *; }

-keep class ru.bgitu.core.navigation.** { *; }

-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep interface com.huawei.hms.analytics.type.HAEventType{*;}
-keep interface com.huawei.hms.analytics.type.HAParamType{*;}
-keep class com.huawei.hms.analytics.HiAnalyticsInstance{*;}
-keep class com.huawei.hms.analytics.HiAnalytics{*;}
-keep class com.huawei.libcore.io.**{*;}

-keep class ru.bgitu.core.data.push.FirebasePushService { *; }
-keep class ru.bgitu.core.data.push.FirebasePushInterface { *; }

-dontwarn android.securitydiagnose.HwSecurityDiagnoseManager$StpExtraStatusInfo
-dontwarn android.securitydiagnose.HwSecurityDiagnoseManager
-dontwarn android.telephony.HwTelephonyManager
-dontwarn com.huawei.android.app.PackageManagerEx
-dontwarn com.huawei.android.content.pm.ApplicationInfoEx
-dontwarn com.huawei.android.os.BuildEx$VERSION
-dontwarn com.huawei.android.util.NoExtAPIException
-dontwarn com.huawei.appgallery.log.LogAdaptor
-dontwarn com.huawei.hianalytics.process.HiAnalyticsConfig$Builder
-dontwarn com.huawei.hianalytics.process.HiAnalyticsConfig
-dontwarn com.huawei.hianalytics.process.HiAnalyticsInstance$Builder
-dontwarn com.huawei.hianalytics.process.HiAnalyticsInstance
-dontwarn com.huawei.hianalytics.process.HiAnalyticsManager
-dontwarn com.huawei.hianalytics.util.HiAnalyticTools
-dontwarn com.huawei.hms.support.account.request.AccountAuthParams
-dontwarn com.huawei.hms.support.hwid.result.AuthHuaweiId
-dontwarn com.huawei.libcore.io.ExternalStorageFile
-dontwarn com.huawei.libcore.io.ExternalStorageFileInputStream
-dontwarn com.huawei.libcore.io.ExternalStorageFileOutputStream
-dontwarn com.huawei.libcore.io.ExternalStorageRandomAccessFile
-dontwarn com.huawei.ohos.localability.BundleAdapter
-dontwarn com.huawei.ohos.localability.base.BundleInfo
-dontwarn com.huawei.ohos.localability.base.DeviceInfo
-dontwarn com.huawei.system.BuildEx