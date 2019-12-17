package com.yc.mmrecover.view.adapters;

import android.icu.text.PluralRules;
import android.text.Html;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.activitys.PayActivity;
import com.yc.mmrecover.model.bean.VipItemInfo;
import com.yc.mmrecover.utils.UserInfoHelper;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2019/12/16 08:44.
 */
public class VipItemAdapter extends BaseQuickAdapter<VipItemInfo, BaseViewHolder> {
    private SparseArray<View> sparseArray;
    private SparseArray<TextView> textViewSparseArray;

    public VipItemAdapter(@Nullable List<VipItemInfo> data) {
        super(R.layout.vip_item_view, data);
        sparseArray = new SparseArray<>();
        textViewSparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, VipItemInfo item) {
        if (item != null) {
            helper.setText(R.id.tv_98, item.getPrice());

            String desp = item.getDesp();

            Spanned value = Html.fromHtml(desp + "<font color='#de1700'>+VIP专属客服</font>");
            helper.setText(R.id.tv_content1, value);

            int position = helper.getAdapterPosition();
            if (position == 0) {

                helper.itemView.setBackground(new BackgroundShape(mContext, 50, R.color.white, 10, R.color.yellow_btn));
                helper.setGone(R.id.tv_recommend, true);
            } else {
                helper.itemView.setBackground(new BackgroundShape(mContext, 50, R.color.white, 10, R.color.gray_bk2));
                helper.setGone(R.id.tv_recommend, false);
            }
            if (UserInfoHelper.getVipType() == 1) {
                if (UserInfoHelper.getVipType() == item.getLevel()) {
                    helper.itemView.setVisibility(View.GONE);
                }
            }
            sparseArray.put(position, helper.itemView);
            textViewSparseArray.put(position, helper.getView(R.id.tv_recommend));
        }
    }

    private void resetState() {
        if (sparseArray.size() > 0) {
            for (int i = 0; i < sparseArray.size(); i++) {
                sparseArray.get(i).setBackground(new BackgroundShape(mContext, 50, R.color.white, 10, R.color.gray_bk2));
                textViewSparseArray.get(i).setVisibility(View.GONE);
            }
        }
    }

    public void setSelected(int position) {
        resetState();
        sparseArray.get(position).setBackground(new BackgroundShape(mContext, 50, R.color.white, 10, R.color.yellow_btn));
        textViewSparseArray.get(position).setVisibility(View.VISIBLE);
    }
}
