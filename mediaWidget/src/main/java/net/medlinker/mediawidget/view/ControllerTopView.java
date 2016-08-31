package net.medlinker.mediawidget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.medlinker.mediawidget.R;

/**
 * 功能描述
 *
 * @author <a href="mailto:yangjiantao@medlinker.net">Jiantao.Yang</a>
 *         2016/8/30 10:59
 * @version 1.0
 */
public class ControllerTopView extends FrameLayout implements View.OnClickListener{

    private View mRootView, mRightLayout, mImageMoreMenu;
    private TextView mTitleView, mVideoRate, mSwitchParts;

    private int mFullScreenHeight, mNormalHeight;

    private OnClickListener mOnClickListener;

    public ControllerTopView(Context context) {
        super(context);
        setupViews(context);
    }

    public ControllerTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupViews(context);
    }

    public ControllerTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ControllerTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupViews(context);
    }

    private void setupViews(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_blue_controller_top_view, this);
        mRootView = view.findViewById(R.id.controller_top_layout);
        mRightLayout = view.findViewById(R.id.controller_top_right_layout);
        mImageMoreMenu = view.findViewById(R.id.image_more_menu);
        mImageMoreMenu.setOnClickListener(this);
        mImageMoreMenu.setTag(R.id.image_more_menu, ControllerView.CONTROLL_TOP_MORE_MENU_VIEW_ID);

        mTitleView = (TextView) view.findViewById(R.id.title_view);
        mTitleView.setOnClickListener(this);
        mTitleView.setTag(R.id.title_view, ControllerView.CONTROLL_TOP_TITLE_VIEW_ID);
        mVideoRate = (TextView) view.findViewById(R.id.video_top_rate);
        mVideoRate.setOnClickListener(this);
        mVideoRate.setTag(R.id.video_top_rate, ControllerView.CONTROLL_TOP_VIDEORATE_VIEW_ID);
        mSwitchParts = (TextView) view.findViewById(R.id.video_top_switch_parts);
        mSwitchParts.setOnClickListener(this);
        mSwitchParts.setTag(R.id.video_top_switch_parts, ControllerView.CONTROLL_TOP_SWITCH_PARTS_VIEW_ID);
        mFullScreenHeight = context.getResources().getDimensionPixelSize(R.dimen.controller_fullscreen_top_view_height);
        mNormalHeight = context.getResources().getDimensionPixelSize(R.dimen.controller_normal_top_view_height);

        setScreenMode(false);
    }

    public void setTitle(String title){
        mTitleView.setText(title);
    }

    public void setScreenMode(boolean isFullScreen){
        if(isFullScreen){
            fullScreenMode();
        }else{
            normalScreenMode();
        }
    }

    public void setViewsOnClickListener(OnClickListener listener){
        this.mOnClickListener = listener;
    }

    private void normalScreenMode() {
        setRootViewLayoutParams(mNormalHeight);
        mRootView.setAlpha(0.35f);
        mRightLayout.setVisibility(GONE);
        mImageMoreMenu.setVisibility(VISIBLE);
    }

    /**
     * 全屏
     */
    private void fullScreenMode() {
        setRootViewLayoutParams(mFullScreenHeight);
        mRootView.setAlpha(0.85f);
        mRightLayout.setVisibility(VISIBLE);
        mImageMoreMenu.setVisibility(GONE);
    }

    private void setRootViewLayoutParams(int height) {
        ViewGroup.LayoutParams layoutParams = mRootView.getLayoutParams();
        if(layoutParams != null){
            layoutParams.height = height;
        }else{
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        }
        mRootView.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View view) {
        if(mOnClickListener != null){
            mOnClickListener.onClick(view);
        }
    }
}
