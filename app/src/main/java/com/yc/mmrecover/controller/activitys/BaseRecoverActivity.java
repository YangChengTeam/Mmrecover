package com.yc.mmrecover.controller.activitys;

import android.os.Environment;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public abstract class BaseRecoverActivity extends BaseActivity {
    @BindView(R.id.gridView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_note)
    TextView mTvNote;

    protected BaseQuickAdapter mAdapter;
    protected List<MediaInfo> mMediaList;

    public abstract String initTitle();

    public abstract String initPath();

    @Override
    protected void initViews() {
        initTitle("已恢复的" + initTitle());
        mTvNote.setText(initTitle() + "存储在：" + initPath());

        this.mMediaList = new ArrayList<>();
        scanDisk(Environment.getExternalStorageDirectory() + "/" + initPath());
    }


    private void scanDisk(String str) {
        new File(str).listFiles((file, str1) -> {
            File file2 = new File(file, str1);
            String absolutePath = file2.getAbsolutePath();
            if (file2.isDirectory()) {
                BaseRecoverActivity.this.scanDisk(absolutePath);
            } else {
                long length = file2.length();
                MediaInfo mediaBean = new MediaInfo();
                mediaBean.setLastModifyTime((int) (file2.lastModified() / 1000));
                mediaBean.setPath(absolutePath);
                mediaBean.setFileName(file2.getName());
                mediaBean.setSize(length);
                mediaBean.setStrSize(Func.getSizeString(length));
                BaseRecoverActivity.this.mMediaList.add(mediaBean);
            }
            return false;
        });
    }
}
