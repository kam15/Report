package com.example.report;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class PieChart_Activity extends Activity {

	SQLiteDatabase database;
	Cursor c;

	int[] pieChartValues = { 25, 15, 20, 40 };
	String[] pieChartlabels = { "Food", "cloths", "abc", "sonu" };

	// ArrayList<Finance> pieChartlabels=new ArrayList<Finance>();

	private static int[] COLORS = new int[] { Color.YELLOW, Color.BLUE,
			Color.GREEN, Color.CYAN };
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	private GraphicalView mChartView;
	private TextView textView1;
	Button button_dialog_specificDate;
	int flag = 1;
	int databaseflag = 1;
	String changedDate;
	NumberPicker np;
	String dt;
	AlertDialog.Builder alert;
	String getMonthFromnumberpicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pie_chart_);

		final Context mContext = this;
		mRenderer.setApplyBackgroundColor(true);
		// mRenderer.setBackgroundColor(Color.argb(100, 80, 80, 50));
		mRenderer.setChartTitleTextSize(15);
		mRenderer.setLabelsTextSize(20);
		mRenderer.setLabelsColor(color.black);
		mRenderer.setDisplayValues(true);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setLegendTextSize(20);
		mRenderer.setShowLabels(true);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(180);

		Calendar d = new GregorianCalendar();
		// Date d = new Date(System.currentTimeMillis());
		System.out
				.println("date=" + d.get(Calendar.DATE) + "month="
						+ d.get(Calendar.DAY_OF_MONTH) + "year="
						+ d.get(Calendar.YEAR));
		int yr = d.get(Calendar.YEAR);

		if (yr > 99) {
			dt = d.get(Calendar.DATE) + "/" + d.get(Calendar.MONTH) + "/20"
					+ (yr % 100);
		} else {
			dt = d.get(Calendar.DATE) + "/" + d.get(Calendar.DAY_OF_MONTH)
					+ "/19" + (yr % 100);
		}

		drawPieForSpecificDate(dt);
		Button button_specificDate = (Button) this
				.findViewById(R.id.button_specificDate);

		button_specificDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// custom dialog

				final Dialog dialog = new Dialog(mContext);
				dialog.setContentView(R.layout.dialog_specificdate);
				dialog.setTitle("Select Specific Date");
				CalendarView cv = (CalendarView) dialog
						.findViewById(R.id.calendarView1);
				button_dialog_specificDate = (Button) dialog
						.findViewById(R.id.button_dialog_specificDate);
				cv.setClickable(true);

				flag = 1;
				try {

					Class<?> cvClass = cv.getClass();
					Field field = cvClass.getDeclaredField("mMonthName");
					field.setAccessible(true);

					try {
						TextView tv = (TextView) field.get(cv);
						tv.setTextColor(Color.RED);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}

				cv.setOnDateChangeListener(new OnDateChangeListener() {

					@Override
					public void onSelectedDayChange(CalendarView view,
							int year, int month, int dayOfMonth) {
						flag = 0;
						System.out.println("----------------datechanged");
						Toast.makeText(getApplicationContext(),
								"" + dayOfMonth, 0).show();
						// return day and dismiss the dialog

						if (year > 99) {
							changedDate = dayOfMonth + "/" + month + "/20"
									+ (year % 100);
						} else {
							changedDate = dayOfMonth + "/" + month + "/19"
									+ (year % 100);
						}

						System.out.println("----------------------"
								+ changedDate);

						drawPieForSpecificDate(changedDate);
					}
				});

				// set the custom dialog components - text, image and button

				button_dialog_specificDate
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								// tv.setText(String.valueOf(np.getValue()));
								dialog.dismiss();
								if (flag == 1)
									drawPieForSpecificDate(dt);
							}
						});
				dialog.show();

			}
		});// button_specificDate setListener

		Button button_specificMonth = (Button) this
				.findViewById(R.id.button_specificMonth);

		alert = new AlertDialog.Builder(this);

		button_specificMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// custom dialog

				LayoutInflater inflater = (LayoutInflater) getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View npView = inflater.inflate(R.layout.dialog_specificmonth,
						null);
				alert.setTitle("Select Specific Month");

				np = (NumberPicker) npView.findViewById(R.id.numberPicker1);
				np.setMaxValue(12);
				np.setMinValue(1);
				// np.setBackgroundColor(Color.BLUE);
				np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

				alert.setPositiveButton("Set",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								getMonthFromnumberpicker = String.valueOf(np
										.getValue());
								drawPieForSpecificMonth(getMonthFromnumberpicker);
								System.out.println("getmonth"
										+ getMonthFromnumberpicker);
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Cancel.

							}
						});

				alert.setView(npView).show();
			}

		});

		// final Dialog dialog = new Dialog(mContext);
		// dialog.setContentView(R.layout.dialog_specificmonth);

		// dialog.setTitle("Select Specific Month");
		//
		// np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
		// np.setMaxValue(12);
		// np.setMinValue(1);
		// np.setBackgroundColor(Color.LTGRAY);
		// np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		// Button button_dialog_setMonth = (Button) dialog
		// .findViewById(R.id.button_dialog_setMonth);
		// Button button_dialog_cancelMonth = (Button) dialog
		// .findViewById(R.id.button_dialog_cancelMonth);
		//
		// button_dialog_setMonth
		// .setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// System.out.println("-------------"
		// + np.getValue());
		// // tv.setText(String.valueOf(np.getValue()));
		// dialog.dismiss();
		// }
		// });
		// button_dialog_cancelMonth
		// .setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });
		//
		// // drawPieForSpecificDate(changedDate);
		//
		// // set the custom dialog components - text, image and button
		//
		// dialog.show();
		//
		// //resizing the dialog
		// WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		//
		// lp.copyFrom(dialog.getWindow().getAttributes());
		// lp.width = 290;
		// lp.height = 400;
		// lp.x=-0;
		// lp.y=70;
		// dialog.getWindow().setAttributes(lp);
		// }
		// });// button_specificDate setListener

	}

	void drawPieForSpecificDate(String dt) {

		DatabaseHandler db = new DatabaseHandler(this);
		List<Finance> list = db.getuniqueRecords(dt);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		System.out.println("in side inside ");
		if (databaseflag == 0) {
			if (mRenderer != null)
				mRenderer.removeAllRenderers();
			if (mSeries != null)
				mSeries.clear();
		}
		if (!list.isEmpty()) {
			System.out.println("in side ");
			databaseflag = 0;
			for (int i = 0; i < list.size(); i++) {

				Finance f = list.get(i);

				mSeries.add("" + f.getCategory() + (mSeries.getItemCount() + 1),
						f.getAmount());

				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();

				renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
						% COLORS.length]);
				mRenderer.addSeriesRenderer(renderer);

				if (mChartView != null) {
					mChartView.repaint();

					textView1.setText("");
				}

			}
		} else {
			if (mChartView != null)
				mChartView.repaint();
			textView1
					.setText("You have not added data any data for today till  ");
		}

	}

	void drawPieForSpecificMonth(String month) {

		DatabaseHandler db = new DatabaseHandler(this);
		List<Finance> list = db.getmonthRecords(month);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		System.out.println("in side inside ");
		if (databaseflag == 0) {
			if (mRenderer != null)
				mRenderer.removeAllRenderers();
			if (mSeries != null)
				mSeries.clear();
		}
		if (!list.isEmpty()) {
			System.out.println("in side ");
			databaseflag = 0;
			for (int i = 0; i <list.size(); i++) {

				Finance f = list.get(i);

				mSeries.add("" + f.getCategory() + (mSeries.getItemCount() + 1),
						f.getAmount());

				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();

				renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
						% COLORS.length]);
				mRenderer.addSeriesRenderer(renderer);

				if (mChartView != null) {
					mChartView.repaint();

					textView1.setText("");
				}

			}
		} else {
			if (mChartView != null)
				mChartView.repaint();
			textView1
					.setText("You have not added data any data for today till");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setClickEnabled(true);
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						// Toast.makeText(PieChart_Activity.this,
						// "No chart element selected", Toast.LENGTH_SHORT)
						// .show();
						mChartView.repaint();

					} else {
						for (int i = 0; i < mSeries.getItemCount(); i++) {
							mRenderer.getSeriesRendererAt(i).setHighlighted(
									i == seriesSelection.getPointIndex());

						}
						mChartView.repaint();
						Toast.makeText(
								PieChart_Activity.this,
								"Chart data point index "
										+ seriesSelection.getPointIndex()
										+ " selected" + " point value="
										+ seriesSelection.getValue(),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			if (mSeries.getItemCount() == 0)
				textView1
						.setText("You have not added data any data for today till now  ");
		} else {
			mChartView.repaint();

		}
	}
}
