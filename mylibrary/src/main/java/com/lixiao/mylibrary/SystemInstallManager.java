package com.lixiao.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;

public class SystemInstallManager {

    private static final String tag="SystemInstallManager>>>";


    /**
     * 安装
     *
     * @param context 接收外部传进来的context
     */
   public static void install_1(Context context, String mUrl) {
        // 核心是下面几句代码
        Log.i(tag,"kankaninstall:"+mUrl);
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        //在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File f = new File(mUrl); //缓存路径
        String type = "application/vnd.android.package-archive";    //安装路径
        try {
            if (Build.VERSION.SDK_INT >= 24){   //判断安卓系统是否大于7.0  大于7.0使用以下方法
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() +".fileprovider",f);      //转为URI格式  第二个参数为AndroidManifest.xml中定义的authorities的值
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                installintent.addFlags(installintent.FLAG_GRANT_READ_URI_PERMISSION);
                installintent.setDataAndType(uri,type);
            } else{
                installintent.setDataAndType(Uri.fromFile(f), type);//存储位置为Android/data/包名/file/Download文件夹
            }
            context.startActivity(installintent);   //启动生命周期
        }catch (Exception e)
        {
            Log.i(tag,"kankaninstall----err:"+e.toString());
            e.printStackTrace();
        }


    }

    /**
     * 外部应用安装器安装apk（原生接口）
     * @param context
     * @param path apk的路径
     * @return
     */
    public static boolean install_2(Context context, String path) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }






    public static void install(Activity activity, String apkPath){
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//判断是否是8.0或以上
                boolean haveInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
                if (!haveInstallPermission) {
                    Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                    activity.startActivityForResult(intent, 10001);
                }else{
                    Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", new File(apkPath));
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    activity.startActivity(install);
                }
            } else {
                Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", new File(apkPath));
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                activity.startActivity(install);
            }
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(install);
        }
    }

}
