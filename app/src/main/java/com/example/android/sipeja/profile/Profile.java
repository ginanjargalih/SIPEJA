package com.example.android.sipeja.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.sipeja.MainActivity;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;

public class Profile extends AppCompatActivity {

    public static final String EXTRA_MESSAGE1 = "profile" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //untuk toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profil");


        //ambil intent
        Intent intent = getIntent();
        //ambil datanya
        String pesan = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);
        Toast t = Toast.makeText(getApplicationContext(),pesan,Toast.LENGTH_LONG);
        t.show();


        //Creating a shared preference
        SharedPreferences sharedPreferences = Profile.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        String user = sharedPreferences.getString(Config.Name,"");

        TextView txtView=(TextView)findViewById(R.id.user);
        txtView.setText(user);
    }

    //tollbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mKembali:
                klikKembali();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void klikKembali() {
        Intent intent2 = getIntent();
        intent2.putExtra(MainActivity.EXTRA_MESSAGE1,"");
        setResult(RESULT_OK, intent2);
        finish();

    }

}
