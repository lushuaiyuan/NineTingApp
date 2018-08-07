package com.zzti.lsy.ninetingapp.home.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.home.entity.ProjectAddressEntitiy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：anxin on 2018/8/7 14:54
 * 项目部适配器
 */
public class ProjectAddressAdapter extends BaseAdapter {
    private List<ProjectAddressEntitiy> projectAddressEntitiys;

    public ProjectAddressAdapter(List<ProjectAddressEntitiy> projectAddressEntitiys) {
        this.projectAddressEntitiys = projectAddressEntitiys;
    }

    @Override
    public int getCount() {
        return projectAddressEntitiys == null ? 0 : projectAddressEntitiys.size();
    }

    @Override
    public ProjectAddressEntitiy getItem(int i) {
        return projectAddressEntitiys.get(i);
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
        viewHolder.tvProjectAddressName.setText(getItem(i).getName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_projectAddress_name)
        TextView tvProjectAddressName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
