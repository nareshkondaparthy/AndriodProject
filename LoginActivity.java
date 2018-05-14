package com.jdsports.universityapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

	DBHelper dbHelper ;
	SQLiteDatabase db = null;
	EditText m_etUser;
	EditText m_etPass;
	String strUsername;
	String strPassword;
	String strUser;
	String m_strCourse = "";
	String m_strStaff = "";


	AutoCompleteTextView m_autoCourse,m_autoStaff ;

	ArrayList<String> m_lstUsername;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		BackupDB();

		dbHelper = new DBHelper(LoginActivity.this,"University",null,2);
		db = dbHelper.getWritableDatabase();

		m_lstUsername = new ArrayList<>();

		m_etUser = (EditText)findViewById(R.id.etUser);
		m_etPass = (EditText)findViewById(R.id.etPass);
		m_autoCourse = (AutoCompleteTextView)findViewById(R.id.autoCourse);
		m_autoStaff = (AutoCompleteTextView)findViewById(R.id.autoStaff);


		Button btnStaffReg = (Button)findViewById(R.id.btnStaffReg);
		btnStaffReg.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent i = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(i);
			}
		});

		Button btnAdminEntry = (Button)findViewById(R.id.btnAdminEntry);
		btnAdminEntry.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent i = new Intent(LoginActivity.this,AdminEntry.class);
				startActivity(i);
			}
		});



		Cursor c = db.rawQuery("select * from StudentData", null);
		if(c != null )
		{
			if(c.getCount() > 0)
			{
				if(c.moveToFirst())
					do {
						strUsername = c.getString(4);

						m_lstUsername.add(strUsername);

						System.out.println(m_lstUsername );
					} while (c.moveToNext());
			}else
				Toast.makeText(LoginActivity.this, "There are no registered ID's", Toast.LENGTH_LONG).show();
		}


		final ArrayList<String> lst_Coursees = new ArrayList<>();
		lst_Coursees.add("CSE");
		lst_Coursees.add("IT");
		lst_Coursees.add("ECE");
		lst_Coursees.add("EEE");
		lst_Coursees.add("MECH");

		ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(LoginActivity.this,android.R.layout.simple_list_item_1,lst_Coursees);
		m_autoCourse.setAdapter(courseAdapter);
		m_autoCourse.setThreshold(1);
		m_autoCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

				m_strCourse = lst_Coursees.get(pos).toString();
			}
		});

		m_autoCourse.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				m_autoCourse.showDropDown();
				return false;
			}
		});


		final ArrayList<String> lst_staff = new ArrayList<>();
		lst_staff.add("Student");
		lst_staff.add("Faculty");

		ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(LoginActivity.this,android.R.layout.simple_list_item_1,lst_staff);
		m_autoStaff.setAdapter(arrayadapter);
		m_autoStaff.setThreshold(1);
		m_autoStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

				m_strStaff = lst_staff.get(pos).toString();
			}
		});

		m_autoStaff.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				m_autoStaff.showDropDown();
				return false;
			}
		});

		Button btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	strUser = m_etUser.getText().toString();
            	String strPass = m_etPass.getText().toString();

				if(strUser.equalsIgnoreCase("admin") && strPass.equalsIgnoreCase("admin"))
				{
					Intent i = new Intent(LoginActivity.this, AdminView.class);
					startActivity(i);
				}else {

					if(!m_strCourse.equalsIgnoreCase("")) {
						if (m_lstUsername.contains(strUser)) {
							Cursor c = db.rawQuery("select Password,Staff,Name from StudentData where MobileNo='" + strUser + "'" + "and Course = '" + m_strCourse + "'"  + "and Staff = '" + m_strStaff + "'" , null);
							if (c != null) {
								if (c.getCount() > 0) {

									if (c.moveToFirst()) {
										do {
											strPassword = c.getString(0);
											String strStaff = c.getString(1);
											String strName = c.getString(2);
											if (strPass.equalsIgnoreCase(strPassword)) {
												Intent i = new Intent(LoginActivity.this, Dashboard.class);
												i.putExtra("Course", m_strCourse);
												i.putExtra("Name", strUser);
												i.putExtra("Staff", strStaff);
												i.putExtra("UserName",strName);
												startActivity(i);
												//finish();
											}
										}
										while (c.moveToNext());

									}
								} else
									Toast.makeText(LoginActivity.this, "You have not registered with the selected course", Toast.LENGTH_LONG).show();
							}
						} else
							ShowDialog("Error", "Invalid Username/Password");
					}else
						Toast.makeText(LoginActivity.this, "Please select course", Toast.LENGTH_LONG).show();

				}
			}
        });
	}



	private void ShowDialog(String title, String msg)
	{
		final AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this,android.R.style.Theme_Material_Dialog);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setCancelable(false);

		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();

			}
		});
		dialog.create();
		dialog.show();
	}

	public void BackupDB() {
		try {
			String inFileName = "/data/data/com.jdsports.universityapp/databases/University.sqlite";
			File dbFile = new File(inFileName);
			if (!dbFile.exists())
				inFileName = "/data/data/com.jdsports.universityapp/databases/University";

			dbFile = new File(inFileName);

			FileInputStream fis = new FileInputStream(dbFile);

			String outFileName = Environment.getExternalStorageDirectory() + "/University.sqlite";

			OutputStream output = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

			// Close the streams
			output.flush();
			output.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
