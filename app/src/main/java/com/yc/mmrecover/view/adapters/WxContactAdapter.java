package com.yc.mmrecover.view.adapters;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.WxContactInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.view.wdiget.FilletImageView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


public class WxContactAdapter extends BaseQuickAdapter<WxContactInfo, BaseViewHolder> {

    private boolean mIsContactList;

    public WxContactAdapter(@Nullable List<WxContactInfo> data, boolean isContactList) {
        super(R.layout.item_wx_contact, data);
        this.mIsContactList = isContactList;

    }

    @Override
    protected void convert(BaseViewHolder helper, WxContactInfo item) {

        if (item != null) {
            helper.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_content, item.getContent());


//            Log.e("TAG", "content: " + item.getContent());
            Glide.with(mContext).asBitmap().load(item.getHeadPath()).error(R.mipmap.user_head)
                    .circleCrop().diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(true)
                    .into((FilletImageView) helper.getView(R.id.im_head));
            if (this.mIsContactList) {
                helper.setGone(R.id.tv_content, false);
            } else {
                helper.setGone(R.id.tv_content, true);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("wx contact content = ");
                stringBuilder.append(item.getContent());
                helper.setText(R.id.tv_time, Func.formatData("yyyy/MM/dd", (long) item.getLastTime()));
            }
            if (item.getContactType() == 0) {
                helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.red_word));
            } else {
                helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.black_word));
            }

        }

    }
}
