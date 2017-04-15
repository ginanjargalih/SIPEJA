package com.example.android.sipeja.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sipeja.Menu_awal;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Verifikasi_order extends AppCompatActivity {

    public static final String EXTRA_MESSAGE7 = "diterima" ;
    String idTransaksi;
    String kode_verifikasi_order;

    // deklarasi variabel
    private ProgressDialog loading;
    public int hitungLog;


    ListView listLog; //deklarasi list untuk menampilkan username dan aktivitas

    ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();

    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private int noOfBtns; //
    private Button[] btns; //

    public static final String KEY_USERNAME = "id_transaksi";
    public static final String Verification = "kode_transaksi";

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
        idTransaksi = sharedPreferences.getString(Config.id_transaksi, "");

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

        kode_verifikasi_order = "Q2ZWjS9LCMKx2hRhzLK26fr3JsMRWvTZ26C3s9VnagdxppWWWAd8XKJFZ2ezr7KG";
        getData(); //masuk ke fungsi getData

    }


    //content
    private void Btnfooter() {
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val==0?0:1;
        noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

        LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay2);

        btns    =new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }
    }


    /**
     * Method for Checking Button Backgrounds```````
     */


    private void CheckBtnBackGroud(int index)
    {
        if(noOfBtns != 0) {
            for (int i = 0; i < noOfBtns; i++) {
                if (i == index) {
                    btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.box_blue));
                    btns[i].setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btns[i].setTextColor(getResources().getColor(android.R.color.black));
                }
            }
        }
    }
    /**
     * Method for loading data in listview
     * @param number
     */
    private void loadList(int number)
    {
        ArrayList<String> sort = new ArrayList<String>();

        int start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<items.size())
            {
                sort.add(items.get(i));
            }
            else
            {
                break;
            }
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,sort);
        listLog.setAdapter(adapter);

        //untuk bisa di klik
        listLog.setClickable(true);
        listLog.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String isiBaris = (String) listLog.getItemAtPosition(position);
                String pesan = isiBaris;
                String kept = pesan.substring( 0, pesan.indexOf("-"));
                Toast toast = Toast.makeText(getApplicationContext(), kept, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    //fungsi untuk mengambil data dari database
    private void getData() {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Pengambilan data..",false,false);

        String url = Config.URL+ "API_Verifikasi_Order/sample.php"; //inisialiasai url

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Verifikasi_order.this,"Tidak ada Koneksi",Toast.LENGTH_LONG).show();
            }
        })
        {
            //fungsi untuk memasukan nilai untuk di POST
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,idTransaksi);
                params.put(Verification,kode_verifikasi_order);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //menampilkan username dari tabel users dan aktivitas dari tabel log
    private void showJSON(String response){
        listLog = (ListView) findViewById(R.id.listview2);
        try {
            JSONArray result = new JSONArray(response);

            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                String a = Data.getString(Config.Sampel_id);
                String b = Data.getString(Config.Sampel_nama);

                // your code
                items.add(a + "-" + b );
                hitungLog = hitungLog + 1;


            }

            TOTAL_LIST_ITEMS = hitungLog;
            NUM_ITEMS_PAGE = 25; //25 data per list

            Btnfooter();

            loadList(0);
            CheckBtnBackGroud(0);

        }catch(JSONException e){
            e.printStackTrace();

        }

        //parsing json
        loading.dismiss();
        /*adapter = new ArrayAdapter<String>(this, R.layout.listview,items);loading.dismiss();
        listLog.setAdapter(adapter);
        listLog.setClickable(true);*/

    }

    //tombol
    public void klik_balik(View view) {
        Intent intent2 = getIntent();
        intent2.putExtra(Detail_Order.EXTRA_MESSAGE6,"");
        setResult(RESULT_OK, intent2);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent2 = getIntent();
        intent2.putExtra(Detail_Order.EXTRA_MESSAGE6,"");
        setResult(RESULT_OK, intent2);
        finish();
    }

    public void klikSelesai(View view) {

        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin ?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Starting login activity
                        Intent intent = new Intent(Verifikasi_order.this, Order.class);
                        startActivity(intent);

                        finish();
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




}
