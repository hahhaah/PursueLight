package com.example.bottombartest.view.calendar;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

import static com.example.bottombartest.utils.UIHelper.dip2px;

/**
 * 创建时间: 2020/07/17 10:07 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
class CustomMonthView extends MonthView {

  private int mRadius;

  //自定义魅族标记的文本画笔
  private Paint mTextPaint = new Paint();

  //24节气画笔
  private Paint mSolarTermTextPaint = new Paint();

  //背景圆点
  private Paint mPointPaint = new Paint();

  //今天的背景色
  private Paint mCurrentDayPaint = new Paint();

  //圆点半径
  private float mPointRadius;

  private int mPadding;

  private float mCircleRadius;

  //自定义魅族标记的圆形背景
  private Paint mSchemeBasicPaint = new Paint();

  private float mSchemeBaseLine;

  public CustomMonthView(Context context) {
    super(context);

    init(context);
  }

  private void init(Context context) {
    mTextPaint.setTextSize(dip2px(context, 8));
    mTextPaint.setColor(0xffffffff);
    mTextPaint.setAntiAlias(true);
    mTextPaint.setFakeBoldText(true);

    mSolarTermTextPaint.setColor(0xff489dff);
    mSolarTermTextPaint.setAntiAlias(true);
    mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

    mSchemeBasicPaint.setAntiAlias(true);
    mSchemeBasicPaint.setStyle(Paint.Style.FILL);
    mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
    mSchemeBasicPaint.setFakeBoldText(true);
    mSchemeBasicPaint.setColor(Color.WHITE);


    mCurrentDayPaint.setAntiAlias(true);
    mCurrentDayPaint.setStyle(Paint.Style.FILL);
    mCurrentDayPaint.setColor(0xFFeaeaea);

    mPointPaint.setAntiAlias(true);
    mPointPaint.setStyle(Paint.Style.FILL);
    mPointPaint.setTextAlign(Paint.Align.CENTER);
    mPointPaint.setColor(Color.RED);

    mCircleRadius = dip2px(getContext(), 7);

    mPadding = dip2px(getContext(), 3);

    mPointRadius = dip2px(context, 2);

    Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
    mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dip2px(getContext(), 1);

    //兼容硬件加速无效的代码
    setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
    //4.0以上硬件加速会导致无效
    mSelectedPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

    setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
    mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

  }

  @Override
  protected void onPreviewHook() {
    mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
    mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5;
  }

  @Override
  protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
    int cx = x + mItemWidth / 2;
    int cy = y + mItemHeight / 2;
    canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
    return true;
  }

  @Override
  protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
    boolean isSelected = isSelected(calendar);
    if (isSelected) {
      mPointPaint.setColor(Color.WHITE);
    } else {
      mPointPaint.setColor(Color.GRAY);
    }
  }

  @Override
  protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
    int cx = x + mItemWidth / 2;
    int cy = y + mItemHeight / 2;
    int top = y - mItemHeight / 6;

    if (calendar.isCurrentDay() && !isSelected) {
      canvas.drawCircle(cx, cy, mRadius, mCurrentDayPaint);
    }

    if (hasScheme) {
      canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicPaint);
      mTextPaint.setColor(calendar.getSchemeColor());
      canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
    }


    if (calendar.isWeekend() && calendar.isCurrentMonth()) {
      mCurMonthTextPaint.setColor(0xFF489dff);
      mCurMonthLunarTextPaint.setColor(0xFF489dff);
      mSchemeTextPaint.setColor(0xFF489dff);
      mSchemeLunarTextPaint.setColor(0xFF489dff);
      mOtherMonthLunarTextPaint.setColor(0xFF489dff);
      mOtherMonthTextPaint.setColor(0xFF489dff);
    } else {
      mCurMonthTextPaint.setColor(0xff333333);
      mCurMonthLunarTextPaint.setColor(0xffCFCFCF);
      mSchemeTextPaint.setColor(0xff333333);
      mSchemeLunarTextPaint.setColor(0xffCFCFCF);

      mOtherMonthTextPaint.setColor(0xFFe1e1e1);
      mOtherMonthLunarTextPaint.setColor(0xFFe1e1e1);
    }

    if (isSelected) {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
              mSelectTextPaint);
      canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
    } else if (hasScheme) {

      canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
              calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

      canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
              !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint : mSchemeLunarTextPaint);
    } else {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
              calendar.isCurrentDay() ? mCurDayTextPaint :
                      calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

      canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
              calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                      calendar.isCurrentMonth() ? !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint  :
                              mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
    }
  }
}
