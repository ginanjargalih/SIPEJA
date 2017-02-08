package com.example.android.sipeja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sipeja.config.Config;

public class Menu_awal extends AppCompatActivity {

    //share prefrence
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_awal);

        //Creating a shared preference
        sharedpreferences = Menu_awal.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

    }


    public void klik(View view) {

        String value = sharedpreferences.getString("nameKey",null);
        if (value == null) {
            Intent it = new Intent(Menu_awal.this,Login.class);
            startActivity(it);
        }
        else {
            final ProgressDialog ringProgressDialog = ProgressDialog.show(Menu_awal.this, "Mohon Tunggu ...",	"Masuk ke Aplikasi ...", true);
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

            Intent it = new Intent(Menu_awal.this,MainActivity.class);
            startActivity(it);
        }


    }

}
