package com.lixiao.mylibrary.install.observer;

public interface InstallSubject {
    public void addObserver(InstallObserver baseObserver);

    public void removeObserver(InstallObserver baseObserver);

    public void close();

    public void nowActivityNeedRequestPermission();

    public void getPermissionOk();

    public void getPermissionErr();

    public void installErr(String errStr);
}
