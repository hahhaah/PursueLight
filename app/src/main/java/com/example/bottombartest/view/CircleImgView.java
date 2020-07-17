package com.example.bottombartest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 创建时间: 2020/07/16 21:28 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class CircleImgView extends AppCompatImageView {
  public static boolean isCircle = true;
  private float mWidth;
  private float mHeight;
  private Paint mPaint;
  private float mRealRadius;
  private Matrix mMatrix;

  public CircleImgView(Context context) {
    this(context,null);
  }

  public CircleImgView(Context context, AttributeSet attrs) {
    this(context, attrs,0);
  }

  public CircleImgView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mPaint = new Paint();
    mPaint.setAntiAlias(true);   //设置抗锯齿
    mMatrix = new Matrix();      //初始化缩放矩阵
  }

  //测量控件的宽高，并获取其内切圆的半径
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mHeight = getMeasuredHeight();
    mWidth = getMeasuredWidth();
    mRealRadius = Math.min(mWidth, mHeight) / 2;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Drawable drawable = getDrawable();
    if (drawable == null) {
      super.onDraw(canvas);
      return;
    }
    if (drawable instanceof BitmapDrawable && isCircle) {
      mPaint.setShader(initBitmapShader((BitmapDrawable) drawable));//将着色器设置给画笔
      canvas.drawCircle(mWidth / 2, mHeight / 2, mRealRadius,
              mPaint);//使用画笔在画布上画圆
      return;
    }
    super.onDraw(canvas);
  }

  //获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,
  // 通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
  private Shader initBitmapShader(BitmapDrawable drawable) {
    Bitmap bitmap = drawable.getBitmap();
    BitmapShader bitmapShader =
            new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    float scale =
            Math.max(mWidth / bitmap.getWidth(), mHeight / bitmap.getHeight());
    mMatrix.setScale(scale, scale);//将图片宽高等比例缩放，避免拉伸
    bitmapShader.setLocalMatrix(mMatrix);
    return bitmapShader;
  }
}
