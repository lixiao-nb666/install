package com.lixiao.mylibrary.install;

public interface InstallImp{
        public void installIsOk();

        public void installIsErr();

            /**
             * 这个情况需要自己去监听广播
             */
        public void isSystemInstall();

}