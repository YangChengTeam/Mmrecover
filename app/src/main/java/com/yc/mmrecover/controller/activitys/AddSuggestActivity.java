package com.yc.mmrecover.controller.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.UploadInfo;
import com.yc.mmrecover.model.bean.UserInfo;
import com.yc.mmrecover.model.engin.SuggestEngine;
import com.yc.mmrecover.utils.MediaUtility;
import com.yc.mmrecover.utils.UserInfoHelper;
import com.yc.mmrecover.view.adapters.SuggestPicAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import rx.Subscriber;
import rx.functions.Action1;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by suns  on 2019/12/14 10:44.
 */
public class AddSuggestActivity extends BaseActivity {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_word_count)
    TextView tvWordCount;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private List<UploadInfo> uploadInfos = new ArrayList<>();
    private SuggestPicAdapter suggestPicAdapter;


    private static final int REQUEST_LOAD_IMAGE_FROM_GALLERY = 0x10;
    private SuggestEngine suggestEngine;

    private String[] strArr = new String[1];

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_suggest;
    }

    @Override
    protected void initViews() {
        initTitle("意见反馈");
        initRecyclerView();
        suggestEngine = new SuggestEngine(this);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        suggestPicAdapter = new SuggestPicAdapter(null);
        recyclerView.setAdapter(suggestPicAdapter);
        getData();
        initListener();
    }


    private void getData() {
        uploadInfos.add(new UploadInfo());
        suggestPicAdapter.setNewData(uploadInfos);
    }

    private void initListener() {
        suggestPicAdapter.setOnItemClickListener((adapter, view, position) -> {
            UploadInfo item = suggestPicAdapter.getItem(position);
            if (item != null) {
                if (UploadInfo.ITEM_CASE == item.getItemType()) {
                    openGallery();
                }
//                else {
//                    strArr[0] = item.getPath();
//                    Intent intent = new Intent(AddSuggestActivity.this, ImagePageViewActivity.class);
//                    intent.putExtra("image_path", strArr);
//                    intent.putExtra("position", 0);
//                    intent.putExtra("is_guid", true);
//                    startActivity(intent);
//                }
            }
        });
        suggestPicAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            uploadInfos.remove(position);
            suggestPicAdapter.notifyItemRemoved(position);
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();
                String count = (200 - result.length()) + "/" + 200;
                tvWordCount.setText(count);
                if (result.length() > 6) {
                    tvSubmit.setSelected(true);
                } else {
                    tvSubmit.setSelected(false);
                }
            }
        });

        RxView.clicks(tvSubmit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            String content = etContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtil.toast2(AddSuggestActivity.this, "请先填写反馈内容");
                return;

            }
            if (content.length() < 7) {
                ToastUtil.toast2(AddSuggestActivity.this, "反馈的内容不能少于6个字");
                return;
            }

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < uploadInfos.size() - 1; i++) {
                stringBuffer.append(uploadInfos.get(i).getUrl()).append(",");
            }

            stringBuffer.deleteCharAt(stringBuffer.length() - 1);

            addSuggest(content, stringBuffer.toString());
        });
        RxView.clicks(tvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            finish();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                REQUEST_LOAD_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_LOAD_IMAGE_FROM_GALLERY) {


            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            if (result != null) {
                String path = MediaUtility.getPath(AddSuggestActivity.this.getApplicationContext(),
                        result);
                File file = new File(path);
                if (file.isFile()) {
                    zipFile(file);
                }
            }

        }
    }

    private void zipFile(File file) {
        if (file == null) {
            Toast.makeText(AddSuggestActivity.this, "获取图片失败--file", Toast.LENGTH_SHORT).show();
            return;
        }
        Luban.with(AddSuggestActivity.this)
                .load(file)
                .ignoreBy(100)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        uploadFile(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }

    private void uploadFile(File file) {
        suggestEngine.uploadPic(file).subscribe(new Subscriber<ResultInfo<UploadInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UploadInfo> uploadInfoResultInfo) {
                if (uploadInfoResultInfo != null && uploadInfoResultInfo.getCode() == HttpConfig.STATUS_OK && uploadInfoResultInfo.getData() != null) {
                    UploadInfo data = uploadInfoResultInfo.getData();
                    data.setType(UploadInfo.ITEM_CONTENT);
                    uploadInfos.add(0, data);
                    suggestPicAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addSuggest(String content, String urls) {
        UserInfo userInfo = UserInfoHelper.getUserInfo();

        suggestEngine.addSuggest(userInfo.getId(), content, urls).subscribe(new Subscriber<ResultInfo<List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<String>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.getCode() == HttpConfig.STATUS_OK) {
                    ToastUtil.toast2(AddSuggestActivity.this, "意见反馈成功");
                    finish();
                }
            }
        });
    }
}
