package com.dadaxueche.student.dadaapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;

import java.util.HashMap;
import java.util.List;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2015-09-18
 * Time: 13:20
 */
public class LGradeEditAdapter extends BaseAdapter {
    private Context context;
    private List<GradeBean> list;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected = new HashMap<>();

    public LGradeEditAdapter(Context context, List<GradeBean> list) {
        this.context = context;
        this.list = list;
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_my_grades_item_edit, null);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.detailscheckbox);
            viewHolder.examtime = (TextView) convertView.findViewById(R.id.exam_time);
            viewHolder.usetime = (TextView) convertView.findViewById(R.id.exam_usetime);
            viewHolder.grade = (TextView) convertView.findViewById(R.id.exam_grade);
            viewHolder.evaluate = (TextView) convertView.findViewById(R.id.grade_evaluate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GradeBean bean = list.get(position);
        boolean canRemove = bean.isCanRemove();
        viewHolder.examtime.setText(list.get(position).getBegintime());
        viewHolder.usetime.setText(list.get(position).getUsetime());
        viewHolder.grade.setText(list.get(position).getGrade());
        viewHolder.evaluate.setText(list.get(position).getEvaluate());
        if (GlobalData.getInstance().flag) {
            if (canRemove) {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
                if (getIsSelected().get(position) == null) {
                    getIsSelected().put(position, false);
                }
                viewHolder.checkBox.setChecked(getIsSelected().get(position));
            } else {
                viewHolder.checkBox.setChecked(false);
            }
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                isSelected.put(position, isChecked);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView examtime;
        TextView usetime;
        TextView grade;
        TextView evaluate;
        CheckBox checkBox;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        LGradeEditAdapter.isSelected = isSelected;
    }

    public void remove(int position) {
        this.list.remove(position);
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return this.isSelected;
    }

}