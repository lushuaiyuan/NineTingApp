package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.FactoryInfoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：anxin on 2018/8/7 14:54
 * 生产厂家适配器
 */
public class FactoryAdapter extends BaseAdapter {
    private List<FactoryInfoEntity> factoryInfoEntities;

    public FactoryAdapter(List<FactoryInfoEntity> factoryInfoEntities) {
        this.factoryInfoEntities = factoryInfoEntities;
    }

    @Override
    public int getCount() {
        return factoryInfoEntities == null ? 0 : factoryInfoEntities.size();
    }

    @Override
    public FactoryInfoEntity getItem(int i) {
        return factoryInfoEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.get()).inflate(R.layout.item_text, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(getItem(i).getFactoryName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
