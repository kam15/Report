package com.example.report;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

public class PFActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_demo_activity_main);
		System.out.println("after main");
		Date d = new Date(System.currentTimeMillis());
		System.out.println("date=" + d.getDate() + "month=" + d.getMonth()
				+ "year=" + d.getYear());
		int yr = d.getYear();
		String dt;
		if (yr > 99) {
			dt = "10" + "/" + d.getMonth() + "/20" + (yr % 100);
		} else {
			dt = "10" + "/" + d.getMonth() + "/19" + (yr % 100);
		}

		DatabaseHandler db = new DatabaseHandler(this);
		System.out.println("after db");
		for (int i = 0; i < 10; i++)
			db.addTrans(new Finance(dt, 10, "food", "hotel"));
		for (int i = 10; i < 30; i++)
			db.addTrans(new Finance(dt, 20, "travel", "des" + i));
		for (int i = 10; i < 50; i++)
			db.addTrans(new Finance(dt, 50, "shoppping", "mall"));
		System.out.println("inserted");
		List<Finance> allList = db.getAllRecords();
		List<Finance> list = db.getRecords(dt);

		// System.out.println(list.size() + " " + allList.size());
		for (Finance f : list) {
			String log = "Date: " + f.getDate() + " ,Amount: " + f.getAmount()
					+ " ,Category: " + f.getCategory() + " ,Description: "
					+ f.getDescription();
			// Writing Contacts to log
			// System.out.println(log);

		}// for

		list = db.getuniqueRecords(dt);

		System.out.println(list.size() + " " + allList.size());
		for (Finance f : list) {
			String log = "For unique Amount: " + f.getAmount() + " Category="
					+ f.getCategory();
			// Writing Contacts to log
			System.out.println(log);

		}// for

		list = db.getmonthRecords("8");

		System.out.println("size=" + list.size() + " " + allList.size());
		for (Finance f : list) {
			String log = "For Month:Amount: " + f.getAmount() + " Category="
					+ f.getCategory();
			// Writing Contacts to log
			System.out.println(log);

		}// for

	//	db.delAllTrans(dt);
		list = db.getRecords(dt);
		System.out.println(list.size() + " " + allList.size());
	}
}