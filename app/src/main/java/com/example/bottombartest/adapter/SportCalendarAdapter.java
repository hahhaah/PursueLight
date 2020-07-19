package com.example.bottombartest.adapter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.bottombartest.R;
import com.example.bottombartest.entity.PathRecord;
import com.example.bottombartest.utils.MotionUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 创建时间: 2020/07/19 17:57 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class SportCalendarAdapter extends BaseQuickAdapter<PathRecord, BaseViewHolder> {

  private DecimalFormat decimalFormat = new DecimalFormat("0.00");
  private DecimalFormat intFormat = new DecimalFormat("#");

  public SportCalendarAdapter(int layoutResId, @Nullable List<PathRecord> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, PathRecord item) {
    Log.d("xzw", "convert: "+ item.toString());
    helper.setText(R.id.distance, decimalFormat.format(item.getDistance() / 1000d));
    helper.setText(R.id.duration, MotionUtils.formatSeconds(item.getDuration()));
    helper.setText(R.id.calorie, intFormat.format(item.getCalorie()));
  }
}