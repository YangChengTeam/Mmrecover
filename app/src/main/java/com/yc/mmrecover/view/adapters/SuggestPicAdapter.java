package com.yc.mmrecover.view.adapters;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.UploadInfo;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2019/12/14 11:31.
 */
public class SuggestPicAdapter extends BaseMultiItemQuickAdapter<UploadInfo, BaseViewHolder> {
    public SuggestPicAdapter(@Nullable List<UploadInfo> data) {
        super(data);
        addItemType(UploadInfo.ITEM_CASE, R.layout.suggest_item_view);
        addItemType(UploadInfo.ITEM_CONTENT, R.layout.suggest_item_view);

    }

    @Override
    protected void convert(BaseViewHolder helper, UploadInfo item) {

        ImageView imageView = helper.getView(R.id.iv_pic);


        if (item.getItemType() == UploadInfo.ITEM_CASE) {
            imageView.setImageResource(R.mipmap.login_user);
            helper.setGone(R.id.iv_delete, false);
        } else {
            Glide.with(mContext).load(item.getUrl()).error(R.mipmap.login_user).into(imageView);
            helper.setGone(R.id.iv_delete, true).addOnClickListener(R.id.iv_delete);
        }

    }
}
