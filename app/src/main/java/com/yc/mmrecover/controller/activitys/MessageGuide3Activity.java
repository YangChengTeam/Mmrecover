package com.yc.mmrecover.controller.activitys;


import android.widget.TextView;

import com.yc.mmrecover.R;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MessageGuide3Activity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_operation)
    TextView tvOperation;
    @BindView(R.id.list_view)
    RecyclerView listView;
    @BindView(R.id.tv_backup1)
    TextView tvBackup1;
    @BindView(R.id.tv_backup)
    TextView tvBackup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bak_msg_guid3;
    }

    @Override
    protected void initViews() {

        initTitle("恢复微信消息");
//        ListView listView = (ListView) findViewById(R.id.list_view);
//        this.mAdapter = new BakInfoAdapter(this, this.mDirList);
//        listView.setAdapter(this.mAdapter);
//        listView.setOnItemClickListener(new C08581());
//        TextView textView = (TextView) findViewById(C0810R.C0809id.tv_backup1);
//        TextView textView2 = (TextView) findViewById(C0810R.C0809id.tv_backup);
//        if (GlobalData.isNeedConnectPC) {
//            textView.setText("如果连接电脑备份失败，可以重启BackupWX.exe再次尝试");
//            textView2.setVisibility(8);
//            return;
//        }
//        textView2.setOnClickListener(new C08592());

    }


}
