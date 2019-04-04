package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.StatisticalList;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author lsy
 * @create 2018/12/5 14:25
 * @Describe 生产记录的适配器
 */
public class ProductRecordAdapter extends BaseQuickAdapter<StatisticalList, BaseViewHolder> {
    public ProductRecordAdapter(List<StatisticalList> statisticalLists) {
        super(R.layout.item_product_record, statisticalLists);
    }

    DecimalFormat df = new DecimalFormat("#0.00");

    @Override
    protected void convert(BaseViewHolder helper, StatisticalList item) {
        helper.setText(R.id.tv_carNumber, item.getPlateNumber())
                .setText(R.id.tv_time, item.getSlDateTime())
                .setText(R.id.tv_amount, item.getSquareQuantity())
                .setText(R.id.tv_oilMass, item.getQilWear())
                .setText(R.id.tv_oilMassRatio, df.format((Double.parseDouble(item.getQilWear()) / Double.parseDouble(item.getSquareQuantity()))));
    }

}
