package com.test.kani.outsidermanagement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class OutsiderManagementFragment extends Fragment
{
    ListView outsiderManagementListView;
    OutsiderManagementListAdapter adapter;
    FloatingActionButton addFloatingBtn, removeFloatingBtn;

    private ArrayList<HashMap<String, Object>> outsiderManagementList;
    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

    View view;

    private BaseDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        this.initOutsiderManagementList();

        this.view = inflater.inflate(R.layout.fragment_outsider_management, container, false);

        // Inflate the layout for this fragment
        return this.view;
    }

    private void initOutsiderManagementList()
    {
        Log.d("OutManagementFragment", "Initialize outsider management list");

        this.outsiderManagementList = new ArrayList<> ();

        if( loadingDialog == null )
            loadingDialog = new LoadingDialog(getContext());

        loadingDialog.show("OutsiderManagement Loading");

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
                        Log.d("OutManagementFragment", "Task is not successful");
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
                    Log.d("OutManagementFragment", "outsider history is not found");
                    outsiderManagementList = new ArrayList<>();
                }
                else if( obj != null && obj instanceof Boolean)
                {
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    outsiderManagementList = (ArrayList<HashMap<String, Object>>) obj;

                    for( HashMap<String, Object> map : outsiderManagementList )
                    {
                        Set<String> keys = map.keySet();
                        Iterator<String> iter = keys.iterator();

                        while( iter.hasNext() )
                        {
                            String key = iter.next();

                            if( map.get(key) == null )
                                map.put(key, "");
                        }

                        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                        if( map.get("startDate").toString().compareTo(now) <= 0 )
                            map.put("checked", null);
                        else
                            map.put("checked", false);

                        map.put("outsiderDuring", map.get("startDate").toString() + " ~ " + map.get("endDate").toString());
                    }

                    Collections.sort(outsiderManagementList, new Comparator<HashMap<String, Object>>()
                    {
                        @Override
                        public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2)
                        {
                            return o1.get("startDate").toString().compareTo(o2.get("startDate").toString());
                        }
                    });
                }


                bindUI(view);
            }
        });

        FireStoreConnectionPool.getInstance().selectGreaterThanDate(fireStoreCallbackListener,
                "outsider", "supervisorId", MainActivity.myInfoMap.get("id").toString(),
                "endDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void bindUI(View view)
    {
        // Bind views
        this.outsiderManagementListView = view.findViewById(R.id.outsider_management_listView);
        this.adapter = new OutsiderManagementListAdapter(getActivity(), R.layout.outsider_management_item, this.outsiderManagementList);
        this.addFloatingBtn = view.findViewById(R.id.add_floatingBtn);
        this.removeFloatingBtn = view.findViewById(R.id.remove_floatingBtn);

        // Set attributes
        this.outsiderManagementListView.setAdapter(this.adapter);

        // Add Events
        this.addFloatingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("addFloatingActionButton", "Clicked : ");

                dialog = new OutsiderAddDialog(getActivity(), R.style.Theme_Dialog);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                ((OutsiderAddDialog) dialog).setDialogSetting();

                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                params.width = 700;
                params.height = 970;
                dialog.getWindow().setAttributes(params);

                dialog.setUpdateComponentListener(new BaseDialog.UpdateComponentListener() {
                    @Override
                    public void updateComponent(Object obj)
                    {
                        outsiderManagementList.add((HashMap<String, Object>) obj);

                        Collections.sort(outsiderManagementList, new Comparator<HashMap<String, Object>>()
                        {
                            @Override
                            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2)
                            {
                                return o1.get("startDate").toString().compareTo(o2.get("startDate").toString());
                            }
                        });

                        adapter.notifyDataSetChanged();
                    }
                });

                dialog.show();
            }
        });

        this.removeFloatingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("revFloatingActionButton", "Clicked : ");

                Iterator<HashMap<String, Object>> iter = outsiderManagementList.iterator();

                while( iter.hasNext() )
                {
                    HashMap<String, Object> map = iter.next();

                    if( map.get("checked") != null && (boolean) map.get("checked") )
                    {
                        loadingDialog.show("Outsider Removing");
                        FireStoreConnectionPool.getInstance().deleteOne(fireStoreCallbackListener,
                                "outsider", map.get("documentId").toString());

                        iter.remove();
                    }
                }
            }
        });
    }
}