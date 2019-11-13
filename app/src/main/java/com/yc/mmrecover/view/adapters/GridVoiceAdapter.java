package com.yc.mmrecover.view.adapters;


import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.ScreenUtil;
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

        int width = ScreenUtil.getWidth(mContext);
        int imgWH = (width - ScreenUtil.dip2px(mContext, 32) - ScreenUtil.dip2px(mContext, 30)) / 4;
        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.width = imgWH;
        layoutParams.height = imgWH;

        Log.d(TAG, "convert: getFileName " + item.getFileName());

        helper.setText(R.id.tv_voice_time, Func.formatData("yyyy/MM/dd HH:mm:ss", item.getLastModifyTime()));
        helper.setVisible(R.id.im_select, item.isSelect());
    }


}
