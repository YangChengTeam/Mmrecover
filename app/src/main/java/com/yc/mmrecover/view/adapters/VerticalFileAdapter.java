package com.yc.mmrecover.view.adapters;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;

import java.util.List;

/**
 * Created by caokun on 2019/11/13 14:09.
 */

public class VerticalFileAdapter extends BaseQuickAdapter<MediaInfo, BaseViewHolder> {

    public VerticalFileAdapter(@Nullable List<MediaInfo> data) {
        super(R.layout.item_view_file, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaInfo item) {
//        Glide.with(this.mContext).load(item.getPath()).into((ImageView) helper.getView(R.id.im_image));
        helper.setText(R.id.tv_time2, item.getFileName());
        helper.setText(R.id.tv_name, item.getFileName());
        helper.setVisible(R.id.im_select, item.isSelect());
    }


}
