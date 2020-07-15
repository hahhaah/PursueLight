package com.example.bottombartest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.bottombartest.R;
import com.example.bottombartest.utils.LogUtils;
import com.example.bottombartest.utils.UIHelper;

import java.lang.reflect.Field;

/*
 * author：xuziwei
 * date：2020-07-15 21:30
 * description：自定义轮播图view
 * God bless my code!!
 */
public class LooperView extends LinearLayout {

  private TextView mTitleView;
  private MyViewPager mViewPager;
  private LinearLayout mPointContainer;
  private int mDuration;
  private boolean mHasTitle;

  private TitleBindListener mTitleBindListener = null;
  private InnerPageAdapter mInnerAdapter = null;

  private static final String TAG = "LooperView";

  public LooperView(Context context) {
    this(context,null);
  }

  public LooperView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs,0);
  }

  public LooperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.layout_looper_view, this, true);
    //获取自定义属性值
    TypedArray typedArray = context.obtainStyledAttributes(R.styleable.LooperView);
    mDuration = typedArray.getInteger(R.styleable.LooperView_switchTime,2500);
    mHasTitle = typedArray.getBoolean(R.styleable.LooperView_hasTitle,false);
    LogUtils.d(TAG,"duration-->"+mDuration+", mHasTitle-->"+mHasTitle);
    typedArray.recycle();
    initView(context);
    initEvent();
  }

  private void initEvent() {
    /**
     *
     * @param position
     * position有两个情况，position positionOffset 为0时，就是当前的Position
     *  如果有滑动，position则会是下一个准备看到的position
     *
     * @param positionOffset 位置偏移量，取值为0到1，[0，1）
     * @param positionOffsetPixels 位置偏移量，这个是像素界别的
     */
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        //滑动以后停下来的回调，position指所停在的位置
        //这个时候我们去获取标题
        if(mTitleBindListener != null) {
          String title = mTitleBindListener.getTitle(position);
          mTitleView.setText(title);
        }
        updateIndicator();
      }

      @Override
      public void onPageScrollStateChanged(int state) {
        //滑动状态的改变，有停止的，滑动中的.
        //ViewPager#SCROLL_STATE_IDLE
        //ViewPager#SCROLL_STATE_DRAGGING
        //ViewPager#SCROLL_STATE_SETTLING
      }
    });
  }

  private void initView(Context context) {
    mViewPager = this.findViewById(R.id.content_pager);
    mViewPager.setPageMargin(UIHelper.dip2px(getContext(),10));
    mViewPager.setOffscreenPageLimit(3);
    setViewPagerScroller(context);
    mTitleView = this.findViewById(R.id.content_title);
    mPointContainer = this.findViewById(R.id.content_point_container);

    mViewPager.setDuration(mDuration);
  }

  public void setData(InnerPageAdapter innerPageAdapter,TitleBindListener listener){
    mViewPager.setAdapter(innerPageAdapter);
    mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
    this.mInnerAdapter = innerPageAdapter;
    this.mTitleBindListener = listener;
    if(mTitleBindListener != null) {
      String title = mTitleBindListener.getTitle(0);
      mTitleView.setText(title);
    }
    updateIndicator();
    innerPageAdapter.registerDataSetObserver(new DataSetObserver() {
      @Override
      public void onChanged() {
        updateIndicator();
      }
    });
  }

  private void updateIndicator(){
    if(mInnerAdapter != null) {
      //先删除
      mPointContainer.removeAllViews();
      int indicatorSize = mInnerAdapter.getDataSize();
      for(int i = 0; i < indicatorSize; i++) {
        View view = new View(getContext());
        if((mViewPager.getCurrentItem() % mInnerAdapter.getDataSize() == i)) {
          view.setBackgroundColor(Color.parseColor("#ff0000"));
        } else {
          view.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(UIHelper.dip2px(getContext(),5),UIHelper.dip2px(getContext(),5));
        layoutParams.setMargins(UIHelper.dip2px(getContext(),5),0,UIHelper.dip2px(getContext(),5),0);
        view.setLayoutParams(layoutParams);
        //添加到容器里
        mPointContainer.addView(view);
      }
    }

  }

  public interface TitleBindListener{
    String getTitle(int position);
  }

  public static abstract class InnerPageAdapter extends PagerAdapter {

    public abstract int getDataSize();

    @Override
    public int getCount() {
      return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      int itemPosition = position % getDataSize();
      View itemView = getItemView(container,itemPosition);
      container.addView(itemView);
      return itemView;
    }

    protected abstract View getItemView(ViewGroup container, int position);

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((View)object);
    }
  }

  /**
   *通过反射设置
   */
  private void setViewPagerScroller(Context context) {

    try {
      Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
      scrollerField.setAccessible(true);
      Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
      interpolator.setAccessible(true);

      Scroller scroller = new Scroller(context, (Interpolator) interpolator.get(null)) {
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
          super.startScroll(startX, startY, dx, dy, duration * 7);    // 这里是关键，将duration变长或变短
        }
      };
      scrollerField.set(mViewPager, scroller);
    } catch (NoSuchFieldException e) {
      // Do nothing.
    } catch (IllegalAccessException e) {
      // Do nothing.
    }
  }

}
