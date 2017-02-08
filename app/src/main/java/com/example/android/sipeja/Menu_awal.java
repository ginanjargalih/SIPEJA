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

public class Menu_awal extends AppCompatActivity {

    //share prefrence
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Password = "passKey";
    public static final String NIP = "nipKey";
    public static final String Email = "emailKey";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_awal);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }


    public void klik(View view) {

        String value = sharedpreferences.getString("nameKey",null);
        if (value == null) {
            Toast.makeText(getApplicationContext(), "Anda belum memasukan data !", Toast.LENGTH_LONG).show();

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
