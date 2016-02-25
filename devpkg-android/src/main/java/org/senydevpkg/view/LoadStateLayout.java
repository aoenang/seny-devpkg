package org.senydevpkg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
 * LoadStateLayout，用来作为网络数据加载显示界面，可以设置加载中，空数据，加载成功，加载错误4种状态所显示的View
 * <p/>
 * Created by Seny on 2016/2/18.
 */
public class LoadStateLayout extends FrameLayout {
    public static final int STATE_LOADING = 0;
    public static final int STATE_EMPTY = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_ERROR = 3;
    protected LayoutInflater mInflater;
    private int mState = STATE_LOADING;//当前的状态,默认为加载状态
    private View mEmptyView;//数据为空时显示的View
    private View mErrorView;//加载错误时显示的View
    private View mContentView;//加载成功时显示的View
    private View mLoadingView;//正在加载时显示数据的View

    public LoadStateLayout(Context context) {
        this(context, null);
    }

    public LoadStateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadStateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("LoadStateLayout can host only one content child for STATE_SUCCESS!");

        }
        mContentView = getChildAt(0);//获取第0个子View，并设置为ContentView
        mContentView.setVisibility(GONE);//默认隐藏
    }


    /**
     * 获取当前视图状态
     *
     * @return
     */
    public int getState() {
        return mState;
    }

    /**
     * 设置当前的显示状态，更新显示视图
     *
     * @param state one of {@link #STATE_LOADING}, {@link #STATE_EMPTY},
     *              {@link #STATE_SUCCESS},{@link #STATE_ERROR}
     */
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
            case STATE_EMPTY:
            case STATE_SUCCESS:
            case STATE_ERROR:
                mState = state;
                updateViewState();
                break;
            default:
                throw new IllegalArgumentException("must be one of STATE_LOADING,STATE_EMPTY,STATE_SUCCESS,STATE_ERROR !");
        }
    }

    /**
     * 获取数据为空时显示的View
     *
     * @return
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 设置加载数据为空时显示的布局ID
     *
     * @param layoutResId
     */
    public void setEmptyView(int layoutResId) {
        setEmptyView(mInflater.inflate(layoutResId, this, false));
    }

    /**
     * 设置加载数据为空时显示的View
     *
     * @param view
     */
    public void setEmptyView(View view) {
        removeView(mEmptyView);
        mEmptyView = view;
        addStateView(view);
        updateViewState();
    }

    /**
     * 获取加载错误适合显示的View
     *
     * @return
     */
    public View getErrorView() {
        return mErrorView;
    }

    /**
     * 设置加载数据错误时显示的布局id
     *
     * @param layoutResId
     */
    public void setErrorView(int layoutResId) {
        setErrorView(mInflater.inflate(layoutResId, this, false));
    }

    /**
     * 设置加载数据错误时显示的View
     *
     * @param view
     */
    public void setErrorView(View view) {
        removeView(mErrorView);
        mErrorView = view;
        addStateView(view);
        updateViewState();
    }

    /**
     * 获取显示数据的View
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }


    /**
     * 获取加载时显示的View
     *
     * @return
     */
    public View getLoadingView() {
        return mLoadingView;
    }

    /**
     * 设置加载数据错误时显示的布局id
     *
     * @param layoutResId
     */
    public void setLoadingView(int layoutResId) {
        setLoadingView(mInflater.inflate(layoutResId, this, false));
    }

    /**
     * 设置加载数据错误时显示的View
     *
     * @param view
     */
    public void setLoadingView(View view) {
        removeView(mLoadingView);
        mLoadingView = view;
        addStateView(view);
        updateViewState();
    }

    /**
     * 添加state rootView,初始隐藏
     *
     * @param view
     */
    private void addStateView(View view) {
        if (view != null) {
            ViewGroup.LayoutParams defaultParams = view.getLayoutParams();//如果默认没有LayoutParams或者不是FrameLayout.LayoutParams,重新生成一个
            if (defaultParams == null || !(defaultParams instanceof LayoutParams)) {
                defaultParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            LayoutParams params = (LayoutParams) defaultParams;
            params.gravity = Gravity.CENTER;//局中显示
            view.setVisibility(GONE);
            addView(view, params);
        }
    }

    /**
     * 根据当前状态，显示对应的View
     */
    private void updateViewState() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mState == STATE_LOADING ? VISIBLE : GONE);

        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mState == STATE_EMPTY ? VISIBLE : GONE);

        }
        if (mContentView != null) {
            mContentView.setVisibility(mState == STATE_SUCCESS ? VISIBLE : GONE);

        }
        if (mErrorView != null) {
            mErrorView.setVisibility(mState == STATE_ERROR ? VISIBLE : GONE);
        }
    }

}
