package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.ProductRecordEntity;

import java.util.List;

/**
 * @author lsy
 * @create 2018/12/5 14:25
 * @Describe 生产记录的适配器
 */
public class ProductRecordAdapter extends BaseQuickAdapter<ProductRecordEntity, BaseViewHolder> {
    public ProductRecordAdapter(List<ProductRecordEntity> productRecordEntities) {
        super(R.layout.item_product_record, productRecordEntities);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductRecordEntity item) {
        helper.setText(R.id.tv_carNumber, item.getCarNumber())
                .setText(R.id.tv_time, item.getTime())
                .setText(R.id.tv_amount, item.getProductAmount())
                .setText(R.id.tv_oilMass, item.getOilMass())
                .setText(R.id.tv_oilMassRatio, item.getOrilMassRatio());
    }

}
