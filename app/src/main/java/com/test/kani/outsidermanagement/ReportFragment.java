package com.test.kani.outsidermanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ReportFragment extends Fragment
{
    LinearLayout subtitleLinearLayout, bottomLinearLayout;
    ListView reportListView;
    EditText contentEditText;
    Button reportBtn;

    ArrayList<HashMap<String, Object>> reportList;
    private ReportListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        this.initReportList();

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        this.bindUI(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initReportList()
    {
        Log.d("ReportFragment", "Initialize report list");

        this.reportList = new ArrayList<> ();

        HashMap<String, Object> temp1 = new HashMap<> ();
        HashMap<String, Object> temp2 = new HashMap<> ();
        HashMap<String, Object> temp3 = new HashMap<> ();
        HashMap<String, Object> temp4 = new HashMap<> ();

        temp1.put("type", "관심");
        temp1.put("checked", false);
        temp1.put("id", "18-11222421");
        temp1.put("class", "상병");
        temp1.put("name", "유성우");
        temp1.put("reportDate", "2018.10.13 21:07");
        temp1.put("reportContent", "현재 속초 앞바다에 있습니다. 이상 없습니다!");

        temp2.put("type", "배려");
        temp2.put("checked", false);
        temp2.put("id", "18-11222421");
        temp2.put("class", "일병");
        temp2.put("name", "홍길동");
        temp2.put("reportDate", "2018.10.14 21:07");
        temp2.put("reportContent", "잘자고 있습니다. 이상");

        temp3.put("type", "일반");
        temp3.put("checked", false);
        temp3.put("id", "18-11222421");
        temp3.put("class", "상병");
        temp3.put("name", "신상호");
        temp3.put("reportDate", "2018.09.13 21:07");
        temp3.put("reportContent", "잘 도착했습니다.");

        temp4.put("type", "일반");
        temp4.put("checked", false);
        temp4.put("id", "18-11222421");
        temp4.put("class", "상병");
        temp4.put("name", "김명");
        temp4.put("reportDate", "2018.10.12 21:07");
        temp4.put("reportContent", "이상무!");

        this.reportList.add(temp1);
        this.reportList.add(temp2);
        this.reportList.add(temp3);
        this.reportList.add(temp4);
    }

    private void bindUI(View view)
    {
        // Bind views
        this.subtitleLinearLayout = view.findViewById(R.id.subtitle_linear_layout);
        this.reportListView = view.findViewById(R.id.report_listView);
        this.adapter = new ReportListAdapter(getActivity(), R.layout.report_item, this.reportList);
        this.bottomLinearLayout = view.findViewById(R.id.bottom_linear_layout);
        this.contentEditText = view.findViewById(R.id.content_editText);
        this.reportBtn = view.findViewById(R.id.report_btn);

        // Set attributes
        this.reportListView.setAdapter(this.adapter);

        if( MainActivity.isOfficer )    // 간부이면
            this.bottomLinearLayout.setVisibility(LinearLayout.GONE);
        else
            this.subtitleLinearLayout.setVisibility(LinearLayout.GONE);

        // Add Events
        this.contentEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if( s.toString().trim().length() == 0 )
                    reportBtn.setEnabled(false);
                else
                    reportBtn.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        this.reportBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("reportBtn", "Clicked : ");

                HashMap<String, Object> map = new HashMap<> ();

                map.put("type", "일반");
                map.put("checked", false);
                map.put("id", "18-11222421");
                map.put("class", "상병");
                map.put("name", "유성우");
                map.put("reportDate", new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new Date()));
                map.put("reportContent", contentEditText.getText().toString().trim());

                reportList.add(map);
                contentEditText.setText("");
                adapter.notifyDataSetChanged();

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        });
    }
}
