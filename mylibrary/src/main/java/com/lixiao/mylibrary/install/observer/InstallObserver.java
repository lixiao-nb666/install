package com.lixiao.mylibrary.install.observer;

public interface InstallObserver {

    public void nowActivityNeedRequestPermission();

    public void getPermissionOk();

    public void getPermissionErr();

    public void installErr(String errStr);
}
