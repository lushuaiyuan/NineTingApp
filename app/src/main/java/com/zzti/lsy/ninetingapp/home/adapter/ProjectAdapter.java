package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;
import com.zzti.lsy.ninetingapp.entity.ProEntity;
import com.zzti.lsy.ninetingapp.entity.ProjectEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：anxin on 2018/8/7 14:54
 * 项目部
 */
public class ProjectAdapter extends BaseAdapter {
    private List<ProjectEntity> projectEntities;

    public ProjectAdapter(List<ProjectEntity> projectEntities) {
        this.projectEntities = projectEntities;
    }

    @Override
    public int getCount() {
        return projectEntities == null ? 0 : projectEntities.size();
    }

    @Override
    public ProjectEntity getItem(int i) {
        return projectEntities.get(i);
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
        viewHolder.tvName.setText(getItem(i).getProjectName());
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
