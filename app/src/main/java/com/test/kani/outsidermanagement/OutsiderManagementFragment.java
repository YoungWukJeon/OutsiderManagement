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
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OutsiderManagementFragment extends Fragment
{
    ListView outsiderManagementListView;
    OutsiderManagementListAdapter adapter;
    CheckBox selectCheckBox;
    FloatingActionButton addFloatingBtn, removeFloatingBtn;

    private ArrayList<HashMap<String, Object>> outsiderManagementList;

    private BaseDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        this.initOutsiderManagementList();

        View view = inflater.inflate(R.layout.fragment_outsider_management, container, false);

        this.bindUI(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initOutsiderManagementList()
    {
        Log.d("OutManagementFragment", "Initialize outsider management list");

        this.outsiderManagementList = new ArrayList<> ();

        HashMap<String, Object> temp1 = new HashMap<> ();
        HashMap<String, Object> temp2 = new HashMap<> ();
        HashMap<String, Object> temp3 = new HashMap<> ();
        HashMap<String, Object> temp4 = new HashMap<> ();
        HashMap<String, Object> temp5 = new HashMap<> ();
        HashMap<String, Object> temp6 = new HashMap<> ();

        temp1.put("type", "일반");
        temp1.put("checked", false);
        temp1.put("class", "상병");
        temp1.put("name", "유성우");
        temp1.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");    // currentTimeMillis로 바꿀예정
        temp1.put("outsiderReason", "정기 휴가 9일 + 혹한기 유공 4일");

        temp2.put("type", "관심");
        temp2.put("checked", false);
        temp2.put("class", "병장");
        temp2.put("name", "유성우");
        temp2.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");    // currentTimeMillis로 바꿀예정
        temp2.put("outsiderReason", "정기 휴가 9일 + 혹한기 유공 4일");

        temp3.put("type", "배려");
        temp3.put("checked", true);
        temp3.put("class", "병장");
        temp3.put("name", "유성우");
        temp3.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");    // currentTimeMillis로 바꿀예정
        temp3.put("outsiderReason", "정기 휴가 9일 + 혹한기 유공 4일");

        temp4.put("type", "배려");
        temp4.put("checked", true);
        temp4.put("class", "일병");
        temp4.put("name", "유성우");
        temp4.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");    // currentTimeMillis로 바꿀예정
        temp4.put("outsiderReason", "정기 휴가 9일 + 혹한기 유공 4일");

        temp5.put("type", "일반");
        temp5.put("checked", false);
        temp5.put("class", "이병");
        temp5.put("name", "유성우");
        temp5.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");    // currentTimeMillis로 바꿀예정
        temp5.put("outsiderReason", "정기 휴가 9일 + 혹한기 유공 4일");

        temp6.put("type", "관심");
        temp6.put("checked", true);
        temp6.put("class", "이병");
        temp6.put("name", "유성우");
        temp6.put("outsiderDuring", "2018.10.10 ~ 2018.10.23");    // currentTimeMillis로 바꿀예정
        temp6.put("outsiderReason", "정기 휴가 9일 + 혹한기 유공 4일");

        this.outsiderManagementList.add(temp1);
        this.outsiderManagementList.add(temp2);
        this.outsiderManagementList.add(temp3);
        this.outsiderManagementList.add(temp4);
        this.outsiderManagementList.add(temp5);
        this.outsiderManagementList.add(temp6);
    }

    private void bindUI(View view)
    {
        // Bind views
        this.outsiderManagementListView = view.findViewById(R.id.outsider_management_listView);
        this.adapter = new OutsiderManagementListAdapter(getActivity(), R.layout.outsider_management_item, this.outsiderManagementList);
        this.selectCheckBox = view.findViewById(R.id.select_checkBox);
        this.addFloatingBtn = view.findViewById(R.id.add_floatingBtn);
        this.removeFloatingBtn = view.findViewById(R.id.remove_floatingBtn);

        // Set attributes
        this.outsiderManagementListView.setAdapter(this.adapter);

        // Add Events
        this.selectCheckBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("selectCheckBox", "Clicked : ");
                checkBoxSwitch(((CheckBox) v).isChecked());
            }
        });

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
//                        setResult();
                        outsiderManagementList.add((HashMap<String, Object>) obj);
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

                    if( (boolean) map.get("checked") )
                    {
                        iter.remove();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void checkBoxSwitch(boolean flag)
    {
        for( HashMap<String, Object> map : outsiderManagementList )
        {
            map.put("checked", flag);
//            (this.adapter.getItem())selectCheckBox.setChecked(flag);
        }
    }
}