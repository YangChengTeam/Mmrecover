package com.yc.mmrecover.view.adapters;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.UploadInfo;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2019/12/14 11:31.
 */
public class SuggestPicAdapter extends BaseQuickAdapter<UploadInfo, BaseViewHolder> {
    public SuggestPicAdapter(@Nullable List<UploadInfo> data) {
        super(R.layout.suggest_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UploadInfo item) {
        Glide.with(mContext).load(item.getUrl()).error(R.mipmap.login_user).into((ImageView) helper.getView(R.id.iv_pic));
    }
}
