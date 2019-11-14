package com.yc.mmrecover.controller.activitys;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.view.adapters.VerticalFileAdapter;

import java.io.File;


public class RecoverFileActivity extends BaseRecoverActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_recover;
    }


    @Override
    public String initTitle() {
        return "文件";
    }

    @Override
    public String initPath() {
        return "数据恢复助手/微信文件恢复/";
    }

    @Override
    protected void initViews() {
        super.initViews();
        this.mAdapter = new VerticalFileAdapter(this.mMediaList);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setAdapter(this.mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                openFile(mMediaList.get(position).getPath());
            }
        });

    }

    public void openFile(String path) {
        if (path == null)
            return;

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        String[][] MATCH_ARRAY = {
                {".doc", "application/msword"},
                {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                {".xls", "application/vnd.ms-excel"},
                {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
                {".txt", "text/plain"},
                {".pptx", "tapplication/vnd.openxmlformats-officedocument.presentationml.presentation"},
                {".ppt", "application/vnd.ms-powerpoint"},
                {".xml", "text/plain"},
        };
        String type = "";
        for (int i = 0; i < MATCH_ARRAY.length; i++) {
            if (path.contains(MATCH_ARRAY[i][0])) {
                type = MATCH_ARRAY[i][1];
                break;
            }
        }
        try {
            Uri uri = FileProvider.getUriForFile(this, "com.yc.mmrecover.myFileProvider", new File(path));

            intent.setDataAndType(uri, type);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "无法打开该格式文件!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
