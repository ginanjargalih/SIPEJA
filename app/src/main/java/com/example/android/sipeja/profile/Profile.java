package com.example.android.sipeja.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.example.android.sipeja.Menu_utama;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;

public class Profile extends AppCompatActivity {

    public static final String EXTRA_MESSAGE5 = "profile" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //warna status bar
        if(Build.VERSION.SDK_INT >= 21){

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        

        //ambil intent
        Intent intent = getIntent();
        //ambil datanya
        String pesan = intent.getStringExtra(Menu_utama.EXTRA_MESSAGE5);


        //Creating a shared preference
        SharedPreferences sharedPreferences = Profile.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        String user = sharedPreferences.getString(Config.Name,"");
        String nip = sharedPreferences.getString(Config.NIP,"");
        String email = sharedPreferences.getString(Config.Email,"");

        TextView txtView=(TextView)findViewById(R.id.username);
        txtView.setText(user);

        TextView txtView2=(TextView)findViewById(R.id.nip);
        txtView2.setText(nip);

        TextView txtView3=(TextView)findViewById(R.id.email);
        txtView3.setText(email);

        //untuk foto
        String s=user.substring(0,1);

        TextDrawable drawable = TextDrawable.builder().buildRoundRect(s,R.color.colorKontak, 100);

        ImageView image = (ImageView) findViewById(R.id.imgProfile);
        image.setImageDrawable(drawable);
    }


    public void klikKembali(View view) {
        Intent intent2 = getIntent();
        intent2.putExtra(Menu_utama.EXTRA_MESSAGE5,"");
        setResult(RESULT_OK, intent2);
        finish();

    }

}
