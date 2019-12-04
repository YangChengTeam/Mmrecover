package com.yc.mmrecover.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kk.utils.VUiKit;
import com.yc.mmrecover.model.bean.MediaInfo;

import java.io.File;

import butterknife.ButterKnife;

public abstract class BaseScanFragment extends Fragment {
    protected View mRootView;
    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), null);
            mContext = getActivity();
            ButterKnife.bind(this, mRootView);
            initViews();
        }
        return mRootView;
    }

    private boolean isDestory = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestory = true;
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    public abstract boolean filterExt(String path);

    public abstract MediaInfo getMediaInfo(File file);

    public abstract void notifyDataSetChanged(MediaInfo mediaInfo);

    public void scanDisk(){
        String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg";
        scanDisk(str);
    }

    private void scanDisk(String str) {
        File[] files = new File(str).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (isDestory) {
                break;
            }
            File file = files[i];
            if (file.isDirectory()) {
                scanDisk(file.getAbsolutePath());
            } else {
                if (filterExt(file.getAbsolutePath())) {
                    MediaInfo mediaInfo = getMediaInfo(file);
                    VUiKit.post(() -> {
                        if (mediaInfo.getFileName() != null) {
                            notifyDataSetChanged(mediaInfo);
                        }
                    });
                }
            }
        }
    }


}
