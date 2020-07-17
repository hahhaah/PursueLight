package com.example.bottombartest.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.bottombartest.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.YearView;

import static com.example.bottombartest.utils.UIHelper.dip2px;

/**
 * 创建时间: 2020/07/17 10:15 <br>
 * 作者: xuziwei <br>
 * 描述: 自定义年视图
 * God bless my code!!
 */
public class CustomYearView extends YearView {

  private int mTextPadding;

  private Paint mLeapYearTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  public CustomYearView(Context context) {
    super(context);

    mTextPadding = dip2px(context,3);

    mLeapYearTextPaint.setTextSize(dip2px(context, 12));
    mLeapYearTextPaint.setColor(0xffd1d1d1);
    mLeapYearTextPaint.setAntiAlias(true);
    mLeapYearTextPaint.setFakeBoldText(true);
  }

  @Override
  protected void onDrawMonth(Canvas canvas, int year, int month, int x, int y, int width, int height) {
    String text = getContext().getResources().getStringArray(R.array.month_string_array)[month-1];
    canvas.drawText(text,
            x + mItemWidth/2 - mTextPadding,
            y + mMonthTextBaseLine,
            mMonthTextPaint);

    if (month == 2 && isLeapYear(year)) {
      float w = getTextWidth(mMonthTextPaint, text);

      canvas.drawText("闰年",
              x + mItemWidth / 2 - mTextPadding + w + dip2px(getContext(), 6),
              y + mMonthTextBaseLine,
              mLeapYearTextPaint);
    }
  }

  private float getTextWidth(Paint paint, String text) {
    return paint.measureText(text);
  }

  private  boolean isLeapYear(int year) {
    return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
  }

  @Override
  protected void onDrawWeek(Canvas canvas, int week, int x, int y, int width, int height) {
    String text = getContext().getResources().getStringArray(R.array.year_view_week_string_array)[week];
    canvas.drawText(text,
            x + width / 2,
            y + mWeekTextBaseLine,
            mWeekTextPaint);
  }

  @Override
  protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
    int cx = x + mItemWidth / 2;
    int cy = y + mItemHeight / 2;
    int radius = Math.min(mItemWidth, mItemHeight) / 8 * 5;
    canvas.drawCircle(cx, cy, radius, mSelectedPaint);
    return true;
  }

  @Override
  protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

  }

  @Override
  protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
    float baselineY = mTextBaseLine + y;
    int cx = x + mItemWidth / 2;

    if (isSelected) {
      canvas.drawText(String.valueOf(calendar.getDay()),
              cx,
              baselineY,
              hasScheme ? mSchemeTextPaint : mSelectTextPaint);
    } else if (hasScheme) {
      canvas.drawText(String.valueOf(calendar.getDay()),
              cx,
              baselineY,
              calendar.isCurrentDay() ? mCurDayTextPaint : mSchemeTextPaint);

    } else {
      canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
              calendar.isCurrentDay() ? mCurDayTextPaint : mCurMonthTextPaint);
    }
  }
}