package net.medlinker.mediawidget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import net.medlinker.mediawidget.R;

/**
 * 功能描述
 *
 * @author <a href="mailto:yangjiantao@medlinker.net">Jiantao.Yang</a>
 *         2016/8/11 19:49
 * @version 1.0
 */
public class ControllerView extends FrameLayout implements View.OnClickListener{

    private TextView mCurrentTimeView, mTotalTimeView;
    private ImageView mPauseStartIcon, mFullScreenSwitchIcon, mSwitchVolumeIcon;
    private ControllerTopView mTopView;
    private SeekBar mProgressBar;
    private MediaPlayerSeekView mWidgetSeekView;
    private MediaPlayerControllerVolumeView mWidgetVolumeView;
    private View mBufferView;
    private OnClickListener mOnClickListener;

    //view点击事件id
    public static final int CONTROLL_PAUSE_START_VIEW_ID = 0x01;
    public static final int CONTROLL_FULL_SCREEN_SWITCH_VIEW_ID = 0x02;
    public static final int CONTROLL_SWITCH_VOLUME_VIEW_ID = 0x03;
    public static final int CONTROLL_TOP_MORE_MENU_VIEW_ID = 0x04;
    public static final int CONTROLL_TOP_TITLE_VIEW_ID = 0x05;
    public static final int CONTROLL_TOP_VIDEORATE_VIEW_ID = 0x06;
    public static final int CONTROLL_TOP_SWITCH_PARTS_VIEW_ID = 0x07;


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
        View view = LayoutInflater.from(context).inflate(R.layout.video_blue_media_player_controller, this);
        mTopView = (ControllerTopView) view.findViewById(R.id.top_view);
        mTopView.setViewsOnClickListener(this);
        mPauseStartIcon = (ImageView) view.findViewById(R.id.video_start_pause_image_view);
        mPauseStartIcon.setOnClickListener(this);
        mPauseStartIcon.setTag(R.id.video_start_pause_image_view, CONTROLL_PAUSE_START_VIEW_ID);
        mFullScreenSwitchIcon = (ImageView) view.findViewById(R.id.video_fullscreen_switch_view);
        mFullScreenSwitchIcon.setOnClickListener(this);
        mFullScreenSwitchIcon.setTag(R.id.video_fullscreen_switch_view, CONTROLL_FULL_SCREEN_SWITCH_VIEW_ID);
        mSwitchVolumeIcon = (ImageView) view.findViewById(R.id.video_volume_ic);
        mSwitchVolumeIcon.setOnClickListener(this);
        mSwitchVolumeIcon.setTag(R.id.video_volume_ic, CONTROLL_SWITCH_VOLUME_VIEW_ID);
        mCurrentTimeView = (TextView) view.findViewById(R.id.video_current_time_text_view);
        mTotalTimeView = (TextView) view.findViewById(R.id.video_total_time_text_view);

        mProgressBar = (SeekBar) view.findViewById(R.id.video_seekbar);
        mWidgetSeekView = (MediaPlayerSeekView) view.findViewById(R.id.widget_seek_view);
        mWidgetVolumeView = (MediaPlayerControllerVolumeView) view.findViewById(R.id.widget_controller_volume);
    }

    public SeekBar getProgressBar() {
        return mProgressBar;
    }


    public void setEndTime(String time) {
        mTotalTimeView.setText(time);
    }

    public void setCurrentTime(String time) {
        mCurrentTimeView.setText(time);
    }

    public void updatePausePlay(boolean playing) {
        mPauseStartIcon.setImageResource(playing ? R.drawable.video_pause_land_image : R.drawable.video_play_land_image);
    }

    public void setViewsOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View view) {
        if(mOnClickListener != null){
            mOnClickListener.onClick(view);
        }
    }
}
