package com.example.bottombartest.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bottombartest.R;

/**
 * 创建时间: 2020/07/17 09:23 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class SelectDialog extends Dialog {

  private ClickListener mClickListener;
  private Context mContext;

  public SelectDialog(@NonNull Context context) {
    super(context);
    mContext = context;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
  }

  private void init() {
    LayoutInflater inflater = LayoutInflater.from(mContext);
    View view = inflater.inflate( R.layout.select_dialog_view, null);
    setContentView(view);
    setCanceledOnTouchOutside(true);
    setCancelable(true);

    TextView takePhoto = view.findViewById(R.id.item_take_photo);
    TextView gallery = view.findViewById(R.id.item_galley);
    TextView cancel = view.findViewById(R.id.item_cancel);

    takePhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mClickListener != null) {
          mClickListener.OnTakePhoto();
        }
      }
    });

    gallery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mClickListener != null) {
          mClickListener.OnChoosePhoto();
        }
      }
    });

    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mClickListener != null) {
          mClickListener.OnCancel();
        }
      }
    });

    Window window = getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.setWindowAnimations(R.style.popupAnimation);
    WindowManager.LayoutParams lp = window.getAttributes();
    // 获取屏幕宽高
    DisplayMetrics d = mContext.getResources().getDisplayMetrics();
    // 设置宽高
    lp.width = (int) (d.widthPixels * 0.9);
    window.setAttributes(lp);
  }

  public interface ClickListener{
    void OnChoosePhoto();

    void OnTakePhoto();

    void OnCancel();
  }

  public void setClickListener(ClickListener listener){
    mClickListener = listener;
  }
}
