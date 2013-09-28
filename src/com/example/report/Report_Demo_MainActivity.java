package com.example.report;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Report_Demo_MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_demo_activity_main);
		Button genPieChart=(Button)findViewById(R.id.generatePieChart);
		
		
		genPieChart.setOnClickListener(new OnClickListener(){
		    public void onClick(View v) {
		        Intent pieChartIntent=new Intent(Report_Demo_MainActivity.this,PieChart_Activity.class);
		        startActivity(pieChartIntent);
		        
		    
		        
		       
		    }
		});
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_main, menu);
		return true;
	}

	
	public void callDB(View v)
	{
		Intent i=new Intent(this,PFActivity.class);
		startActivity(i);
	}
}
