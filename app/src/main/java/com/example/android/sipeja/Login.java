package com.example.android.sipeja;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.android.sipeja.Menu_awal.Email;
import static com.example.android.sipeja.Menu_awal.NIP;
import static com.example.android.sipeja.Menu_awal.Name;
import static com.example.android.sipeja.Menu_awal.Password;


public class Login extends AppCompatActivity {
    public EditText editEmail, editPassword, editName;
    Button btnSignIn, btnRegister;

    public String inputNama;
    public String inputPassword;

    String URL= "http://sipeja.pe.hu/test_android/index.php";

    JSONParser jsonParser=new JSONParser();

    int i=0;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void launchRingDialog(View view) {
        editName=(EditText)findViewById(R.id.editName);
        editPassword=(EditText)findViewById(R.id.editPassword);


        //untuk username
        inputNama = editName.getText().toString();

        //untuk password
        inputPassword = editPassword.getText().toString();

        if(TextUtils.isEmpty(inputNama)) {
            editName.setError("Field Username!");
            return;
        }

        else if(TextUtils.isEmpty(inputPassword)) {
            editPassword.setError("Field Password!");
            return;
        }

        final ProgressDialog ringProgressDialog = ProgressDialog.show(Login.this, "Mohon Tunggu ...",	"Masuk ke Aplikasi ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    AttemptLogin attemptLogin= new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(),editPassword.getText().toString(),"");


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

            String email = args[2];
            String password = args[1];
            String name= args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if(email.length()>0)
                params.add(new BasicNameValuePair("email",email));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("user"),Toast.LENGTH_LONG).show();
                   // masukan_data();

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    public void masukan_data(){
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(Name, "user");
        editor.putString(Password, "password");
        //editor.putString(NIP, "nip");
        //editor.putString(Email, "email");
        editor.apply();
        Toast.makeText(Login.this,"Thanks",Toast.LENGTH_LONG).show();

    }
}