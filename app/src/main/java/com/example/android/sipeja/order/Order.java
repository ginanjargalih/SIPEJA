package com.example.android.sipeja.order;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.android.sipeja.Login;
import com.example.android.sipeja.Menu_awal;
import com.example.android.sipeja.Menu_utama;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;
import com.example.android.sipeja.log_activity.Log;
import com.example.android.sipeja.profile.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.example.android.sipeja.JSONParser;


public class Order extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    public static final String EXTRA_MESSAGE2 = "Order" ;
    static final int ACT2_REQUEST = 99;  // request code
    public static final String EXTRA_MESSAGE5 = "Berhasil";

    private TextView title;

    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;

    //untuk detail transaksi
    String text = Config.kode;
    String URL = Config.URL + "API_transaksi/index.php";
    JSONParser jsonParser = new JSONParser();

    private final static String TAG= Order.class.getName().toString();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order");

        //warna status bar
        if(Build.VERSION.SDK_INT >= 21){

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorMain));
        }

        //baca share prefrence
        //Creating a shared preference
        SharedPreferences sharedPreferences = Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        String user = sharedPreferences.getString(Config.Name,"");
        String email = sharedPreferences.getString(Config.Email,"");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
                if(Config.hitung2 == 0){
                    syncSQLiteMySQLDB();
                }
                else if(Config.hitung2 != 0) {
                    dropTable();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //untuk menu default yg dipilih
        //navigationView.setCheckedItem(R.id.nav_order);
        //navigationView.getMenu().performIdentifierAction(R.id.nav_order, 0);

        //pada drawer
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

        // Get data records from SQLite DB
        //baca data hak akses utama
        String user2 = sharedPreferences.getString(Config.akses,"");

        if(user2.equals("pegawai")) {
            ambil_data_sqlite();
        }
        else if(user2.equals("pelanggan")){
            ambil_data_sqlite_pelanggan(user);
        }

        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Mengambil Data dari Server. Mohon Tunggu...");
        prgDialog.setCancelable(false);
        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);

        title = (TextView) findViewById(R.id.title);
        title.setText("Terdapat " + Config.index +" transaksi");


        //untuk swipe
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_order);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
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

    public void ambil_data_sqlite(){
        ArrayList<HashMap<String, String>> userList = controller.getAllUsers();
        // If users exists in SQLite DB
        if (userList.size() != 0) {

            // Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter(Order.this, userList, R.layout.order_item_list_content, new String[] {
                    "Nama_Perusahaan","transaksiName" }, new int[] { R.id.nomor, R.id.no_transaksi });
            final ListView myList = (ListView) findViewById(android.R.id.list);
            myList.setAdapter(adapter);


            //kode membaca transaksi
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                    HashMap<String,String> map =(HashMap<String,String>)myList.getItemAtPosition(position);
                    String isiBaris = map.get("transaksiName");
                    Config.kode = isiBaris;
                    load_data();
                }
            });

        }
    }

    public void ambil_data_sqlite_pelanggan(String s){
        ArrayList<HashMap<String, String>> userList = controller.searchUser(s);
        // If users exists in SQLite DB
        if (userList.size() != 0) {

            // Set the User Array list in ListView
            ListAdapter adapter = new SimpleAdapter(Order.this, userList, R.layout.order_item_list_content, new String[] {
                    "Nama_Perusahaan","transaksiName" }, new int[] { R.id.nomor, R.id.no_transaksi });
            final ListView myList = (ListView) findViewById(android.R.id.list);
            myList.setAdapter(adapter);


            //kode membaca transaksi
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                    HashMap<String,String> map =(HashMap<String,String>)myList.getItemAtPosition(position);
                    String isiBaris = map.get("transaksiName");
                    Config.kode = isiBaris;
                    load_data();
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                android.util.Log.d(TAG, "onQueryTextSubmit ");
                // Get User records from SQLite DB
                ArrayList<HashMap<String, String>> userList = controller.searchUser(s);
                // If users exists in SQLite DB
                if (userList.size() != 0) {

                    // Set the User Array list in ListView
                    ListAdapter adapter = new SimpleAdapter(Order.this, userList, R.layout.order_item_list_content, new String[] {
                            "Nama_Perusahaan","transaksiName" }, new int[] { R.id.nomor, R.id.no_transaksi });
                    final ListView myList = (ListView) findViewById(android.R.id.list);
                    myList.setAdapter(adapter);


                    //kode membaca transaksi
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                            HashMap<String,String> map =(HashMap<String,String>)myList.getItemAtPosition(position);
                            String isiBaris = map.get("transaksiName");
                            Config.kode = isiBaris;
                            load_data();
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "Data tidak ada!", Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                android.util.Log.d(TAG, "onQueryTextChange ");
                return false;
            }

        });

        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Creating a shared preference
        SharedPreferences sharedPreferences = Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        String user = sharedPreferences.getString(Config.akses,"");
        if(user.equals("pegawai")) {

            if (id == R.id.nav_dashboard) {
                klikDashboard();
            } else if (id == R.id.nav_order) {
                //halaman ini
            } else if (id == R.id.nav_notifikasi) {

            } else if (id == R.id.nav_log) {
                klikLogActivity();
            } else if (id == R.id.nav_profile) {
                klikProfile();
            } else if (id == R.id.nav_keluar) {
                klikKeluar();
            }
        }
        else if (user.equals("pelanggan")){
            if (id == R.id.nav_dashboard) {
                Toast.makeText(getApplicationContext(),	"Anda Tidak Memiliki Hak Akses", Toast.LENGTH_LONG).show();
            } else if (id == R.id.nav_order) {
                //halaman ini
            } else if (id == R.id.nav_notifikasi) {

            } else if (id == R.id.nav_log) {
                Toast.makeText(getApplicationContext(),	"Anda Tidak Memiliki Hak Akses", Toast.LENGTH_LONG).show();
            } else if (id == R.id.nav_profile) {
                klikProfile();
            } else if (id == R.id.nav_keluar) {
                klikKeluar();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //fungsi drawer
    //untuk dashboard
    public void klikDashboard() {
        Intent intent = new Intent(this, Menu_utama.class);
        //cara 2
        intent.putExtra(Menu_utama.EXTRA_MESSAGE1, "Dashboard");
        startActivityForResult(intent, ACT2_REQUEST);
    }


    //untuk menampilkan log activity
    public void klikLogActivity() {
        Intent intent = new Intent(this, Log.class);
        //cara 2
        intent.putExtra(Log.EXTRA_MESSAGE4, "Log Aktivitas");
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
                        SharedPreferences sharedPreferences = Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();

                        //Starting login activity
                        Intent intent = new Intent(Order.this, Menu_awal.class);
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

    //untuk sinkron
    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB() {

        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        client.post(Config.URL + "mysqlsqlitesync/getusers.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                updateSQLite(response);
            }
            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateSQLite(String response){
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("userId"));
                    System.out.println(obj.get("userName"));
                    System.out.println(obj.get("nama_pelanggan"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("transaksiId", obj.get("userId").toString());
                    // Add userName extracted from Object
                    queryValues.put("transaksiName", obj.get("userName").toString());
                    // Add Nama_Pelanggan extracted from Object
                    queryValues.put("Nama_Perusahaan", obj.get("nama_pelanggan").toString());
                    // Insert User into SQLite DB
                    controller.insertUser(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("Id", obj.get("userId").toString());
                    map.put("status", "1");
                    usersynclist.add(map);
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
                reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method to inform remote MySQL DB about completion of Sync activity
    public void updateMySQLSyncSts(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("syncsts", json);
        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        client.post(Config.URL + "mysqlsqlitesync/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(),	"Data telah Disinkronkan", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), Order.class);
        startActivity(objIntent);

        Config.hitung2 = 1;
    }

    //hapus db untuk diganti datanya
    private void dropTable() {
        this.deleteDatabase("transaksi.db");
        syncSQLiteMySQLDB();
    }



    //untuk detail order
//untuk json
    public void load_data() {


        final ProgressDialog ringProgressDialog = ProgressDialog.show(Order.this, "", "Mengambil data", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    AttemptLogin attemptLogin = new AttemptLogin();
                    attemptLogin.execute(Config.kode, "");


                } catch (Exception e) {

                }
                ringProgressDialog.dismiss();
            }
        }).start();
    }

    //untuk swipe
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                ambil_data_sqlite();
            }
        }, 3000);
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
                    SharedPreferences sharedPreferences = Order.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putString(Config.id_transaksi, result.getString("idTransaksi"));
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

                    editor.putString(Config.nominal,result.getString("nominal"));
                    editor.putString(Config.biaya_awal,result.getString("biaya_awal"));
                    editor.putString(Config.diskon,result.getString("diskon"));

                    editor.putString(Config.alamat_pelanggan,result.getString("alamatPelanggan"));
                    editor.putString(Config.telepon_pelanggan,result.getString("teleponPelanggan"));
                    editor.putString(Config.jenis_pelanggan,result.getString("jenisPelanggan"));
                    editor.putString(Config.email_pelanggan,result.getString("emailPelanggan"));
                    editor.putString(Config.kota,result.getString("kotaPelanggan"));
                    editor.putString(Config.provinsi,result.getString("propinsiPelanggan"));

                    //Saving values to editor
                    editor.commit();

                    //buka detail
                    klikdetail();




                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    //untuk menampilkan detail
    public void klikdetail() {
        Intent intent = new Intent(this, Detail_Order.class);
        //cara 2
        intent.putExtra(Profile.EXTRA_MESSAGE5, "Detail Profil");
        startActivityForResult(intent, ACT2_REQUEST);
    }

}