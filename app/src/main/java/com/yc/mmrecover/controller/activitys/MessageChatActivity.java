package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.utils.TaskUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.EventPayState;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.model.bean.WxChatMsgInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.MessageUtils;
import com.yc.mmrecover.utils.PlayVoiceTask;
import com.yc.mmrecover.utils.UserInfoHelper;
import com.yc.mmrecover.view.adapters.WxMsgAdapter;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MessageChatActivity extends BaseActivity {

    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.rl_buy)
    RelativeLayout rlBuy;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.rl_mask)
    RelativeLayout mRlMask;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String mFriendUid;
    private String mParent;
    private String mUid;
    private String mUserName;
    private WxMsgAdapter mAdapter;
    private boolean mIsloading;

    private Handler mTimeHandler = new Handler();
    private Runnable runnableDelay = new C08421();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_chat;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        if (intent != null) {
            this.mFriendUid = intent.getStringExtra("uid");
            this.mParent = intent.getStringExtra("account_parent");
            this.mUid = intent.getStringExtra("self_uid");
            this.mUserName = intent.getStringExtra("name");
        }

        initTitle(this.mUserName);

        initRecyclerView();
        scan();

    }

    private void initRecyclerView() {
        mAdapter = new WxMsgAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setAdapter(mAdapter);
        recyclerView.scrollToPosition(this.mAdapter.getItemCount() - 1);

        tvBuy.setBackgroundDrawable(new BackgroundShape(this, 3, R.color.blue));


        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            WxChatMsgInfo wxChatMsgInfo = mAdapter.getItem(position);
            if (null != wxChatMsgInfo) {
                if (wxChatMsgInfo.getType() == WxChatMsgInfo.TYPE_ME || wxChatMsgInfo.getType() == WxChatMsgInfo.TYPE_FRIEND) {
                    if (view.getId() == R.id.im_head) {
                        Intent intent = new Intent(MessageChatActivity.this, DetailUserActivity.class);
                        intent.putExtra("uid", wxChatMsgInfo.getUid());
                        intent.putExtra("is_from_chat", true);
                        startActivity(intent);
                    }
                    int contentType = wxChatMsgInfo.getContentType();
                    if (contentType == 43) {
                        if (view.getId() == R.id.im_pic) {
                            Intent intent = new Intent(MessageChatActivity.this, DetailVideoActivity.class);
                            intent.putExtra("sta_type", 2008);
                            MediaInfo mediaBean = new MediaInfo();
                            mediaBean.setPath(wxChatMsgInfo.getVideoPath());
                            intent.putExtra("image_info", mediaBean);
                            startActivity(intent);
                        }

                    } else if (contentType == 3) {
                        if (view.getId() == R.id.im_pic) {

                            Intent intent = new Intent(MessageChatActivity.this, DetailImageActivity.class);
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
                            MediaInfo mediaBean = new MediaInfo();
                            mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
                            mediaBean.setPath(wxChatMsgInfo.getVideoPath());
                            mediaBean.setSize(length);
                            mediaBean.setStrSize(Func.getSizeString(length));
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("getStrSize = ");
                            stringBuilder3.append(mediaBean.getStrSize());
                            Log.d("TAG", stringBuilder3.toString());
                            mediaBean.setWidth(options.outWidth);
                            mediaBean.setHeight(options.outHeight);
                            intent.putExtra("image_info", mediaBean);
                            startActivity(intent);
                        }
                    } else if (contentType == 34) {
                        if (view.getId() == R.id.ll_voice) {

                            if (UserInfoHelper.getVipType() == 0) {
                                Intent intent = new Intent(MessageChatActivity.this, PayActivity.class);
                                intent.putExtra("sta_type", 2007);
                                startActivity(intent);
                                return;
                            }
                            new PlayVoiceTask(null).execute(wxChatMsgInfo.getVoicePath());
                        }
                    }


                }
            }
        });

        RxView.clicks(tvBuy).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            startActivity(new Intent(this, PayActivity.class));
        });
    }

    private void scan() {
        this.mRlMask.setVisibility(View.VISIBLE);


        TaskUtil.getImpl().runTask(() -> {


            List<WxChatMsgInfo> wxChatMsgInfos = MessageUtils.getWxMsgInfos(this.mFriendUid);


//            if (wxChatMsgInfos != null && wxChatMsgInfos.size() > 0) {
//                for (WxChatMsgInfo wxChatMsgInfo : wxChatMsgInfos) {
////                    if (wxChatMsgInfo.isSend()) {
////                        wxChatMsgInfo.setType(WxChatMsgInfo.TYPE_ME);
//////                        wxChatMsgInfo.setHeadPath(SpUtils.getInstance().getString(Config.HEAD_PATH));
////                    } else {
////                        wxChatMsgInfo.setType(WxChatMsgInfo.TYPE_FRIEND);
////                    }
//
//                    Log.e("TAG", "scan: "+wxChatMsgInfo.toString() );
//                }
//            }

            Log.e("TAG", "wxChatMsgInfos" + JSON.toJSONString(wxChatMsgInfos));
            VUiKit.post(() -> {
                this.mIsloading = false;
                this.mRlMask.setVisibility(View.GONE);
                if (wxChatMsgInfos == null || wxChatMsgInfos.size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    return;
                }

                mAdapter.setNewData(wxChatMsgInfos);
                recyclerView.scrollToPosition(this.mAdapter.getItemCount() - 1);
                this.mTimeHandler.postDelayed(this.runnableDelay, 500);

            });
        });
    }

    class C08421 implements Runnable {

        public void run() {
            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int i) {
            this.space = i;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.left = this.space;
            outRect.right = this.space;
            outRect.bottom = this.space;
            if (recyclerView.getChildLayoutPosition(view) == 0) {
                outRect.top = this.space;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfoHelper.getVipType() == 2) {
            findViewById(R.id.rl_buy).setVisibility(View.GONE);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("GlobalData.vipType = ");
            stringBuilder.append(GlobalData.vipType);
            Log.d("TAG", stringBuilder.toString());

            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void paySuccess(EventPayState eventPayState) {
        rlBuy.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
