package com.example.android.sipeja.log_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.android.sipeja.Login;
import com.example.android.sipeja.Menu_utama;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;
import com.example.android.sipeja.profile.Profile;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Log extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE4 = "Log" ;
    static final int ACT2_REQUEST = 99;  // request code

    // gabung
    // deklarasi variabel
    private EditText editTextId;
    private Button buttonGet;
    private TextView textViewResult;
    private ProgressDialog loading;
    public int hitungLog;
    private String username;

    private TextView title;
    private TextView show_null;

    ListView listLog; //deklarasi list untuk menampilkan username dan aktivitas

    ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();

    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE;
    private int noOfBtns; //
    private Button[] btns; //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //untuk content
        title = (TextView) findViewById(R.id.title);
        getData(); //masuk ke fungsi getData

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log Aktivitas");


        //warna status bar
        if(Build.VERSION.SDK_INT >= 21){

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorMain));
        }

        //baca share prefrence
        //Creating a shared preference
        SharedPreferences sharedPreferences = Log.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        String user = sharedPreferences.getString(Config.Name,"");
        String email = sharedPreferences.getString(Config.Email,"");
        username = sharedPreferences.getString(Config.NamePengguna,"");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //untuk menu default yg dipilih
        navigationView.setCheckedItem(R.id.nav_log);
        navigationView.getMenu().performIdentifierAction(R.id.nav_log, 0);

        //drawer
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nama);
        nav_user.setText(user);


        View hView2 =  navigationView.getHeaderView(0);
        TextView nav_email = (TextView)hView2.findViewById(R.id.email);
        nav_email.setText(email);

        View hView3 =  navigationView.getHeaderView(0);
        String s=user.substring(0,1);
        TextDrawable drawable = TextDrawable.builder().buildRoundRect(s,R.color.colorKontak, 100);

        ImageView image = (ImageView)hView3.findViewById(R.id.imageView);
        image.setImageDrawable(drawable);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            klikDashboard();

        } else if (id == R.id.nav_order) {

        } else if (id == R.id.nav_notifikasi) {

        } else if (id == R.id.nav_log) {
            //halaman ini
        }else if (id == R.id.nav_profile) {
            klikProfile();
        } else if (id == R.id.nav_keluar) {
            klikKeluar();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //content
    private void Btnfooter() {
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val==0?0:1;
        noOfBtns=TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

        LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);

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
        show_null = (TextView) findViewById(R.id.textView5);
        if(noOfBtns != 0) {

            title.setText("Halaman " + (index + 1) + " dari " + noOfBtns);
            for (int i = 0; i < noOfBtns; i++) {
                if (i == index) {
                    btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.box_blue));
                    btns[i].setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    btns[i].setTextColor(getResources().getColor(android.R.color.black));
                }
            }
        }else{
            show_null.setText("Belum ada aktivitas untuk saat ini");

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
                android.R.layout.simple_list_item_1,
                sort);
        listLog.setAdapter(adapter);
    }

    //fungsi untuk mengambil data dari database
    private void getData() {
        //String id = editTextId.getText().toString().trim();

        /*if (id.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }*/

        loading = ProgressDialog.show(this,"Mohon Tunggu","Pengambilan data..",false,false);

        String url = Config.URL+ "json.php"; //inisialiasai url

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Log.this,"Tidak ada Koneksi",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //menampilkan username dari tabel users dan aktivitas dari tabel log
    private void showJSON(String response){
        listLog = (ListView) findViewById(R.id.listview);
        try {
            JSONArray result = new JSONArray(response);

            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                String a = Data.getString(Config.KEY_USERNAME);
                String b = Data.getString(Config.KEY_AKTIVITAS);
                String c = Data.getString(Config.KEY_WAKTU);

                if (a.equals(username)) {
                    // your code
                    items.add(b + " " + " " + c);

                    hitungLog = hitungLog + 1;
                }
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


    //fungsi drawer
    //untuk dashboard
    public void klikDashboard() {
        Intent intent = new Intent(this, Menu_utama.class);
        //cara 2
        intent.putExtra(Menu_utama.EXTRA_MESSAGE1, "Dashboard");
        startActivityForResult(intent, ACT2_REQUEST);
    }


    //untuk menampilkan profile
    public void klikProfile() {
        Intent intent = new Intent(this, Profile.class);
        //cara 2
        intent.putExtra(Profile.EXTRA_MESSAGE5, "Profil Anda");
        startActivityForResult(intent, ACT2_REQUEST);
    }

    //untuk fungsi keluar dan menghapus data di share prefrence
    public void klikKeluar() {

        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda yakin untuk keluar?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //delete a shared preference
                        SharedPreferences sharedPreferences = Log.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();

                        //Starting login activity
                        Intent intent = new Intent(Log.this, Login.class);
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
