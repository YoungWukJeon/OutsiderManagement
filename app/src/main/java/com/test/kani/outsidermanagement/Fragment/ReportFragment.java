package com.test.kani.outsidermanagement.Fragment;

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
import android.widget.Toast;

import com.test.kani.outsidermanagement.Activity.MainActivity;
import com.test.kani.outsidermanagement.Utilitiy.FireStoreCallbackListener;
import com.test.kani.outsidermanagement.Utilitiy.FireStoreConnectionPool;
import com.test.kani.outsidermanagement.Utilitiy.LoadingDialog;
import com.test.kani.outsidermanagement.R;
import com.test.kani.outsidermanagement.Adapter.ReportListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        this.initReportList();

        this.view = inflater.inflate(R.layout.fragment_report, container, false);

        // Inflate the layout for this fragment
        return this.view;
    }

    private void initReportList()
    {
        Log.d("ReportFragment", "Initialize report list");

        if( loadingDialog == null )
            loadingDialog = new LoadingDialog(getContext());

        loadingDialog.show("Report Loading");

        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            final int TASK_FAILURE = 1;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
                    case TASK_FAILURE:
                        Log.d("ReportFragment", "Task is not successful");
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
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();

                if (!isSuccesful)
                {
                    occurError(TASK_FAILURE);
                    return;
                }

                if (obj == null)
                {
                    Log.d("ReportFragment", "Report history is not found");
                    reportList = new ArrayList<>();
                }
                else if (obj != null && !(obj instanceof Boolean) && !(obj instanceof String))
                    reportList = (ArrayList<HashMap<String, Object>>) obj;

                Collections.sort(reportList, new Comparator<HashMap<String, Object>>()
                {
                    @Override
                    public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2)
                    {
                        return -o1.get("reportDate").toString().compareTo(o2.get("reportDate").toString());
                    }
                });

                bindUI(view);
            }
        });

        if( (boolean) MainActivity.myInfoMap.get("officer") )   // 간부이면
            FireStoreConnectionPool.getInstance().select(fireStoreCallbackListener,
                    "report", "supervisorId", MainActivity.myInfoMap.get("id").toString());
        else
            FireStoreConnectionPool.getInstance().select(fireStoreCallbackListener,
                    "report", "memberId", MainActivity.myInfoMap.get("id").toString());
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

        if( (boolean) MainActivity.myInfoMap.get("officer") )    // 간부이면
        {
            this.subtitleLinearLayout.setVisibility(LinearLayout.VISIBLE);
            this.bottomLinearLayout.setVisibility(LinearLayout.GONE);
        }
        else
        {
            this.subtitleLinearLayout.setVisibility(LinearLayout.GONE);
            this.bottomLinearLayout.setVisibility(LinearLayout.VISIBLE);
        }

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

                if ((boolean) MainActivity.myInfoMap.get("isOutsider"))
                {
                    final HashMap<String, Object> map = new HashMap<>();
                    String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

                    map.put("outsiderType", MainActivity.myInfoMap.get("outsiderType"));
                    map.put("memberId", MainActivity.myInfoMap.get("id"));
                    map.put("supervisorId", MainActivity.myInfoMap.get("supervisorId"));
                    map.put("class", MainActivity.myInfoMap.get("class"));
                    map.put("name", MainActivity.myInfoMap.get("name"));
                    map.put("reportDate", now);
                    map.put("reportContent", contentEditText.getText().toString().trim());
                    map.put("tel", MainActivity.myInfoMap.get("tel"));

                    contentEditText.setText("");

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);

                    loadingDialog.show("Reporting now");

                    FireStoreConnectionPool.getInstance().insertNoID(fireStoreCallbackListener, map, "report");
                    FireStoreConnectionPool.getInstance().updateOne(fireStoreCallbackListener, "outsider",
                            "memberId", MainActivity.myInfoMap.get("id").toString(), "startDate", "endDate", "reportDate", now);

                    reportList.add(map);
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(getContext(), "현재 출타 중이지 않아서 보고를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
