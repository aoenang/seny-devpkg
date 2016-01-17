package com.itheima.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.senydevpkg.DevPkg;
import org.senydevpkg.utils.APKDownloader;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {


    private android.widget.EditText eturl;
    private android.widget.Button btndownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btndownload = (Button) findViewById(R.id.btn_download);
        this.eturl = (EditText) findViewById(R.id.et_url);
        btndownload.setOnClickListener(this);
        DevPkg.init(getApplication());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                String apkUri = eturl.getText().toString();
                APKDownloader.getInstance().downloadAPK(apkUri, apkUri.substring(apkUri.lastIndexOf("/")));
                break;
        }
    }
}
