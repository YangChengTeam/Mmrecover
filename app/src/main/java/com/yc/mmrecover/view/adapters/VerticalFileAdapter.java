package com.yc.mmrecover.view.adapters;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
import com.yc.mmrecover.utils.Func;

import java.util.List;


public class VerticalFileAdapter extends BaseQuickAdapter<MediaInfo, BaseViewHolder> {


    private boolean isShowSelect = true;

    public void setShowSelect(boolean showSelect) {
        isShowSelect = showSelect;
    }

    public VerticalFileAdapter(@Nullable List<MediaInfo> data) {
        super(R.layout.item_view_file, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaInfo item) {
        helper.setText(R.id.tv_time2, Func.formatData("yyyy/MM/dd HH:mm:ss", item.getLastModifyTime()) + "   " + Func.getSizeString(item.getSize()));
        helper.setText(R.id.tv_name, item.getFileName());
        helper.setVisible(R.id.im_select, item.isSelect());
        if (isShowSelect) {
            helper.getView(R.id.rl_select).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.rl_select).setVisibility(View.GONE);
        }
    }
}
