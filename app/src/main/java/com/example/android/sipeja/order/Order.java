package com.example.android.sipeja.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.android.sipeja.Login;
import com.example.android.sipeja.Menu_utama;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;
import com.example.android.sipeja.log_activity.Log;
import com.example.android.sipeja.profile.Profile;

public class Order extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE2 = "Order" ;
    static final int ACT2_REQUEST = 99;  // request code

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        navigationView.setCheckedItem(R.id.nav_order);
        navigationView.getMenu().performIdentifierAction(R.id.nav_order, 0);

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
        getMenuInflater().inflate(R.menu.order, menu);
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
            //halaman ini
        } else if (id == R.id.nav_notifikasi) {

        } else if (id == R.id.nav_log) {
            klikLogActivity();
        }else if (id == R.id.nav_profile) {
            klikProfile();
        } else if (id == R.id.nav_keluar) {
            klikKeluar();
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
                        Intent intent = new Intent(Order.this, Login.class);
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
