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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.android.sipeja.JSONParser;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;

public class Detail_Order extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 99;

    //untuk detail transaksi
    String text = Config.kode;
    String URL = Config.URL + "API_transaksi/index.php";
    JSONParser jsonParser = new JSONParser();

    String number;

    LinearLayout linear1, showless, review;
    LinearLayout linear2;

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


        //Creating a shared preference
        SharedPreferences sharedPreferences = Detail_Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //panggil fungsi load data
        if(Config.hitung == 0) {
            load_data();
        }


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

        // ***********fungsi detail order
        //baca data
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
        }
    }

    //untuk json
    public void load_data() {


        final ProgressDialog ringProgressDialog = ProgressDialog.show(Detail_Order.this, "Mohon Tunggu", "Masuk ke Aplikasi", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    AttemptLogin attemptLogin = new AttemptLogin();
                    attemptLogin.execute(text, "");


                } catch (Exception e) {

                }
                ringProgressDialog.dismiss();
            }
        }).start();
    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {

            String kode = args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("kode", kode));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    //Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();

                    //sp
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Detail_Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putString(Config.kode_transaki, result.getString("kodeTransaksi"));
                    editor.putString(Config.status_transaki, result.getString("status_transaksi"));
                    editor.putString(Config.tanggal_transaksi,result.getString("tanggalT"));
                    editor.putString(Config.nama_lab, result.getString("NamaLab"));
                    editor.putString(Config.Pelanggan, result.getString("NamaPelanggan"));
                    editor.putString(Config.nama_sertifikat, result.getString("nama_sertifikat"));
                    editor.putString(Config.alamat_sertifikat, result.getString("alamat_sertifikat"));
                    editor.putString(Config.sertifikat_inggris, result.getString("sertifikat_dalam_inggris"));
                    editor.putString(Config.sisa_sampel, result.getString("sisa_sampel"));
                    editor.putString(Config.keterangan, result.getString("keterangan"));
                    editor.putString(Config.nama_kontak, result.getString("namaCP"));
                    editor.putString(Config.nomor_kontak, result.getString("nomerTelepon"));
                    editor.putString(Config.status_pembayaran,result.getString("Status_pembayaran"));


                    //Saving values to editor
                    editor.commit();

                    //mulai ulang
                    Config.hitung =1;
                    refresh();


                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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


        //Saving values to editor
        editor.commit();

        Intent intent2 = getIntent();
        intent2.putExtra(Order.EXTRA_MESSAGE5, "");
        setResult(RESULT_OK, intent2);
        finish();

    }

    public void refresh(){
        Intent refresh = new Intent(this, Detail_Order.class);
        startActivity(refresh);//Start the same Activity
        finish(); //finish Activity.
    }

    @Override
    public void onBackPressed() {
        Config.hitung = 0;

        //Creating a shared preference
        SharedPreferences sharedPreferences = Detail_Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Adding values to editor
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


        //Saving values to editor
        editor.commit();


        Intent intent2 = getIntent();
        intent2.putExtra(Order.EXTRA_MESSAGE5, "");
        setResult(RESULT_OK, intent2);
        finish();
    }
}