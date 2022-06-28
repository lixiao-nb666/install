package com.lixiao.mylibrary.install.observer;

import android.content.Context;

public interface InstallObserver {

    public void nowActivityNeedRequestPermission();

    public void getPermissionOk();

    public void getPermissionErr();

    public void installErr(String errStr);

    public void needSystemInstallByFileProvider(Context context, String apkPath);
}
