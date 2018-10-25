package com.test.kani.outsidermanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MyInfoFragment extends Fragment
{
    TextView idTextView, passwordTextView, fromTextView, classTextView, nameTextView, phoneTextView, supervisorTextView, startDateTextView, endDateTextView;
    EditText passwordEditText, fromEditText, classEditText, nameEditText, phoneEditText, supervisorEditText, startDateEditText, endDateEditText;
    Button modifyBtn, saveBtn, cancelBtn;

    private HashMap<String, Object> myInfoMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        this.initMyInfo();

        View view = inflater.inflate(R.layout.fragment_my_info, container, false);

        this.bindUI(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initMyInfo()
    {
        Log.d("MyInfoFragment", "Initialize my info");
        this.myInfoMap = new HashMap<> ();

        this.myInfoMap.put("officer", true);  // 병인지 간부인지 여부
        this.myInfoMap.put("id", "18-00001");
        this.myInfoMap.put("password", "1q2w3e4r!!");
        this.myInfoMap.put("from", "111연대 통신중대");
        this.myInfoMap.put("class", "소위");
        this.myInfoMap.put("name", "아무개");
        this.myInfoMap.put("tel", "010-1234-5678");
        this.myInfoMap.put("supervisor", "없음");
        this.myInfoMap.put("startDate", "2018-03-02");
        this.myInfoMap.put("endDate", "2020-06-30");
    }

    private void bindUI(View view)
    {
        // Bind Views
        this.idTextView = view.findViewById(R.id.id_textView);
        this.passwordTextView = view.findViewById(R.id.password_textView);
        this.fromTextView = view.findViewById(R.id.from_textView);
        this.classTextView = view.findViewById(R.id.class_textView);
        this.nameTextView = view.findViewById(R.id.name_textView);
        this.phoneTextView = view.findViewById(R.id.phone_textView);
        this.supervisorTextView = view.findViewById(R.id.supervisor_textView);
        this.startDateTextView = view.findViewById(R.id.start_date_textView);
        this.endDateTextView = view.findViewById(R.id.end_date_textView);

        this.passwordEditText = view.findViewById(R.id.password_editText);
        this.fromEditText = view.findViewById(R.id.from_editText);
        this.classEditText = view.findViewById(R.id.class_editText);
        this.nameEditText = view.findViewById(R.id.name_editText);
        this.phoneEditText = view.findViewById(R.id.phone_editText);
        this.supervisorEditText = view.findViewById(R.id.supervisor_editText);
        this.startDateEditText = view.findViewById(R.id.start_date_editText);
        this.endDateEditText = view.findViewById(R.id.end_date_editText);

        this.modifyBtn = view.findViewById(R.id.modify_btn);
        this.saveBtn = view.findViewById(R.id.save_btn);
        this.cancelBtn = view.findViewById(R.id.cancel_btn);

        // Set Attributes
        this.idTextView.setText(this.myInfoMap.get("id").toString().trim());
        this.passwordTextView.setText(this.myInfoMap.get("password").toString().trim());
        this.fromTextView.setText(this.myInfoMap.get("from").toString().trim());
        this.classTextView.setText(this.myInfoMap.get("class").toString().trim());
        this.nameTextView.setText(this.myInfoMap.get("name").toString().trim());
        this.phoneTextView.setText(this.myInfoMap.get("tel").toString().trim());
        this.supervisorTextView.setText(this.myInfoMap.get("supervisor").toString().trim());
        this.startDateTextView.setText(this.myInfoMap.get("startDate").toString().trim());
        this.endDateTextView.setText(this.myInfoMap.get("endDate").toString().trim());

        // Add Events
        this.modifyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Log.d("Modify Button", "Clicked");
                modifyBtn.setVisibility(Button.GONE);
                saveBtn.setVisibility(Button.VISIBLE);
                cancelBtn.setVisibility(Button.VISIBLE);
                moveTextFromTextView();
                switchTextView(TextView.GONE);
                switchEditText(EditText.VISIBLE);
            }
        });

        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Log.d("Modify Button", "Clicked");
                Log.d("My Info", "Changed");
                modifyBtn.setVisibility(Button.VISIBLE);
                saveBtn.setVisibility(Button.GONE);
                cancelBtn.setVisibility(Button.GONE);
                changeMyInfo();
                moveTextFromEditText();
                switchTextView(TextView.VISIBLE);
                switchEditText(EditText.GONE);
            }
        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Log.d("Cancel Button", "Clicked");
                modifyBtn.setVisibility(Button.VISIBLE);
                saveBtn.setVisibility(Button.GONE);
                cancelBtn.setVisibility(Button.GONE);
                switchTextView(TextView.VISIBLE);
                switchEditText(EditText.GONE);
            }
        });
    }

    private void moveTextFromTextView()
    {
        this.passwordEditText.setText(this.passwordTextView.getText().toString().trim());
        this.fromEditText.setText(this.fromTextView.getText().toString().trim());
        this.classEditText.setText(this.classTextView.getText().toString().trim());
        this.nameEditText.setText(this.nameTextView.getText().toString().trim());
        this.phoneEditText.setText(this.phoneTextView.getText().toString().trim());
        this.supervisorEditText.setText(this.supervisorTextView.getText().toString().trim());
        this.startDateEditText.setText(this.startDateTextView.getText().toString().trim());
        this.endDateEditText.setText(this.endDateTextView.getText().toString().trim());
    }

    private void moveTextFromEditText()
    {
        this.passwordTextView.setText(this.passwordEditText.getText().toString().trim());
        this.fromTextView.setText(this.fromEditText.getText().toString().trim());
        this.classTextView.setText(this.classEditText.getText().toString().trim());
        this.nameTextView.setText(this.nameEditText.getText().toString().trim());
        this.phoneTextView.setText(this.phoneEditText.getText().toString().trim());
        this.supervisorTextView.setText(this.supervisorEditText.getText().toString().trim());
        this.startDateTextView.setText(this.startDateEditText.getText().toString().trim());
        this.endDateTextView.setText(this.endDateEditText.getText().toString().trim());
    }

    private void changeMyInfo()
    {
        this.myInfoMap.put("password", this.passwordEditText.getText().toString().trim());
        this.myInfoMap.put("from", this.fromEditText.getText().toString().trim());
        this.myInfoMap.put("class", this.classEditText.getText().toString().trim());
        this.myInfoMap.put("name", this.nameEditText.getText().toString().trim());
        this.myInfoMap.put("tel", this.phoneEditText.getText().toString().trim());
        this.myInfoMap.put("supervisor", this.supervisorEditText.getText().toString().trim());
        this.myInfoMap.put("startDate", this.startDateEditText.getText().toString().trim());
        this.myInfoMap.put("endDate", this.endDateEditText.getText().toString().trim());
    }

    private void switchTextView(int visibility)
    {
        this.passwordTextView.setVisibility(visibility);
        this.fromTextView.setVisibility(visibility);
        this.classTextView.setVisibility(visibility);
        this.nameTextView.setVisibility(visibility);
        this.phoneTextView.setVisibility(visibility);
        this.supervisorTextView.setVisibility(visibility);
        this.startDateTextView.setVisibility(visibility);
        this.endDateTextView.setVisibility(visibility);
    }

    private void switchEditText(int visibility)
    {
        this.passwordEditText.setVisibility(visibility);
        this.fromEditText.setVisibility(visibility);
        this.classEditText.setVisibility(visibility);
        this.nameEditText.setVisibility(visibility);
        this.phoneEditText.setVisibility(visibility);
        this.supervisorEditText.setVisibility(visibility);
        this.startDateEditText.setVisibility(visibility);
        this.endDateEditText.setVisibility(visibility);
    }
}