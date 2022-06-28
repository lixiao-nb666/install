package com.lixiao.mylibrary.install;

import android.content.Context;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class ReflectionInstallManager {

    private static final String tag="ReflectionInstall>>>>";









    /**
     * 执行具体的静默安装逻辑，需要手机ROOT。
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public static boolean compulsoryInstall(final Context context, final String apkPath,InstallImp installImp) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请root权限，并获得实例
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
//            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;

            }
        } catch (Exception e) {
//            Log.i(tag, "compulsoryInstall>>>"+ e.toString());

        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
//                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }





    /**
     * install slient
     *
     * @param filePath
     * @return 0 means normal, 1 means file not exist, 2 means other exception error
     */
   public  static    boolean installSilent(String filePath) {
        boolean result;
        File file = new File(filePath);
        if (filePath == null || filePath.length() == 0 || file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return false;
        }
        String[] args = {"pm", "install", "-r", filePath};
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            result = true;
        } else {
            result = false;

        }
//        Log.d("test-test", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }


//    public static  boolean installSilentWithReflection(Context context, String filePath, InstallImp installImp, String pageName) {
////        Log.d(tag, "--o----packageInstalled =ee0: ---------");
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            Method method = packageManager.getClass().getDeclaredMethod("installPackage",
//                    new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class});
//            method.setAccessible(true);
//            File apkFile = new File(filePath);
//            Uri apkUri = Uri.fromFile(apkFile);
//            method.invoke(packageManager, new Object[]{apkUri, new IPackageInstallObserver.Stub() {
//                @Override
//                public void packageInstalled(String pkgName, int resultCode) throws RemoteException {
////                    Log.d(tag, "--o----packageInstalled = " + pkgName + "; resultCode = " + resultCode);
//                    if(null!=installImp){
//                        installImp.installIsOk();
//                    }
//
//                }
//            }, Integer.valueOf(2), pageName});
////            PackageManager.INSTALL_REPLACE_EXISTING = 2;
//            return true;
//        }
//        catch (InvocationTargetException e){
////            Log.d(tag, "--o----packageInstalled =ee2: "+e.toString() );
//        }catch (NoSuchMethodException e) {
////            Log.d(tag, "--o----packageInstalled =ee3: "+e.toString() );
//        } catch (Exception e) {
////            Log.d(tag, "--o----packageInstalled =ee4: "+e.toString() );
//
//        }
//        return  false;
//
//    }



//    private static final String TAG = "PackageManager_R";
//    private static final String ERROR_TAG = "ReflectError " + TAG;
//
//    private static Method sMethod_installPackage;

    /**
     * 静默安装（原生接口）
     *
     * @param path
     * @return
     */
//        public static boolean installPackage(String path, final SystemAppInstall.InstalledCallBack callBack) {
//
//            if (sMethod_installPackage == null) {
//                try {
//                    sMethod_installPackage = PackageManager.class.getMethod("installPackage", Uri.class, IPackageInstallObserver.class, int.class, String.class);
//                } catch (Exception e) {
//                    LogUtils.w(ERROR_TAG, "", e);
//                    e.printStackTrace();
//                }
//            }
//
//            IPackageInstallObserver observer = new IPackageInstallObserver.Stub() {
//                public void packageInstalled(String packageName, int returnCode) {
//                    if (returnCode != 1) {
//                        callBack.onResult(packageName, false);
//                    } else {
//                        callBack.onResult(packageName, true);
//                    }
//                }
//            };
//
//            PackageManager pm = AppContextUtils.getAppContext().getPackageManager();
//            try {
//                sMethod_installPackage.invoke(pm, Uri.parse("file://" + path), observer, 0, null);
//                return true;
//            } catch (Exception e) {
//                LogUtils.w(ERROR_TAG, "", e);
//                e.printStackTrace();
//            }
//            return false;
//        }









//        // 适配android9.0的安装方法。
//        public void install28(Context context, String apkFilePath, Class<CustomBroadcastReceiver> receiver) {
//            Log.d(TAG, "install28 path=" + apkFilePath);
//            File apkFile = new File(apkFilePath);
//            PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
//            PackageInstaller.SessionParams sessionParams
//                    = new PackageInstaller.SessionParams(PackageInstaller
//                    .SessionParams.MODE_FULL_INSTALL);
//            sessionParams.setSize(apkFile.length());
//
//            int sessionId = createSession(packageInstaller, sessionParams);
//            Log.d(TAG, "install28  sessionId=" + sessionId);
//            if (sessionId != -1) {
//                boolean copySuccess = copyInstallFile(packageInstaller, sessionId, apkFilePath);
//                Log.d(TAG, "install28  copySuccess=" + copySuccess);
//                if (copySuccess) {
//                    execInstallCommand(context, packageInstaller, sessionId, receiver);
//                }
//            }
//        }
//
//    private int createSession(PackageInstaller packageInstaller,
//                              PackageInstaller.SessionParams sessionParams) {
//        int sessionId = -1;
//        try {
//            sessionId = packageInstaller.createSession(sessionParams);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sessionId;
//    }
//
//    private boolean copyInstallFile(PackageInstaller packageInstaller,
//                                    int sessionId, String apkFilePath) {
//        InputStream in = null;
//        OutputStream out = null;
//        PackageInstaller.Session session = null;
//        boolean success = false;
//        try {
//            File apkFile = new File(apkFilePath);
//            session = packageInstaller.openSession(sessionId);
//            out = session.openWrite("base.apk", 0, apkFile.length());
//            in = new FileInputStream(apkFile);
//            int total = 0, c;
//            byte[] buffer = new byte[65536];
//            while ((c = in.read(buffer)) != -1) {
//                total += c;
//                out.write(buffer, 0, c);
//            }
//            session.fsync(out);
//            Log.i(TAG, "streamed " + total + " bytes");
//            success = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                out.close();
//                in.close();
//                session.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return success;
//    }

//    private void execInstallCommand(Context context, PackageInstaller packageInstaller, int sessionId, Class<CustomBroadcastReceiver> receiver) {
//        PackageInstaller.Session session = null;
//        try {
//            Intent intent = new Intent(context, receiver);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                    1, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            session.commit(pendingIntent.getIntentSender());
//            Log.i(TAG, "begin session");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            session.close();
//        }
//    }
}
