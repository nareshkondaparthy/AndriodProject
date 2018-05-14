package com.jdsports.universityapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Dashboard extends Activity {

	DBHelper dbHelper;
	SQLiteDatabase db = null;
	ArrayList<ClassDetails> lst_ClsDet;
	String strCourse = "";
	String strName = "";
	String strStaff = "";
	String strUserName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		dbHelper = new DBHelper(Dashboard.this, "University", null, 2);
		db = dbHelper.getWritableDatabase();

		Bundle b = getIntent().getExtras();
		if (b != null) {
			strCourse = b.getString("Course");
			strName = b.getString("Name");
			strStaff = b.getString("Staff");
			strUserName = b.getString("UserName");
		}


		TextView tvWelcomeNote = (TextView) findViewById(R.id.tv_welcomeNote);
		tvWelcomeNote.setText(" WELCOME " + strUserName.toUpperCase());

		lst_ClsDet = new ArrayList<>();

		if (!strStaff.equalsIgnoreCase( "Faculty"))
		{

			Cursor c = db.rawQuery("select * from AdminEntry where Course='" + strCourse + "'", null);
			if (c != null) {
				if (c.getCount() > 0) {
					if (c.moveToFirst()) {
						do {

							ClassDetails clsDet = new ClassDetails();
							clsDet.setCourse(c.getString(1));
							clsDet.setSubject(c.getString(2));
							clsDet.setDate(c.getString(3));
							clsDet.setFromTime(c.getString(4));
							clsDet.setToTime(c.getString(5));
							clsDet.setRoom(c.getString(6));
							clsDet.setFaculity(c.getString(7));

							lst_ClsDet.add(clsDet);

						} while (c.moveToNext());

						TextView txtFaculity = (TextView) findViewById(R.id.txtFaculity);
						txtFaculity.setVisibility(View.VISIBLE);

						ListView listView = (ListView) findViewById(R.id.listView);
						CustomAdapter customAdapter = new CustomAdapter(Dashboard.this, lst_ClsDet , 0 );
						listView.setAdapter(customAdapter);
					}
				} else
					Toast.makeText(Dashboard.this, "No data", Toast.LENGTH_LONG).show();

			}
		} else {
			Cursor c = db.rawQuery("select * from AdminEntry where Course='" + strCourse + "'" + "and Staff = '" + strUserName + "'", null);
			if (c != null) {
				if (c.getCount() > 0) {
					if (c.moveToFirst()) {
						do {

							ClassDetails clsDet = new ClassDetails();
							clsDet.setCourse(c.getString(1));
							clsDet.setSubject(c.getString(2));
							clsDet.setDate(c.getString(3));
							clsDet.setFromTime(c.getString(4));
							clsDet.setToTime(c.getString(5));
							clsDet.setRoom(c.getString(6));

							lst_ClsDet.add(clsDet);

						} while (c.moveToNext());

						TextView txtFaculity = (TextView) findViewById(R.id.txtFaculity);
						txtFaculity.setVisibility(View.GONE);

						ListView listView = (ListView) findViewById(R.id.listView);
						CustomAdapter customAdapter = new CustomAdapter(Dashboard.this, lst_ClsDet , 1);
						listView.setAdapter(customAdapter);
					}
				} else
					Toast.makeText(Dashboard.this, "No data", Toast.LENGTH_LONG).show();
			}
		}
	}
}
