package com.yc.mmrecover.view.wdiget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;


public class VTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private static final int FLAG_START_AUTO_SCROLL = 0;
    private static final int FLAG_STOP_AUTO_SCROLL = 1;
    private int currentId;
    private Handler handler;
    private OnItemClickListener itemClickListener;
    private Context mContext;
    private int mPadding;
    private float mTextSize;
    private int textColor;
    private ArrayList<CharSequence> textList;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public void setText(float f, int i, int i2) {
        this.mTextSize = f;
        this.mPadding = i;
        this.textColor = i2;
    }

    public VTextView(Context context) {
        this(context, (AttributeSet) null);
        this.mContext = context;
    }

    public VTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTextSize = 16.0f;
        this.mPadding = 5;
        this.textColor = ViewCompat.MEASURED_STATE_MASK;
        this.currentId = -1;
        this.mContext = context;
        this.textList = new ArrayList();
    }

    public void setAnimTime(long j) {
        setFactory(this);
        Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) j, 0.0f);
        translateAnimation.setDuration(j);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        Animation translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-j));
        translateAnimation2.setDuration(j);
        translateAnimation2.setInterpolator(new AccelerateInterpolator());
        setInAnimation(translateAnimation);
        setOutAnimation(translateAnimation2);
    }

    public void setTextStillTime(final long j) {
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        if (VTextView.this.textList.size() > 0) {
                            VTextView.this.currentId = VTextView.this.currentId + 1;
                            VTextView.this.setText((CharSequence) VTextView.this.textList.get(VTextView.this.currentId % VTextView.this.textList.size()));
                        }
                        VTextView.this.handler.sendEmptyMessageDelayed(0, j);
                        return;
                    case 1:
                        VTextView.this.handler.removeMessages(0);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public void setTextList(ArrayList<CharSequence> arrayList) {
        this.textList.clear();
        this.textList.addAll(arrayList);
        this.currentId = -1;
    }

    public void startAutoScroll() {
        this.handler.sendEmptyMessage(0);
    }

    public void stopAutoScroll() {
        this.handler.sendEmptyMessage(1);
    }

    public TextView makeView() {
        TextView textView = new TextView(this.mContext);
        textView.setGravity(19);
        textView.setMaxLines(1);
        textView.setPadding(this.mPadding, this.mPadding, this.mPadding, this.mPadding);
        textView.setTextColor(this.textColor);
        textView.setTextSize(this.mTextSize);
        textView.setClickable(true);
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VTextView.this.itemClickListener != null && VTextView.this.textList.size() > 0 && VTextView.this.currentId != -1) {
                    VTextView.this.itemClickListener.onItemClick(VTextView.this.currentId % VTextView.this.textList.size());
                }
            }
        });
        return textView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

}

