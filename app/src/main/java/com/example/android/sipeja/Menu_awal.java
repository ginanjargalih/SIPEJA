package com.example.android.sipeja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sipeja.config.Config;

public class Menu_awal extends AppCompatActivity {

    //share prefrence
    SharedPreferences sharedpreferences;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_awal);

        //warna status bar
        if(Build.VERSION.SDK_INT >= 21){

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        //Creating a shared preference
        sharedpreferences = Menu_awal.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);
        cek_login();


    }



    public void klik_pegawai(View view) {
        Config.akses = 1;
        Intent it = new Intent(Menu_awal.this,Login.class);
        startActivity(it);

    }

    public void cek_login(){
        String value = sharedpreferences.getString("nameKey",null);
        if (value != null) {
            final ProgressDialog ringProgressDialog = ProgressDialog.show(Menu_awal.this, null,	"Masuk ke Aplikasi", true);
            ringProgressDialog.setCancelable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);

                    } catch (Exception e) {

                    }
                    ringProgressDialog.dismiss();
                }
            }).start();

            Intent it = new Intent(Menu_awal.this,Menu_utama.class);
            startActivity(it);
        }
    }

}
