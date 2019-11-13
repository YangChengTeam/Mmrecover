package com.yc.mmrecover.controller.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.mmrecover.R;
import com.yc.mmrecover.model.bean.MediaInfo;
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
                openFileByPath(RecoverFileActivity.this, mMediaList.get(position).getPath());
            }
        });

    }

    /**
     * 根据路径打开文件
     *
     * @param context 上下文
     * @param path    文件路径
     */
    public void openFileByPath(Context context, String path) {
        if (context == null || path == null)
            return;

        Log.d("ssssss", "openFileByPath: path " + path);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //文件的类型
        String type = "";
        for (int i = 0; i < MATCH_ARRAY.length; i++) {
            //判断文件的格式
            if (path.toString().contains(MATCH_ARRAY[i][0].toString())) {
                type = MATCH_ARRAY[i][1];
                break;
            }
        }
        try {
            //设置intent的data和Type属性
            Uri uri = FileProvider.getUriForFile(context, "com.yc.mmrecover.myFileProvider", new File(path));
            intent.setDataAndType(uri, type);
            //跳转
            context.startActivity(intent);
        } catch (Exception e) { //当系统没有携带文件打开软件，提示
            Log.d("ssssss", "openFileByPath: e " + e.toString());
            Toast.makeText(context, "无法打开该格式文件!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //建立一个文件类型与文件后缀名的匹配表
    private final String[][] MATCH_ARRAY = {
            //{后缀名，    文件类型}
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel application/x-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".txt", "text/plain"},
            {".pptx", "tapplication/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".xml", "text/plain"},
    };
}
