package com.yc.mmrecover.pay.alipay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.mmrecover.R;

import androidx.appcompat.app.AlertDialog;


/**
 * Created by zhangkai on 2017/2/21.
 */

public class LoadingDialog extends AlertDialog {

    private Activity mActivity;
    //    ImageView ivCircle;
    TextView tvMsg;

    public LoadingDialog(Activity context) {

        super(context, R.style.customDialog);
        this.mActivity = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        tvMsg = findViewById(R.id.loading_view_tv);
        setCancelable(true);

    }

    public void showLoadingDialog() {
        try {

            if (mActivity != null && !mActivity.isFinishing()) {
                dismiss();
                if (!isShowing()) {
                    show();
                }
            }
        } catch (Exception e) {
            Log.d("mylog", "LoadDialog --showLoadingDialog: " + e.toString());
        }
    }

    public void dismissLoadingDialog() {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                dismiss();
                if (isShowing()) {
                    hide();
                }
            }
        } catch (Exception e) {
            Log.d("mylog", "LoadDialog --dismissDialog: " + e.toString());
        }
    }

    public void setText(String text) {

        if (tvMsg != null) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(text);
        }
        showLoadingDialog();
    }

    private int getLayoutID() {
        return R.layout.loading_view;
    }
}
