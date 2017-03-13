package com.example.android.sipeja.dashboard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aziz on 11/03/2017.
 */

public class Status_Pembayaran extends Fragment {
    public View v;
    //variabel global
    ProgressDialog loading;
    PieChart chart1;

    List<String> labs;
    List<String> year;
    Spinner spinner, spinner2;
    ArrayList<com.github.mikephil.charting.data.Entry> Entry;
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
        v = inflater.inflate(R.layout.status_pembayaran, container, false);
        chart1 = (PieChart) v.findViewById(R.id.chart1);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);

        //show();
        return v;
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
//        return inflater.inflate(R.layout.senin, container, false);
    }

    public void show(){
        chart1.setCenterText(generateCenterSpannableText());
        Entry = new ArrayList<>();
        //BarEntryLabels = new ArrayList<String>();

//        chart1 = (PieChart) getActivity().findViewById(R.id.chart1);
        //chart1.setCenterText(generateCenterSpannableText());

        // =================== Spinner untuk laboratorium ======================//

        // Spinner element
//        spinner = (Spinner) getActivity().findViewById(R.id.spinner);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

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

        // ========================= Spinner untuk Pendidikan ==============================

        // Spinner element
//        spinner2 = (Spinner) getActivity().findViewById(R.id.spinner2);

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
                    Entry.clear();
                    //BarEntryLabels.clear();
                    if(spinner.getSelectedItem().equals("Tahun")){
                        chart1.clear();
                    }else {
                        getData(labs.get(i), spinner.getSelectedItem().toString());
                    }
                }else{
                    chart1.clear();
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
                    Entry.clear();
                    //BarEntryLabels.clear();
                    if(spinner2.getSelectedItem().equals("Laboratorium")){
                        chart1.clear();
                    }else {
                        getData(spinner2.getSelectedItem().toString(), year.get(i));
                    }
                }else{
                    chart1.clear();
                }
//                Toast.makeText(getActivity(), year.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //fungsi untuk mengambil data dari database
    private void getData(final String namalab, final String tahun) {
        //String id = editTextId.getText().toString().trim();

        /*if (id.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }*/

        loading = ProgressDialog.show(getActivity(),"Mohon Tunggu","Pengambilan data..",false,false);

        String url = Config.URL+ "dashboard/loadStatusPembayaran.php"; //inisialiasai url

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
    private void showJSON(String response) {

        try {
            JSONArray result = new JSONArray(response);

            JSONObject Data1 = result.getJSONObject(0);
            String lunas = Data1.getString(Config.KEY_LUNAS);
            JSONObject Data2 = result.getJSONObject(1);
            String belumLunas = Data2.getString(Config.KEY_BELUMLUNAS);

            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(Integer.parseInt(lunas), 0));
            entries.add(new Entry(Integer.parseInt(belumLunas), 1));

            PieDataSet dataset = new PieDataSet(entries, "# of Calls");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("Lunas");
            labels.add("Belum Lunas");

            PieData data = new PieData(labels, dataset);
            dataset.setColors(ColorTemplate.LIBERTY_COLORS); //
            chart1.setDescription("Status Pembayaran");
            chart1.setData(data);
            chart1.animateY(2000);

            if(lunas.equals("0") && belumLunas.equals("0")){
                chart1.clear();
            }

            chart1.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //parsing json
        loading.dismiss();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Status\nPembayaran");
        //s.setSpan(new RelativeSizeSpan(1.7f), 0, 17, 0);
        //s.setSpan(new StyleSpan(Typeface.NORMAL), 3, s.length() - 10, 0);
        //s.setSpan(new ForegroundColorSpan(Color.GRAY), 3, s.length() - 4, 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        //s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    /*public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(getActivity(), "landscape", Toast.LENGTH_SHORT).show();
            spinner.setSelection(0);
            spinner2.setSelection(0);

        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(getActivity(), "portrait", Toast.LENGTH_SHORT).show();
            spinner.setSelection(0);
            spinner2.setSelection(0);

        }
    }*/
}
