package net.medlinker.mediawidget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.medlinker.mediawidget.R;

/**
 * 功能描述
 *
 * @author <a href="mailto:yangjiantao@medlinker.net">Jiantao.Yang</a>
 *         2016/8/11 19:49
 * @version 1.0
 */
public class ControllerView extends FrameLayout{

    private View mLandscapeBackIcon;

    private TextView mLandscapeTitle;

    private TextView mSwitchParts;

    private TextView mSwitchResolution;


    public ControllerView(Context context) {
        super(context);
        setupViews(context);
    }

    public ControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupViews(context);
    }

    public ControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ControllerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupViews(context);
    }

    private void setupViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.video_blue_media_player_controller, this);
    }




}
