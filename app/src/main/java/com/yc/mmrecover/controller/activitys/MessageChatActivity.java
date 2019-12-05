package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kk.utils.LogUtil;
import com.kk.utils.TaskUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.constant.Config;
import com.yc.mmrecover.model.bean.WxAccountInfo;
import com.yc.mmrecover.model.bean.WxChatMsgInfo;
import com.yc.mmrecover.utils.MessageUtils;
import com.yc.mmrecover.utils.SpUtils;
import com.yc.mmrecover.view.adapters.WxMsgAdapterNew;

import java.util.List;

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
    private WxMsgAdapterNew mAdapter;
    private boolean mIsloading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_chat;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            this.mFriendUid = intent.getStringExtra("uid");
            this.mParent = intent.getStringExtra("account_parent");
            this.mUid = intent.getStringExtra("self_uid");
            this.mUserName = intent.getStringExtra("name");
        }


        Log.e("TAG", "initViews: " + mFriendUid);
        initRecyclerView();
        scan();

    }

    private void initRecyclerView() {
        mAdapter = new WxMsgAdapterNew(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setAdapter(mAdapter);
        recyclerView.scrollToPosition(this.mAdapter.getItemCount() - 1);
//        recyclerView.addOnScrollListener(new C08432());
//        TextView textView = (TextView) findViewById(C0810R.C0809id.tv_buy);
//        textView.setBackgroundDrawable(new BackgroundShape(this, 3, C0810R.color.blue));
//        textView.setOnClickListener(new C08443());


    }

    private void scan() {
        this.mRlMask.setVisibility(View.VISIBLE);


        TaskUtil.getImpl().runTask(() -> {


            List<WxChatMsgInfo> wxChatMsgInfos = MessageUtils.getWxMsgInfos(this.mFriendUid);

            if (wxChatMsgInfos != null && wxChatMsgInfos.size() > 0) {
                for (WxChatMsgInfo wxChatMsgInfo : wxChatMsgInfos) {
                    if (wxChatMsgInfo.isSend()) {
                        wxChatMsgInfo.setType(WxChatMsgInfo.TYPE_ME);
                        wxChatMsgInfo.setHeadPath(SpUtils.getInstance().getString(Config.HEAD_PATH));
                    } else {
                        wxChatMsgInfo.setType(WxChatMsgInfo.TYPE_FRIEND);
                    }
                }
            }
//            Log.e("TAG", "wxChatMsgInfos" + JSON.toJSONString(wxChatMsgInfos));
            VUiKit.post(() -> {
                this.mRlMask.setVisibility(View.GONE);
                mAdapter.setNewData(wxChatMsgInfos);
            });
        });
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


}
