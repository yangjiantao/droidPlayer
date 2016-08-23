package net.medlinker.mediawidget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 音量调节控件
 * todo
 * @author <a href="mailto:yangjiantao@medlinker.net">Jiantao.Yang</a>
 *         2016/8/11 21:01
 * @version 1.0
 */
public class MediaPlayerControllerVolumeView extends FrameLayout{
    public MediaPlayerControllerVolumeView(Context context) {
        super(context);
    }

    public MediaPlayerControllerVolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaPlayerControllerVolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MediaPlayerControllerVolumeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
