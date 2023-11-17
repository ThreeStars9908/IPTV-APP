package androidx.leanback.leanbackshowcase.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.leanbackshowcase.R;

public class SplashActivity extends Activity {
    public TextView txt_version;
    public String str_version = "1.0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
                // close this activity
                finish();

            }
        }, 3*1000); // wait for 5 seconds
    }



    public void initView(){
        txt_version = findViewById(R.id.versionlbl);
    }
    public void initData(){
        str_version = "VERSION " + str_version;
        txt_version.setText(str_version);
    }
}
