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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class OutsiderAddDialog extends BaseDialog
{
    RadioGroup typeRadioGroup;
    EditText startDateEditText, endDateEditText, outsiderReasonEditText;
    ListView memberListView;
    Button registBtn, cancelBtn;

    MemberListAdapter adapter;
    private Context context;
    private ArrayList<HashMap<String, Object>> memberList;
    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    private HashMap<String, Object> addFactorMap;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }


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

        if( loadingDialog == null )
            loadingDialog = new LoadingDialog(getContext());

        loadingDialog.show("MemberList Loading");

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
                        Log.d("OutsiderAddDialog", "Task is not successful");
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
                    Log.d("OutsiderAddDialog", "memberList is not found");
                    memberList = new ArrayList<>();
                }
                else if( obj != null && obj instanceof  String )
                {
                    addFactorMap.put("documentId", obj.toString());
                    addFactorMap.put("outsiderDuring", addFactorMap.get("startDate").toString() + " ~ "
                            + addFactorMap.get("endDate").toString());
                    addFactorMap.put("checked", false);
                    updateComponentListener.updateComponent(addFactorMap);
                    dismiss();
                }
                else
                {
                    memberList = (ArrayList<HashMap<String, Object>>) obj;

                    for( HashMap<String, Object> map : memberList )
                    {
                        Set<String> keys = map.keySet();
                        Iterator<String> iter = keys.iterator();

                        while( iter.hasNext() )
                        {
                            String key = iter.next();

                            if( map.get(key) == null )
                                map.put(key, "");
                        }

                        map.put("checked", false);
                    }
                }
                Collections.sort(memberList, new Comparator<HashMap<String, Object>>()
                {
                    @Override
                    public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2)
                    {
                        return o1.get("name").toString().compareTo(o2.get("name").toString());
                    }
                });

                bindUI();
            }
        });

        FireStoreConnectionPool.getInstance().select(fireStoreCallbackListener,
                "member", "supervisorId", MainActivity.myInfoMap.get("id").toString());
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
                if( (boolean) memberList.get(position).get("checked") )
                {
                    memberList.get(position).put("checked", false);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                else
                {
                    for (int i = 0; i < memberList.size(); ++i)
                    {
                        if (parent.getChildAt(i) != null)
                            parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                        memberList.get(i).put("checked", false);
                    }

                    memberList.get(position).put("checked", true);
                    view.setBackgroundColor(Color.parseColor("#FFF2CC"));
                }
            }
        });

        this.registBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("registBtn", "Clicked : ");

                loadingDialog.show("Outsider Adding");

                addFactorMap = new HashMap<> ();

                addFactorMap.put("type", getType());
                addFactorMap.put("startDate", startDateEditText.getText().toString().trim());
                addFactorMap.put("endDate", endDateEditText.getText().toString().trim());
                addFactorMap.put("outsiderReason", outsiderReasonEditText.getText().toString().trim());
                addFactorMap.put("reportDate", null);

                Iterator<HashMap<String, Object>> iter = memberList.iterator();

                while( iter.hasNext() )
                {
                    HashMap<String, Object> tempMap = iter.next();

                    if( (boolean) tempMap.get("checked") )
                    {
                        addFactorMap.put("class", tempMap.get("class").toString().trim());
                        addFactorMap.put("name", tempMap.get("name").toString().trim());
                        addFactorMap.put("memberId", tempMap.get("documentId").toString().trim());
                        addFactorMap.put("tel", tempMap.get("tel").toString().trim());
                        addFactorMap.put("supervisorId", MainActivity.myInfoMap.get("id").toString());
                        break;
                    }
                }

                if( addFactorMap.containsKey("memberId") )
                    FireStoreConnectionPool.getInstance().insertNoID(fireStoreCallbackListener, addFactorMap, "outsider");
                else
                {
                    Toast.makeText(context, "출타자를 선택하세요.", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
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
