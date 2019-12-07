package com.yc.mmrecover.view.adapters;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.activitys.DetailImageActivity;
import com.yc.mmrecover.controller.activitys.DetailUserActivity;
import com.yc.mmrecover.controller.activitys.MessageDetailActivity;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.model.bean.WxChatMsgInfo;
import com.yc.mmrecover.utils.Func;

import java.io.File;
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
                    helper.setText(R.id.tv_msg, item.getContent()).addOnClickListener(R.id.im_head);
                    Glide.with(mContext).asBitmap().load(item.getHeadPath()).circleCrop().into((ImageView) helper.getView(R.id.im_head));


                    helper.getView(R.id.im_head).setOnClickListener(view -> {
                        Intent intent = new Intent(mContext, DetailUserActivity.class);
                        intent.putExtra("uid", item.getUid());
                        intent.putExtra("is_from_chat", true);
                        mContext.startActivity(intent);
                    });
                    int contentType = item.getContentType();

                    if (contentType == 3 || contentType == 43 || contentType == 47) {
                        helper.setGone(R.id.im_pic, true)
                                .setGone(R.id.ll_voice, false);

                        String imgPath = item.getImgPath();
                        if (contentType == 43) {
                            helper.setGone(R.id.im_video_mask, true)
                                    .addOnClickListener(R.id.im_pic);


//                            viewHolder2.im_pic.setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View view) {
//
//                                }
//                            });
                            imgPath = item.getVideoPath();
                        } else if (contentType == 3) {
                            helper.setGone(R.id.im_video_mask, false)
                                    .addOnClickListener(R.id.im_pic);
                            Glide.with(mContext).asBitmap().load(item.getImgPath()).into((ImageView) helper.getView(R.id.im_pic));
                            helper.getView(R.id.im_pic)
//                            String finalImgPath = imgPath;
                                    .setOnClickListener(view -> {
                                        Intent intent = new Intent(mContext, DetailImageActivity.class);
                                        String imgPath1 = item.getImgPath();
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("absolutePath = ");
                                        stringBuilder.append(imgPath1);
                                        Log.d("TAG", stringBuilder.toString());
                                        File file = new File(imgPath1);
                                        long length = file.length();
                                        StringBuilder stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("length = ");
                                        stringBuilder2.append(length);
                                        Log.d("TAG", stringBuilder2.toString());
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeFile(imgPath1, options);
                                        MediaInfo mediaBean = new MediaInfo();
                                        mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
                                        mediaBean.setPath(item.getImgPath());
                                        mediaBean.setSize(length);
                                        mediaBean.setStrSize(Func.getSizeString(length));
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("getStrSize = ");
                                        stringBuilder3.append(mediaBean.getStrSize());
                                        Log.d("TAG", stringBuilder3.toString());
                                        mediaBean.setWidth(options.outWidth);
                                        mediaBean.setHeight(options.outHeight);
                                        intent.putExtra("image_info", mediaBean);
                                        mContext.startActivity(intent);
                                    });
                        } else {
                            helper.setGone(R.id.im_video_mask, false);
                        }

                        if (TextUtils.isEmpty(imgPath)) {
                            helper.setGone(R.id.tv_msg, true);

                            return;
                        }
                        helper.setGone(R.id.tv_msg, false);

                        Glide.with(mContext).load(item.getImgPath()).into((ImageView) helper.getView(R.id.im_pic));
                    } else if (contentType == 34) {
                        helper.setGone(R.id.ll_voice, true)
                                .addOnClickListener(R.id.ll_voice);

//                        viewHolder2.ll_voice.setOnClickListener(view -> {
//                            if (GlobalData.vipType == 1) {
//                                Intent intent = new Intent(mContext, PayActivity.class);
//                                intent.putExtra("sta_type", 2007);
//                                mContext.startActivity(intent);
//                                return;
//                            }
//                            new PlayVoiceTask(null).execute(WxMsgAdapter.this.mDataList.get(i).getVoicePath());
//                        });
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
//                    helper.setText(R.id.tv_system, Html.fromHtml(item.getContent()))
//                            .setGone(R.id.tv_system, true)
//                            .setGone(R.id.tv_time, false);

                    break;
            }
        }


    }

}
