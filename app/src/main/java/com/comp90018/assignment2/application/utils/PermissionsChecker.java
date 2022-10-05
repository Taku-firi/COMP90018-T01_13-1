package com.comp90018.assignment2.application.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context){
        mContext=context.getApplicationContext();
    }

    public boolean lacksPermissions(String... permissions){
        for (String permission: permissions){
            if (lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            return ContextCompat.checkSelfPermission(mContext,permission)== PackageManager.PERMISSION_DENIED;
        }
        return false;
    }
}
