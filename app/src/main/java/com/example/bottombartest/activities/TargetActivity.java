package com.example.bottombartest.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.bottombartest.R;
import com.example.bottombartest.adapter.TargetAdapter;
import com.example.bottombartest.db.DataManager;
import com.example.bottombartest.db.RealmHelper;
import com.example.bottombartest.entity.Target;
import com.example.bottombartest.utils.LogUtils;
import com.example.bottombartest.utils.MyConstants;
import com.example.bottombartest.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.bottombartest.utils.MyConstants.REQUEST_NEW_TARGET;
import static com.example.bottombartest.utils.MyConstants.TARGET;
import static com.example.bottombartest.utils.MyConstants.TIME;

/**
 * 创建时间: 2020/07/17 11:26 <br>
 * 作者: xuziwei <br>
 * 描述: 目标界面
 * God bless my code!!
 */
public class TargetActivity extends AppCompatActivity {

  private static final String TAG = "TargetActivity";

  private List<Target> mTargets = new ArrayList<>();

  private TextView mTvGoCreate;
  private RecyclerView mRecyclerView;
  private TargetAdapter mTargetAdapter;
  private DataManager dataManager = null;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_target);
    initView();
    getTargets();
    updateUI();
    initEvent();
  }

  private void getTargets() {
    dataManager = new DataManager(new RealmHelper());
    List<Target> targetList = dataManager.queryAllTargets();
    if (targetList != null) {
      mTargets.clear();

      for (Target target : targetList) {
        Target newTarget = new Target();
        newTarget.setId(target.getId());
        newTarget.setContent(target.getContent());
        newTarget.setTime(target.getTime());
        newTarget.setFinished(target.isFinished());
        mTargets.add(newTarget);
      }
    }
    updateUI();
  }

  private void initView() {
    mTvGoCreate = findViewById(R.id.go_create_target);
    mTvGoCreate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);
    mRecyclerView = findViewById(R.id.target_list);
    mTargetAdapter = new TargetAdapter();

    LinearLayoutManager manager = new LinearLayoutManager(TargetActivity.this);
    manager.setOrientation(RecyclerView.VERTICAL);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top = UIHelper.dip2px(TargetActivity.this,6);
        outRect.bottom = UIHelper.dip2px(TargetActivity.this,5);
        outRect.left = UIHelper.dip2px(TargetActivity.this,5);
      }
    });


    ItemTouchHelper helper = new ItemTouchHelper(new TargetAdapter.MyMoveCallback(mTargetAdapter));
    helper.attachToRecyclerView(mRecyclerView);

    mRecyclerView.setAdapter(mTargetAdapter);

  }

  private void initEvent() {
    findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
      }
    });

    mTvGoCreate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(TargetActivity.this,NewTargetActivity.class);
        startActivityForResult(intent, REQUEST_NEW_TARGET);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if( requestCode == REQUEST_NEW_TARGET && resultCode == RESULT_OK){
      //更新列表
      String content = data.getStringExtra(TARGET);
      String time = data.getStringExtra(TIME);
      Target target = new Target(content,time);
      LogUtils.d("xzw",target.toString());
      if (mTargets != null) {
        mTargets.add(target);
        dataManager.insertTarget(target);
      }
      updateUI();
    } else {
      ToastUtils.showShort("目标是不是太大了？再试试吧～");
    }
  }

  private void updateUI(){
    mTargetAdapter.setData(mTargets);
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "saveTarget-->" + mTargets.size());
    mTargets.clear();
    mTargets = null;
  }
}
