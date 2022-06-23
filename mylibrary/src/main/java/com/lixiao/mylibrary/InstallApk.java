package com.lixiao.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import androidx.core.content.FileProvider;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class InstallApk {
    private static InstallApk installApk;
    private InstallApk(){
    }

    public static InstallApk getInstance(){
        if(null==installApk){
            synchronized (InstallApk.class){
                if(null==installApk){
                    installApk=new InstallApk();
                }
            }
        }
        return installApk;
    }

    private  String tag = "InstallApk>>>";

    public void installApk(Activity context, String path, boolean auto, final InstallImp setInstallImp, boolean needRemove){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InstallImp installImp=setInstallImp;
                if(installImp==null){
                    installImp=new InstallImp() {
                        @Override
                        public void installIsOk() {
                            Log.i(tag, "install------log；6---ok");
                        }

                        @Override
                        public void installIsErr() {
                            Log.i(tag, "install------log；6---err");

                        }

                        @Override
                        public void isSystemInstall() {
                            Log.i(tag, "install------log；6---system");

                        }
                    };
                }



                Log.i(tag, "install------log；1");
                if(auto){

                    boolean isInstallEd=false;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        isInstallEd=SilentInstallManager.silentInstallApk(context,path);
                    }
                    Log.i(tag, "install------log；2");
                    if(!isInstallEd){
                        isInstallEd=ReflectionInstallManager.compulsoryInstall(context,path,installImp);
                    }
                    Log.i(tag, "install------log；3");
                    if(!isInstallEd){
                        Log.i(tag, "install------log；4");
                        isInstallEd= ReflectionInstallManager.installSilent(path);
                    }
                    if(isInstallEd){
                        Log.i(tag, "install------log；4");
                        installImp.installIsOk();
                        if(needRemove){
                            File removeFile=new File(path);
                            removeFile.delete();
                        }

                    }else {
                        if(!ReflectionInstallManager.installSilentWithReflection(context,path,installImp,context.getPackageName())){
                            Log.i(tag, "install------log；6");
                            installImp.isSystemInstall();
                           SystemInstallManager. install(context,path);
                        }
                    }
                }else {
                    installImp.isSystemInstall();
                    SystemInstallManager.install(context,path);
                }

            }
        }).start();
    }














}
