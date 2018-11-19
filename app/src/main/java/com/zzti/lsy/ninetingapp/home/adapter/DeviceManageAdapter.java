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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            //得到对应position的数据集
            DeviceManageEntity deviceManageEntity = (DeviceManageEntity) dataList.get(position).getT();
            ((HeadViewHolder) holder).projectName.setText(deviceManageEntity.getProjectName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, position);
                    }
                }
            });
        }
        if (holder instanceof ContentViewHolder) {
            //从数据集合中取出该项
            DeviceManageEntity.DeviceDetial deviceDetial = (DeviceManageEntity.DeviceDetial) dataList.get(position).getT();
            ((ContentViewHolder) holder).tv_amount.setText(deviceDetial.getCmount());
            ((ContentViewHolder) holder).tv_carType.setText(deviceDetial.getCarType());
            ((ContentViewHolder) holder).tv_repair_normal_Amount.setText(deviceDetial.getRepairAmount() + "/" + deviceDetial.getNormalAmount());
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getDataType();
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
        public TextView tv_repair_normal_Amount;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tv_carType = itemView.findViewById(R.id.tv_carType);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_repair_normal_Amount = itemView.findViewById(R.id.tv_repair_normal_Amount);
        }
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);

    }


}
