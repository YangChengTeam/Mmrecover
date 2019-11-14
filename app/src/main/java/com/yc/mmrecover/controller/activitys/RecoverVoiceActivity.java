package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.GridSpacingItemDecoration;
import com.yc.mmrecover.utils.PlayVoiceTask;
import com.yc.mmrecover.view.adapters.GridVoiceAdapter;


public class RecoverVoiceActivity extends BaseRecoverActivity {

    private PlayVoiceTask mPlayTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_recover;
    }

    @Override
    public String initTitle() {
        return "音频";
    }

    @Override
    public String initPath() {
        return "数据恢复助手/微信音频恢复/";
    }

    @Override
    protected void initViews() {
        super.initViews();
        this.mAdapter = new GridVoiceAdapter(this.mMediaList);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d("sssss", "onItemClick: position "+position);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("long click = ");
                stringBuilder.append(position);
                Log.d("sssss", "onItemLongClick: " + stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append("getPath = ");
                stringBuilder.append(((MediaInfo) RecoverVoiceActivity.this.mMediaList.get(position)).getPath());
                Log.d("sssss", "onItemLongClick:22222 " + stringBuilder.toString());
//                if (GlobalData.vipType == 1) {
//                    Intent intent = new Intent(RecoverVoiceActivity.this, PayActivity.class);
//                    intent.putExtra("sta_type", 5003);
//                    RecoverVoiceActivity.this.startActivity(intent);
//                } else {
                    RecoverVoiceActivity.this.mPlayTask = new PlayVoiceTask(new PlayVoiceTask.PlayCallback() {
                        public void onStart(int i) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("onStart = ");
                            stringBuilder.append(i);
                            Log.d("sssss", "onStart: stringBuilder.toString() " + stringBuilder.toString());
//                            ShowVoiceActivity.this.showPlayLayout((i / 100) + 1);
                        }

                        public void onStop() {
                            Log.d("sssss", "onStop: ");
                        }
                    });
                    RecoverVoiceActivity.this.mPlayTask.execute(new String[]{((MediaInfo) RecoverVoiceActivity.this.mMediaList.get(position)).getPath()});
//                }
            }
        });
    }
}
