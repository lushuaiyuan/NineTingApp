package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;

import java.util.List;

/**
 * author：anxin on 2018/8/9 10:29
 * 维修配件的适配器
 */
public class PartsListAdapter extends BaseQuickAdapter<PartsInfoEntity, BaseViewHolder> {
    public PartsListAdapter(List<PartsInfoEntity> partsEntities) {
        super(R.layout.item_parts, partsEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, PartsInfoEntity item) {
        helper.setText(R.id.tv_partsName, item.getPartsName())
                .setText(R.id.tv_alarmValue, item.getAlarmNumber())
                .setText(R.id.tv_model, item.getPartsModel())
                .setText(R.id.tv_num, item.getPartsNumber())
                .setText(R.id.tv_price, item.getPurchasedPrice());
        if (Integer.parseInt(item.getAlarmNumber()) > Integer.parseInt(item.getPartsNumber())) {//库存数量小于告警值
            helper.setVisible(R.id.tv_status, true);
            helper.setTextColor(R.id.tv_status, App.get().getResources().getColor(R.color.color_red));
            helper.setText(R.id.tv_status, "库存不足");
        } else {
            helper.setVisible(R.id.tv_status, false);
        }


//        if (tag == 1) {//代表维修申请进来
//            helper.getView(R.id.tv_operator).setVisibility(View.GONE);
//        } else if (tag == 2) { //代表点击配件列表菜单进来
//            helper.getView(R.id.tv_operator).setVisibility(View.GONE);
//        }
    }

//    private int tag;//1代表维修申请进来（获取配件名称）  2代表点击配件列表菜单进来
//
//    public void setTag(int tag) {
//        this.tag = tag;
//    }
}
