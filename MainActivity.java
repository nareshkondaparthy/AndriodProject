package com.jdsports.universityapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    EditText m_etName , m_etStudentIDNo ,m_etAddress ,m_etCountry;
    EditText m_etEmailId ,m_etPassword ,m_etConfirmPswd;
    AutoCompleteTextView m_autoCourse;
    AutoCompleteTextView m_autoStaff;
    String m_strCourse = "";
    String m_strStaff = "";
    String m_strName = "";
    String m_strStudentIDNo = "";
    String m_strCountry = "";
    String m_strAddress = "";
    String m_strEmailId = "";
    String m_strPassword = "";
    String m_strConfirmPswd = "";

    DBHelper dbHelper ;
    SQLiteDatabase db = null;

    ArrayList<String> m_lstCourse;
    ArrayList<String> m_lstMandatory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this,"University",null,2);
        db = dbHelper.getWritableDatabase();


        m_lstCourse = new ArrayList<>();
        m_lstMandatory = new ArrayList<>();

        m_etName = (EditText)findViewById(R.id.et_name);
        m_etStudentIDNo = (EditText)findViewById(R.id.et_mobileno);
        m_etCountry = (EditText)findViewById(R.id.et_country);
        m_etAddress = (EditText)findViewById(R.id.et_address);
        m_etEmailId = (EditText)findViewById(R.id.et_emailid);
        m_etPassword = (EditText)findViewById(R.id.et_password);
        m_etConfirmPswd = (EditText)findViewById(R.id.et_confirmPswd);
        m_autoCourse = (AutoCompleteTextView)findViewById(R.id.autoCourse);
        m_autoStaff = (AutoCompleteTextView)findViewById(R.id.autoStaff);

        m_etName.setOnFocusChangeListener(focusChangeListener);
        m_etStudentIDNo.setOnFocusChangeListener(focusChangeListener);
        m_etCountry.setOnFocusChangeListener(focusChangeListener);
        m_etAddress.setOnFocusChangeListener(focusChangeListener);
        m_etEmailId.setOnFocusChangeListener(focusChangeListener);
        m_etPassword.setOnFocusChangeListener(focusChangeListener);
        m_etConfirmPswd.setOnFocusChangeListener(focusChangeListener);
        m_autoCourse.setOnFocusChangeListener(focusChangeListener);
        m_autoStaff.setOnFocusChangeListener(focusChangeListener);


        final ArrayList<String> lst_Coursees = new ArrayList<>();
        lst_Coursees.add("CSE");
        lst_Coursees.add("IT");
        lst_Coursees.add("ECE");
        lst_Coursees.add("EEE");
        lst_Coursees.add("MECH");

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,lst_Coursees);
        m_autoCourse.setAdapter(courseAdapter);
        m_autoCourse.setThreshold(1);
        m_autoCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                m_strCourse = lst_Coursees.get(pos).toString();
                if(m_lstMandatory.contains(m_autoCourse.getTag().toString()))
                    m_lstMandatory.remove(m_autoCourse.getTag().toString());
            }
        });

        m_autoCourse.setOnTouchListener(new OnTouchListener() {
			
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

        ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,lst_staff);
        m_autoStaff.setAdapter(arrayadapter);
        m_autoStaff.setThreshold(1);
        m_autoStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                m_strStaff = lst_staff.get(pos).toString();
            }
        });
        
        m_autoStaff.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				m_autoStaff.showDropDown();
				return false;
			}
		});

        Button btnReg = (Button)findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m_strName = m_etName.getText().toString();
                m_strStudentIDNo = m_etStudentIDNo.getText().toString();
                m_strAddress = m_etAddress.getText().toString();
                m_strCountry = m_etCountry.getText().toString();
                m_strEmailId = m_etEmailId.getText().toString();
                m_strPassword = m_etPassword.getText().toString();
                m_strConfirmPswd = m_etConfirmPswd.getText().toString();

                if (m_strPassword.equalsIgnoreCase(m_strConfirmPswd))
                {

                   if(GetCourseCount(m_strStudentIDNo))
                   {
                       ShowDialog("Reached Max Limit" ,"User is not allowed to enroll more than 4 courses.", 1);
                   }else
                   {
                       if(GetSubjectCount(m_strStudentIDNo)!= null)
                       {
                           if( m_lstCourse.contains(m_strCourse))
                           {
                               ShowMutliDialog("Error" ,"User was already enrolled with this Course.Do you want to enroll with another course?","Yes","No");
                               m_autoCourse.requestFocus();
                               m_lstMandatory.add(m_autoCourse.getTag().toString());
                           } else {

                               if (Validation()) {
                                   dbHelper.addStudentContact(m_strName, m_strPassword, m_strConfirmPswd, m_strStudentIDNo, m_strEmailId, m_strAddress, m_strCountry, m_strCourse, m_strStaff);
                                   ShowDialog("Success", "Registered Successfully",1);
                               } else
                                   //Toast.makeText(MainActivity.this,"Please enter all fields data",Toast.LENGTH_LONG).show();
                                   ShowDialog("Alert", "Please enter all fields data",0);
                           }
                       }else
                       {
                           if (Validation()) {
                               dbHelper.addStudentContact(m_strName, m_strPassword, m_strConfirmPswd, m_strStudentIDNo, m_strEmailId, m_strAddress, m_strCountry, m_strCourse, m_strStaff);
                               ShowDialog("Success", "Registered Successfully", 1);
                           } else
                               //Toast.makeText(MainActivity.this,"Please enter all fields data",Toast.LENGTH_LONG).show();
                               ShowDialog("Alert", "Please enter all fields data", 1);
                       }

                   }

                } else {
                    Toast.makeText(MainActivity.this, "Password doesn't match", Toast.LENGTH_LONG).show();
                }


            }
        });

        Button btnadmin = (Button)findViewById(R.id.btnadmin);
        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AdminEntry.class);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean GetCourseCount(String m_strStudentIDNo)
    {
        Cursor c = db.rawQuery("select count(Course) from StudentData where MobileNo='" + m_strStudentIDNo + "'", null);
        if (c != null) {
            if (c.getCount() > 0) {

                if (c.moveToFirst())
                {
                    String strCourseCount = c.getString(0);
                    int nCourseCount = Integer.parseInt(strCourseCount);
                    if(nCourseCount > 4)
                        return true;
                    else
                        return false;
                }
            }
        }
        return false;
    }

    private ArrayList<String> GetSubjectCount(String m_strMobileNo)
    {
        Cursor c = db.rawQuery("select Course from StudentData where MobileNo='" + m_strMobileNo + "'", null);
        if (c != null) {
            if (c.getCount() > 0) {

                if (c.moveToFirst())
                {
                    String strCourse = c.getString(0);
                    m_lstCourse.add(strCourse);
                        return m_lstCourse;
                }
            }
        }
        return null;
    }

    private void ShowDialog(String title, String msg, final int i)
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_Material_Dialog);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                if(i== 1) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        dialog.create();
        dialog.show();
    }

    private void ShowMutliDialog(String title, String msg,String strPositive,String strNegative)
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_Material_Dialog);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(false);

        dialog.setPositiveButton(strPositive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                m_autoCourse.setText("");
                m_strCourse = "";

            }
        });
        dialog.setNegativeButton(strNegative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        dialog.create();
        dialog.show();
    }

    private boolean Validation()
    {
       /* if(m_strName.equalsIgnoreCase("") && m_strPassword.equalsIgnoreCase("") && m_strConfirmPswd.equalsIgnoreCase("") && m_strMobileNo.equalsIgnoreCase("") && m_strAddress.equalsIgnoreCase("")
                && m_strEmailId.equalsIgnoreCase("") && m_strCountry.equalsIgnoreCase("")&& m_strCourse.equalsIgnoreCase("") && m_strStaff.equalsIgnoreCase(""))
        {*/
       if(m_lstMandatory.size() > 0 )
           return false;
       else
           return true;

    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                String strTag = ((EditText) v).getTag().toString();
                String strManditory = "is mandatory";
                if (((EditText) v).getText().toString().trim().length() == 0)
                {
                    if(!m_lstMandatory.contains(strTag))
                        m_lstMandatory.add(strTag);
                    ((EditText) v).setError(((EditText) v).getTag() + " " + strManditory);
                }
                else
                {
                    ((EditText) v).setError(null);
                    if(m_lstMandatory.contains(strTag))
                        m_lstMandatory.remove(strTag);
                }

                if( ((EditText) v).getTag().toString().equalsIgnoreCase("Email ID"))
                {

                    if(!emailValidator(((EditText) v).getText().toString()))
                    {
                        ((EditText) v).setError(((EditText) v).getTag() + " " + "not valid");
                        if(!m_lstMandatory.contains(strTag))
                        {
                            m_lstMandatory.add(strTag);
                        }

                    } else
                    {
                        ((EditText) v).setError(null);

                        if(m_lstMandatory.contains(strTag))
                            m_lstMandatory.remove(strTag);
                    }
                }

                if (v instanceof AutoCompleteTextView) {
                    if (((EditText) v).getText().toString().trim().length() == 0) {
                        ((EditText) v).setError(((EditText) v).getTag() + " " + strManditory);
                        if(!m_lstMandatory.contains(strTag))
                        {
                            m_lstMandatory.add(strTag);
                        }
                    } else {
                        ((AutoCompleteTextView) v).setError(null);

                        if(m_lstMandatory.contains(strTag))
                             m_lstMandatory.remove(strTag);

                    }
                }
            }
        }
    };


    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
