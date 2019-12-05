package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.fragments.MsgContactChatFragment;
import com.yc.mmrecover.controller.fragments.MsgContactFragment;
import com.yc.mmrecover.controller.fragments.MsgFileFragment;
import com.yc.mmrecover.controller.fragments.MsgPhotoFragment;
import com.yc.mmrecover.view.wdiget.BackgroundShape;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class MessageContactActivity extends BaseActivity {
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.rl_buy)
    RelativeLayout rlBuy;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.ll_bottom_menu)
    LinearLayout llBottomMenu;
    @BindView(R.id.rl_mask)
    RelativeLayout rlMask;
    private String mParent;
    private String mUid;
    private int mLastTab = 0;


    private static final String TAG = "MessageContactActivity";

    private List<Fragment> mFragmentList = new ArrayList<>();
    private String[] menuList = new String[]{"微信", "通讯录", "相册", "文件"};
    private int[] selectMenu = new int[]{R.mipmap.msg_chat_select, R.mipmap.msg_contact_select, R.mipmap.msg_pic_select, R.mipmap.msg_file_select};
    private int[] unSelectMenu = new int[]{R.mipmap.msg_chat, R.mipmap.msg_contact, R.mipmap.msg_pic, R.mipmap.msg_file};


    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_contact;
    }

    @Override
    protected void initViews() {
        initTitle("恢复微信消息");
        Intent intent = getIntent();

        if (intent != null) {

            this.mParent = intent.getStringExtra("account_parent");
            this.mUid = intent.getStringExtra("uid");
        }


        initFragmentList();

        this.llBottomMenu.removeAllViews();
        for (int i = 0; i < this.menuList.length; i++) {
            View.inflate(this, R.layout.item_menu, this.llBottomMenu);
            ImageView imageView = this.llBottomMenu.getChildAt(i).findViewById(R.id.im_menu);
            ((TextView) this.llBottomMenu.getChildAt(i).findViewById(R.id.tv_menu)).setText(this.menuList[i]);
            imageView.setImageDrawable(getResources().getDrawable(this.unSelectMenu[i]));
            int finalI = i;
            this.llBottomMenu.getChildAt(i).findViewById(R.id.ll_menu).setOnClickListener(view -> {
                setTabSelect(finalI);
                showFragment(finalI);
                mLastTab = finalI;
            });
        }
        setTabSelect(0);
        tvBuy.setBackgroundDrawable(new BackgroundShape(this, 3, R.color.blue));
//        textView.setOnClickListener(new C08462());

    }


    private void initFragmentList() {
        Log.e(TAG, "----initFragmentList----");
        this.mFragmentList.clear();
        this.mFragmentList.add(new MsgContactChatFragment());
        this.mFragmentList.add(new MsgContactFragment());
        this.mFragmentList.add(new MsgPhotoFragment());
        this.mFragmentList.add(new MsgFileFragment());
        showFragment(0);
    }

    private void showFragment(int i) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment hide : this.mFragmentList) {
            beginTransaction.hide(hide);
        }
        Fragment fragment = this.mFragmentList.get(i);
        if (fragment.isAdded()) {
            this.mFragmentList.get(i).onResume();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add fragment = ");
            stringBuilder.append(i);
            Log.d(TAG, stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("mainTab");
            stringBuilder2.append(i);
            beginTransaction.add(R.id.fl_content, fragment, stringBuilder2.toString());
        }
        beginTransaction.show(fragment);
        beginTransaction.commitAllowingStateLoss();

    }


    private void setTabSelect(int i) {
        ImageView imageView = this.llBottomMenu.getChildAt(this.mLastTab).findViewById(R.id.im_menu);
        ((TextView) this.llBottomMenu.getChildAt(this.mLastTab).findViewById(R.id.tv_menu)).setTextColor(ContextCompat.getColor(this, R.color.gray_word));
        imageView.setImageDrawable(ContextCompat.getDrawable(this, this.unSelectMenu[this.mLastTab]));
        ImageView imageView2 = this.llBottomMenu.getChildAt(i).findViewById(R.id.im_menu);
        ((TextView) this.llBottomMenu.getChildAt(i).findViewById(R.id.tv_menu)).setTextColor(ContextCompat.getColor(this, R.color.blue));
        imageView2.setImageDrawable(ContextCompat.getDrawable(this, this.selectMenu[i]));
    }


}
