package com.lixiao.mylibrary.install;

import java.util.Random;

public class InstallConfig {

    public static final int install_code= new Random().nextInt(9999);

    public static final boolean def_installErrCanUseSystemInstall=false;

    public static final long requstTimeBetween=60*1000;
}
