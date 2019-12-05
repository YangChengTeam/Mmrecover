package com.yc.mmrecover.controller.fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.utils.TaskUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.activitys.DetailImageActivty;
import com.yc.mmrecover.helper.GridSpacingItemDecoration;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.view.adapters.GridVideoAdapter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by suns  on 2019/12/4 11:50.
 */
public class MsgPhotoFragment extends BaseScanFragment {
    @BindView(R.id.gridView)
    RecyclerView gridView;
    @BindView(R.id.rl_mask)
    RelativeLayout rlMask;
    @BindView(R.id.tv_mask)
    TextView mTvMask;

    private GridVideoAdapter mAdapter;
    private List<MediaInfo> mMediaList;
    private boolean mIsOperate;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void initViews() {
        gridView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mAdapter = new GridVideoAdapter(null);
        gridView.setAdapter(mAdapter);
        gridView.addItemDecoration(new GridSpacingItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));

        mMediaList = new ArrayList<>();
        initData();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
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
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (mIsOperate) {
                return false;
            }
            Intent intent = new Intent(getActivity(), DetailImageActivty.class);
            intent.putExtra("info", (Serializable) adapter.getData().get(position));
            intent.putExtra("path", "/数据恢复助手/微信图片恢复/");
            startActivity(intent);
            return false;
        });
    }


    private void initData() {
        this.mIsOperate = true;
        this.mMediaList.clear();
        this.mAdapter.notifyDataSetChanged();
        rlMask.setVisibility(View.VISIBLE);
        this.mTvMask.setText("微信相册扫描中");
        TaskUtil.getImpl().runTask(() -> {
            scanDisk();
            VUiKit.post(() -> {

                notifyDataSetChanged(null);
            });

        });

    }

    @Override
    public boolean filterExt(String path) {
        return true;
    }

    @Override
    public MediaInfo getMediaInfo(File file) {
        MediaInfo mediaBean = new MediaInfo();
        String absolutePath = file.getAbsolutePath();
        long length = file.length();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, options);
        if (options.outWidth > 0 && options.outHeight > 0 && options.outMimeType != null && options.outMimeType.toLowerCase().contains("image") && !options.outMimeType.toLowerCase().contains("webp")) {
            mediaBean.setLastModifyTime((int) (file.lastModified() / 1000));
            mediaBean.setPath(absolutePath);
            mediaBean.setSize(length);
            mediaBean.setStrSize(Func.getSizeString(length));
            mediaBean.setWidth(options.outWidth);
            mediaBean.setHeight(options.outHeight);
            mediaBean.setFileName(file.getName());
        }
        return mediaBean;
    }

    @Override
    public void notifyDataSetChanged(MediaInfo mediaInfo) {
        if (mediaInfo != null) {
            this.mMediaList.add(mediaInfo);
            this.mAdapter.setNewData(this.mMediaList);
            this.mAdapter.notifyDataSetChanged();

        } else {
            this.mIsOperate = false;
            this.rlMask.setVisibility(View.GONE);
        }
    }
}
