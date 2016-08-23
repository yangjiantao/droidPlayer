package net.medlinker.mediawidget.util;

import java.util.Formatter;
import java.util.Locale;

/**
 * 功能描述
 *
 * @author <a href="mailto:yangjiantao@medlinker.net">Jiantao.Yang</a>
 *         2016/8/11 20:51
 * @version 1.0
 */
public class MediaPlayerUtils {

    private static StringBuilder mFormatBuilder;
    private static Formatter mFormatter;

    static {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    public static String getVideoDisplayTime(long timeMs) {
        int totalSeconds = (int) timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
