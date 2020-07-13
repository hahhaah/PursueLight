package com.example.bottombartest.utils.permission;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;


import com.example.bottombartest.MyApplication;
import com.example.bottombartest.utils.DeviceHelper;
import com.example.bottombartest.utils.LogUtils;

import java.util.Arrays;

/*
 * author：xuziwei
 * date：2020-07-13 18:30
 * description：
 * God bless my code!!
 */
public class APermission {

  private Builder builder;

  private static final String TAG = "APermission";

  public static Builder builder(FragmentActivity mActivity) {
    return new Builder(mActivity);
  }

  public static Builder builder(Activity mActivity) {
    return new Builder(mActivity);
  }

  public static class Builder {
    private String[] permissions;
    private FragmentActivity mActivity;
    private PermissionListener permissionListener;
    private String tipContent = null;
    private int code = Permissions.PERMISSION_REQUEST_CODE;

    private Builder(FragmentActivity activity) {
      this.mActivity = activity;
    }

    private Builder(Activity activity) {
      this.mActivity = (FragmentActivity) activity;
    }

    public Builder setCode(int code) {
      this.code = code;
      return this;
    }

    public Builder setTipContent(String tipContent) {
      this.tipContent = tipContent;
      return this;
    }

    public Builder callBack(PermissionListener permissionListener) {

      this.permissionListener = permissionListener;
      return this;
    }

    public Builder setPermissions(String[] permissions) {
      LogUtils.d(TAG,"permissionListener--" + "setPermissions" + Arrays.toString(permissions));
      this.permissions = permissions;
      return this;
    }

    public APermission request() {
      APermission axdPermission = new APermission();
      axdPermission.builder = this;
      if (!(permissions == null || permissions.length == 0)) {
        axdPermission.requestPermission();
      }
      if (mActivity instanceof PermissionActivity) {
        ((PermissionActivity) mActivity).setAxdPermission(axdPermission);
      }
      return axdPermission;
    }
  }

  PermissionListener getPermissionListener() {
    if (builder == null) {
      return null;
    }
    return builder.permissionListener;
  }

  private void requestPermission() {
    if (PermissionUtil.hasSelfPermissions(builder.mActivity, builder.permissions) || !DeviceHelper.isOverMarshmallow()) {
      builder.permissionListener.onPassed();
    } else {
      String[] unPassPermissions = PermissionUtil.cutPassPermissions(builder.mActivity, builder.permissions);
      if (unPassPermissions.length == 0) {
        builder.permissionListener.onPassed();
        return;
      }
      ActivityCompat.requestPermissions(builder.mActivity, unPassPermissions, builder.code);
    }
  }

  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if ((requestCode == builder.code && PermissionUtil.verifyPermissions(grantResults))
            || PermissionUtil.hasSelfPermissions(builder.mActivity, builder.permissions)) {
      builder.permissionListener.onPassed();
    } else {
      boolean neverAsk = !PermissionUtil.shouldShowRequestPermissionRationale(builder.mActivity, permissions);
      if (!builder.permissionListener.onDenied(neverAsk)) {
        if (neverAsk) {
          Toast.makeText(MyApplication.getAppContext(),"权限缺失",Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(MyApplication.getAppContext(),"权限缺失",Toast.LENGTH_SHORT).show();
        }
      }

    }
  }

  /**
   * 从设置页面返回后
   */
  public void onSettingResult(@NonNull APermission axdPermission) {
    axdPermission.requestPermission();
  }

  public void destroy() {
    builder = null;
  }
}
