package com.lixiao.mylibrary.install;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;
import com.lixiao.mylibrary.install.observer.InstallSubscriptionSubject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SystemInstallManager {

    private static final String tag="SystemInstallManager>>>";






   public  static   boolean  onActivityResult(int requestCode, int resultCode, boolean needAutoInstall,Context context){
       if (requestCode == InstallConfig.install_code) {
           if (resultCode == Activity.RESULT_OK) {

               if(needAutoInstall){
                   if(null==context){
                       InstallSubscriptionSubject.getInstance().installErr("onActivityResult:want to auto install apk,but !!context == null");
                   }else {
                       for(String apkPath:needApkPaths){
                            installByFileProvider(context,apkPath);
                       }
                   }
               }
               InstallSubscriptionSubject.getInstance().getPermissionOk();
//                Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", new File(ReaderApplication.getInstace().filePath));//在AndroidManifest中的android:authorities值
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
//                this.startActivity(install);
//           showToast( "权限已经打开，请再推送安装程序");
           } else {
               InstallSubscriptionSubject.getInstance().getPermissionErr();
//           showToast( "未打开'安装未知来源'开关,无法安装,请打开后重试");
           }
            return true;
       }
        return false;
   }

    /**
     * 申请权限
     */
   public static void onActivityRequestPermission(Activity activity){
       Uri packageURI = Uri.parse("package:" + activity.getPackageName());
       Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
       activity.startActivityForResult(intent, InstallConfig.install_code);
   }



    private static long lastRequesPermissionTime;
    private static List<String> needApkPaths=new ArrayList<>();

    public static void install(Context context, String apkPath){
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//判断是否是8.0或以上
                boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!haveInstallPermission) {
                    long nowTime=System.currentTimeMillis();
                    if(nowTime-lastRequesPermissionTime>=InstallConfig.requstTimeBetween){
                        needApkPaths.clear();
                    }
                    lastRequesPermissionTime=nowTime;
                    needApkPaths.add(apkPath);
                    InstallSubscriptionSubject.getInstance().nowActivityNeedRequestPermission();
                }else{
                    installByFileProvider(context,apkPath);
                }
            } else {
                installByFileProvider(context,apkPath);
            }
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

    public static void installByFileProvider(Context context,String apkPath){
        Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(apkPath));
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(install);
    }

}
