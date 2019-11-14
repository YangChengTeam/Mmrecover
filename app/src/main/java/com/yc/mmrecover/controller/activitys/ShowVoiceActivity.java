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
        recyclerView.setAdapter(this.mAdapter);

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
                    return false;
                }
//                startActivity(new Intent(ShowVoiceActivity.this, DetailVideoActivity.class));
                ShowVoiceActivity.this.mPlayTask = new PlayVoiceTask(ShowVoiceActivity.this);
                ShowVoiceActivity.this.mPlayTask.execute(new String[]{((MediaInfo) ShowVoiceActivity.this.mMediaList.get(position)).getPath()});
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
        return path.endsWith(".amr");

    }

    @Override
    public MediaInfo getMediaInfo(File file) {
        long length = file.length();
        String absolutePath = file.getAbsolutePath();
        MediaInfo mediaBean = new MediaInfo();
        mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
        mediaBean.setPath(absolutePath);
        mediaBean.setSize(length);
        mediaBean.setStrSize(Func.getSizeString(length));
        mediaBean.setFileName(file.getName());
        return mediaBean;
    }

    @Override
    protected void start2RecoverActivity() {
        startActivity(new Intent(this, RecoverVoiceActivity.class));
    }

}
