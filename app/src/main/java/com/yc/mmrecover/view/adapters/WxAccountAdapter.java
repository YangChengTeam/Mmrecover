package com.yc.mmrecover.view.adapters;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.WxAccountInfo;

import java.util.List;

import androidx.annotation.Nullable;

public class WxAccountAdapter extends BaseQuickAdapter<WxAccountInfo, BaseViewHolder> {

    public WxAccountAdapter(@Nullable List<WxAccountInfo> data) {
        super(R.layout.item_wx_accout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WxAccountInfo item) {
        if (item != null) {
            try {


                helper.setText(R.id.tv_name, "昵称:" + item.getNickName())
                        .setText(R.id.tv_phone, "手机号:" + item.getPhone())
                        .setText(R.id.tv_wx, "微信号:" + item.getWxAccount());

                Glide.with(mContext).asBitmap().load(item.getHeadPath()).circleCrop().error(R.mipmap.user_head).into((ImageView) helper.getView(R.id.im_head));
            } catch (Exception e) {
                Log.e(TAG, "convert: " + e.getMessage());
            }
        }
    }
}
