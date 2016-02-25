package org.seny.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.seny.android.sample.adapter.NewsAdapter;
import org.seny.android.sample.adapter.WeatherAdapter;
import org.seny.android.sample.protocol.NewsProtocol;
import org.seny.android.sample.protocol.WeatherProtocol;
import org.seny.android.sample.resp.NewsResponse;
import org.seny.android.sample.resp.WeatherResponse;
import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.resp.IResponse;
import org.senydevpkg.utils.MyToast;
import org.senydevpkg.view.LoadStateLayout;


public class MainActivity extends Activity implements View.OnClickListener, HttpLoader.HttpListener {


    protected HttpLoader HL;
    protected LoadStateLayout mPager;
    protected WeatherAdapter mWeatherAdapter;
    protected NewsAdapter mNewsAdapter;
    private ListView mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        HL = HttpLoader.getInstance(this);
    }

    private void initView() {
        findViewById(R.id.btn_request_get).setOnClickListener(this);
        findViewById(R.id.btn_request_post).setOnClickListener(this);

        mPager = (LoadStateLayout) findViewById(R.id.lp_result);
        mPager.setEmptyView(R.layout.layout_loadpager_state_empty);
        mPager.setErrorView(R.layout.layout_loadpager_state_error);
        mPager.setLoadingView(R.layout.layout_loadpager_state_loading);

        mContentView = (ListView) findViewById(R.id.lv_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_get:
                //发起Get天气请求
                new WeatherProtocol().doRequest(HL, this).setTag(this);
                break;
            case R.id.btn_request_post:
                //发起Post请求
                new NewsProtocol().doRequest(HL, this).setTag(this);
                break;
        }
        //设置为加载状态视图
        mPager.setState(LoadStateLayout.STATE_LOADING);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {

        switch (requestCode) {
            case Constants.REQUEST_CODE_WEATHER:

                //根据请求码，处理对应结果
                WeatherResponse resp = (WeatherResponse) response;
                if (resp.errno != 0) {//参数错误，没有返回数据
                    mPager.setState(LoadStateLayout.STATE_EMPTY);
                } else {
                    mPager.setState(LoadStateLayout.STATE_SUCCESS);
                    //设置数据
                    if (mWeatherAdapter == null) {
                        mWeatherAdapter = new WeatherAdapter(getApplication(), resp.data.weather);
                        mContentView.setAdapter(mWeatherAdapter);
                    } else {
                        //更新数据
                        mWeatherAdapter.notifyDataSetChanged(resp.data.weather);
                    }

                }
                break;
            case Constants.REQUEST_CODE_GETUSERDATA:

                NewsResponse newsResponse = (NewsResponse) response;
                if (newsResponse.errno != 0) {
                    mPager.setState(LoadStateLayout.STATE_EMPTY);
                } else {
                    mPager.setState(LoadStateLayout.STATE_SUCCESS);
                    //设置数据
                    if (mNewsAdapter == null) {
                        mNewsAdapter = new NewsAdapter(getApplicationContext(), newsResponse.data.tag);
                        mContentView.setAdapter(mNewsAdapter);
                    } else {
                        //更新数据
                        mNewsAdapter.notifyDataSetChanged(newsResponse.data.tag);
                    }

                }
                break;

        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

        MyToast.show(this, "Request " + requestCode + " error , msg : " + error.getMessage());
        mPager.setState(LoadStateLayout.STATE_ERROR);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (HL != null) {
            HL.cancelRequest(this);//取消所有当前activity中发出的请求
        }
    }


}
