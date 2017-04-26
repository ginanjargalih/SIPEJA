package com.example.android.sipeja.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.sipeja.config.Config.KEY_ID_TEKNISI;

public class Memilih_Teknisi extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Memilih Teknisi";

    // deklarasi variabel
    //ListView listLog; //deklarasi list untuk menampilkan parameter
    //public int hitungLog;

    ListView listTeknisi;
    String[] mIdTekLab;
    String[] mNamaTekLab;
    Button bSubmit;
    SparseBooleanArray sparseBooleanArray ;
    private ProgressDialog loading;


    String kode;
    String kode_laboratorium;
    String kode_verifikasi_order;
    String id_pegawai;

    //misal buat yang dipilih
    String[] id_teknisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memilih__teknisi);

        //Creating a shared preference
        SharedPreferences sharedPreferences = Memilih_Teknisi.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        kode = sharedPreferences.getString(Config.Sampel_id, "");
        kode_laboratorium= sharedPreferences.getString(Config.KODE_LAB, "");
        id_pegawai = sharedPreferences.getString(Config.id_pegawai, "");

        String jumlah_sampel = sharedPreferences.getString(Config.Sampel_jumlah,"");
        String jumlah_sertifikat = sharedPreferences.getString(Config.Sampel_sertifikat, "");
        String ket = sharedPreferences.getString(Config.Sampel_keterangan, "");
        String tgl = sharedPreferences.getString(Config.Sampel_perkiraanSelesai, "");
        String lab = sharedPreferences.getString(Config.nama_lab, "");
        TextView txtView = (TextView) findViewById(R.id.iphone6s);
        txtView.setText(kode);

        TextView txtView2 = (TextView) findViewById(R.id.date);
        txtView2.setText(tgl);

        TextView txtView3 = (TextView) findViewById(R.id.condition2);
        txtView3.setText(jumlah_sampel);

        TextView txtView4 = (TextView) findViewById(R.id.brand2);
        txtView4.setText(jumlah_sertifikat);

        TextView txtView5 = (TextView) findViewById(R.id.size2);
        txtView5.setText(ket);

        TextView txtView6 = (TextView) findViewById(R.id.nama_lab);
        txtView6.setText(lab);

        TextView txtView7 = (TextView) findViewById(R.id.kode_lab);
        txtView7.setText(kode_laboratorium);

        kode_verifikasi_order = "9cP6jF8KDGyfEPv7GBNAtA78Ha8GkzGEpSrXqWG6qye2JATby9AfEAz2yy9UMBcd";

        //aksi saat submit
        bSubmit = (Button) findViewById(R.id.bSubmit);


        getData();
    }


    //fungsi untuk mengambil data dari database
    private void getData() {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Pengambilan data..",false,false);

        String url = Config.URL+ "administrasi_lab/teknisiLab.php"; //inisialiasai url

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Memilih_Teknisi.this,"Tidak ada Koneksi",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //menampilkan parameter
    private void showJSON(String response){

        listTeknisi = (ListView) findViewById(R.id.listTeknisi);
        try {
            final JSONArray result = new JSONArray(response);
            mIdTekLab = new String[result.length()];
            mNamaTekLab = new String[result.length()];

            // Parsing json
            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                mIdTekLab[i] = Data.getString(Config.KEY_ID_TEKNISI);
                mNamaTekLab[i] = Data.getString(Config.KEY_NAMA_TEKNISI);
            }

            listTeknisi.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub

                    sparseBooleanArray = listTeknisi.getCheckedItemPositions(); //

                    String ValueHolder = "" ; //

                    int i = 0 ;

                    if(listTeknisi.getCheckedItemCount() > 0){
                        bSubmit.setEnabled(true);

                    }else{
                        bSubmit.setEnabled(false);
                    }

                    while (i < sparseBooleanArray.size()) { //

                        if (sparseBooleanArray.valueAt(i)) {

                            ValueHolder += mNamaTekLab [ sparseBooleanArray.keyAt(i) ] + ",";
                        }

                        i++ ;
                    }

                    ValueHolder = ValueHolder.replaceAll("(,)*$", "");

                    Toast.makeText(Memilih_Teknisi.this, "" + ValueHolder, Toast.LENGTH_LONG).show();

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }  ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Memilih_Teknisi.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, mNamaTekLab );
        listTeknisi.setAdapter(adapter);
        loading.dismiss();
    }

    //tombol
    public void klik_balik(View view) {
        Intent intent2 = getIntent();
        intent2.putExtra(Administrasi_Lab.EXTRA_MESSAGE8,"");
        setResult(RESULT_OK, intent2);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent2 = getIntent();
        intent2.putExtra(Administrasi_Lab.EXTRA_MESSAGE8,"");
        setResult(RESULT_OK, intent2);
        finish();
    }

    public void klik_simpan(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin ?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //aksi disini

                     /*   for(i=0; i< id_teknisi.length;i++) {
                            updateTeknisiSampel(id_teknisi[i]);
                        }*/
                        int i =0;
                        while (i < sparseBooleanArray.size()) { //

                            if (sparseBooleanArray.valueAt(i)) {
                                updateTeknisiSampel(mIdTekLab[ sparseBooleanArray.keyAt(i) ]);
                            }
                            i++ ;
                        }

                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    //untuk update transaksi item
    private void updateTeknisiSampel(final String mIdTekLab){
        final String random = "dApw4BxhCn8Rbkfkk9jSf4RNfEUKSxfX3gx857LETpLcHUZDhJWGbf5gqr6x458Q";

        class UpdateEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Memilih_Teknisi.this,"Update Data...","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Memilih_Teknisi.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(Config.Sampel_id,kode);
                hashMap.put(Config.KEY_Teknisi_Masuk,mIdTekLab);
                hashMap.put(Config.KEY_EMP_Verifikasi,random);

                hashMap.put(Config.KEY_EMP_log,id_pegawai);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_Teknisi_Sampel,hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }

}