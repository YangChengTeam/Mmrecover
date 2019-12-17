package com.yc.mmrecover.view.adapters;

import android.text.Html;
import android.text.TextUtils;
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
public class WxMsgAdapter extends BaseMultiItemQuickAdapter<WxChatMsgInfo, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param
     */
    public WxMsgAdapter(List<WxChatMsgInfo> data) {
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
                    helper.setText(R.id.tv_msg, item.getContent()).addOnClickListener(R.id.im_head);
                    Glide.with(mContext).asBitmap().load(item.getHeadPath()).error(R.mipmap.user_head).circleCrop().into((ImageView) helper.getView(R.id.im_head));


                    int contentType = item.getContentType();

                    if (contentType == 3 || contentType == 43 || contentType == 47) {
                        helper.setGone(R.id.im_pic, true)
                                .setGone(R.id.ll_voice, false);

                        String imgPath = item.getImgPath();
                        if (contentType == 43) {
                            helper.setGone(R.id.im_video_mask, true)
                                    .addOnClickListener(R.id.im_pic);

                            imgPath = item.getVideoPath();

                        } else if (contentType == 3) {
                            helper.setGone(R.id.im_video_mask, false)
                                    .addOnClickListener(R.id.im_pic);
                            Glide.with(mContext).asBitmap().load(item.getImgPath()).error(R.mipmap.water_mark).into((ImageView) helper.getView(R.id.im_pic));
//
                        } else {
                            helper.setGone(R.id.im_video_mask, false);
                        }

                        if (TextUtils.isEmpty(imgPath)) {
                            helper.setGone(R.id.tv_msg, true);

                            return;
                        }
                        helper.setGone(R.id.tv_msg, false);

                        Glide.with(mContext).load(item.getImgPath()).error(R.mipmap.water_mark).into((ImageView) helper.getView(R.id.im_pic));
                    } else if (contentType == 34) {
                        helper.setGone(R.id.ll_voice, true)
                                .addOnClickListener(R.id.ll_voice);

                        helper.setGone(R.id.tv_msg, false)
                                .setGone(R.id.im_pic, false)
                                .setGone(R.id.im_video_mask, false);


                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(item.getVoiceSec());
                        stringBuilder.append("\"");
                        helper.setText(R.id.tv_voice, stringBuilder.toString());
                    } else {
                        helper.setText(R.id.tv_msg, item.getContent())
                                .setGone(R.id.tv_msg, true)
                                .setGone(R.id.im_video_mask, false)
                                .setGone(R.id.im_pic, false)
                                .setGone(R.id.ll_voice, false);

                    }
                    break;

                case WxChatMsgInfo.TYPE_TITLE:
                    contentType = item.getContentType();
                    if (contentType == 10000 || contentType == 570425393) {
                        helper.setText(R.id.tv_system, Html.fromHtml(item.getContent()))
                                .setGone(R.id.tv_system, true)
                                .setGone(R.id.tv_time, false);
                    } else {

                        helper.setText(R.id.tv_time, Html.fromHtml(item.getContent()))
                                .setGone(R.id.tv_system, false)
                                .setGone(R.id.tv_time, true);
                    }

                    break;
            }
        }


    }

}
