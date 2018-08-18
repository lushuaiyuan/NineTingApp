package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.ConditionEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConditionAdapter extends BaseAdapter {
    private List<ConditionEntity> conditions;

    public ConditionAdapter(List<ConditionEntity> conditions) {
        this.conditions = conditions;
    }


    @Override
    public int getCount() {
        return conditions == null ? 0 : conditions.size();
    }

    @Override
    public ConditionEntity getItem(int i) {
        return conditions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.get()).inflate(R.layout.item_condition, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvConditionName.setText(getItem(i).getName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_conditionName)
        TextView tvConditionName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
