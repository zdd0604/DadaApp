package com.dadaxueche.student.dadaapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dadaxueche.student.dadaapp.R;

import java.util.List;


/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2015-09-18
 * Time: 13:20
 */
public class LGradeAdapter extends BaseAdapter {
    private Context context;
    private List<GradeBean> list;

    public LGradeAdapter(Context context, List<GradeBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_my_grades_item, parent,false);
            viewHolder.examtime = (TextView) convertView.findViewById(R.id.exam_time);
            viewHolder.usetime = (TextView) convertView.findViewById(R.id.exam_usetime);
            viewHolder.grade = (TextView) convertView.findViewById(R.id.exam_grade);
            viewHolder.evaluate = (TextView) convertView.findViewById(R.id.grade_evaluate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.examtime.setText(list.get(position).getBegintime());
        viewHolder.usetime.setText(list.get(position).getUsetime());
        viewHolder.grade.setText(list.get(position).getGrade());
        viewHolder.evaluate.setText(list.get(position).getEvaluate());
        return convertView;
    }

    class ViewHolder {
        TextView examtime;
        TextView usetime;
        TextView grade;
        TextView evaluate;
    }
}