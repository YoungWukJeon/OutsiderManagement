package com.test.kani.outsidermanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class CallVisitFragment extends Fragment
{
    ListView callVisitListView;
    CallVisitListAdapter adapter;

    private ArrayList<HashMap<String, Object>> callVisitList;
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
        this.initCallVisitList();

        this.view = inflater.inflate(R.layout.fragment_call_visit, container, false);

        // Inflate the layout for this fragment

        return this.view;
    }

    private void initCallVisitList()
    {
        Log.d("CallVisitFragment", "Initialize call visit list");

        if( loadingDialog == null )
            loadingDialog = new LoadingDialog(getContext());

        loadingDialog.show("CallVisit Loading");

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
                        Log.d("CallVisitFragment", "Task is not successful");
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
                    Log.d("CallVisitFragment", "Call-visit history is not found");
                    callVisitList = new ArrayList<>();
                }
                else
                {
                    callVisitList = (ArrayList<HashMap<String, Object>>) obj;

                    for( HashMap<String, Object> map : callVisitList )
                    {
                        Set<String> keys = map.keySet();
                        Iterator<String> iter = keys.iterator();

                        while( iter.hasNext() )
                        {
                            String key = iter.next();

                            if( map.get(key) == null )
                                map.put(key, "");
                        }

                        map.put("outsiderDuring", map.get("startDate").toString() + " ~ " + map.get("endDate").toString());
                    }
                }
                Collections.sort(callVisitList, new Comparator<HashMap<String, Object>>()
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

        FireStoreConnectionPool.getInstance().selectBetweenDate(fireStoreCallbackListener,
                "outsider", "supervisorId", MainActivity.myInfoMap.get("id").toString(),
                "startDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                "endDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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