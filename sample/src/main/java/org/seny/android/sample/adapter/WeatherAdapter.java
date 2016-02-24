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

import java.util.List;

public class WeatherAdapter extends AbsBaseAdapter<WeatherResponse.DataEntity.WeatherEntity> {

    public WeatherAdapter(Context context, List<WeatherResponse.DataEntity.WeatherEntity> data) {
        super(context, data);
    }


    @Override
    protected AbsBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(getContext(), R.layout.item_weather, null);
        return new WeatherViewHolder(inflate);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position) {

        WeatherViewHolder mvh = (WeatherViewHolder) holder;
        WeatherResponse.DataEntity.WeatherEntity data = getItem(position);
        mvh.tv_time.setText(data.time);
        mvh.tv_weather.setText(data.weather);
        mvh.tv_temperature.setText(data.temperature);
    }

    class WeatherViewHolder extends AbsBaseAdapter.ViewHolder {

        public TextView tv_time;
        public TextView tv_weather;
        public TextView tv_temperature;

        /**
         * 传入 root view，将自动绑定TAG
         *
         * @param view root view
         */
        public WeatherViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_text1);
            tv_weather = (TextView) view.findViewById(R.id.tv_text2);
            tv_temperature = (TextView) view.findViewById(R.id.tv_text3);
        }

    }
}

