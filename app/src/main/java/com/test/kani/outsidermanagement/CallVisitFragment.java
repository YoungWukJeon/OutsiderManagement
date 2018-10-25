package com.test.kani.outsidermanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class CallVisitFragment extends Fragment
{
    ListView callVisitListView;
    CallVisitListAdapter adapter;

    private ArrayList<HashMap<String, Object>> callVisitList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        this.initCallVisitList();

        View view = inflater.inflate(R.layout.fragment_call_visit, container, false);

        this.bindUI(view);
        // Inflate the layout for this fragment

        return view;
    }

    private void initCallVisitList()
    {
        Log.d("CallVisitFragment", "Initialize call visit list");
        this.callVisitList = new ArrayList<> ();

        HashMap<String, Object> temp1 = new HashMap<> ();
        HashMap<String, Object> temp2 = new HashMap<> ();
        HashMap<String, Object> temp3 = new HashMap<> ();
        HashMap<String, Object> temp4 = new HashMap<> ();
        HashMap<String, Object> temp5 = new HashMap<> ();
        HashMap<String, Object> temp6 = new HashMap<> ();

        temp1.put("type", "일반");
        temp1.put("class", "상병");
        temp1.put("name", "유성우");
        temp1.put("reportDate", "2018.10.25 16:17");    // currentTimeMillis로 바꿀예정
        temp1.put("tel", "010-1234-5567");
        temp1.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");

        temp2.put("type", "관심");
        temp2.put("class", "병장");
        temp2.put("name", "유성우");
        temp2.put("reportDate", "2018.10.25 16:17");    // currentTimeMillis로 바꿀예정
        temp2.put("tel", "010-1111-2222");
        temp2.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");

        temp3.put("type", "배려");
        temp3.put("class", "병장");
        temp3.put("name", "유성우");
        temp3.put("reportDate", "2018.10.25 16:17");    // currentTimeMillis로 바꿀예정
        temp3.put("tel", "010-3333-4444");
        temp3.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");

        temp4.put("type", "배려");
        temp4.put("class", "일병");
        temp4.put("name", "유성우");
        temp4.put("reportDate", "2018.10.25 16:17");    // currentTimeMillis로 바꿀예정
        temp4.put("tel", "010-5555-6666");
        temp4.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");

        temp5.put("type", "일반");
        temp5.put("class", "이병");
        temp5.put("name", "유성우");
        temp5.put("reportDate", "2018.10.25 16:17");    // currentTimeMillis로 바꿀예정
        temp5.put("tel", "010-7777-8888");
        temp5.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");

        temp6.put("type", "관심");
        temp6.put("class", "이병");
        temp6.put("name", "유성우");
        temp6.put("reportDate", "2018.10.25 16:17");    // currentTimeMillis로 바꿀예정
        temp6.put("tel", "010-9999-0000");
        temp6.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");

        this.callVisitList.add(temp1);
        this.callVisitList.add(temp2);
        this.callVisitList.add(temp3);
        this.callVisitList.add(temp4);
        this.callVisitList.add(temp5);
        this.callVisitList.add(temp6);
    }

    private void bindUI(View view)
    {
        // Bind views
        this.callVisitListView = view.findViewById(R.id.call_visit_listView);
        this.adapter = new CallVisitListAdapter(getActivity(), R.layout.call_visit_item, this.callVisitList);

        // Set attributes
        this.callVisitListView.setAdapter(this.adapter);

        // Add Events
    }
}
