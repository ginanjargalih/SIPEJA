package com.example.android.sipeja.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;

public class Verifikasi_order extends AppCompatActivity {

    public static final String EXTRA_MESSAGE7 = "diterima" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_order);


        //warna status bar
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorMain));
        }

        //Creating a shared preference
        SharedPreferences sharedPreferences = Verifikasi_order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        String kode = sharedPreferences.getString(Config.kode_transaki, "");
        String stp = sharedPreferences.getString(Config.status_pembayaran,"");
        String tanggal_t = sharedPreferences.getString(Config.tanggal_transaksi, "");

        if (stp.equals("1")){
            TextView txtView11 = (TextView) findViewById(R.id.free);
            txtView11.setText("Lunas");
        }
        else if (stp.equals("null")){
            TextView txtView11 = (TextView) findViewById(R.id.free);
            txtView11.setText("Belum Lunas");
        }

        TextView txtView = (TextView) findViewById(R.id.iphone6s);
        txtView.setText(kode);

        TextView txtView2 = (TextView) findViewById(R.id.date);
        txtView2.setText(tanggal_t);

    }


}
