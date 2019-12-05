package com.yc.mmrecover.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.activitys.MessageDetailActivity;
import com.yc.mmrecover.controller.activitys.PayActivity;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.WxChatMsgInfo;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by suns  on 2019/12/4 14:44.
 */
public class WxMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param
     */
//    public WxMsgAdapter(List<WxChatMsgInfo> data) {
//        super(data);
////        addItemType();
//    }

//    @Override
//    protected void convert(BaseViewHolder helper, WxChatMsgInfo item) {
//
//    }

    private static final int LOAD_NUM = 10;
    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_ME = 0;
    public static final int TYPE_TITLE = 2;
    private Context mContext;
    private List<WxChatMsgInfo> mDataList;

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_system;
        TextView tv_time;

        TitleViewHolder(View view) {
            super(view);
            this.tv_time = view.findViewById(R.id.tv_time);
            this.tv_time.setBackgroundDrawable(new BackgroundShape(mContext, 3, R.color.gray_bk3));
            this.tv_system = view.findViewById(R.id.tv_system);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView im_head;
        ImageView im_pic;
        ImageView im_video_mask;
        LinearLayout ll_voice;
        TextView tv_msg;
        TextView tv_voice;

        ViewHolder(View view) {
            super(view);
            this.im_head = view.findViewById(R.id.im_head);
            this.tv_msg = view.findViewById(R.id.tv_msg);
            this.im_pic = view.findViewById(R.id.im_pic);
            this.im_video_mask = view.findViewById(R.id.im_video_mask);
            this.ll_voice = view.findViewById(R.id.ll_voice);
            this.tv_voice = view.findViewById(R.id.tv_voice);
        }
    }

    public WxMsgAdapter(List<WxChatMsgInfo> list) {
        this.mDataList = list;
    }

    public void setNewData(List<WxChatMsgInfo> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate;
        this.mContext = viewGroup.getContext();
        if (i == 1) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_left, viewGroup, false);
        } else if (i != 0) {
            return new TitleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_time, viewGroup, false));
        } else {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_right, viewGroup, false);
        }
        return new ViewHolder(inflate);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        WxChatMsgInfo wxChatMsgInfo = this.mDataList.get(i);
        int contentType = wxChatMsgInfo.getContentType();
        TitleViewHolder titleViewHolder;
        if (getItemViewType(i) != 2) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            Glide.with(this.mContext).load(wxChatMsgInfo.getHeadPath()).error(R.mipmap.user_head).into(viewHolder2.im_head);
            viewHolder2.im_head.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MessageDetailActivity.class);
                    intent.putExtra("uid", (wxChatMsgInfo).getUid());
                    intent.putExtra("is_from_chat", true);
                    mContext.startActivity(intent);
                }
            });
            if (contentType == 3 || contentType == 43 || contentType == 47) {
                viewHolder2.im_pic.setVisibility(View.VISIBLE);
                viewHolder2.ll_voice.setVisibility(View.GONE);
                String imgPath = wxChatMsgInfo.getImgPath();
                if (contentType == 43) {
                    viewHolder2.im_video_mask.setVisibility(View.VISIBLE);
                    viewHolder2.im_pic.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
//                            Intent intent = new Intent(mContext, DetailVideoActivity.class);
//                            intent.putExtra("sta_type", 2008);
//                            Serializable mediaBean = new MediaBean();
//                            mediaBean.setPath(((ChatMsgBean) ChatMsgAdapter.this.mDataList.get(i)).getVideoPath());
//                            intent.putExtra("image_info", mediaBean);
//                            ChatMsgAdapter.this.mContext.startActivity(intent);
                        }
                    });
                    imgPath = wxChatMsgInfo.getVideoPath();
                } else if (contentType == 3) {
                    viewHolder2.im_video_mask.setVisibility(View.GONE);
                    viewHolder2.im_pic.setOnClickListener(view -> {
                        Intent intent = new Intent(mContext, MessageDetailActivity.class);
                        String imgPath1 = wxChatMsgInfo.getImgPath();
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
//                            Serializable mediaBean = new MediaBean();
//                            mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
//                            mediaBean.setPath(imgPath);
//                            mediaBean.setSize(length);
//                            mediaBean.setStrSize(Func.getSizeString(length));
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("getStrSize = ");
//                            stringBuilder3.append(mediaBean.getStrSize());
                        Log.d("TAG", stringBuilder3.toString());
//                            mediaBean.setWidth(options.outWidth);
//                            mediaBean.setHeight(options.outHeight);
//                            intent.putExtra("image_info", mediaBean);
//                            ChatMsgAdapter.this.mContext.startActivity(intent);
                    });
                } else {
                    viewHolder2.im_video_mask.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(imgPath)) {
                    viewHolder2.tv_msg.setVisibility(View.VISIBLE);
                    return;
                }
                viewHolder2.tv_msg.setVisibility(View.GONE);
                Glide.with(this.mContext).load(imgPath).into(viewHolder2.im_pic);
            } else if (contentType == 34) {
                viewHolder2.ll_voice.setVisibility(View.VISIBLE);
                viewHolder2.ll_voice.setOnClickListener(view -> {
                    if (GlobalData.vipType == 1) {
                        Intent intent = new Intent(mContext, PayActivity.class);
                        intent.putExtra("sta_type", 2007);
                        mContext.startActivity(intent);
                        return;
                    }
//                        new PlayVoiceTask(null).execute(new String[]{((ChatMsgBean) ChatMsgAdapter.this.mDataList.get(i)).getVoicePath()});
                });
                viewHolder2.tv_msg.setVisibility(View.GONE);
                viewHolder2.im_pic.setVisibility(View.GONE);
                viewHolder2.im_video_mask.setVisibility(View.GONE);
                TextView textView = viewHolder2.tv_voice;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(wxChatMsgInfo.getVoiceSec());
                stringBuilder.append("\"");
                textView.setText(stringBuilder.toString());
            } else {
                viewHolder2.tv_msg.setText(wxChatMsgInfo.getContent());
                viewHolder2.tv_msg.setVisibility(View.GONE);
                viewHolder2.im_video_mask.setVisibility(View.VISIBLE);
                viewHolder2.im_pic.setVisibility(View.GONE);
                viewHolder2.ll_voice.setVisibility(View.GONE);
            }
        } else if (contentType == 10000 || contentType == 570425393) {
            titleViewHolder = (TitleViewHolder) viewHolder;
            titleViewHolder.tv_system.setText(Html.fromHtml(wxChatMsgInfo.getContent()));
            titleViewHolder.tv_system.setVisibility(View.VISIBLE);
            titleViewHolder.tv_time.setVisibility(View.GONE);
        } else {
            titleViewHolder = (TitleViewHolder) viewHolder;
            titleViewHolder.tv_time.setText(wxChatMsgInfo.getContent());
            titleViewHolder.tv_system.setVisibility(View.GONE);
            titleViewHolder.tv_time.setVisibility(View.VISIBLE);
        }
    }

    public int getItemViewType(int i) {
        return this.mDataList == null ? i : this.mDataList.get(i).getType();
    }


    public int getItemCount() {
        return this.mDataList == null ? 0 : this.mDataList.size();
    }
}
