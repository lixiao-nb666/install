package com.lixiao.mylibrary.install;

import android.content.Context;
import android.os.Build;

import com.lixiao.mylibrary.install.observer.InstallSubscriptionSubject;

import java.io.File;

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

    private boolean installErrCanUseSystemInstal=InstallConfig.def_installErrCanUseSystemInstall;

    public void setInstallErrCanUseSystemInstal(boolean set){
        this.installErrCanUseSystemInstal=set;
    }


    private  String tag = "InstallApk>>>";

    public void installApk(Context context, String path, boolean auto, final InstallImp setInstallImp, boolean needRemove){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InstallImp installImp=setInstallImp;
                if(installImp==null) {
                    installImp = new InstallImp() {
                        @Override
                        public void installIsOk() {
                        }

                        @Override
                        public void installIsErr() {
                        }

                        @Override
                        public void isSystemInstall() {
                        }
                    };

                }
               if(!new File(path).exists()){
                   installImp.installIsErr();
                   InstallSubscriptionSubject.getInstance().installErr("installApk err: file no exists --"+path);
                   return;
               }
                if(auto){
                    boolean isInstallEd=false;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        isInstallEd=SilentInstallManager.silentInstallApk(context,path);
                    }
                    if(!isInstallEd){
                        isInstallEd=ReflectionInstallManager.compulsoryInstall(context,path,installImp);
                    }
                    if(!isInstallEd){
                        isInstallEd= ReflectionInstallManager.installSilent(path);
                    }
                    if(isInstallEd){
                        installImp.installIsOk();
                        if(needRemove){
                            File removeFile=new File(path);
                            removeFile.delete();
                        }
                    }else {
                        if(!ReflectionInstallManager.installSilentWithReflection(context,path,installImp,context.getPackageName())){
                            if(installErrCanUseSystemInstal){
                                installImp.isSystemInstall();
                                SystemInstallManager. install(context,path);
                            }
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
