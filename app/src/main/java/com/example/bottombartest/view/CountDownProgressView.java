package com.example.bottombartest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.bottombartest.R;

/*
 * author：xuziwei
 * date：2020-07-13 18:13
 * description：
 * God bless my code!!
 */
public class CountDownProgressView extends AppCompatTextView {
  // 圆实心的颜色
  private int circSolidColor;

  // 圆边框的颜色
  private int circFrameColor;

  // 圆边框的宽度
  private int circFrameWidth = 4;

  //圆的半径
  private int circRadius;

  // 进度条的颜色
  private int progressColor;

  // 进度条的宽度
  private int progressWidth = 4;

  private int textColor;

  private Rect mBounds;
  private Paint mPaint;
  private RectF mArcRectF;

  private int mCenterX;
  private int mCenterY;

  private String mText = "跳过";

  // 进度倒计时时间
  private long timeMillis = 3000;

  // 进度条通知
  private OnProgressListener mProgressListener;

  // 进度默认100
  private int progress = 100;

  // 进度条类型
  private ProgressType mProgressType = ProgressType.COUNT_BACK;

  public CountDownProgressView(Context context) {
    this(context,null);
  }

  public CountDownProgressView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();

    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownProgress);
    if (typedArray != null) {
      // 圆实心的颜色属性
      if (typedArray.hasValue(R.styleable.CountDownProgress_circSolidColor)) {
        // 获取用户设置的颜色
        circSolidColor = typedArray.getColor(R.styleable.CountDownProgress_circSolidColor, 0);
      } else {
        //默认是浅灰色
        circSolidColor = typedArray.getColor(R.styleable.CountDownProgress_circSolidColor, Color.parseColor("#D3D3D3"));
      }

      // 圆边框颜色属性
      if (typedArray.hasValue(R.styleable.CountDownProgress_circFrameColor)) {
        //有这个属性索引 获取用户设置的颜色
        circFrameColor = typedArray.getColor(R.styleable.CountDownProgress_circFrameColor, 0);
      } else {
        //没有这个属性索引 默认是深灰色
        circFrameColor = typedArray.getColor(R.styleable.CountDownProgress_circFrameColor, Color.parseColor("#A9A9A9"));
      }

      // 文字颜色属性
      if (typedArray.hasValue(R.styleable.CountDownProgress_ProgressText_color)) {
        //有这个属性索引 获取用户设置的颜色
        textColor = typedArray.getColor(R.styleable.CountDownProgress_ProgressText_color, 0);
      } else {
        //没有这个属性索引 默认是白色
        textColor = typedArray.getColor(R.styleable.CountDownProgress_ProgressText_color, Color.parseColor("#ffffff"));
      }

      // 进度条颜色属性
      if (typedArray.hasValue(R.styleable.CountDownProgress_progressColor)) {
        //有这个属性索引 获取用户设置的颜色
        progressColor = typedArray.getColor(R.styleable.CountDownProgress_progressColor, 0);
      } else {
        //没有这个属性索引 默认是蓝色
        progressColor = typedArray.getColor(R.styleable.CountDownProgress_progressColor, Color.parseColor("#0000FF"));
      }

      //这里千万不要忘记回收
      typedArray.recycle();
    }

  }

  public void setTimeMillis(int i) {
    this.timeMillis = timeMillis;
    invalidate();
  }


  /**
   * 进度条类型(方便做一个扩展，这里默认的是倒数进度条)
   */
  public enum ProgressType {
    //顺数进度条，从0-100；
    COUNT,

    // 倒数进度条，从100-0；
    COUNT_BACK;
  }

  /**
   * 设置进度条类型。
   *
   * @param progressType {@link ProgressType}.
   */
  public void setProgressType(ProgressType progressType) {
    this.mProgressType = progressType;
    resetProgress();
    /**
     * 请求重绘View
     * 1,直接调用invalidate()方法，请求重新draw()，但只会绘制调用者本身。
     * 2,setSelection()方法 ：请求重新draw()，但只会绘制调用者本身。
     * 3,setVisibility()方法 ： 当View可视状态在INVISIBLE转换VISIBLE时，会间接调用invalidate()方法，继而绘制该View
     * 4 ,setEnabled()方法 ： 请求重新draw()，但不会重新绘制任何视图包括该调用者本身。
     */
    invalidate();
  }



  // 重置进度
  private void resetProgress() {
    switch (mProgressType) {
      case COUNT:
        progress = 0;
        break;
      case COUNT_BACK:
        progress = 100;
        break;
    }
  }


  private void init() {
    mPaint = new Paint();
    mBounds = new Rect();
    mArcRectF = new RectF();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    if (width > height) {
      height = width;
    } else {
      width = height;
    }
    circRadius = width / 2;
    setMeasuredDimension(width, height);
  }

  /**
   * 绘制过程
   * 注意：canvas在绘制过程中是一层层覆盖的。所以绘制顺序不要写反了。如果先绘制实心圆在绘制文字在绘制空心圆。这时你会发现文字是看不到了，被覆盖了。
   * @param canvas
   */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    /**
     *  获取view的边界
     */
    getDrawingRect(mBounds);

    mCenterX = mBounds.centerX();
    mCenterY = mBounds.centerY();

    // 画实心圆
    mPaint.setAntiAlias(true); //设置抗锯齿
    mPaint.setStyle(Paint.Style.FILL); //实心填充style
    mPaint.setColor(circSolidColor);
    canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), circRadius, mPaint);


    // 画外边框(空心圆,即圆边框)
    mPaint.setAntiAlias(true);//设置抗锯齿
    mPaint.setStyle(Paint.Style.STROKE);//空心style
    mPaint.setStrokeWidth(circFrameWidth);//设置空心线宽度
    mPaint.setColor(circFrameColor);
    canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), circRadius - circFrameWidth, mPaint);

    // 画文字
    Paint text_paint = getPaint(); //注意：如果是继承的view，这里是没有这个getPaint()方法的。大家可以看到它是Textview包下的方法
    text_paint.setColor(textColor);
    text_paint.setAntiAlias(true);
    text_paint.setTextAlign(Paint.Align.CENTER);
    float textY = mCenterY - (text_paint.descent() + text_paint.ascent()) / 2;
    canvas.drawText(mText, mCenterX, textY, text_paint);


    // 画进度条
    mPaint.setColor(progressColor);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(progressWidth);
    mPaint.setStrokeCap(Paint.Cap.ROUND);  //当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式  Cap.ROUND,或方形样式Cap.SQUARE
    mArcRectF.set(mBounds.left + progressWidth, mBounds.top + progressWidth, mBounds.right - progressWidth, mBounds.bottom - progressWidth);
    canvas.drawArc(mArcRectF, -90, 360 * progress / 100, false, mPaint); //这里的-90.是方向，大家可以改成0，90，180，-90等就可以看到效果区别

  }

  private Runnable progressChangeTask = new Runnable() {
    @Override
    public void run() {
      removeCallbacks(this);
      switch (mProgressType) {
        case COUNT:
          progress += 1;
          break;
        case COUNT_BACK:
          progress -= 1;
          break;
      }
      if (progress >= 0 && progress <= 100) {
        if (mProgressListener != null)
          mProgressListener.onProgress(progress);
        invalidate();
        postDelayed(progressChangeTask, timeMillis / 60);
      } else
        progress = validateProgress(progress);
    }


  };

  private int validateProgress(int progress) {
    if (progress > 100)
      progress = 100;
    else if (progress < 0)
      progress = 0;
    return progress;
  }

  public void start() {
    stop();
    post(progressChangeTask);
  }


  public void stop() {
    removeCallbacks(progressChangeTask);
  }


  public void reStart() {
    resetProgress();
    start();
  }


  public interface OnProgressListener {

    //进度通知
    void onProgress(int progress);
  }

  public void setProgressListener(OnProgressListener mProgressListener) {
    this.mProgressListener = mProgressListener;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (Math.abs(x - (mBounds.centerX())) <= (circRadius) * 2 && Math.abs(y - (mBounds.centerY())) <= (circRadius) * 2) {
        }
        break;
      default:
    }
    return super.onTouchEvent(event);
  }

}
