package com.example.bottombartest.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottombartest.R;
import com.example.bottombartest.activities.TargetActivity;
import com.example.bottombartest.entity.DailyBean;
import com.example.bottombartest.entity.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 创建时间: 2020/07/22 20:41 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.InnerHolder> {

  private List<Target> mTargets  = new ArrayList<>();

  private ItemClickListener mClickListener;

  @NonNull
  @Override
  public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target,parent,false);
    return new TargetAdapter.InnerHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
    View view = holder.itemView;
    Target target = mTargets.get(position);
    CheckBox checkBox = view.findViewById(R.id.box_finished);
    TextView content = view.findViewById(R.id.target_content);
    TextView date = view.findViewById(R.id.target_date);

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        boolean origin = mTargets.get(position).isFinished();
        mTargets.get(position).setFinished(!origin);
        notifyItemChanged(position);
      }
    });

    checkBox.setChecked(target.isFinished());
    content.setText(target.getContent());
    date.setText(target.getTime());
  }


  @Override
  public int getItemCount() {
    return mTargets.size();
  }

  public void setData(List<Target> targets){
    mTargets.clear();
    mTargets.addAll(targets);
    notifyDataSetChanged();
  }

  public class InnerHolder extends RecyclerView.ViewHolder{

    public InnerHolder(@NonNull View itemView) {
      super(itemView);
    }
  }

  public static class MyMoveCallback extends ItemTouchHelper.Callback{

    private TargetAdapter mAdapter;

    public MyMoveCallback(TargetAdapter adapter) {
      mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
      return makeMovementFlags(ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT,
              ItemTouchHelper.START|ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
      int startPosition = viewHolder.getAdapterPosition();
      int endPosition = target.getAdapterPosition();
      //the item to swap
      int index=startPosition;

      //drag direction
      int dir=startPosition-endPosition>0?-1:1;

      while(index<endPosition){
        Collections.swap(mAdapter.getTargetList(), index,index+dir );
        index+=dir;
      }

      recyclerView.getAdapter().notifyItemMoved(startPosition, endPosition);
      return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
      int position = viewHolder.getAdapterPosition();
      if (direction == ItemTouchHelper.END||direction == ItemTouchHelper.START) {
        mAdapter.getTargetList().remove(position);
        mAdapter.notifyItemRemoved(position);
      }
    }
  }

  private List<Target> getTargetList() {
    return mTargets;
  }

  public interface ItemClickListener{
    void onItemClick(List<Target> targetList, int pos);
  }

  public void setOnItemClick(ItemClickListener listener){
    mClickListener = listener;
  }
}
