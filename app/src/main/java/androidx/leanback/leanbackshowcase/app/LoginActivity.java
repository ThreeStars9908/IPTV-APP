package androidx.leanback.leanbackshowcase.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.leanback.leanbackshowcase.R;

import com.marsad.stylishdialogs.StylishAlertDialog;
import com.squareup.picasso.Picasso;

public class LoginActivity extends Activity implements View.OnClickListener {
    public ImageView img_back;
    public String str_img_back = "https://images.pexels.com/photos/534164/pexels-photo-534164.jpeg?auto=compress&cs=tinysrgb&w=1600";
    public Button btn_login, btn_getCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
    }

    public void initView(){

        img_back = findViewById(R.id.img_back);
        btn_login = findViewById(R.id.login_btn);
        btn_getCode = findViewById(R.id.login_activatecode);
        btn_login.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
    }
    public void initData(){

        Picasso.get().load(str_img_back).into(img_back);
    }

    @Override
    public void onBackPressed() {
        new StylishAlertDialog(this, StylishAlertDialog.WARNING)
                .setTitleText("Are you sure?")
                .setContentText("You want to exit?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new StylishAlertDialog.OnStylishClickListener() {
                    @Override
                    public void onClick(StylishAlertDialog sDialog) {
                        finish();
                    }
                })
                .setCancelButton("No", new StylishAlertDialog.OnStylishClickListener() {
                    @Override
                    public void onClick(StylishAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                }).show();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn){
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

}
