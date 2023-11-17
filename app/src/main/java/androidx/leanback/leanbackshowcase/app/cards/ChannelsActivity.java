package androidx.leanback.leanbackshowcase.app.cards;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.leanback.leanbackshowcase.R;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class ChannelsActivity extends Activity {
    public ExoPlayer player;
    public SimpleExoPlayerView player_video;

    public ConstraintLayout layout_msg;
    public String videoURL = "https://file-examples.com/storage/fe83b11fb06553bbba686e7/2017/04/file_example_MP4_480_1_5MG.mp4";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        initView();
        initData();
    }

    public void initView(){
        player_video = findViewById(R.id.video_view2);
        layout_msg = findViewById(R.id.MSGVIDEO);
    }
    public void initData(){
//        player = new ExoPlayer.Builder(getBaseContext()).build();
//        player_video.setPlayer(player);
//        MediaItem mediaItem = MediaItem.fromUri(videoURL);
//        player.setMediaItem(mediaItem);
//        player.prepare();
//        player.setPlayWhenReady(true);
//        layout_msg.setVisibility(View.GONE);
    }
}
