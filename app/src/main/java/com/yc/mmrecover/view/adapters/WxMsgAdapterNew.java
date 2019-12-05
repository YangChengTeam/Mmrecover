package com.yc.mmrecover.view.adapters;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.WxChatMsgInfo;

import java.util.List;

/**
 * Created by suns  on 2019/12/4 14:44.
 */
public class WxMsgAdapterNew extends BaseMultiItemQuickAdapter<WxChatMsgInfo, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param
     */
    public WxMsgAdapterNew(List<WxChatMsgInfo> data) {
        super(data);
        addItemType(WxChatMsgInfo.TYPE_ME, R.layout.item_chat_right);
        addItemType(WxChatMsgInfo.TYPE_FRIEND, R.layout.item_chat_left);
        addItemType(WxChatMsgInfo.TYPE_TITLE, R.layout.item_chat_time);

    }

    @Override
    protected void convert(BaseViewHolder helper, WxChatMsgInfo item) {
        if (item != null) {
            switch (item.getType()) {
                case WxChatMsgInfo.TYPE_ME:
                case WxChatMsgInfo.TYPE_FRIEND:
                    helper.setText(R.id.tv_msg, item.getContent());
                    Glide.with(mContext).asBitmap().load(item.getHeadPath()).circleCrop().into((ImageView) helper.getView(R.id.im_head));
                    break;
                case WxChatMsgInfo.TYPE_TITLE:
                    break;
            }
        }
    }

}
