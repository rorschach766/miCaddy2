package com.example.jmo.micaddy.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.jmo.micaddy.MainActivity;
import com.example.jmo.micaddy.R;

import java.util.Locale;

/**
 * Created by jmo on 15/03/2017.
 */

public class CreateRoundActivity extends Activity implements View.OnClickListener{
    private EditText courseName;
    private Button btnStart;
    private Button btnCancel;
    private EditText editDate;

    private DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormatter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_round);

        btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        findViewsById();

        setDateTimeField();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDateTimeField() {
        editDate.setOnClickListener((View.OnClickListener) this);

        Calendar newCalender = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                editDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));
    }

    private void findViewsById() {
        editDate = (EditText) findViewById(R.id.edit_date);
        editDate.setInputType(InputType.TYPE_NULL);
        editDate.requestFocus();
    }

    public void onClick(View v){
        if(v == editDate){
            datePickerDialog.show();
        }
    }
}
