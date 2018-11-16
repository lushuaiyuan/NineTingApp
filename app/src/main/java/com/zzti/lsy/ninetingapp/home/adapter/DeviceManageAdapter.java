package com.zzti.lsy.ninetingapp.home.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.DeviceManageEntity;
import com.zzti.lsy.ninetingapp.entity.RecycleViewItemData;

import java.util.List;

/**
 * @author lsy
 * @create 2018/11/15 21:33
 * @Describe
 */
public class DeviceManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TITLE = 1;//表头
    private static final int TYPE_CONTENT = 2;//内容
    private List<RecycleViewItemData> dataList;//数据集合

    public DeviceManageAdapter(List<RecycleViewItemData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_manage_title, parent, false);
            HeadViewHolder viewHolder = new HeadViewHolder(view);
            return viewHolder;
        }
        if (viewType == TYPE_CONTENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_manage_content, parent, false);
            ContentViewHolder viewHolder = new ContentViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            //得到对应position的数据集
            String projectName = (String) dataList.get(position).getT();
            ((HeadViewHolder) holder).projectName.setText(projectName);
        }
        if (holder instanceof ContentViewHolder) {
            //从数据集合中取出该项
            DeviceManageEntity.DeviceDetial deviceDetial = (DeviceManageEntity.DeviceDetial) dataList.get(position).getT();
            ((ContentViewHolder) holder).tv_amount.setText(deviceDetial.getAmount() + "辆");
            ((ContentViewHolder) holder).tv_carType.setText(deviceDetial.getCarType());
            ((ContentViewHolder) holder).tv_repairAmount.setText("维修" + deviceDetial.getRepairAmount() + "辆");
            ((ContentViewHolder) holder).tv_normalAmount.setText("正常" + deviceDetial.getNormalAmount() + "辆");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class HeadViewHolder extends RecyclerView.ViewHolder {
        public TextView projectName;

        public HeadViewHolder(View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.tv_projectName);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_carType;
        public TextView tv_amount;
        public TextView tv_repairAmount;
        public TextView tv_normalAmount;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tv_carType = itemView.findViewById(R.id.tv_carType);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_repairAmount = itemView.findViewById(R.id.tv_repairAmount);
            tv_normalAmount = itemView.findViewById(R.id.tv_normalAmount);
        }
    }

}
