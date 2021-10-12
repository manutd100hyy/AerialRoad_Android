package com.sytz.aerialroad;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class NaviActivity extends AppCompatActivity implements View.OnClickListener {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_LOCATION = 3;
    private static final int REQUEST_PHONESTATE = 4;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSIONS_CAMERA = { Manifest.permission.CAMERA};
    private static String[] PERMISSIONS_LOCATION = { Manifest.permission.ACCESS_FINE_LOCATION};
    private static String[] PERMISSIONS_PHONESTATE = {Manifest.permission.READ_PHONE_STATE};

    public boolean isHavePermission = false;

    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have camera permission
//        if (ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA,
//                    REQUEST_CAMERA);
//            return false;
//        }

        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
            return false;
        }

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_LOCATION,
                    REQUEST_LOCATION);
            return false;
        }

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_PHONESTATE,
                    REQUEST_PHONESTATE);
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        //申请屏幕常亮的函数
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        checkPermission();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.case_start: {
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.case_continue: {
                //startActivity(new Intent(this, PreviewActivity.class));
                break;
            }
            case R.id.case_look: {
                //startActivity(new Intent(this, CaseActivity.class));
                break;
            }
            case R.id.case_setting: {
                //startActivity(new Intent(this, SettingActivity.class));
                break;
            }
            default:
                break;
        }

        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (String str : permissions) {
            List<String> camera_list = Arrays.asList(PERMISSIONS_CAMERA);
            if (camera_list.contains(str)) {
                checkPermission();
            }

            List<String> file_list = Arrays.asList(PERMISSIONS_STORAGE);
            if (file_list.contains(str)) {
                checkPermission();
            }

            List<String> loc_list = Arrays.asList(PERMISSIONS_LOCATION);
            if (loc_list.contains(str)) {
                checkPermission();
            }

            List<String> state_list = Arrays.asList(PERMISSIONS_PHONESTATE);
            if (state_list.contains(str)) {
                checkPermission();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder bd = new AlertDialog.Builder(this);
        bd.setTitle("警告");
        bd.setMessage("是否退出软件?");
        bd.setNegativeButton("取消", null);
        bd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        bd.show();
    }

    private void checkPermission() {
        //动态申请权限
        //Android6.0以上需要动态申请权限
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !verifyStoragePermissions(this)) {
            return;
        }

        findViewById(R.id.case_start).setOnClickListener(this);
        findViewById(R.id.case_continue).setOnClickListener(this);
        findViewById(R.id.case_look).setOnClickListener(this);
        findViewById(R.id.case_setting).setOnClickListener(this);

        //创建项目的根目录
        TZFileTool.appRootDir = TZFileTool.getSDPath() + "/AerialRoad/";
        (new File(TZFileTool.appRootDir)).mkdirs();
    }
}