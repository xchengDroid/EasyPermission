package com.xcheng.permission;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyPermission发起类
 * Created by chengxin on 2017/11/9.
 */
public class EasyPermission {

    private PermissionFragment mPermissionFragment;
    static final String TAG = "EasyPermission";

    private EasyPermission(Activity activity) {
        mPermissionFragment = (PermissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        boolean isNewInstance = mPermissionFragment == null;
        if (isNewInstance) {
            mPermissionFragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(mPermissionFragment, TAG)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
    }

    public static PermissionRequest.Builder with(@NonNull Activity activity) {
        return new PermissionRequest.Builder(new EasyPermission(activity));
    }

    void request(PermissionRequest permissionRequest) {
        final OnRequestCallback onRequestCallback = permissionRequest.onRequestCallback;
        if (!isMarshmallow()) {
            onRequestCallback.onAllowed();
        } else {
            String[] permissions = permissionRequest.permissions;
            List<String> deniedPerms = findDeniedPermissions(mPermissionFragment.getContext(), permissions);
            if (!deniedPerms.isEmpty()) {
                mPermissionFragment.requestPermissions(permissionRequest);
            } else {
                onRequestCallback.onAllowed();
            }
        }
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 是否有该权限
     *
     * @return true代表有，false 其他
     */
    public static boolean isGranted(@NonNull Context context, @NonNull String permission) {
        return !isMarshmallow() || /*兼容 AppOps*/PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 找到未授权的Permission
     */
    public static List<String> findDeniedPermissions(@NonNull Context context, @NonNull String... permissions) {
        List<String> deniedPerms = new ArrayList<>();
        for (String value : permissions) {
            if (!isGranted(context, value)) {
                deniedPerms.add(value);
            }
        }
        return deniedPerms;
    }

    /**
     * 找到未授权的权限，但是未被完全拒绝
     */
    public static List<String> findRationalePermissions(@NonNull Activity activity, @NonNull String... permissions) {
        List<String> rationalePerms = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                rationalePerms.add(permission);
            }
        }
        return rationalePerms;
    }

    public static String[] toArray(@NonNull List<String> permissions) {
        return permissions.toArray(new String[permissions.size()]);
    }
}
