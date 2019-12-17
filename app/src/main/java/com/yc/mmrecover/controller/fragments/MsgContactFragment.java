package com.yc.mmrecover.controller.fragments;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.kk.utils.TaskUtil;
import com.kk.utils.VUiKit;
import com.yc.mmrecover.R;
import com.yc.mmrecover.controller.activitys.DetailUserActivity;
import com.yc.mmrecover.model.bean.WxContactInfo;
import com.yc.mmrecover.utils.MessageUtils;
import com.yc.mmrecover.utils.UserInfoHelper;
import com.yc.mmrecover.view.adapters.WxContactAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by suns  on 2019/12/4 11:49.
 */
public class MsgContactFragment extends BaseFragment {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.im_close)
    ImageView imClose;
    @BindView(R.id.list_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_mask)
    TextView tvMask;
    @BindView(R.id.rl_mask)
    RelativeLayout mRlMask;
    private String mParent;
    private String mUid;
    private WxContactAdapter wxContactAdapter;
    private List<WxContactInfo> wxContactInfos;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initViews() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            this.mParent = intent.getStringExtra("account_parent");
            this.mUid = intent.getStringExtra("uid");
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MsgContactChatFragment mParent = ");
        stringBuilder.append(this.mParent);
        Log.d("TAG", stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("MsgContactChatFragment mUid = ");
        stringBuilder.append(this.mUid);
        Log.d("TAG", stringBuilder.toString());
        initRecyclerView();
        scan();
        initListener();

    }

    private void initListener() {
        wxContactAdapter.setOnItemClickListener((adapter, view, position) -> {
            WxContactInfo item = wxContactAdapter.getItem(position);

            if (item != null) {

                Intent intent = new Intent(MsgContactFragment.this.getActivity(), DetailUserActivity.class);
                intent.putExtra("uid", item.getUid());
                intent.putExtra("self_uid", MsgContactFragment.this.mUid);
                intent.putExtra("account_parent", MsgContactFragment.this.mParent);
                MsgContactFragment.this.startActivity(intent);

            }

        });
        etContent.addTextChangedListener(new C08813());
        RxView.clicks(imClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe((v) -> {
            etContent.setText("");
        });

    }


    private class C08813 implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C08813() {
        }

        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(editable.toString())) {
                wxContactAdapter.setNewData(wxContactInfos);
                return;
            }
            String obj = editable.toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("afterTextChanged = ");
            stringBuilder.append(obj);
            Log.d("TAG", stringBuilder.toString());
            List<WxContactInfo> arrayList = new ArrayList<>();

            for (WxContactInfo wxContactBean : wxContactInfos) {
                if (wxContactBean.getName().contains(obj)) {
                    arrayList.add(wxContactBean);
                } else if (isLetter(obj)) {
                    obj = obj.toLowerCase();
                    Object obj2 = null;
                    int i = 0;
                    int i2 = -1;
                    Object obj3 = null;
                    while (i < obj.length()) {
                        int indexOf = wxContactBean.getQuanPin().indexOf(obj.charAt(i));
                        if ((i == 0 && indexOf != 0) || indexOf <= i2) {
                            break;
                        }
                        i++;
                        i2 = indexOf;
                        obj3 = 1;
                    }
                    obj2 = obj3;
                    if (obj2 != null) {
                        arrayList.add(wxContactBean);
                    }
                }
            }
            wxContactAdapter.setNewData(arrayList);
        }
    }


    public boolean isLetter(String str) {
        return str.matches("^[a-zA-Z]+$");
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wxContactAdapter = new WxContactAdapter(null, true);

        recyclerView.setAdapter(wxContactAdapter);
    }


    private void scan() {
        this.mRlMask.setVisibility(View.VISIBLE);
        this.tvMask.setText("联系人扫描中");

        TaskUtil.getImpl().runTask(() -> {


            wxContactInfos = MessageUtils.getWxContactInfos();
            VUiKit.post(() -> {
                this.mRlMask.setVisibility(View.GONE);

                wxContactAdapter.setNewData(wxContactInfos);
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfoHelper.getVipType() == 2) {
            wxContactAdapter.notifyDataSetChanged();
        }
    }
}
