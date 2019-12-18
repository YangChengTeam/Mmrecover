package com.yc.mmrecover.controller.activitys;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.utils.TaskUtil;
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.WxAccountInfo;
import com.yc.mmrecover.utils.MessageUtils;
import com.yc.mmrecover.view.adapters.WxAccountAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MessageUserActivity extends BaseActivity {


    @BindView(R.id.user_recycler_view)
    RecyclerView userRecyclerView;
    @BindView(R.id.tv_wait)
    TextView mTvMask;
    @BindView(R.id.rl_mask)
    RelativeLayout mRlMask;

    private WxAccountAdapter wxAccountAdapter;


    @Override

    protected int getLayoutId() {
        return R.layout.activity_message_user;
    }

    @Override
    protected void initViews() {
        initTitle("微信账号");


        initRecyclerView();
        scan();
        initListener();
    }

    private void initListener() {
        wxAccountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!MessageUtils.isBackup()) {
                ToastUtil.toast2(MessageUserActivity.this, "您还没有备份，请先备份");
                return;
            }
            WxAccountInfo item = wxAccountAdapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(MessageUserActivity.this, MessageContactActivity.class);
                intent.putExtra("account_parent", item.getParent());
                intent.putExtra("uid", item.getWxAccount());
                startActivity(intent);
            }
        });

    }

    private void initRecyclerView() {
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        wxAccountAdapter = new WxAccountAdapter(null);
        userRecyclerView.setAdapter(wxAccountAdapter);
        userRecyclerView.setHasFixedSize(true);
    }


    private void scan() {


        this.mRlMask.setVisibility(View.VISIBLE);
        this.mTvMask.setText("微信账号扫描中");

        TaskUtil.getImpl().runTask(() -> {
            List<WxAccountInfo> accountInfos = new ArrayList<>();
            WxAccountInfo wxAccountInfo = MessageUtils.getWxAccountInfo();

//            Log.e("TAG", "scan: " + wxAccountInfo);

            VUiKit.post(() -> {
                if (wxAccountInfo == null) {
                    showDialog();
                    return;
                }
                this.mRlMask.setVisibility(View.GONE);
                accountInfos.add(wxAccountInfo);
                wxAccountAdapter.setNewData(accountInfos);
            });
        });
    }


    private boolean showDialog() {
        if (!MessageUtils.isBackup()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("提示");
            alertDialog.setMessage("您还没有备份，请先备份");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定", (dialog, which) -> {
                alertDialog.dismiss();
                finish();
            });
            alertDialog.show();
            return true;
        }
        return false;
    }

}
