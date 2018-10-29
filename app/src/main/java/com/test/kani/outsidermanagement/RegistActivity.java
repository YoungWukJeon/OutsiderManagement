package com.test.kani.outsidermanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistActivity extends AppCompatActivity
{
    RadioGroup typeRadioGroup;
    EditText idEditText, passwordEditText, fromEditText, classEditText, nameEditText, telEditText,
            supervisorEditText, startDateEditText, endDateEditText;
    Button registBtn, cancelBtn;

    private HashMap<String, Object> myInfoMap;
    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

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
        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            final int ID_EXISTED = 0;
            final int TASK_FAILURE = 1;

            boolean idValidate = false;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
                    case ID_EXISTED:
                        Log.d("RegistActivity", "This ID is existed");
                        Toast.makeText(getApplicationContext(), "아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                        idEditText.selectAll();
                        idEditText.requestFocus();
                        break;
                    case TASK_FAILURE:
                        Log.d("RegistActivity", "Task is not successful");
                        break;
                    default:
                        break;
                }

                if( loadingDialog.isShowing() )
                    loadingDialog.dismiss();
            }

            @Override
            public void doNext(boolean isSuccesful, Object obj)
            {
                if( !isSuccesful )
                {
                    occurError(TASK_FAILURE);
                    return;
                }

                if( !idValidate && obj == null )
                {
                    String id = myInfoMap.get("id").toString();
                    myInfoMap.remove("id");
                    FireStoreConnectionPool.getInstance().insert(fireStoreCallbackListener, myInfoMap,
                            "member", id);
                    idValidate = true;
                    return;
                }
                else if( !idValidate && obj != null )
                {
                    occurError(ID_EXISTED);
                    return;
                }

                if( loadingDialog != null )
                    loadingDialog.dismiss();

                Toast.makeText(getApplicationContext(), "회원가입완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


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
                myInfoMap.put("isOutsider", false);

                if( !(boolean) myInfoMap.get("officer") )
                    myInfoMap.put("supervisorId", supervisorEditText.getText().toString().trim());
                else
                {
                    myInfoMap.put("supervisorId", null);
                    myInfoMap.put("report", new ArrayList());
                }

                if( loadingDialog == null )
                    loadingDialog = new LoadingDialog(RegistActivity.this);

                loadingDialog.show("Regist");

                FireStoreConnectionPool.getInstance().selectOne(fireStoreCallbackListener,
                        "member", myInfoMap.get("id").toString());

//                FireStoreConnectionPool.getInstance().insert(fireStoreCallbackListener, myInfoMap,
//                        "outsider", "member", "user", myInfoMap.get("id").toString());
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
