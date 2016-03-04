package org.senydevpkg.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

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
 * 公共BaseAdapter，抽取了contentView的复用，提高Adapter的开发效率.必须使用AbsBaseAdapter.ViewHolder来初始化View
 * <p/>
 * Created by Seny on 2016/2/21.
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    private BaseHolder holder;
    private List<T> mData = new ArrayList<>();

    /**
     * 接收AbsListView要显示的数据
     *
     * @param data    要显示的数据
     */
    public AbsBaseAdapter( List<T> data) {
        this.mData = data;
    }

    public List<T> getData() {
        return mData;
    }

    /**
     * 更新AbsListView的数据
     *
     * @param newData 新数据
     */
    public void notifyDataSetChanged(List<T> newData) {
        this.mData = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (BaseHolder) convertView.getTag();
        } else {
            holder = onCreateViewHolder(parent, getItemViewType(position));
        }
        Assert.notNull(holder);
        holder.bindData(getItem(position));
        return holder.rootView;
    }

    /**
     * 创建ViewHolder
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    protected abstract BaseHolder onCreateViewHolder(ViewGroup parent, int viewType);
}
