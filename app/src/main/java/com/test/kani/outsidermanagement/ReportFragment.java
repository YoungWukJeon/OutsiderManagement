package com.test.kani.outsidermanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        this.initReportList();

        view = inflater.inflate(R.layout.fragment_report, container, false);


        // Inflate the layout for this fragment
        return view;
    }

    private void initReportList()
    {
        Log.d("ReportFragment", "Initialize report list");

        this.reportList = new ArrayList<> ();

        if( (boolean) MainActivity.myInfoMap.get("officer") )
        {
            for( final HashMap<String, Object> map : (ArrayList<HashMap<String, Object>>) MainActivity.myInfoMap.get("report") )
            {
                FireStoreConnectionPool.getInstance().getDB().collection("member").whereEqualTo("id", map.get("id"))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2)
                    {
                        if (task2.isSuccessful() && !task2.getResult().isEmpty())
                        {
                            for (QueryDocumentSnapshot document2 : task2.getResult() )
                            {
                                map.put("class", document2.getData().get("class"));
                                map.put("name", document2.getData().get("name"));
                                map.put("checked", false);
                            }

                            reportList.add(map);
                            bindUI(view);
                        }
                        else
                            Toast.makeText(getContext(), "list recieve fails", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else
        {
            FireStoreConnectionPool.getInstance().getDB().collection("member").whereEqualTo("id", MainActivity.myInfoMap.get("supervisor"))
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if( task.isSuccessful() && !task.getResult().isEmpty() )
                    {
                        for (QueryDocumentSnapshot document : task.getResult())
                            for( final HashMap<String, Object> map : (ArrayList<HashMap<String, Object>>) document.getData().get("report") )
                            {
                                if( map.get("id").toString().equals(MainActivity.myInfoMap.get("id").toString()) )
                                    reportList.add(map);
                            }

                        bindUI(view);
                    }
                    else
                        Toast.makeText(getContext(), "list recieve fails", Toast.LENGTH_SHORT).show();
                }
            });
        }

//        temp1.put("type", "관심");
//        temp1.put("checked", false);
//        temp1.put("id", "18-11222421");
//        temp1.put("class", "상병");
//        temp1.put("name", "유성우");
//        temp1.put("reportDate", "2018.10.13 21:07");
//        temp1.put("reportContent", "현재 속초 앞바다에 있습니다. 이상 없습니다!");

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

                final HashMap<String, Object> map = new HashMap<> ();

                map.put("type", "일반");
                map.put("id", MainActivity.myInfoMap.get("id"));
                map.put("reportDate", new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new Date()));
                map.put("reportContent", contentEditText.getText().toString().trim());

                FireStoreConnectionPool.getInstance().getDB().collection("member").whereEqualTo("id", MainActivity.myInfoMap.get("supervisor"))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if( task.isSuccessful() && !task.getResult().isEmpty() )
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                ArrayList<HashMap<String, Object>> list = ((ArrayList<HashMap<String, Object>>) document.getData().get("report"));
                                list.add(map);

                                FireStoreConnectionPool.getInstance().getDB().collection("member").document(document.getId())
                                        .update("report", list);
                            }
                        }
                        else
                            Toast.makeText(getContext(), "report fails", Toast.LENGTH_SHORT).show();

                        map.put("class", MainActivity.myInfoMap.get("class"));
                        map.put("name", MainActivity.myInfoMap.get("name"));
                        reportList.add(map);
                        adapter.notifyDataSetChanged();
                    }
                });


                contentEditText.setText("");



                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        });
    }
}
