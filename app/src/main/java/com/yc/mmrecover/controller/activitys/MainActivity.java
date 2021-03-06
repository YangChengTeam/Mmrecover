package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.TaskUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.BroadcastInfo;
import com.yc.mmrecover.model.bean.IndexInfo;
import com.yc.mmrecover.model.bean.UserInfo;
import com.yc.mmrecover.model.engin.IndexEngine;
import com.yc.mmrecover.utils.Func;
import com.yc.mmrecover.utils.MessageUtils;
import com.yc.mmrecover.utils.UiUtils;
import com.yc.mmrecover.utils.UserInfoHelper;
import com.yc.mmrecover.view.wdiget.VTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;


public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_percent)
    TextView tvPercent;

    @BindView(R.id.tv_size)
    TextView tvSize;

    @BindView(R.id.gl_container)
    GridLayout gridLayout;

    @BindView(R.id.tv_broadcast)
    VTextView broadcastVTextView;

    @BindView(R.id.im_user)
    ImageView myBtn;
    @BindView(R.id.tv_app_name)
    TextView tvAppName;

    private String[][] tmpBroadcastInfo;

    private int[] btnImageList = new int[]{R.mipmap.home_chat, R.mipmap.home_video, R.mipmap.home_pic, R.mipmap.home_voice, R.mipmap.home_doc, R.mipmap.home_customer};
    private String[] btnTextList = new String[]{"聊天记录", "微信视频", "微信图片", "微信语音", "微信文档", "专属客服"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        RxView.clicks(myBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            startActivity(new Intent(MainActivity.this, MyActivity.class));
        });

        tvAppName.setText(UiUtils.getAppName());
        tvSize.setText(Func.getUsedInternalMemorySize(this) + "/" + Func.getInternalMemorySize(this));
        tvPercent.setText(Func.getUsedMemoryPresent() + "");

        initBoradInfo();
        initGridLayout();

        TaskUtil.getImpl().runTask(() -> {
            try {
                MessageUtils.unPackHuaweiBackup();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        getUserInfo();
    }

    private void initBoradInfo() {
        String[][] r1 = new String[15][];
        r1[0] = new String[]{"华为Mate8用户", "恢复微信聊天记录", "9分钟前"};
        r1[1] = new String[]{"红米Note8用户", "恢复微信视频", "7分钟前"};
        r1[2] = new String[]{"华为P9用户", "恢复46张微信照片", "13分钟前"};
        r1[3] = new String[]{"华为Mate7用户", "恢复微信聊天记录", "26分钟前"};
        r1[4] = new String[]{"小米4A用户", "恢复微信聊天记录", "16分钟前"};
        r1[5] = new String[]{"一加5t用户", "恢复微信聊天记录", "6分钟前"};
        r1[6] = new String[]{"荣耀畅玩4X用户", "恢复微信聊天记录", "8分钟前"};
        r1[7] = new String[]{"OPPOA57用户", "恢复16张微信照片", "15分钟前"};
        r1[8] = new String[]{"红米Note3用户", "恢复微信聊天记录", "25分钟前"};
        r1[9] = new String[]{"华为畅享8用户", "恢复18张微信照片", "5分钟前"};
        r1[10] = new String[]{"红米Note4用户", "恢复微信聊天记录", "32分钟前"};
        r1[11] = new String[]{"VivoX9用户", "恢复微信视频", "15分钟前"};
        r1[12] = new String[]{"魅族Note9用户", "恢复微信聊天记录", "16分钟前"};
        r1[13] = new String[]{"OPPOA5用户", "恢复132张微信照片", "12分钟前"};
        r1[14] = new String[]{"OPPOR17用户", "恢复微信聊天记录", "3分钟前"};
        tmpBroadcastInfo = r1;

        List arrayList = new ArrayList();
        for (int i = 0; i < this.tmpBroadcastInfo.length; i++) {
            BroadcastInfo broadcastInfo = new BroadcastInfo();
            broadcastInfo.setUserName(this.tmpBroadcastInfo[i][0]);
            broadcastInfo.setContent(this.tmpBroadcastInfo[i][1]);
            broadcastInfo.setTime(this.tmpBroadcastInfo[i][2]);
            arrayList.add(broadcastInfo);
        }

        this.broadcastVTextView.setText(14.0f, 5, getResources().getColor(R.color.white));
        this.broadcastVTextView.setTextStillTime(5000);
        this.broadcastVTextView.setAnimTime(500);
        this.broadcastVTextView.startAutoScroll();

        ArrayList list = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(((BroadcastInfo) arrayList.get(i)).getUserName());
            stringBuilder2.append(" ");
            stringBuilder2.append(((BroadcastInfo) arrayList.get(i)).getContent());
            stringBuilder2.append("   ");
            stringBuilder2.append(((BroadcastInfo) arrayList.get(i)).getTime());
            CharSequence stringBuilder3 = stringBuilder2.toString();
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("content = ");
            stringBuilder4.append(stringBuilder3);
            int length = (((BroadcastInfo) arrayList.get(i)).getUserName().length() + ((BroadcastInfo) arrayList.get(i)).getContent().length()) + 1;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder3);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_word2)), 0, length, 34);
            list.add(spannableStringBuilder);
        }
        this.broadcastVTextView.setTextList(list);
    }

    private void initGridLayout() {
        int i = 0;

        while (i < this.btnTextList.length) {
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.rowSpec = GridLayout.spec(i / 3, 1.0f);
            layoutParams.columnSpec = GridLayout.spec(i % 3, 1.0f);
            View homeButton = getHomeButton(this.btnTextList[i], this.btnImageList[i]);
            homeButton.setTag(i);
            RxView.clicks(homeButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
                switch (Integer.valueOf(homeButton.getTag().toString())) {
                    case 0:
//                        if (!UserInfoHelper.isGotoSuperVip(this))
                        startActivity(new Intent(MainActivity.this, MessageGuideActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, ShowVideoActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, ShowImageActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, ShowVoiceActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, ShowFileActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, ContactActivity.class));
                        break;
                }
            });
            gridLayout.addView(homeButton, layoutParams);
            i++;
        }
    }

    private View getHomeButton(String str, int i) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_home_entry, null);
        ((TextView) inflate.findViewById(R.id.tv_name)).setText(str);
        ((ImageView) inflate.findViewById(R.id.im_icon)).setImageDrawable(getResources().getDrawable(i));
        return inflate;
    }

    private void getUserInfo() {
        IndexEngine indexEngine = new IndexEngine(this);
        indexEngine.getIndexInfo().subscribe(new Subscriber<ResultInfo<IndexInfo>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<IndexInfo> indexInfoResultInfo) {
                if (indexInfoResultInfo != null && indexInfoResultInfo.getCode() == HttpConfig.STATUS_OK && indexInfoResultInfo.getData() != null) {
                    IndexInfo indexInfo = indexInfoResultInfo.getData();
                    UserInfo userInfo = indexInfo.getUserInfo();

                    userInfo.setIsVip(indexInfo.getUserLevel().getLevel());
                    userInfo.setVip_name(indexInfo.getUserLevel().getName());
                    userInfo.setWx(indexInfo.getKf().getWx());
                    userInfo.setQq(indexInfo.getKf().getQq());
                    UserInfoHelper.saveUserInfo(userInfo);
                }
            }
        });
    }


}
