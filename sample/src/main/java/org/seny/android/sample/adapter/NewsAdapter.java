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
import org.senydevpkg.base.BaseHolder;

import java.util.List;

public class NewsAdapter extends AbsBaseAdapter<NewsResponse.DataEntity.TagEntity> {

    private final Context mContext;

    public NewsAdapter(Context context, List<NewsResponse.DataEntity.TagEntity> data) {
        super(context,data);
        mContext = context;
    }


    @Override
    protected BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new NewsViewHolder(mContext);
    }


    static class NewsViewHolder extends BaseHolder<NewsResponse.DataEntity.TagEntity> {

        public TextView tv_text1;
        public TextView tv_text2;
        public TextView tv_text3;

        public NewsViewHolder(Context context) {
            super(context);
        }


        @Override
        protected View initView() {
            View view = View.inflate(getContext(), R.layout.item_weather, null);
            tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
            tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
            tv_text3 = (TextView) view.findViewById(R.id.tv_text3);
            return view;
        }


        @Override
        public void bindData(NewsResponse.DataEntity.TagEntity data) {
            tv_text1.setText(data.name);
            tv_text2.setText(data.type);
            tv_text3.setText(String.valueOf(data.count));
        }
    }
}

