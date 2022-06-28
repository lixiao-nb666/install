package com.lixiao.mylibrary.install.observer;

import java.util.ArrayList;
import java.util.List;

public class InstallSubscriptionSubject implements InstallSubject {
    private static InstallSubscriptionSubject baseSubscriptionSubject;
    private List<InstallObserver> observerList = new ArrayList<>();

    public static InstallSubscriptionSubject getInstance() {
        if (null == baseSubscriptionSubject) {
            synchronized (InstallSubscriptionSubject.class) {
                if (null == baseSubscriptionSubject) {
                    baseSubscriptionSubject = new InstallSubscriptionSubject();
                }
            }
        }
        return baseSubscriptionSubject;
    }

    @Override
    public void addObserver(InstallObserver baseObserver) {
        observerList.add(baseObserver);
    }

    @Override
    public void removeObserver(InstallObserver baseObserver) {
        observerList.remove(baseObserver);
    }

    @Override
    public void close() {
        observerList.clear();
    }

    @Override
    public void nowActivityNeedRequestPermission() {
        for (InstallObserver observer : observerList) {
            observer.nowActivityNeedRequestPermission();
        }
    }

    @Override
    public void getPermissionOk() {
        for (InstallObserver observer : observerList) {
            observer.getPermissionOk();
        }
    }

    @Override
    public void getPermissionErr() {
        for (InstallObserver observer : observerList) {
            observer.getPermissionErr();
        }
    }


    @Override
    public void installErr(String errStr) {
        for (InstallObserver observer : observerList) {
            observer.installErr(errStr);
        }
    }
}
