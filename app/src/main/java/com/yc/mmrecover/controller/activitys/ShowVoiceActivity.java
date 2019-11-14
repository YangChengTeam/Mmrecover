package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.GridSpacingItemDecoration;
import com.yc.mmrecover.utils.PlayVoiceTask;
import com.yc.mmrecover.view.adapters.GridVoiceAdapter;

import java.io.File;

public class ShowVoiceActivity extends BaseShowActivity {

    private PlayVoiceTask mPlayTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_show;
    }

    @Override
    protected void initViews() {
        super.initViews();
        GridLayoutManager layoutManage = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        recyclerView.setLayoutManager(layoutManage);
//        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mIsOperate) {
                    return;
                }
                MediaInfo mediaInfo = (MediaInfo) adapter.getData().get(position);
                if (mediaInfo.isSelect()) {
                    view.findViewById(R.id.im_select).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.im_select).setVisibility(View.VISIBLE);
                }
                mediaInfo.setSelect(!mediaInfo.isSelect());
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mIsOperate) {
//                    Toast.makeText(ShowVoiceActivity.this, "请等待扫描完成", Toast.LENGTH_SHORT).show();
                    return false;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("long click = ");
                stringBuilder.append(position);
                Log.d("sssss", "onItemLongClick: " + stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append("getPath = ");
                stringBuilder.append(((MediaInfo) ShowVoiceActivity.this.mMediaList.get(position)).getPath());
                Log.d("sssss", "onItemLongClick:22222 " + stringBuilder.toString());
//                if (GlobalData.vipType == 1) {
//                    Intent intent = new Intent(ShowVoiceActivity.this, PayActivity.class);
//                    intent.putExtra("sta_type", 5003);
//                    ShowVoiceActivity.this.startActivity(intent);
//                } else {
                ShowVoiceActivity.this.mPlayTask = new PlayVoiceTask(new PlayVoiceTask.PlayCallback() {
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
                ShowVoiceActivity.this.mPlayTask.execute(new String[]{((MediaInfo) ShowVoiceActivity.this.mMediaList.get(position)).getPath()});
//                }
                return false;
            }
        });
    }

    @Override
    protected String initTitle() {
        return "音频";
    }

    @Override
    protected String initPath() {
        return "数据恢复助手/微信音频恢复/";
    }

    @Override
    public BaseQuickAdapter initAdapter() {
        return new GridVoiceAdapter(null);
    }

    @Override
    public boolean filterExt(String path) {
        return path.contains(".amr");
    }

    @Override
    public MediaInfo getMediaInfo(File file2) {
        if (file2 != null) {
            return new MediaInfo();
        }
        long length = file2.length();
        String absolutePath = file2.getAbsolutePath();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("scan voice = ");
        stringBuilder.append(absolutePath);
        Log.d("ssssss", "getMediaInfo: " + stringBuilder.toString());
        MediaInfo mediaBean = new MediaInfo();
        mediaBean.setLastModifyTime((int) (file2.lastModified() / 1000));
        mediaBean.setPath(absolutePath);
        mediaBean.setSize(length);
        mediaBean.setStrSize(Func.getSizeString(length));
        mediaBean.setFileName(file2.getName());
        return mediaBean;
    }

    @Override
    protected void start2RecoverActivity() {
        startActivity(new Intent(this, RecoverVoiceActivity.class));
        finish();
    }

}
