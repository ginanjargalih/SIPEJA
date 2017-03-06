package com.example.android.sipeja.order;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.android.sipeja.config.Config;

public class DBController  extends SQLiteOpenHelper {



	public DBController(Context applicationcontext) {
		super(applicationcontext, "transaksi.db", null,Config.db_version);
	}
	//Creates Table
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE transaksi ( transaksiId INTEGER, transaksiName TEXT,Nama_Perusahaan TEXT, UNIQUE(transaksiId, transaksiName) ON CONFLICT REPLACE)";
		database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS transaksi";
		database.execSQL(query);
		onCreate(database);
	}


	/**
	 * Inserts User into SQLite DB
	 * @param queryValues
	 */
	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("transaksiId", queryValues.get("transaksiId"));
		values.put("transaksiName", queryValues.get("transaksiName"));
		values.put("Nama_Perusahaan", queryValues.get("Nama_Perusahaan"));
		database.insertWithOnConflict("transaksi", null, values,SQLiteDatabase.CONFLICT_REPLACE);
		database.close();
	}

	/**
	 * Get list of Users from SQLite DB as Array List
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getAllUsers() {
		//untuk jumlah transaksi
		Config.index = 0;

		ArrayList<HashMap<String, String>> usersList;
		usersList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM transaksi";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("transaksiId", cursor.getString(0));
				map.put("transaksiName", cursor.getString(1));
				map.put("Nama_Perusahaan", cursor.getString(2));
				usersList.add(map);

				Config.index = Config.index + 1;

			} while (cursor.moveToNext());
		}
		database.close();
		return usersList;
	}

}
