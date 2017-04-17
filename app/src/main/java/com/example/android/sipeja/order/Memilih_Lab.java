package com.example.android.sipeja.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
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
import android.widget.Spinner;
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

public class Memilih_Lab extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    public static final String EXTRA_MESSAGE8 = "Memilih lab" ;

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

    public static final String KEY_USERNAME = "id_transaksiItem";
    public static final String Verification = "kode_transaksi";
    public static final String KEY_LAB = "kode_lab";

    String kode;
    String kode_laboratorium;
    String kode_verifikasi_order;

    //untuk lingkup
    //Declaring an Spinner
    private Spinner spinner;

    //An ArrayList for Spinner Items
    private ArrayList<String> students;

    //JSON Array
    private JSONArray result;

    //TextViews to display details
    private TextView textViewName;
    private TextView textViewCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memilih__lab);


        //warna status bar
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorMain));
        }

        //untuk lingkup
        //Initializing the ArrayList
        students = new ArrayList<String>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);

        //Initializing TextViews
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCourse = (TextView) findViewById(R.id.textViewCourse);


        //Creating a shared preference
        SharedPreferences sharedPreferences = Memilih_Lab.this.getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        //baca data
        kode = sharedPreferences.getString(Config.Sampel_id, "");
        kode_laboratorium= sharedPreferences.getString(Config.KODE_LAB, "");

        String jumlah_sampel = sharedPreferences.getString(Config.Sampel_jumlah,"");
        String jumlah_sertifikat = sharedPreferences.getString(Config.Sampel_sertifikat, "");
        String ket = sharedPreferences.getString(Config.Sampel_keterangan, "");
        String tgl = sharedPreferences.getString(Config.Sampel_perkiraanSelesai, "");

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

        kode_verifikasi_order = "9cP6jF8KDGyfEPv7GBNAtA78Ha8GkzGEpSrXqWG6qye2JATby9AfEAz2yy9UMBcd";
        getData();

        //Untuk spinner
        getData_lingkup();



    }

    //content
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
            }
        });
    }

    //fungsi untuk mengambil data dari database
    private void getData() {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Pengambilan data..",false,false);

        String url = Config.URL+ "API_Verifikasi_Order/parameter.php"; //inisialiasai url

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Memilih_Lab.this,"Tidak ada Koneksi",Toast.LENGTH_LONG).show();
            }
        })
        {
            //fungsi untuk memasukan nilai untuk di POST
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,kode);
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
                String a = Data.getString(Config.Parameter_nama);
                String b = Data.getString(Config.Parameter_jumlah);
                String c = Data.getString(Config.Parameter_harga);
                String d = Data.getString(Config.Parameter_metode);

                // your code
                items.add("Nama Parameter : "+a+  System.getProperty("line.separator") +
                        "Jumlah : " + b + System.getProperty("line.separator")+ "Harga : " + c + System.getProperty("line.separator")+
                        "Metode : "+d);
                hitungLog = hitungLog + 1;


            }

            TOTAL_LIST_ITEMS = hitungLog;
            NUM_ITEMS_PAGE = 25; //25 data per list

            loadList(0);

        }catch(JSONException e){
            e.printStackTrace();

        }

        //parsing json
        loading.dismiss();

    }


    //tombol
    public void klik_balik(View view) {
        Intent intent2 = getIntent();
        intent2.putExtra(Verifikasi_order.EXTRA_MESSAGE6,"");
        setResult(RESULT_OK, intent2);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent2 = getIntent();
        intent2.putExtra(Verifikasi_order.EXTRA_MESSAGE6,"");
        setResult(RESULT_OK, intent2);
        finish();
    }

    //untuk spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)  {
        //Setting the values to textviews for a selected item
        textViewName.setText(getLab(position));
        textViewCourse.setText(getNamaLingkup(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        textViewName.setText("");
        textViewCourse.setText("");
    }

    private void getData_lingkup(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    //untuk spinner
    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students.add(json.getString(Config.Lingkup_Nama));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(Memilih_Lab.this, android.R.layout.simple_spinner_dropdown_item, students));
    }

    //Method to get student name of a particular position
    //untuk kode lab
    private String getLab(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.Lingkup_Lab);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    //Doing the same with this method as we did with getName()
    //untuk nama lingkup
    private String getNamaLingkup(int position){
        String course="";
        try {
            JSONObject json = result.getJSONObject(position);
            course = json.getString(Config.Lingkup_Nama);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return course;
    }

    //Doing the same with this method as we did with getName()
    //untuk ID dari Lingkup
    private String getSession(int position){
        String session="";
        try {
            JSONObject json = result.getJSONObject(position);
            session = json.getString(Config.Lingkup_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return session;
    }
}
