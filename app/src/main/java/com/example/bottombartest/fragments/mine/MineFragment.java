package com.example.bottombartest.fragments.mine;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.bottombartest.R;
import com.example.bottombartest.activities.LoginActivity;
import com.example.bottombartest.activities.ModifyActivity;
import com.example.bottombartest.activities.RecordActivity;
import com.example.bottombartest.activities.TargetActivity;
import com.example.bottombartest.entity.Weather;
import com.example.bottombartest.interfaces.WeatherRequest;
import com.example.bottombartest.utils.MyConstants;
import com.example.bottombartest.utils.permission.PermissionUtil;
import com.example.bottombartest.view.SelectDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bottombartest.utils.MyConstants.*;

public class MineFragment extends Fragment implements View.OnClickListener {

  private static final String TAG = "MineFragment";

  private View mRootView;
  private ImageView mIvPhoto;
  private TextView mTvInfo;
  private LinearLayout mTargetView;
  private LinearLayout mRecordView;
  private LinearLayout mModifyView;
  private LinearLayout mLogoutView;


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
      }

      @Override
      public void OnTakePhoto() {
        boolean hasPermission = PermissionUtil.hasSelfPermission(getActivity(),Manifest.permission.CAMERA);
        if(hasPermission) {
          try {
            takePhoto();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void OnCancel() {
        dialog.dismiss();
      }
    });
  }

  //从相册中选取图片
  private void choosePhoto() {
    Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    getActivity().startActivityForResult(picture, REQUEST_PHOTO);
  }

  private void takePhoto() throws IOException {
    Intent intent = new Intent();
    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    // 获取文件
    File file = createFileIfNeed("UserPic.png");
    Uri uri = getUri(file);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
    getActivity().startActivityForResult(intent, MyConstants.REQUEST_CAMERA);
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
      uri = FileProvider.getUriForFile(getActivity(), "com.ke.ketalk.fileprovider", file);
    }
    return uri;
  }


  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.target_view:
        Intent intent = new Intent(getActivity(), TargetActivity.class);
        getActivity().startActivity(intent);
        break;
      case R.id.record_view:
        Intent intent2 = new Intent(getActivity(), RecordActivity.class);
        getActivity().startActivity(intent2);
        break;
      case R.id.modify_view:
        Intent intent3 = new Intent(getActivity(), ModifyActivity.class);
        getActivity().startActivity(intent3);
        break;
      case R.id.logout_view:
        SPUtils.getInstance().put(IS_LOGIN, false);
        ToastUtils.showShort("您已退出登录!");
        Intent it = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(it);
        break;
      default:
        break;
    }
  }
}