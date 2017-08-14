package com.dadaxueche.student.dadaapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dada.mylibrary.Gson.ExamResult;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.View.PointButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpf on 8-28-0028.
 */
public class QuestionListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ExamResult.STResultsEntity> result_Selects = new ArrayList<>();
    private boolean canPoint = true;
    private onItemClick onItemClick;
    private Context mContext;
    private int currentPosition = 0;
    private int color_right,color_error;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_question_list,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        mContext = parent.getContext();
        color_right = mContext.getResources().getColor(R.color.Green);
        color_error = mContext.getResources().getColor(R.color.Pink);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = ((MyViewHolder) holder);
        myViewHolder.pointButton.setmCenterColor(Color.GRAY);
        myViewHolder.pointButton.setmTextColor(Color.WHITE);
        if(currentPosition != position)
            myViewHolder.pointButton.setmMessage(String.valueOf(position+1));
        else {
            myViewHolder.pointButton.setmMessage("当前");
            myViewHolder.pointButton.setmCenterColor(Color.BLACK);
        }
        if(canPoint && !result_Selects.get(position).getXzda().isEmpty()) {
            if (result_Selects.get(position).getXzda().equals(result_Selects.get(position).getBzda()))
                myViewHolder.pointButton.setmCenterColor(color_right);
            else
                myViewHolder.pointButton.setmCenterColor(color_error);
        } else {
            if(!result_Selects.get(position).getXzda().isEmpty())
                myViewHolder.pointButton.setmCenterColor(mContext.getResources().getColor(R.color.DeepOrange));
        }
    }

    @Override
    public int getItemCount() {
        return result_Selects.size();
    }

    public void setResult_Selects(List<ExamResult.STResultsEntity> result_Selects) {
        this.result_Selects = result_Selects;
    }

    public void addOnItemClickListener(QuestionListViewAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setCanPoint(boolean canPoint) {
        this.canPoint= canPoint;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        PointButton pointButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            pointButton = (PointButton) itemView.findViewById(R.id.view_question);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClicked(getAdapterPosition());
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public interface onItemClick {
        void onClicked(int position);
    }
}
