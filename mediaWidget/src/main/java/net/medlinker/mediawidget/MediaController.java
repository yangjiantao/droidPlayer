package net.medlinker.mediawidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.pili.pldroid.player.IMediaController;

import net.medlinker.mediawidget.util.MediaPlayerUtils;
import net.medlinker.mediawidget.view.ControllerView;

/**
 * You can write a custom MediaController instead of this class
 * A MediaController widget must implement all the interface defined by com.pili.pldroid.player.IMediaController
 * tip:这里实用anchorView addview添加的controller， 类似实现：windowManager、popupWindow
 */
public class MediaController extends FrameLayout implements IMediaController {

    private static final String TAG = "PLMediaController";
    private IMediaController.MediaPlayerControl mPlayer;
    private Context mContext;
    private int mAnimStyle;
    private ViewGroup mAnchor;
    private ControllerView mRoot;
    private ProgressBar mProgress;
    private long mDuration;
    private boolean mShowing;
    private boolean mDragging;
    private boolean mInstantSeeking = true;
    private static int sDefaultTimeout = 3000;
    private static final int SEEK_TO_POST_DELAY_MILLIS = 200;

    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private AudioManager mAM;
    private Runnable mLastSeekBarRunnable;

    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initController(context);
    }

    public MediaController(Context context) {
        super(context);
        initController(context);
    }

    private boolean initController(Context context) {
        mContext = context.getApplicationContext();
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        return true;
    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     *
     * @return The controller view.
     */
    protected ControllerView makeControllerView() {
        return new ControllerView(mContext);
    }

    /**
     * Control the action when the seekbar dragged by user
     *
     * @param seekWhenDragging
     * True the media will seek periodically
     */
    public void setInstantSeeking(boolean seekWhenDragging) {
        mInstantSeeking = seekWhenDragging;
    }

    /**
     * <p>
     * Change the animation style resource for this controller.
     * </p>
     *
     * <p>
     * If the controller is showing, calling this method will take effect only
     * the next time the controller is shown.
     * </p>
     *
     * @param animationStyle
     * animation style to use when the controller appears and disappears.
     * Set to -1 for the default animation, 0 for no animation,
     * or a resource identifier for an explicit animation.
     *
     */
    public void setAnimationStyle(int animationStyle) {
        mAnimStyle = animationStyle;
    }

    public interface OnShownListener {
        public void onShown();
    }

    private OnShownListener mShownListener;

    public void setOnShownListener(OnShownListener l) {
        mShownListener = l;
    }

    public interface OnHiddenListener {
        public void onHidden();
    }

    private OnHiddenListener mHiddenListener;

    public void setOnHiddenListener(OnHiddenListener l) {
        mHiddenListener = l;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
            }
        }
    };

    private long setProgress() {
        if (mPlayer == null || mDragging)
            return 0;

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;

        mRoot.setEndTime(MediaPlayerUtils.generateTime(mDuration));
        mRoot.setCurrentTime(MediaPlayerUtils.generateTime(position));
        Log.d("yjt"," MediaController setProgress --- position="+position+"; duration="+duration);
        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("yjt"," MediaController dispatchKeyEvent --- ");
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
//            if (mPauseButton != null)
//                mPauseButton.requestFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }

    private OnClickListener mViewsOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            try {
                Object tag = v.getTag(v.getId());
                int caseId = (int) tag;
                switch (caseId){
                    case ControllerView.CONTROLL_PAUSE_START_VIEW_ID:
                        doPauseResume();
                        show(sDefaultTimeout);
                        break;

                    case ControllerView.CONTROLL_TOP_TITLE_VIEW_ID:

                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void updatePausePlay() {
        if (mRoot == null)
            return;
        mRoot.updatePausePlay(mPlayer.isPlaying());
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying())
            mPlayer.pause();
        else
            mPlayer.start();
        updatePausePlay();
    }

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar bar) {
            mDragging = true;
            show(3600000);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mInstantSeeking)
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser)
                return;

            final int newposition = (int) (mDuration * progress) / 1000;
            String time = MediaPlayerUtils.generateTime(newposition);
            if (mInstantSeeking) {
                mHandler.removeCallbacks(mLastSeekBarRunnable);
                mLastSeekBarRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mPlayer.seekTo(newposition);
                    }
                };
                mHandler.postDelayed(mLastSeekBarRunnable, SEEK_TO_POST_DELAY_MILLIS);
            }
            mRoot.setCurrentTime(time);
        }

        public void onStopTrackingTouch(SeekBar bar) {
            if (!mInstantSeeking)
                mPlayer.seekTo((int)(mDuration * bar.getProgress()) / 1000);

            show(sDefaultTimeout);
            mHandler.removeMessages(SHOW_PROGRESS);
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mDragging = false;
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
        }
    };

    private OnClickListener mRewListener = new OnClickListener() {
        public void onClick(View v) {
            int pos = (int)mPlayer.getCurrentPosition();
            pos -= 5000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private OnClickListener mFfwdListener = new OnClickListener() {
        public void onClick(View v) {
            int pos = (int)mPlayer.getCurrentPosition();
            pos += 15000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    /**
     * Set the view that acts as the anchor for the control view.
     *
     * - This can for example be a VideoView, or your Activity's main view.
     * - AudioPlayer has no anchor view, so the view parameter will be null.
     *
     * @param view
     * The view to which to anchor the controller when it is visible.
     */
    @Override
    public void setAnchorView(View view) {
        try {
            mAnchor = (ViewGroup) view;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("yjt","MediaController setAnchorView view is not viewGroup---");
        }
        if (mAnchor == null) {
            sDefaultTimeout = 0; // show forever
        }
        Log.d("yjt","MediaController setAnchorView mAnchor = "+mAnchor);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        removeAllViews();
        mRoot = makeControllerView();
        addView(mRoot, frameParams);
        initControllerView();
    }

    /**
     * todo
     */
    private void initControllerView() {
        mProgress = mRoot.getProgressBar();
        mRoot.getProgressBar().setOnSeekBarChangeListener(mSeekListener);
        mRoot.getProgressBar().setThumbOffset(1);
        mRoot.getProgressBar().setMax(1000);
        mRoot.setViewsOnClickListener(mViewsOnClickListener);
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     *
     * @param timeout
     * The timeout in milliseconds. Use 0 to show the controller until hide() is called.
     */
    @Override
    public void show(int timeout) {
        Log.d("yjt","MediaController show timeout = "+timeout+"; mShowing="+mShowing+"; mAnchor="+mAnchor);
        if (!mShowing && mAnchor != null) {
//            if (mAnchor != null && mAnchor.getWindowToken() != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                    mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                }
//            }
            ViewGroup.LayoutParams tlp =
                    new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            mAnchor.addView( this, tlp);
            mShowing = true;
            if (mShownListener != null)
                mShownListener.onShown();
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
                    timeout);
        }
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void hide() {
        Log.d("yjt","MediaController hide mShowing="+mShowing);
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                mAnchor.removeView( this);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "MediaController already removed");
            }
            mShowing = false;
            if (mHiddenListener != null)
                mHiddenListener.onHidden();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mProgress != null)
            mProgress.setEnabled(enabled);
        super.setEnabled(enabled);
    }
}
