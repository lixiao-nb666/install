# install



* 24版本SDK以上需要导入下面这个类
  import android.content.Context;
  import android.content.Intent;
  import android.net.Uri;
     import androidx.core.content.FileProvider;
     import java.io.File;

  public class SystemInstallManagerByFileProvider {

  private static final String tag="SystemInstallManagerByFileProvider>>>";

  public static void installByFileProvider(Context context,String apkPath){
        Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(apkPath));
        Intent install = new Intent(Intent.ACTION_VIEW);
         install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
         install.setDataAndType(apkUri, "application/vnd.android.package-archive");
         context.startActivity(install);
     }
  }

* 24版本SDK以上需要在配置文件加入这个内容提供者
    *       <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.inno_cn.smartview.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"  />
        </provider>


* 24版本SDK以上需要在xml加入file_path.xml
* <?xml version="1.0" encoding="utf-8"?>
<paths>
	<!--    <external-path path="Android/data/app的包名/" name="files_root" />-->
	<external-path path="." name="external_storage_root" />
</paths>