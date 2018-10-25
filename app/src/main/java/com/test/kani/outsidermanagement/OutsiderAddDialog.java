package com.test.kani.outsidermanagement;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OutsiderAddDialog extends BaseDialog
{
    RadioGroup typeRadioGroup;
    EditText startDateEditText, endDateEditText, outsiderReasonEditText;
    ListView memberListView;
    Button registBtn, cancelBtn;

    MemberListAdapter adapter;
    private Context context;
    private ArrayList<HashMap<String, Object>> memberList;


    public OutsiderAddDialog(Context context, int themeResId)
    {
        super(context, themeResId);
        this.context = context;
    }

    public void setDialogSetting()
    {
        this.setContentView(R.layout.outsider_add_dialog);
        this.initMemberList();
        this.bindUI();
    }

    private void initMemberList()
    {
        Log.d("OutsiderAddDialog", "Initialize member list");
        this.memberList = new ArrayList<>();

        HashMap<String, Object> temp1 = new HashMap<> ();
        HashMap<String, Object> temp2 = new HashMap<> ();
        HashMap<String, Object> temp3 = new HashMap<> ();
        HashMap<String, Object> temp4 = new HashMap<> ();
        HashMap<String, Object> temp5 = new HashMap<> ();
        HashMap<String, Object> temp6 = new HashMap<> ();

        temp1.put("class", "상병");
        temp1.put("id", "17-72001242");
        temp1.put("name", "유성우");
        temp1.put("checked", false);

        temp2.put("class", "병장");
        temp2.put("id", "17-72001242");
        temp2.put("name", "폭풍우");
        temp2.put("checked", false);

        temp3.put("class", "상병");
        temp3.put("id", "17-72001242");
        temp3.put("name", "유성우");
        temp3.put("checked", false);

        temp4.put("class", "일병");
        temp4.put("id", "17-72001242");
        temp4.put("name", "빛돌");
        temp4.put("checked", false);

        temp5.put("class", "이병");
        temp5.put("id", "17-72001242");
        temp5.put("name", "구운몽");
        temp5.put("checked", false);

        temp6.put("class", "이병");
        temp6.put("id", "17-72001242");
        temp6.put("name", "아무개");
        temp6.put("checked", false);

        this.memberList.add(temp1);
        this.memberList.add(temp2);
        this.memberList.add(temp3);
        this.memberList.add(temp4);
        this.memberList.add(temp5);
        this.memberList.add(temp6);
    }

    private void bindUI()
    {
        // Bind views
        this.typeRadioGroup = findViewById(R.id.type_radioGroup);
        this.startDateEditText = findViewById(R.id.start_date_editText);
        this.endDateEditText = findViewById(R.id.end_date_editText);
        this.outsiderReasonEditText = findViewById(R.id.outsider_reason_editText);
        this.memberListView = findViewById(R.id.member_listView);
        this.registBtn = findViewById(R.id.regist_btn);
        this.cancelBtn = findViewById(R.id.cancel_btn);
        this.adapter = new MemberListAdapter(getContext(), R.layout.member_item, this.memberList);

        // Set attributes
        this.memberListView.setAdapter(this.adapter);

        // Add events
        this.memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                for( int i = 0; i < memberList.size(); ++i )
                {
                    if( parent.getChildAt(i) != null )
                        parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                    memberList.get(i).put("checked", false);
                }

                memberList.get(position).put("checked", true);
                view.setBackgroundColor(Color.parseColor("#FFF2CC"));
            }
        });

        this.registBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("registBtn", "Clicked : ");

                HashMap<String, Object> map = new HashMap<> ();
                map.put("type", getType());
                map.put("startDate", startDateEditText.getText().toString().trim());
                map.put("endDate", endDateEditText.getText().toString().trim());
                map.put("outsiderReason", outsiderReasonEditText.getText().toString().trim());

                Iterator<HashMap<String, Object>> iter = memberList.iterator();

                while( iter.hasNext() )
                {
                    HashMap<String, Object> tempMap = iter.next();

                    if( (boolean) tempMap.get("checked") )
                    {
                        map.put("class", tempMap.get("class").toString().trim());
                        map.put("name", tempMap.get("name").toString().trim());
                        break;
                    }
                }

                Log.d("Regist Member", map.get("type").toString() + ", " + map.get("startDate").toString() + ", " +
                        map.get("endDate").toString() + ", " + map.get("outsiderReason").toString() + ", " +
                        map.get("class").toString() + ", " + map.get("name").toString());

                dismiss();
            }
        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("cancelBtn", "Clicked : ");
                dismiss();
            }
        });
    }

    private String getType()
    {
        switch( this.typeRadioGroup.getCheckedRadioButtonId() )
        {
            case R.id.option1:
                return "일반";
            case R.id.option2:
                return "배려";
            case R.id.option3:
                return "관심";
        }

        return null;
    }
}
