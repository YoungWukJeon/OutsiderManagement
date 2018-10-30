package com.test.kani.outsidermanagement;

import android.content.Intent;
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
import java.util.Map;

public class MyInfoFragment extends Fragment
{
    TextView idTextView, passwordTextView, fromTextView, classTextView, nameTextView, telTextView, supervisorTextView, startDateTextView, endDateTextView;
    EditText passwordEditText, fromEditText, classEditText, nameEditText, telEditText, supervisorEditText, startDateEditText, endDateEditText;
    Button logoutBtn, modifyBtn, saveBtn, cancelBtn;

    HashMap<String, Object> myInfoMap;
    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

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
        this.myInfoMap.putAll(MainActivity.myInfoMap);
        this.myInfoMap.remove("id");
    }

    private void bindUI(View view)
    {
        // Bind Views
        this.idTextView = view.findViewById(R.id.id_textView);
        this.passwordTextView = view.findViewById(R.id.password_textView);
        this.fromTextView = view.findViewById(R.id.from_textView);
        this.classTextView = view.findViewById(R.id.class_textView);
        this.nameTextView = view.findViewById(R.id.name_textView);
        this.telTextView = view.findViewById(R.id.tel_textView);
        this.supervisorTextView = view.findViewById(R.id.supervisor_textView);
        this.startDateTextView = view.findViewById(R.id.start_date_textView);
        this.endDateTextView = view.findViewById(R.id.end_date_textView);

        this.passwordEditText = view.findViewById(R.id.password_editText);
        this.fromEditText = view.findViewById(R.id.from_editText);
        this.classEditText = view.findViewById(R.id.class_editText);
        this.nameEditText = view.findViewById(R.id.name_editText);
        this.telEditText = view.findViewById(R.id.tel_editText);
        this.supervisorEditText = view.findViewById(R.id.supervisor_editText);
        this.startDateEditText = view.findViewById(R.id.start_date_editText);
        this.endDateEditText = view.findViewById(R.id.end_date_editText);

        this.logoutBtn = view.findViewById(R.id.logout_btn);
        this.modifyBtn = view.findViewById(R.id.modify_btn);
        this.saveBtn = view.findViewById(R.id.save_btn);
        this.cancelBtn = view.findViewById(R.id.cancel_btn);

        // Set Attributes
        this.idTextView.setText(MainActivity.myInfoMap.get("id").toString().trim());
        this.passwordTextView.setText(this.myInfoMap.get("password").toString().trim());
        this.fromTextView.setText(this.myInfoMap.get("from").toString().trim());
        this.classTextView.setText(this.myInfoMap.get("class").toString().trim());
        this.nameTextView.setText(this.myInfoMap.get("name").toString().trim());
        this.telTextView.setText(this.myInfoMap.get("tel").toString().trim());
        this.startDateTextView.setText(this.myInfoMap.get("startDate").toString().trim());
        this.endDateTextView.setText(this.myInfoMap.get("endDate").toString().trim());

        if( !(boolean) this.myInfoMap.get("officer") )
            this.supervisorTextView.setText(this.myInfoMap.get("supervisorId").toString().trim());
        else
            this.supervisorTextView.setText("없음");

        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            //            final int ID_EXISTED = 0;
            final int TASK_FAILURE = 1;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
    //                    case ID_EXISTED:
    //                        Log.d("RegistActivity", "This ID is existed");
    //                        Toast.makeText(getContext(), "아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
    ////                        idEditText.selectAll();
    ////                        idEditText.requestFocus();
    //                        break;
                    case TASK_FAILURE:
                        Log.d("MyInfoFragment", "Task is not successful");
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
                if( loadingDialog != null )
                    loadingDialog.dismiss();

                if( !isSuccesful )
                {
                    occurError(TASK_FAILURE);
                    return;
                }

//                Toast.makeText(getContext(), "정보수정완료", Toast.LENGTH_SHORT).show();
            }
        });


        // Add Events
        this.logoutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity.myInfoMap = null;
                MainActivity.document = null;

                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

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
                logoutBtn.setVisibility(Button.GONE);
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
                logoutBtn.setVisibility(Button.VISIBLE);

//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
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
                logoutBtn.setVisibility(Button.VISIBLE);

//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    private void moveTextFromTextView()
    {
        this.passwordEditText.setText(this.passwordTextView.getText().toString().trim());
        this.fromEditText.setText(this.fromTextView.getText().toString().trim());
        this.classEditText.setText(this.classTextView.getText().toString().trim());
        this.nameEditText.setText(this.nameTextView.getText().toString().trim());
        this.telEditText.setText(this.telTextView.getText().toString().trim());
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
        this.telTextView.setText(this.telEditText.getText().toString().trim());
        this.supervisorTextView.setText(this.supervisorEditText.getText().toString().trim());
        this.startDateTextView.setText(this.startDateEditText.getText().toString().trim());
        this.endDateTextView.setText(this.endDateEditText.getText().toString().trim());
    }

    private void changeMyInfo()
    {
        if( loadingDialog == null )
            loadingDialog = new LoadingDialog(getContext());

        loadingDialog.show("MyInfo Updating");

        this.myInfoMap.put("password", this.passwordEditText.getText().toString().trim());
        this.myInfoMap.put("from", this.fromEditText.getText().toString().trim());
        this.myInfoMap.put("class", this.classEditText.getText().toString().trim());
        this.myInfoMap.put("name", this.nameEditText.getText().toString().trim());
        this.myInfoMap.put("tel", this.telEditText.getText().toString().trim());
        this.myInfoMap.put("startDate", this.startDateEditText.getText().toString().trim());
        this.myInfoMap.put("endDate", this.endDateEditText.getText().toString().trim());

        Map<String, Object> map = new HashMap<> ();
        map.put("class", MainActivity.myInfoMap.get("class").toString());
        map.put("name", MainActivity.myInfoMap.get("name").toString());

        if( !(boolean) this.myInfoMap.get("officer") )
        {
            this.myInfoMap.put("supervisorId", this.supervisorEditText.getText().toString().trim());
            map.put("supervisorId", MainActivity.myInfoMap.get("supervisorId").toString());
            map.put("tel", MainActivity.myInfoMap.get("tel").toString());
        }

        FireStoreConnectionPool.getInstance().update(fireStoreCallbackListener, myInfoMap,
                "member", MainActivity.myInfoMap.get("id").toString());

        FireStoreConnectionPool.getInstance().updateBatch(fireStoreCallbackListener, map,
                "report", "memberId", MainActivity.myInfoMap.get("id").toString());

        FireStoreConnectionPool.getInstance().updateBatch(fireStoreCallbackListener, map,
                "outsider", "memberId", MainActivity.myInfoMap.get("id").toString());

    }

    private void switchTextView(int visibility)
    {
        this.passwordTextView.setVisibility(visibility);
        this.fromTextView.setVisibility(visibility);
        this.classTextView.setVisibility(visibility);
        this.nameTextView.setVisibility(visibility);
        this.telTextView.setVisibility(visibility);
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
        this.telEditText.setVisibility(visibility);
        this.supervisorEditText.setVisibility(visibility);
        this.startDateEditText.setVisibility(visibility);
        this.endDateEditText.setVisibility(visibility);
    }
}