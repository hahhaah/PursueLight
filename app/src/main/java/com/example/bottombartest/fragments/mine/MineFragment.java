package com.example.bottombartest.fragments.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bottombartest.R;
import com.example.bottombartest.activities.LoginActivity;
import com.example.bottombartest.activities.ModifyActivity;
import com.example.bottombartest.activities.RecordActivity;
import com.example.bottombartest.activities.TargetActivity;
import com.example.bottombartest.utils.MyConstants;
import com.example.bottombartest.view.SelectDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.example.bottombartest.utils.MyConstants.*;

public class MineFragment extends Fragment implements View.OnClickListener {

  private static final String TAG = "MineFragment";

  private View mRootView;
  private ImageView mIvPhoto;
  private TextView mTvInfo;
  private TextView mTvIntro;
  private LinearLayout mTargetView;
  private LinearLayout mRecordView;
  private LinearLayout mModifyView;
  private LinearLayout mLogoutView;

  private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    super.onCreateView(inflater, container, savedInstanceState);
    mRootView = inflater.inflate(R.layout.fragment_mine,container,false);
    
    initView();
    initEvent();
    return mRootView;
  }

  private void initView() {
    mIvPhoto = mRootView.findViewById(R.id.iv_logo);
    mTargetView = mRootView.findViewById(R.id.target_view);
    mRecordView = mRootView.findViewById(R.id.record_view);
    mModifyView = mRootView.findViewById(R.id.modify_view);
    mLogoutView = mRootView.findViewById(R.id.logout_view);
    mTvInfo = mRootView.findViewById(R.id.user_name);
    mTvIntro = mRootView.findViewById(R.id.user_intro);


    updateIntro();
  }

  private void updateIntro() {
    if (!TextUtils.isEmpty(SPUtils.getInstance().getString(INTRO))) {
      mTvIntro.setText(SPUtils.getInstance().getString(INTRO));
    } else {
      mTvIntro.setText("这个用户很懒，什么都没有写");
    }

    if (!TextUtils.isEmpty(SPUtils.getInstance().getString(INTRO))) {
      mTvInfo.setText(SPUtils.getInstance().getString(USER_NAME));
    }
  }

  private void initEvent() {
    mTargetView.setOnClickListener(this);
    mRecordView.setOnClickListener(this);
    mModifyView.setOnClickListener(this);
    mLogoutView.setOnClickListener(this);
    mIvPhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showTakePhotoDialog();
      }
    });
  }

  //弹出对话框 提供相机、相册、取消选项
  private void showTakePhotoDialog() {
    final SelectDialog dialog = new SelectDialog(getActivity());
    dialog.show();
    dialog.setClickListener(new SelectDialog.ClickListener() {
      @Override
      public void OnChoosePhoto() {
        choosePhoto();
        dialog.dismiss();
      }

      @Override
      public void OnTakePhoto() {
        //获取权限
        getPermission();
        try {
          takePhoto();
        } catch (IOException e) {
          e.printStackTrace();
        }
        dialog.dismiss();
      }

      @Override
      public void OnCancel() {
        dialog.dismiss();
      }
    });
  }

  private void getPermission() {
    if (EasyPermissions.hasPermissions(getActivity(), permissions)) {
      //已经打开权限
      ToastUtils.showShort("已经申请相关权限");
    } else {
      //没有打开相关权限、申请权限
      EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
      String state = Environment.getExternalStorageState();
      if (!state.equals(Environment.MEDIA_MOUNTED)) return;

      //显示原图
      File file = null;
      try {
        file = createFileIfNeed("UserPic.png");
        Uri inUri = getUri(file);
        cropPhoto(inUri);

      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK
            && null != data) {
      try {
        Uri inUri = data.getData();//获取路径
        cropPhoto(inUri);
        refreshLogo(inUri);
      } catch (Exception e) {
        //"上传失败");
        ToastUtils.showShort("头像上传失败");
        e.printStackTrace();
      }
    }

    if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
      Uri resultUri = UCrop.getOutput(data);
      refreshLogo(resultUri);
    } else if (resultCode == UCrop.RESULT_ERROR) {
      final Throwable cropError = UCrop.getError(data);
    }
  }

  //更新头像
  private void refreshLogo(Uri resultUri) {
    Log.d(TAG, "refreshLogo: -->");
    Glide.with(getActivity()).load(resultUri).skipMemoryCache(true) // 不使用内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(mIvPhoto);
  }

  private void choosePhoto() {
    //这是打开系统默认的相册(就是你系统怎么分类,就怎么显示,首先展示分类列表)
    Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(picture, REQUEST_PHOTO);
  }

  private void takePhoto() throws IOException {
    Intent intent = new Intent();
    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    // 获取文件
    File file = createFileIfNeed("UserPic.png");
    Uri uri = getUri(file);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
    startActivityForResult(intent, MyConstants.REQUEST_CAMERA);
  }

  // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
  private File createFileIfNeed(String fileName) throws IOException {
    String fileA =
            getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/nbinpic";
    Log.d("fileName", fileA);
    File fileJA = new File(fileA);
    if (!fileJA.exists()) {
      fileJA.mkdirs();
    }
    File file = new File(fileA, fileName);
    if (!file.exists()) {
      file.createNewFile();
    }

    Log.d("fileName", file.getAbsolutePath() + file.exists());
    return file;
  }

  private Uri getUri(File file) {
    //拍照后原图回存入此路径下
    Uri uri;
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
      uri = Uri.fromFile(file);
    } else {
      /**
       * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
       * 并且这样可以解决MIUI系统上拍照返回size为0的情况
       */
      uri = FileProvider.getUriForFile(getActivity(), "com.example.bottombartest.fileprovider", file);
    }
    return uri;
  }

  private void cropPhoto (Uri inUri) throws IOException {
    // 获取文件
    File fileOut = createFileIfNeed("UserIcon1.jpg");
    UCrop.Options options = new UCrop.Options();
    options.setCircleDimmedLayer(true);//设置是否为圆形裁剪框
    options.setHideBottomControls(true);
    options.setToolbarTitle("移动和缩放");//设置标题栏文字
    options.setToolbarWidgetColor(Color.parseColor("#ffffff"));//标题字的颜色以及按钮颜色
    options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
    options.setToolbarColor(Color.parseColor("#000000")); // 设置标题栏颜色
    options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
    options.setCropGridColor(Color.parseColor("#ffffff"));//设置裁剪网格的颜色
    options.setShowCropGrid(false);  //设置是否显示裁剪网格
    options.setShowCropFrame(false); //设置是否显示裁剪边框(true为方形边框)
    options.setMaxScaleMultiplier(3);//设置最大缩放比例
    options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
    options.setCompressionQuality(100);
    UCrop.of(inUri,Uri.fromFile(fileOut))
            .withAspectRatio(1, 1).withOptions(options)
            .start((AppCompatActivity) getActivity());
  }
  

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.target_view:
        Intent intent = new Intent(getActivity(), TargetActivity.class);
        startActivity(intent);
        break;
      case R.id.record_view:
        Intent intent2 = new Intent(getActivity(), RecordActivity.class);
        startActivity(intent2);
        break;
      case R.id.modify_view:
        Intent intent3 = new Intent(getActivity(), ModifyActivity.class);
        startActivity(intent3);
        break;
      case R.id.logout_view:
        SPUtils.getInstance().put(IS_LOGIN, false);
        SPUtils.getInstance().clear();
        ToastUtils.showShort("您已退出登录!");
        Intent it = new Intent(getActivity(), LoginActivity.class);
        startActivity(it);
        break;
      default:
        break;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    updateIntro();
  }
}