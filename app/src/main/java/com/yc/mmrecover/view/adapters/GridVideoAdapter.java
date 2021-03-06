package com.yc.mmrecover.view.adapters;

import android.view.ViewGroup;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.ScreenUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;

import java.util.List;

public class GridVideoAdapter extends BaseQuickAdapter<MediaInfo, BaseViewHolder> {
    public GridVideoAdapter(@Nullable List<MediaInfo> data) {
        super(R.layout.item_gridview_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaInfo item) {
        int width = ScreenUtil.getWidth(mContext);
        int imgWH = (width - ScreenUtil.dip2px(mContext, 32) - ScreenUtil.dip2px(mContext, 30)) / 4;
        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.width = imgWH;
        layoutParams.height = imgWH;
        Glide.with(this.mContext).load(item.getPath()).into((ImageView) helper.getView(R.id.im_image));
        helper.setText(R.id.tv_size, item.getStrSize());
        helper.setVisible(R.id.im_select, item.isSelect());
    }


}
