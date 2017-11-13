package com.xcheng.permission.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_permission);
        findViewById(R.id.singleRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPermission.with(PermissionActivity.this)
                        .permissions(Manifest.permission.CAMERA)
                        .request(12, new OnRequestCallback() {
                            @Override
                            public void onAllowed() {
                                // request allowed
                                Toast.makeText(PermissionActivity.this, "CAMERA  被允许", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onRefused(DeniedResult deniedResult) {
                                Toast.makeText(PermissionActivity.this, deniedResult.deniedPerms + " 被禁止", Toast.LENGTH_SHORT).show();

                                if (deniedResult.allNeverAsked) {

                                } else {

                                }
                            }
                        });
            }
        });
        findViewById(R.id.multiRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPermission.with(PermissionActivity.this)
                        .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request(12, new OnRequestCallback() {
                            @Override
                            public void onAllowed() {
                                // request allowed
                                Toast.makeText(PermissionActivity.this, "CAMERA WRITE_EXTERNAL_STORAGE 被允许", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onRefused(DeniedResult deniedResult) {
                                Toast.makeText(PermissionActivity.this, deniedResult.deniedPerms + " 被禁止", Toast.LENGTH_SHORT).show();

                                if (deniedResult.allNeverAsked) {
                                } else {

                                }
                            }
                        });

            }
        });
    }
}
