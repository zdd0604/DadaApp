package com.dadaxueche.student.dadaapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dadaxueche.student.dadaapp.R;

import java.util.ArrayList;
import java.util.List;

public class VoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Light_Voice_Bean> list_voice = new ArrayList<>();

    private boolean[] selects = new boolean[list_voice.size()];

    private OnItemClick onItemClick;

    private Context context;

    private int color_no_select = R.color.White;

    private int color_select = R.color.MyGrey;

    public VoiceAdapter(List<Light_Voice_Bean> list,boolean[] selects) {
        this.list_voice = list;
        this.selects = selects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voice_item_layout,parent,false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        if(selects[position]) {
            myViewHolder.setBackgroundColor(context.getResources().getColor(color_select));
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.yuyinbofang));
        } else {
            myViewHolder.setBackgroundColor(context.getResources().getColor(color_no_select));
            myViewHolder.imageView.setImageResource(list_voice.get(position).getImg());
        }
        myViewHolder.textView.setText(list_voice.get(position).getText());
    }

    public void setSelects(boolean[] selects) {
        this.selects = selects;
    }

    @Override
    public int getItemCount() {
        return list_voice.size();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;
        private View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.light_item_img);
            this.textView = (TextView) itemView.findViewById(R.id.light_item_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClick(getAdapterPosition());
        }

        private void setBackgroundColor(int color) {
            view.setBackgroundColor(color);
        }

    }

    public interface OnItemClick {
        void onItemClick(int position);
    }
}