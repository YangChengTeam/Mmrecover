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
import com.yc.mmrecover.view.adapters.GridVideoAdapter;
import com.yc.mmrecover.view.adapters.GridVoiceAdapter;

import java.io.File;
import java.io.Serializable;

public class ShowVoiceActivity extends BaseShowActivity {
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
                if (mIsScan) {
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
                if (mIsScan) {
                    return false;
                }

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
        return null;
    }

    @Override
    public BaseQuickAdapter initAdapter() {
        return new GridVoiceAdapter(null);
    }

    @Override
    public boolean filterExt(String path) {
        path.contains(".amr");
        return true;
    }

    @Override
    public MediaInfo getMediaInfo(File file2) {
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
    }

}
