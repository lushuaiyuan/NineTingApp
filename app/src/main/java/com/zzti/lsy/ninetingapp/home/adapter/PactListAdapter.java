package com.zzti.lsy.ninetingapp.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.PactInfo;

import java.util.List;

/**
 * author：anxin on 2018/10/12 19:16
 * 合同列表的适配器
 */
public class PactListAdapter extends BaseQuickAdapter<PactInfo, BaseViewHolder> {
    public PactListAdapter(List<PactInfo> pactInfos) {
        super(R.layout.item_pact_list, pactInfos);
    }

    @Override
    protected void convert(BaseViewHolder helper, PactInfo item) {

    }
}
