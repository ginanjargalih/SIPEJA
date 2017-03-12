package com.example.android.sipeja.dashboard;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sipeja.R;
import com.example.android.sipeja.config.Config;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.android.sipeja.R.id.chart1;


/**
 * Created by Aziz on 11/03/2017.
 */

public class Pendapatan_Perlab extends Fragment {
    public View v;
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;
    ProgressDialog loading;
    List<String> labs;
    List<String> year;
    Spinner spinner, spinner2;
    ArrayAdapter<String> dataAdapter, dataAdapter2;

    @Override
    public void onResume(){
        //spinner.setSelection(0);
        //spinner2.setSelection(0);
        show();
        super.onResume();
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pendapatan_perlab, container, false);
        chart = (BarChart) v.findViewById(chart1);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);

        //show();
        return v;
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
//        return inflater.inflate(R.layout.senin, container, false);
    }

    public void show(){
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();

        // Spinner Drop down elements
        labs = new ArrayList<String>();
        labs.add("Laboratorium");
        labs.add("Kalibrasi"); //kode 03 id 9
        labs.add("Kimia"); //kode 04 id 16
        labs.add("Semen"); //kode  05 id 3
        labs.add("Beton dan Bahan Bangunan"); //kode 06 id 8
        labs.add("Logam"); //kode 07 id 10
        labs.add("Otomotif"); //kode 08 id 12
        labs.add("Barang Teknik"); //kode 09 id 11
        labs.add("Metalografi"); //kode 10 id 7
        labs.add("Elektronika dan EMC"); //kode 11 id 14
        labs.add("Listrik"); //kode 12 id 13
        labs.add("NDT"); //kode 13 id 6

        // Spinner element
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);

        // Spinner click listener
        //spinner2.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        year = new ArrayList<String>();
        year.add("Tahun");
        year.add("2016");
        year.add("2017");
        year.add("2018");
        year.add("2019");
        year.add("2020");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, labs);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter);

        // Creating adapter for spinner
        dataAdapter2 = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, year);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    BARENTRY.clear();
                    BarEntryLabels.clear();
                    if(spinner.getSelectedItem().equals("Tahun")){
                        chart.clear();
                    }else {
                        getData(labs.get(i), spinner.getSelectedItem().toString());
                    }
                }else{
                    chart.clear();
                }
//                Toast.makeText(getActivity(), labs.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    BARENTRY.clear();
                    BarEntryLabels.clear();
                    if(spinner2.getSelectedItem().equals("Laboratorium")){
                        chart.clear();
                    }else {
                        getData(spinner2.getSelectedItem().toString(), year.get(i));
                    }
                }else{
                    chart.clear();
                }
//                Toast.makeText(getActivity(), year.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void AddValuesToBarEntryLabels(){
        //parameter bulan
        BarEntryLabels.add("January");
        BarEntryLabels.add("February");
        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");
        BarEntryLabels.add("June");
        BarEntryLabels.add("July");
        BarEntryLabels.add("August");
        BarEntryLabels.add("September");
        BarEntryLabels.add("October");
        BarEntryLabels.add("November");
        BarEntryLabels.add("December");
    }

    //fungsi untuk mengambil data dari database
    private void getData(final String namalab, final String tahun) {


        loading = ProgressDialog.show(getActivity(),"Mohon Tunggu","Pengambilan data..",false,false);

        String url = Config.URL+ "dashboard/loadStatusKeuangan.php"; //inisialiasai url

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//              loading.dismiss();
                showJSON(response);
//                Toast.makeText(Status_order.this,"Masuk nih!",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
               Toast.makeText(getActivity(),"Tidak ada Koneksi",Toast.LENGTH_LONG).show();
            }
        }){
            //Post ke PHP
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_LABS, "" + namalab);
                params.put(Config.KEY_TAHUN, "" + tahun);

                //returning parameter
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    //menampilkan username dari tabel users dan aktivitas dari tabel log
    private void showJSON(String response){
        //listLog = (ListVie w) findViewById(R.id.listview);
        int n = 0; //counter
        try {
            JSONArray result = new JSONArray(response);
//            Toast.makeText(this, "BERHASIL", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < result.length(); i++) {
                JSONObject Data = result.getJSONObject(i);
                String a = Data.getString(Config.KEY_BIAYA);

                if(a.equals("null") ){
                    a = "0";
                }else{
                    n++;
                }
                int biaya = Integer.parseInt(a);
                BARENTRY.add(new BarEntry(biaya, i));
            }
        }catch(JSONException e){
            e.printStackTrace();

        }

        //parsing json
        loading.dismiss();

        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY,"Total Pendapatan");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColor(Color.LTGRAY);
        chart.setData(BARDATA);
        chart.animateY(3000); //waktu animasi

        if(n==0){ //jika data null
            chart.clear();
        }

/*    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            spinner.setSelection(0);
            spinner2.setSelection(0);

        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            spinner.setSelection(0);
            spinner2.setSelection(0);

        }
    }*/
    }
}
