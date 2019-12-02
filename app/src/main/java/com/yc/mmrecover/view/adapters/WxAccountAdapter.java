package com.yc.mmrecover.view.adapters;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.model.bean.WxAccountInfo;

import java.util.List;

public class WxAccountAdapter extends BaseQuickAdapter<WxAccountInfo, BaseViewHolder> {

    public WxAccountAdapter(@Nullable List<WxAccountInfo> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WxAccountInfo item) {

    }
}
