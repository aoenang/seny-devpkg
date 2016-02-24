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
import org.seny.android.sample.resp.NewsResponse;
import org.senydevpkg.base.AbsBaseAdapter;

import java.util.List;

public class NewsAdapter extends AbsBaseAdapter<NewsResponse.DataEntity.TagEntity> {

    public NewsAdapter(Context context, List<NewsResponse.DataEntity.TagEntity> data) {
        super(context, data);
    }


    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(getContext(), R.layout.item_weather, null);
        return new NewsViewHolder(inflate);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position) {

        NewsViewHolder mvh = (NewsViewHolder) holder;
        NewsResponse.DataEntity.TagEntity data = getItem(position);
        mvh.tv_text1.setText(data.name);
        mvh.tv_text2.setText(data.type);
        mvh.tv_text3.setText(String.valueOf(data.count));
    }

    class NewsViewHolder extends ViewHolder {

        public TextView tv_text1;
        public TextView tv_text2;
        public TextView tv_text3;

        /**
         * 传入 root view，将自动绑定TAG
         *
         * @param view root view
         */
        public NewsViewHolder(View view) {
            super(view);
            tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
            tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
            tv_text3 = (TextView) view.findViewById(R.id.tv_text3);
        }

    }
}

