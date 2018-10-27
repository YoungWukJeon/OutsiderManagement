package com.test.kani.outsidermanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistActivity extends AppCompatActivity
{
    RadioGroup typeRadioGroup;
    EditText idEditText, passwordEditText, fromEditText, classEditText, nameEditText, telEditText,
            supervisorEditText, startDateEditText, endDateEditText;
    Button registBtn, cancelBtn;

    private HashMap<String, Object> myInfoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        this.bindUI();
    }

    private void bindUI()
    {
        // Bind Views
        this.typeRadioGroup = findViewById(R.id.type_radioGroup);
        this.idEditText = findViewById(R.id.id_editText);
        this.passwordEditText = findViewById(R.id.password_editText);
        this.fromEditText = findViewById(R.id.from_editText);
        this.classEditText = findViewById(R.id.class_editText);
        this.nameEditText = findViewById(R.id.name_editText);
        this.telEditText = findViewById(R.id.tel_editText);
        this.supervisorEditText = findViewById(R.id.supervisor_editText);
        this.startDateEditText = findViewById(R.id.start_date_editText);
        this.endDateEditText = findViewById(R.id.end_date_editText);
        this.registBtn = findViewById(R.id.regist_btn);
        this.cancelBtn = findViewById(R.id.cancel_btn);

        // Set Attributes

        // Add Events
        this.registBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Log.d("Regist Button", "Clicked");

                myInfoMap = new HashMap<> ();

                myInfoMap.put("officer", isOfficer());  // 병인지 간부인지 여부
                myInfoMap.put("id", idEditText.getText().toString().trim());
                myInfoMap.put("password", passwordEditText.getText().toString().trim());
                myInfoMap.put("from", fromEditText.getText().toString().trim());
                myInfoMap.put("class", classEditText.getText().toString().trim());
                myInfoMap.put("name", nameEditText.getText().toString().trim());
                myInfoMap.put("tel", telEditText.getText().toString().trim());
                myInfoMap.put("startDate", startDateEditText.getText().toString().trim());
                myInfoMap.put("endDate", endDateEditText.getText().toString().trim());

                if( !(boolean) myInfoMap.get("officer") )
                    myInfoMap.put("supervisor", supervisorEditText.getText().toString().trim());
                else
                {
                    myInfoMap.put("supervisor", null);
                    myInfoMap.put("report", new ArrayList());
                }
                FireStoreConnectionPool.getInstance().getDB().collection("member").add(myInfoMap);
                finish();
            }
        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Log.d("Cancel Button", "Clicked");
                finish();
            }
        });
    }

    private boolean isOfficer()
    {
        switch( this.typeRadioGroup.getCheckedRadioButtonId() )
        {
            case R.id.option1:
                return true;
            case R.id.option2:
                return false;
        }

        return false;
    }
}
