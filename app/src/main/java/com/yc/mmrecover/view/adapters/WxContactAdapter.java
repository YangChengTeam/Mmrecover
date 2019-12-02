package com.yc.mmrecover.view.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.model.bean.WxContactInfo;

import java.util.List;

public class WxContactAdapter extends BaseQuickAdapter<WxContactInfo, BaseViewHolder> {

    public WxContactAdapter(@Nullable List<WxContactInfo> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WxContactInfo item) {

    }
}
