package com.yc.mmrecover.controller.activitys;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.GlobalData;
import com.yc.mmrecover.view.wdiget.GestureImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * Created by suns  on 2019/12/3 16:03.
 */
public class ImagePageViewActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String[] mImagePathList;
    private int mPosition;
    private String[] mTitleList;


    /* renamed from: com.recover.wechat.app.view.ImagePageViewActivity$3 */
    class C08753 implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageSelected(int i) {
        }

        C08753() {
        }

        public void onPageScrolled(int i, float f, int i2) {
            if (ImagePageViewActivity.this.mTitleList != null && i < ImagePageViewActivity.this.mTitleList.length) {
                tvTitle.setText(ImagePageViewActivity.this.mTitleList[i]);
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_page_view;
    }

    @Override
    protected void initViews() {
        initTitle("图片浏览");
        Intent intent = getIntent();
        if (intent != null) {
            this.mImagePathList = intent.getStringArrayExtra("image_path");
            this.mPosition = getIntent().getIntExtra("position", 0);
            boolean booleanExtra = getIntent().getBooleanExtra("is_guid", false);
            if (!booleanExtra && GlobalData.vipType == 1) {
                Intent intent1 = new Intent(this, PayActivity.class);
                intent.putExtra("sta_type", getIntent().getIntExtra("sta_type", 4003));
                startActivity(intent1);
                finish();
            }
            if (booleanExtra) {
                this.mTitleList = getIntent().getStringArrayExtra("title_list");
            }
        }


        final List<ImageView> arrayList = new ArrayList<>();
        for (String load : this.mImagePathList) {
            GestureImageView gestureImageView = new GestureImageView(this);
            Glide.with(this).load(load).into(gestureImageView);
            gestureImageView.setMaxZoom(10.0f);
            arrayList.add(gestureImageView);
        }
        viewPager.setAdapter(new PagerAdapter() {
            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view == obj;
            }

            public int getCount() {
                return arrayList.size();
            }

            public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
                viewGroup.removeView(arrayList.get(i));
            }

            public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
                viewGroup.addView(arrayList.get(i));
                return arrayList.get(i);
            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(this.mPosition);
        viewPager.addOnPageChangeListener(new C08753());
    }
}