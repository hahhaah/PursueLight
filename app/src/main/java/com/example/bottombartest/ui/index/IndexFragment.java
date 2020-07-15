package com.example.bottombartest.ui.index;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bottombartest.R;
import com.example.bottombartest.entity.LooperItem;
import com.example.bottombartest.view.LooperView;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {

  private List<LooperItem> mData;
  private LooperView mLooperView;
  private View mRootView;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mRootView = inflater.inflate(R.layout.fragment_index,container,false);
    initTestData();
    initView();
    return mRootView;
  }

  private void initTestData() {
    mData = new ArrayList<>();
    mData.add(new LooperItem(R.mipmap.logo,"图片1的标题"));
    mData.add(new LooperItem(R.mipmap.logo,"图片2的标题"));
    mData.add(new LooperItem(R.mipmap.logo,"图片3的标题"));
    mData.add(new LooperItem(R.mipmap.logo,"图片4的标题"));
  }

  private void initView(){
    mLooperView = mRootView.findViewById(R.id.looper_view);
    mLooperView.setData(new LooperView.InnerPageAdapter() {
      @Override
      public int getDataSize() {
        return mData.size();
      }

      @Override
      protected View getItemView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //设置图片
        imageView.setImageResource(mData.get(position).getImgRsId());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        return imageView;
      }
    },new LooperView.TitleBindListener() {
      @Override
      public String getTitle(int position) {
        return mData.get(position % mData.size()).getTitle();
      }
    });
  }
}