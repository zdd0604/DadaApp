package com.dadaxueche.student.dadaapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dada.mylibrary.Gson.K2K3VideoList;
import com.dadaxueche.student.dadaapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2015-10-07
 * Time: 16:20
 */
public class K2K3VideoListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<K2K3VideoList> list;
    private String photo;

    public K2K3VideoListAdapter(Context context, ArrayList<K2K3VideoList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.k2k3videoitem, parent, false);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.k2k3videoitem_image);
            viewHolder.size = (TextView) convertView.findViewById(R.id.k2k3videoitem_size);
            viewHolder.down = (TextView) convertView.findViewById(R.id.k2k3videoitem_isdown);
            viewHolder.name = (TextView) convertView.findViewById(R.id.k2k3videoitem_name);
            viewHolder.hot = (TextView) convertView.findViewById(R.id.k2k3videoitem_ishot);
            viewHolder.relasize = (LinearLayout) convertView.findViewById(R.id.relasize);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        photo = list.get(position).getPhotourl();
        if (!"".equals(photo)) {
            viewHolder.relasize.setMinimumWidth(viewHolder.img.getWidth());
            ImageLoader.getInstance().displayImage(photo, viewHolder.img);
        }
        viewHolder.size.setText(list.get(position).getSize());
        viewHolder.name.setText(list.get(position).getName());
        if ("热门".equals(list.get(position).getIshot())) {
            viewHolder.hot.setVisibility(View.VISIBLE);
        } else if ("".equals(list.get(position).getIshot())) {
            viewHolder.hot.setVisibility(View.GONE);
        }
        if (list.get(position).isdown()) {
            viewHolder.down.setText("已下载");
            viewHolder.down.setTextColor(Color.GREEN);
        } else {
            viewHolder.down.setText("未下载");
            viewHolder.down.setTextColor(Color.GRAY);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView size;
        TextView name;
        TextView hot;
        TextView down;
        LinearLayout relasize;
    }
}