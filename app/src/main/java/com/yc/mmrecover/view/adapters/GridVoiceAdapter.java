package com.yc.mmrecover.view.adapters;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;

import java.util.List;

public class GridVoiceAdapter extends BaseQuickAdapter<MediaInfo, BaseViewHolder> {

    public GridVoiceAdapter(@Nullable List<MediaInfo> data) {
        super(R.layout.item_gridview_voice, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaInfo item) {
        helper.setText(R.id.tv_voice_time, Func.formatData("yyyy/MM/dd HH:mm:ss", item.getLastModifyTime()));
        helper.setVisible(R.id.im_select, item.isSelect());
    }


}
