package com.example.android.sipeja.config;

/**
 * Created by x453 on 08/02/2017.
 */

public class Config {

    public static final String URL = "http://sipeja.pe.hu/";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String MyPREFERENCES = "MyPrefs" ;

    //This would be used to store the email of current logged in user
    public static final String Name = "nameKey";
    public static final String NamePengguna = "npKey";
    public static final String Password = "passKey";
    public static final String NIP = "nipKey";
    public static final String Email = "emailKey";
    public static final String no_hp="123";

    //JSON for log actvitity
    public static final String KEY_USERNAME = "username";
    public static final String KEY_AKTIVITAS = "aktivitas";
    public static final String KEY_WAKTU = "updated_at";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    //untuk menghitung jumlah transaksi
    public  static Integer index = 0;

    //untuk menandai pelanggan atau pegawai
    public static  final String akses = "0";


    //untuk detail transaksi
    public static String kode = "Kode_transaksi";

    public static final String id_transaksi="id";
    public  static final String kode_transaki="kode";
    public  static final String status_transaki="1";
    public  static  final String status_pembayaran ="status2";
    public  static final String nama_lab="lab";
    public  static final String tanggal_transaksi="tgl";
    public  static final String Pelanggan="pel";
    public  static final String nama_sertifikat="ns";
    public  static final String alamat_sertifikat="as";
    public  static final String sertifikat_inggris="ing";
    public  static final String sisa_sampel="ss";
    public  static final String keterangan="kt";
    public  static final String nama_kontak="kontak";
    public  static final String nomor_kontak="nomor";

    public static final String nominal="nominal";
    public static final String biaya_awal="biayaawal";
    public static final String diskon="diskon";

    public static final String alamat_pelanggan="ap";
    public static final String telepon_pelanggan="tp";
    public static final String jenis_pelanggan="jp";
    public static final String email_pelanggan="ep";
    public static final String kota="k";
    public static final String provinsi="p";

    public static final String KODE_LAB="pq";

    public static int db_version=1;
    public static int hitung=0;
    public static int hitung2=0;

    //untuk log transaksi
    public static final String Transaksi_id = "transaksiId";
    public static final String Transaksi_orang="name";
    public static final String Transaksi_AKTIVITAS = "aktivitas";
    public static final String Transaksi_WAKTU = "updated_at";

    //JSON for load status pembayaran
    public static final String KEY_LUNAS = "Lunas"; //total lunas
    public static final String KEY_BELUMLUNAS = "BelumLunas"; //total belum lunas
    public static final String KEY_LABS = "nama"; //nama laboratorium
    public static final String KEY_TAHUN = "tahun"; //tahun

    //JSON for load pendapatan per lab
    public static final String KEY_BIAYA = "biaya"; //total pendapatan per lab

    //JSON for load status order
    public static final String KEY_PROSES = "Proses";
    public static final String KEY_PERBAIKAN = "Perbaikan";
    public static final String KEY_BATAL = "Batal";
    public static final String KEY_SELESAI = "Selesai";

    //JSON for load SPM
    public static final String KEY_TEPAT = "waktuTepat";
    public static final String KEY_TELAT = "waktuTelat";

    //untuk sampel
    public static final String Sampel_id = "id";
    public static final String Sampel_nama = "nama";

    //detail sampel
    public static final String Sampel_jumlah = "jumlahSampel";
    public static final String Sampel_sertifikat = "jumlahSertifikat";
    public static final String Sampel_keterangan = "keterangan";
    public static final String Sampel_perkiraanSelesai = "perkiraanSelesai";

    //untuk parameter
    public static final String Parameter_nama = "Parameter";
    public static final String Parameter_harga = "HargaSatuan";
    public static final String Parameter_jumlah = "jumlah";
    public static final String Parameter_metode = "Metode";

    //untuk lingkup
    //JSON URL
    public static final String DATA_URL = "http://sipeja.pe.hu/API_Lingkup/json.php";

    //Tags used in the JSON String
    public static final String Lingkup_ID= "id";
    public static final String Lingkup_Nama = "nama";
    public static final String Lingkup_Lab = "labId";
    public static final String Kode_Lingkup = "kode_lingkup";

    //JSON array name
    public static final String JSON_ARRAY = "result";
}
