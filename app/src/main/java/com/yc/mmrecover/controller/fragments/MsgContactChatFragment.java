package com.yc.mmrecover.controller.fragments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.utils.TaskUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.activitys.MessageChatActivity;
import com.yc.mmrecover.model.bean.EventPayState;
import com.yc.mmrecover.model.bean.WxContactInfo;
import com.yc.mmrecover.utils.MessageUtils;
import com.yc.mmrecover.view.adapters.WxContactAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by suns  on 2019/12/4 11:47.
 */
public class MsgContactChatFragment extends BaseFragment {
    @BindView(R.id.list_view)
    RecyclerView recyclerView;
    @BindView(R.id.rl_mask)
    RelativeLayout mRlMask;
    @BindView(R.id.tv_mask)
    TextView tvMask;
    private String mParent;
    private String mUid;
    private WxContactAdapter wxContactAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg_chat;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            this.mParent = intent.getStringExtra("account_parent");
            this.mUid = intent.getStringExtra("uid");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MsgContactChatFragment mParent = ");
        stringBuilder.append(this.mParent);
        Log.d("TAG", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("MsgContactChatFragment mUid = ");
        stringBuilder.append(this.mUid);
        Log.d("TAG", stringBuilder.toString());
        initRecyclerView();
        scan();
        initListener();

    }

    private void initListener() {
        wxContactAdapter.setOnItemClickListener((adapter, view, position) -> {
            WxContactInfo item = wxContactAdapter.getItem(position);

            if (item != null) {

                Intent intent = new Intent(getActivity(), MessageChatActivity.class);

                intent.putExtra("uid", item.getUid());
                intent.putExtra("self_uid", mUid);
                intent.putExtra("account_parent", mParent);
                intent.putExtra("name", item.getName());
                MsgContactChatFragment.this.startActivity(intent);
            }

        });
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wxContactAdapter = new WxContactAdapter(null, false);

        recyclerView.setAdapter(wxContactAdapter);
    }


    private void scan() {
        this.mRlMask.setVisibility(View.VISIBLE);
        this.tvMask.setText("微信消息扫描中");

        TaskUtil.getImpl().runTask(() -> {


            List<WxContactInfo> wxContactInfos = MessageUtils.getWxContactInfos();
            if (wxContactInfos != null)
                Collections.sort(wxContactInfos, new C08783());

            VUiKit.post(() -> {
                this.mRlMask.setVisibility(View.GONE);

                wxContactAdapter.setNewData(wxContactInfos);
            });
        });
    }

    /* renamed from: com.recover.wechat.app.view.MsgContactChatFragment$3 */
    class C08783 implements Comparator<WxContactInfo> {
        C08783() {
        }

        public int compare(WxContactInfo wxContactBean, WxContactInfo wxContactBean2) {
            return wxContactBean2.getLastTime() - wxContactBean.getLastTime();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void paySuccess(EventPayState eventPayState) {
        wxContactAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
