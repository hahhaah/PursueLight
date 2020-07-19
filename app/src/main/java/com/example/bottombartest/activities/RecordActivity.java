package com.example.bottombartest.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.bottombartest.R;
import com.example.bottombartest.adapter.SportCalendarAdapter;
import com.example.bottombartest.db.DataManager;
import com.example.bottombartest.db.RealmHelper;
import com.example.bottombartest.entity.PathRecord;
import com.example.bottombartest.entity.SportMotionRecord;
import com.example.bottombartest.utils.DateUtil;
import com.example.bottombartest.utils.MotionUtils;
import com.example.bottombartest.utils.MyConstants;
import com.example.bottombartest.utils.UIHelper;
import com.example.bottombartest.view.calendar.CustomWeekBar;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建时间: 2020/07/17 12:02 <br>
 * 作者: xuziwei <br>
 * 描述: 我的记录界面  有运动日历等
 * God bless my code!!
 */
public class RecordActivity extends AppCompatActivity {

  private CalendarView mCalendarView;
  private CalendarLayout mCalendarLayout;
  private TextView mTextMonthDay;
  private TextView mTextYear;
  private TextView mTextLunar;
  private TextView mTextCurrentDay;
  private RecyclerView mRecyclerView ;
  private LinearLayout mSportAchievement;

  private int mYear;

  private SportCalendarAdapter adapter;
  private List<PathRecord> sportList = new ArrayList<>();

  private DataManager dataManager = null;

  private final int SPORT = 0x0012;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record);

    initView();
    initData();
    initEvent();
  }

  private void initView() {
    mCalendarView = findViewById(R.id.calendarView);
    mCalendarLayout = findViewById(R.id.calendarLayout);
    mTextMonthDay = findViewById(R.id.tv_month_day);
    mTextYear= findViewById(R.id.tv_year);
    mTextLunar = findViewById(R.id.tv_lunar);
    mTextCurrentDay = findViewById(R.id.tv_current_day);
    mRecyclerView = findViewById(R.id.recyclerView);
    mSportAchievement = findViewById(R.id.sport_achievement);
  }

  private void initData() {
    dataManager = new DataManager(new RealmHelper());

    mYear = mCalendarView.getCurYear();
    mTextYear.setText(String.valueOf(mYear));
    mTextMonthDay.setText(UIHelper.getString(R.string.date_month_day, mCalendarView.getCurMonth(), mCalendarView.getCurDay()));
    mTextLunar.setText("今日");
    mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false){
      @Override
      public boolean canScrollVertically() {
        return false;
      }
    });
    mRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.line)));
    adapter = new SportCalendarAdapter(R.layout.adapter_sportcalendar,sportList);
    mRecyclerView.setAdapter(adapter);

    mCalendarView.setWeekStarWithSun();

    mCalendarView.setWeekBar(CustomWeekBar.class);

    updateUI();
  }

  private void initEvent() {
    mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
      @Override
      public void onCalendarOutOfRange(Calendar calendar) {

      }

      @Override
      public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(UIHelper.getString(R.string.date_month_day, calendar.getMonth(), calendar.getDay()));
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

        getSports(DateUtil.formatStringDateShort(calendar.getYear(), calendar.getMonth(), calendar.getDay()));

        LogUtils.d("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
      }
    });
    mCalendarView.setOnYearChangeListener(new CalendarView.OnYearChangeListener() {
      @Override
      public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
      }
    });

    mTextMonthDay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!mCalendarLayout.isExpand()) {
          mCalendarLayout.expand();
          return;
        }
        mCalendarView.showYearSelectLayout(mYear);
        mTextLunar.setVisibility(View.GONE);
        mTextYear.setVisibility(View.GONE);
        mTextMonthDay.setText(String.valueOf(mYear));
      }
    });

    findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mCalendarView.scrollToCurrent();
      }
    });
  }


  private void updateUI() {
    loadSportData();
    getSports(DateUtil.formatStringDateShort(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay()));
  }

  private void loadSportData() {
    try {
      List<SportMotionRecord> records = dataManager.queryRecordList(SPUtils.getInstance().getString(MyConstants.USER_EMAIL, "123@qq.com"));
      Log.d("xzw", "loadSportData: "+records.size());
      if (null != records) {
        Map<String, Calendar> map = new HashMap<>();
        for (SportMotionRecord record : records) {
          String dateTag = record.getDateTag();
          String[] strings = dateTag.split("-");
          int year = Integer.parseInt(strings[0]);
          int month = Integer.parseInt(strings[1]);
          int day = Integer.parseInt(strings[2]);
          map.put(getSchemeCalendar(year, month, day, 0xFFCC0000, "记").toString(),
                  getSchemeCalendar(year, month, day, 0xFFCC0000, "记"));
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
      }
    } catch (Exception e) {
      LogUtils.e("获取运动数据失败", e);
    }
  }

  private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
    Calendar calendar = new Calendar();
    calendar.setYear(year);
    calendar.setMonth(month);
    calendar.setDay(day);
    calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
    calendar.setScheme(text);
    calendar.addScheme(new Calendar.Scheme());
    return calendar;
  }

  private void getSports(String dateTag) {
    try {
      List<SportMotionRecord> records = dataManager.queryRecordList(SPUtils.getInstance().getString(MyConstants.USER_EMAIL, "123@qq.com"), dateTag);
      if (null != records) {

        sportList.clear();
        adapter.notifyDataSetChanged();

        for (SportMotionRecord record : records) {
          PathRecord pathRecord = new PathRecord();
          pathRecord.setId(record.getId());
          pathRecord.setDistance(record.getDistance());
          pathRecord.setDuration(record.getDuration());
          pathRecord.setPathLinePoints(MotionUtils.parseLatLngLocations(record.getPathLine()));
          pathRecord.setStartPoint(MotionUtils.parseLatLngLocation(record.getStartPoint()));
          pathRecord.setEndPoint(MotionUtils.parseLatLngLocation(record.getEndPoint()));
          pathRecord.setStartTime(record.getStartTime());
          pathRecord.setEndTime(record.getEndTime());
          pathRecord.setCalorie(record.getCalorie());
          pathRecord.setSpeed(record.getSpeed());
          pathRecord.setDistribution(record.getDistribution());
          pathRecord.setDateTag(record.getDateTag());
          sportList.add(pathRecord);
        }
        if (sportList.isEmpty()){
          Log.d("xzw", "getSports: "+" is emppty");
          mSportAchievement.setVisibility(View.GONE);
        } else {
          Log.d("xzw", "getSports: "+" is not emppty");
          mSportAchievement.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
      } else {
        mSportAchievement.setVisibility(View.GONE);
      }
    } catch (Exception e) {
      LogUtils.e("获取运动数据失败", e);
      mSportAchievement.setVisibility(View.GONE);
    }
  }

  //recyclerView设置间距
  protected class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      super.getItemOffsets(outRect, view, parent, state);
      outRect.right = mSpace;
      outRect.left = mSpace;
      outRect.bottom = mSpace;
      if (parent.getChildAdapterPosition(view) == 0) {
        outRect.top = mSpace;
      } else {
        outRect.top = 0;
      }

    }

    SpaceItemDecoration(int space) {
      this.mSpace = space;
    }
  }


  @Override
  protected void onDestroy() {
    if (null != dataManager)
      dataManager.closeRealm();
    super.onDestroy();
  }
}
