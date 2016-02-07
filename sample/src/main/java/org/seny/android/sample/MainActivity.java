package org.seny.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.seny.android.sample.resp.WeatherResponse;
import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;
import org.senydevpkg.utils.MyToast;

import java.util.Date;


public class MainActivity extends Activity implements View.OnClickListener, HttpLoader.HttpListener {

    private static final int REQUEST_CODE_WEATHER = 0x01;
    private static final String API = "http://mobile.weather.com.cn/data/zsM/101010100.html";
    protected HttpLoader HL;
    protected TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_gson_request).setOnClickListener(this);
        findViewById(R.id.btn_string_request).setOnClickListener(this);

        mTvResult = (TextView) findViewById(R.id.tv_result);
        HL = HttpLoader.getInstance(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gson_request:

                //使用HttpLoader发送GsonRequest
                HttpParams params = new HttpParams();//设置参数
                params.put("_", new Date(1381891661502l).getTime() + "");
                HL.get(API, params, WeatherResponse.class, REQUEST_CODE_WEATHER, this)//发送Get请求并返回Request对象
                        .setTag(this);//设置tag为activity，方便释放
                break;

            case R.id.btn_string_request:

                //使用HttpLoader发送其他Request，比如StringRequest
                HttpParams strParams = new HttpParams();//设置参数
                strParams.put("_", new Date(1381891661502l).getTime() + "");
                Request<?> strRequest = new StringRequest(API + strParams.toGetParams(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTvResult.setText(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTvResult.setText(error.getMessage());
                    }
                }).setTag(this);
                HL.addRequest(strRequest);
                break;
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {

        switch (requestCode) {
            case REQUEST_CODE_WEATHER:

                //根据请求码，处理对应结果
                WeatherResponse resp = (WeatherResponse) response;

                mTvResult.setText(resp.toString());
                break;

        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

        MyToast.show(this, "Request " + requestCode + " error , msg : " + error.getMessage());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (HL != null) {
            HL.cancelRequest(this);//取消所有当前activity中发出的请求
        }
    }
}
