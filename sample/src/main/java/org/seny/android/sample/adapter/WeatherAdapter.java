package org.seny.android.sample.adapter;

/**
 * ━━━━ Code is far away from ━━━━━━
 * 　　  () 　　　  ()
 * 　　  ( ) 　　　( )
 * 　　  ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━ bug with the XYY protecting━━━
 * <p/>
 * Created by Seny on 2016/2/24.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.seny.android.sample.R;
import org.seny.android.sample.resp.WeatherResponse;
import org.senydevpkg.base.AbsBaseAdapter;
import org.senydevpkg.base.BaseHolder;

import java.util.List;

public class WeatherAdapter extends AbsBaseAdapter<WeatherResponse.DataEntity.WeatherEntity> {


    public WeatherAdapter(Context context, List<WeatherResponse.DataEntity.WeatherEntity> data) {
        super(context,data);
    }

    @Override
    protected BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new WeatherViewHolder(getContext());
    }


    static class WeatherViewHolder extends BaseHolder<WeatherResponse.DataEntity.WeatherEntity> {

        public TextView tv_time;
        public TextView tv_weather;
        public TextView tv_temperature;

        public WeatherViewHolder(Context context) {
            super(context);
        }


        @Override
        protected View initView() {
            View inflate = View.inflate(getContext(), R.layout.item_weather, null);
            tv_time = (TextView) inflate.findViewById(R.id.tv_text1);
            tv_weather = (TextView) inflate.findViewById(R.id.tv_text2);
            tv_temperature = (TextView) inflate.findViewById(R.id.tv_text3);
            return inflate;
        }

        @Override
        public void bindData(WeatherResponse.DataEntity.WeatherEntity data) {
            tv_time.setText(data.time);
            tv_weather.setText(data.weather);
            tv_temperature.setText(data.temperature);
        }
    }
}

