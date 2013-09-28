package com.example.report;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "mcsproj";

	// Contacts table name
	private static final String TABLE_COMPANY = "financenew";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_DESC = "description";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
  
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTICES_TABLE = "CREATE TABLE " + TABLE_COMPANY + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
				+ KEY_AMOUNT + " INTEGER," + KEY_CATEGORY + " TEXT," + KEY_DESC
				+ " TEXT" + ")";
		db.execSQL(CREATE_NOTICES_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);

		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	void addTrans(Finance finance) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, finance.getDate());
		values.put(KEY_AMOUNT, finance.getAmount());
		values.put(KEY_CATEGORY, finance.getCategory());
		values.put(KEY_DESC, finance.getDescription());
		// Inserting Row
		db.insert(TABLE_COMPANY, null, values);

		db.close(); // Closing database connection
	}

	// Getting All Contacts
	public List<Finance> getAllRecords() {
		List<Finance> transList = new ArrayList<Finance>();

		// Select All Query
		String selectQuery = "SELECT date,amount,category,description FROM financenew";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			Log.i("LOg is null", "query null");
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				Log.i("OK", "Yes");
				do {
					Finance f = new Finance();

					f.setDate(cursor.getString(0));
					f.setAmount(cursor.getInt(1));
					f.setCategory(cursor.getString(2));
					f.setDescription(cursor.getString(3));

					// Adding contact to list
					transList.add(f);

				} while (cursor.moveToNext());
			}
		}
		db.close();

		return transList;
	}

	// Getting All Contacts
	public List<Finance> getRecords(String date) {
		List<Finance> recordList = new ArrayList<Finance>();

		// Select All Query
		String selectQuery = "SELECT date,amount,category,description FROM financenew where date=\""
				+ date + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {

				do {
					Finance f = new Finance();

					f.setDate(cursor.getString(0));
					f.setAmount(cursor.getInt(1));
					f.setCategory(cursor.getString(2));
					f.setDescription(cursor.getString(3));
					// Adding contact to list
					recordList.add(f);

				} while (cursor.moveToNext());
			}
		}
		db.close();

		return recordList;
	}

	// Getting All Contacts
	public List<Finance> getmonthRecords(String month) {
		List<Finance> recordList = new ArrayList<Finance>();

		String selectQuery = "SELECT date,sum(amount),category,description FROM financenew group by category";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {

			if (cursor.moveToFirst()) {

				do {
					String date = cursor.getString(0);
					String dt[] = date.split("/");
					if (dt[1].equals(month)) {
						Finance f = new Finance();

						f.setDate(date);
						f.setAmount(cursor.getInt(1));
						f.setCategory(cursor.getString(2));
						f.setDescription(cursor.getString(3));
						// Adding contact to list
						recordList.add(f);
					}

				} while (cursor.moveToNext());
			}
		}
		db.close();

		return recordList;
	}

	// Getting All Contacts
	public List<Finance> getuniqueRecords(String date) {
		List<Finance> recordList = new ArrayList<Finance>();
		String selectQuery = "SELECT sum(amount) ,category FROM financenew where date=\""
				+ date + "\" group by category";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {

			if (cursor.moveToFirst()) {

				do {
					Finance f = new Finance();

					f.setAmount(cursor.getInt(0));
					f.setCategory(cursor.getString(1));

					recordList.add(f);

				} while (cursor.moveToNext());
			}
		}
		db.close();

		return recordList;
	}

	public void delAllTrans(String date) {

		// Select All Query
		String selectQuery = "select * FROM financenew where date=\"" + date
				+ "\"";

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Finance f = new Finance();
					f.setId(Integer.parseInt(cursor.getString(0)));
					db.delete(TABLE_COMPANY, KEY_ID + " = ?",
							new String[] { String.valueOf(f.getId()) }); // Cursor
																			// cursor
																			// =
					db.rawQuery(selectQuery, null);

				} while (cursor.moveToNext());
			}
		}

	}

}
