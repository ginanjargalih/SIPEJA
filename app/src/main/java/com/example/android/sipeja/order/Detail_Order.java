package com.example.android.sipeja.order;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import com.example.android.sipeja.JSONParser;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;
import com.example.android.sipeja.profile.Profile;

public class Detail_Order extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 99;

    public static final String EXTRA_MESSAGE3 = "Log_transaksi" ;
    static final int ACT2_REQUEST = 99;  // request code
    public static final String EXTRA_MESSAGE6 = "Berhasil";


    String number;

    LinearLayout linear1, showless, review;
    LinearLayout linear2;

    LinearLayout linear3, showless2;
    LinearLayout linear4;

    LinearLayout linear5, showless3;
    LinearLayout linear6;

    private ExpandableHeightListView listview;
    private ArrayList<Bean> Bean;
    private JayBaseAdapter baseAdapter;


    public int[] IMAGE = new int[10];

    public String[] TITLE = new String[10] ;

    public String[] RATING = new String[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__order);

        //warna status bar
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorMain));
        }

        //Creating a shared preference
        SharedPreferences sharedPreferences = Detail_Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);


        //untuk status
        String angka = sharedPreferences.getString(Config.status_transaki,"");
        final int angka2 = !angka.equals("")?Integer.parseInt(angka) : 0;
        status_transaksi(angka2);

        //        ********LISTVIEW***********
        listview = (ExpandableHeightListView) findViewById(R.id.listview);


        Bean = new ArrayList<Bean>();

        for (int i = 0; i < TITLE.length; i++) {

            Bean bean = new Bean(IMAGE[i], TITLE[i], RATING[i]);
            Bean.add(bean);

        }

        baseAdapter = new JayBaseAdapter(Detail_Order.this, Bean) {
        };

        listview.setAdapter(baseAdapter);



        //                ***********viewmore**********

        linear1 = (LinearLayout) findViewById(R.id.linear1);
        showless = (LinearLayout) findViewById(R.id.showless);

        linear2 = (LinearLayout) findViewById(R.id.linear2);

        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);

            }
        });

        showless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);


            }
        });

        //                ***********viewmore2**********

        linear3 = (LinearLayout) findViewById(R.id.linear3);
        showless2 = (LinearLayout) findViewById(R.id.showless2);

        linear4 = (LinearLayout) findViewById(R.id.linear4);

        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear4.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.GONE);

            }
        });

        showless2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear4.setVisibility(View.GONE);
                linear3.setVisibility(View.VISIBLE);


            }
        });

        //                ***********viewmore3**********

        linear5 = (LinearLayout) findViewById(R.id.linear5);
        showless3 = (LinearLayout) findViewById(R.id.showless3);

        linear6 = (LinearLayout) findViewById(R.id.linear6);

        linear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear6.setVisibility(View.VISIBLE);
                linear5.setVisibility(View.GONE);

            }
        });

        showless3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linear6.setVisibility(View.GONE);
                linear5.setVisibility(View.VISIBLE);


            }
        });

        // ***********fungsi detail order
        //baca data
        String id = sharedPreferences.getString(Config.id_transaksi, "");
        String kode = sharedPreferences.getString(Config.kode_transaki, "");
        String status_t = sharedPreferences.getString(Config.status_transaki, "");
        String lab = sharedPreferences.getString(Config.nama_lab, "");
        String tanggal_t = sharedPreferences.getString(Config.tanggal_transaksi, "");
        String pelanggan = sharedPreferences.getString(Config.Pelanggan, "");
        String nama_str = sharedPreferences.getString(Config.nama_sertifikat, "");
        String alamat_str = sharedPreferences.getString(Config.alamat_sertifikat, "");
        String str_ing = sharedPreferences.getString(Config.sertifikat_inggris, "");
        String sisa = sharedPreferences.getString(Config.sisa_sampel, "");
        String ket = sharedPreferences.getString(Config.keterangan, "");
        String cp = sharedPreferences.getString(Config.nama_kontak, "");
        String stp = sharedPreferences.getString(Config.status_pembayaran,"");

        String nominal = sharedPreferences.getString(Config.nominal,"");
        String biaya = sharedPreferences.getString(Config.biaya_awal,"");
        String diskon = sharedPreferences.getString(Config.diskon,"");

        String alamat_pelanggan = sharedPreferences.getString(Config.alamat_pelanggan,"");
        String kota_pelanggan = sharedPreferences.getString(Config.kota,"");
        String provinsi_pelanggan = sharedPreferences.getString(Config.provinsi,"");

        String telepon_pelanggan = sharedPreferences.getString(Config.telepon_pelanggan,"");
        String jenis = sharedPreferences.getString(Config.jenis_pelanggan,"");
        String email = sharedPreferences.getString(Config.email_pelanggan,"");


        number = sharedPreferences.getString(Config.nomor_kontak,"");

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

        TextView txtView3 = (TextView) findViewById(R.id.condition2);
        txtView3.setText(lab);

        TextView txtView4 = (TextView) findViewById(R.id.brand2);
        txtView4.setText(pelanggan);

        TextView txtView5 = (TextView) findViewById(R.id.size2);
        txtView5.setText(nama_str);

        TextView txtView6 = (TextView) findViewById(R.id.weight2);
        txtView6.setText(alamat_str);

        //untuk sertifikat
        if(str_ing.equals("0")){
            TextView txtView7 = (TextView) findViewById(R.id.display2);
            txtView7.setText("Tidak");
        }
        else if (str_ing.equals("1")){
            TextView txtView7 = (TextView) findViewById(R.id.display2);
            txtView7.setText("Ya");
        }

        //untuk sisa
        if(sisa.equals("0")) {
            TextView txtView8 = (TextView) findViewById(R.id.camera2);
            txtView8.setText("Tidak");
        }
        else if(sisa.equals("1")){
            TextView txtView8 = (TextView) findViewById(R.id.camera2);
            txtView8.setText("Ya");
        }

        TextView txtView9 = (TextView) findViewById(R.id.video2);
        txtView9.setText(ket);

        TextView txtView10 = (TextView) findViewById(R.id.return2);
        txtView10.setText(cp);


        //nominal
        int convertedVal = Integer.parseInt(nominal);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###.00", symbols);
        String prezzo = decimalFormat.format(convertedVal);

        TextView txtView11 = (TextView) findViewById(R.id.display4);
        txtView11.setText(prezzo);

        //biaya awal
        int convertedVal2 = Integer.parseInt(biaya);
        DecimalFormatSymbols symbols2 = new DecimalFormatSymbols();
        symbols2.setDecimalSeparator(',');

        DecimalFormat decimalFormat2 = new DecimalFormat("Rp #,###.00", symbols2);
        String prezzo2 = decimalFormat2.format(convertedVal2);

        TextView txtView12 = (TextView) findViewById(R.id.camera4);
        txtView12.setText(prezzo2);


        //diskon
        int convertedVal3 = Integer.parseInt(diskon);
        DecimalFormatSymbols symbols3 = new DecimalFormatSymbols();
        symbols2.setDecimalSeparator(',');

        DecimalFormat decimalFormat3 = new DecimalFormat("Rp #,###.00", symbols3);
        String prezzo3 = decimalFormat3.format(convertedVal3);


        TextView txtView13 = (TextView) findViewById(R.id.video4);
        txtView13.setText(prezzo3);


        //detail pelanggan
        TextView txtView14 = (TextView) findViewById(R.id.display5);
        txtView14.setText(pelanggan);

        TextView txtView15 = (TextView) findViewById(R.id.camera5);
        txtView15.setText(alamat_pelanggan);

        TextView txtView16 = (TextView) findViewById(R.id.video8);
        txtView16.setText(kota_pelanggan);

        TextView txtView17 = (TextView) findViewById(R.id.video9);
        txtView17.setText(provinsi_pelanggan);

        TextView txtView18 = (TextView) findViewById(R.id.video5);
        txtView18.setText(jenis);

        TextView txtView19 = (TextView) findViewById(R.id.video6);
        txtView19.setText(email);

        TextView txtView20 = (TextView) findViewById(R.id.video7);
        txtView20.setText(telepon_pelanggan);

        TextView txtView21 = (TextView) findViewById(R.id.price);
        txtView21.setText(id);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    //untuk status transaksi
    public void status_transaksi(int hitung){

        if (hitung == 1){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.statusblm2;
            IMAGE[2] = R.drawable.statusblm3;
            IMAGE[3] = R.drawable.statusblm4;
            IMAGE[4] = R.drawable.statusblm5;
            IMAGE[5] = R.drawable.statusblm6;
            IMAGE[6] = R.drawable.statusblm7;
            IMAGE[7] = R.drawable.statusblm8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Belum dilakukan";
            RATING[2]="Belum dilakukan";
            RATING[3]="Belum dilakukan";
            RATING[4]="Belum dilakukan";
            RATING[5]="Belum dilakukan";
            RATING[6]="Belum dilakukan";
            RATING[7]="Belum dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Setujui Pembayaran");
        }

        else if (hitung == 2){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.statusblm3;
            IMAGE[3] = R.drawable.statusblm4;
            IMAGE[4] = R.drawable.statusblm5;
            IMAGE[5] = R.drawable.statusblm6;
            IMAGE[6] = R.drawable.statusblm7;
            IMAGE[7] = R.drawable.statusblm8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Belum dilakukan";
            RATING[3]="Belum dilakukan";
            RATING[4]="Belum dilakukan";
            RATING[5]="Belum dilakukan";
            RATING[6]="Belum dilakukan";
            RATING[7]="Belum dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Cetak Kaji Ulang");
        }

        else if (hitung == 3){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.statusblm4;
            IMAGE[4] = R.drawable.statusblm5;
            IMAGE[5] = R.drawable.statusblm6;
            IMAGE[6] = R.drawable.statusblm7;
            IMAGE[7] = R.drawable.statusblm8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Belum dilakukan";
            RATING[4]="Belum dilakukan";
            RATING[5]="Belum dilakukan";
            RATING[6]="Belum dilakukan";
            RATING[7]="Belum dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Cetak Barcode");
        }

        else if (hitung == 4){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.status4;
            IMAGE[4] = R.drawable.statusblm5;
            IMAGE[5] = R.drawable.statusblm6;
            IMAGE[6] = R.drawable.statusblm7;
            IMAGE[7] = R.drawable.statusblm8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Sudah dilakukan";
            RATING[4]="Belum dilakukan";
            RATING[5]="Belum dilakukan";
            RATING[6]="Belum dilakukan";
            RATING[7]="Belum dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Verifikasi Order");
        }

        else if (hitung == 5){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.status4;
            IMAGE[4] = R.drawable.status5;
            IMAGE[5] = R.drawable.statusblm6;
            IMAGE[6] = R.drawable.statusblm7;
            IMAGE[7] = R.drawable.statusblm8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Sudah dilakukan";
            RATING[4]="Sudah dilakukan";
            RATING[5]="Belum dilakukan";
            RATING[6]="Belum dilakukan";
            RATING[7]="Belum dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Administrasi Lab");
        }

        else if (hitung == 6){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.status4;
            IMAGE[4] = R.drawable.status5;
            IMAGE[5] = R.drawable.status6;
            IMAGE[6] = R.drawable.statusblm7;
            IMAGE[7] = R.drawable.statusblm8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Sudah dilakukan";
            RATING[4]="Sudah dilakukan";
            RATING[5]="Sudah dilakukan";
            RATING[6]="Belum dilakukan";
            RATING[7]="Belum dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Pengujian dan Kalibrasi Selesai");
        }

        else if (hitung == 7){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.status4;
            IMAGE[4] = R.drawable.status5;
            IMAGE[5] = R.drawable.status6;
            IMAGE[6] = R.drawable.status7;
            IMAGE[7] = R.drawable.statusblm8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Sudah dilakukan";
            RATING[4]="Sudah dilakukan";
            RATING[5]="Sudah dilakukan";
            RATING[6]="Sudah dilakukan";
            RATING[7]="Belum dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Penyusunan Laporan");
        }

        else if (hitung == 8){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.status4;
            IMAGE[4] = R.drawable.status5;
            IMAGE[5] = R.drawable.status6;
            IMAGE[6] = R.drawable.status7;
            IMAGE[7] = R.drawable.status8;
            IMAGE[8] = R.drawable.statusblm9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Sudah dilakukan";
            RATING[4]="Sudah dilakukan";
            RATING[5]="Sudah dilakukan";
            RATING[6]="Sudah dilakukan";
            RATING[7]="Sudah dilakukan";
            RATING[8]="Belum dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Periksa Laporan");
        }

        else if (hitung == 9){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.status4;
            IMAGE[4] = R.drawable.status5;
            IMAGE[5] = R.drawable.status6;
            IMAGE[6] = R.drawable.status7;
            IMAGE[7] = R.drawable.status8;
            IMAGE[8] = R.drawable.status9;
            IMAGE[9] = R.drawable.blmselesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Sudah dilakukan";
            RATING[4]="Sudah dilakukan";
            RATING[5]="Sudah dilakukan";
            RATING[6]="Sudah dilakukan";
            RATING[7]="Sudah dilakukan";
            RATING[8]="Sudah dilakukan";
            RATING[9]="Belum dilakukan";

            ((Button)findViewById(R.id.buy)).setText("Verifikasi Laporan");
        }

        else if (hitung == 10){

            IMAGE[0] = R.drawable.status1;
            IMAGE[1] = R.drawable.status2;
            IMAGE[2] = R.drawable.status3;
            IMAGE[3] = R.drawable.status4;
            IMAGE[4] = R.drawable.status5;
            IMAGE[5] = R.drawable.status6;
            IMAGE[6] = R.drawable.status7;
            IMAGE[7] = R.drawable.status8;
            IMAGE[8] = R.drawable.status9;
            IMAGE[9] = R.drawable.selesai;

            TITLE[0]= "Registrasi";
            TITLE[1]="Pembayaran diterima";
            TITLE[2]="Pemberkasan order";
            TITLE[3]="Verifikasi order";
            TITLE[4]="Proses pengujian / Kalibrasi";
            TITLE[5]="Pengujian / Kalibrasi selesai";
            TITLE[6]="Penyusunan laporan";
            TITLE[7]="Verifikasi laporan";
            TITLE[8]="Penerbitan laporan";
            TITLE[9]="Order selesai";

            RATING[0]="Sudah dilakukan";
            RATING[1]="Sudah dilakukan";
            RATING[2]="Sudah dilakukan";
            RATING[3]="Sudah dilakukan";
            RATING[4]="Sudah dilakukan";
            RATING[5]="Sudah dilakukan";
            RATING[6]="Sudah dilakukan";
            RATING[7]="Sudah dilakukan";
            RATING[8]="Sudah dilakukan";
            RATING[9]="Sudah dilakukan";

            Button playButton = (Button) findViewById(R.id.buy);
            playButton.setVisibility(View.GONE);
        }
    }



    //fungsi fungsi tombol
    // fungsi telepon
    public void telepon(View view) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Detail_Order.this,
                    android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(Detail_Order.this,
                        new String[]{android.Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
        startActivity(callIntent);
    }

    //untuk sms
    public  void sms(View view){
        /** Creating an intent to initiate view action */
        Intent intent = new Intent("android.intent.action.VIEW");

        /** creates an sms uri */
        Uri data = Uri.parse("sms:" + number);

        /** Setting sms uri to the intent */
        intent.setData(data);

        /** Initiates the SMS compose screen, because the activity contain ACTION_VIEW and sms uri */
        startActivity(intent);
    }

    public void klikOrder(View view) {
        Config.hitung = 0;

        //Creating a shared preference
        SharedPreferences sharedPreferences = Detail_Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Adding values to editor
        editor.putString(Config.id_transaksi,"");
        editor.putString(Config.kode_transaki,"");
        editor.putString(Config.status_transaki, "");
        editor.putString(Config.tanggal_transaksi,"");
        editor.putString(Config.nama_lab,"");
        editor.putString(Config.Pelanggan, "");
        editor.putString(Config.nama_sertifikat,"");
        editor.putString(Config.alamat_sertifikat,"");
        editor.putString(Config.sertifikat_inggris,"");
        editor.putString(Config.sisa_sampel,"");
        editor.putString(Config.keterangan,"");
        editor.putString(Config.nama_kontak,"");
        editor.putString(Config.nomor_kontak,"");
        editor.putString(Config.status_pembayaran,"");

        editor.putString(Config.nominal,"");
        editor.putString(Config.biaya_awal,"");
        editor.putString(Config.diskon,"");

        editor.putString(Config.alamat_pelanggan,"");
        editor.putString(Config.telepon_pelanggan,"");
        editor.putString(Config.jenis_pelanggan,"");
        editor.putString(Config.email_pelanggan,"");
        editor.putString(Config.kota,"");
        editor.putString(Config.provinsi,"");


        //Saving values to editor
        editor.commit();

        Intent intent2 = getIntent();
        intent2.putExtra(Order.EXTRA_MESSAGE5, "");
        setResult(RESULT_OK, intent2);
        finish();

    }

    @Override
    public void onBackPressed() {
        Config.hitung = 0;

        //Creating a shared preference
        SharedPreferences sharedPreferences = Detail_Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Adding values to editor
        editor.putString(Config.id_transaksi,"");
        editor.putString(Config.kode_transaki,"");
        editor.putString(Config.status_transaki, "");
        editor.putString(Config.tanggal_transaksi,"");
        editor.putString(Config.nama_lab,"");
        editor.putString(Config.Pelanggan, "");
        editor.putString(Config.nama_sertifikat,"");
        editor.putString(Config.alamat_sertifikat,"");
        editor.putString(Config.sertifikat_inggris,"");
        editor.putString(Config.sisa_sampel,"");
        editor.putString(Config.keterangan,"");
        editor.putString(Config.nama_kontak,"");
        editor.putString(Config.nomor_kontak,"");
        editor.putString(Config.status_pembayaran,"");

        editor.putString(Config.nominal,"");
        editor.putString(Config.biaya_awal,"");
        editor.putString(Config.diskon,"");

        editor.putString(Config.alamat_pelanggan,"");
        editor.putString(Config.telepon_pelanggan,"");
        editor.putString(Config.jenis_pelanggan,"");
        editor.putString(Config.email_pelanggan,"");
        editor.putString(Config.kota,"");
        editor.putString(Config.provinsi,"");

        //Saving values to editor
        editor.commit();


        Intent intent2 = getIntent();
        intent2.putExtra(Order.EXTRA_MESSAGE5, "");
        setResult(RESULT_OK, intent2);
        finish();
    }

    //untuk menampilkan detail
    public void klik_log(View view) {
        Intent intent = new Intent(this, Log_Transaksi.class);
        //cara 2
        intent.putExtra(Log_Transaksi.EXTRA_MESSAGE6, "Log Transaksi");
        startActivityForResult(intent, ACT2_REQUEST);
    }
}