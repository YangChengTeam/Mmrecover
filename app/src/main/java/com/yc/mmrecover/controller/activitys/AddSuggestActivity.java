package com.yc.mmrecover.controller.activitys;

import android.widget.EditText;
import android.widget.TextView;

import com.yc.mmrecover.R;
import com.yc.mmrecover.view.adapters.SuggestPicAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_suggest;
    }

    @Override
    protected void initViews() {
        initTitle("意见反馈");
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SuggestPicAdapter suggestPicAdapter = new SuggestPicAdapter(null);
        recyclerView.setAdapter(suggestPicAdapter);
    }


}
