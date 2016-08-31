package net.medlinker.droidplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT_TEST_URL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    private static final String LOCAL_TEST_URL = "/storage/emulated/0/tencent/tassistant/file/YiHome_Pairing_Tutorial_zh_CN.mp4";

    @BindView(R.id.test_textview)
    TextView tv;

    @BindView(R.id.start)
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.start) void start() {
        Toast.makeText(this, "Hello, start!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PLVideoTextureActivity.class);
        intent.putExtra("videoPath", LOCAL_TEST_URL);
        intent.putExtra("liveStreaming", 0);
        startActivity(intent);
    }
}
