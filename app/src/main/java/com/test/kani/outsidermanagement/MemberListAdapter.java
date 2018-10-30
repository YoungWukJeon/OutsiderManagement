package com.test.kani.outsidermanagement;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberListAdapter extends BaseAdapter
{
    TextView classTextView, idTextView, nameTextView;
    LinearLayout memberItemLinearLayout;

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;
    private int layout;

    public MemberListAdapter(Context context, int layout, ArrayList<HashMap<String, Object>> list)
    {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return this.list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        if( view == null )
        {
            this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = this.inflater.inflate(this.layout, viewGroup, false);
        }

        // Bind views
        this.memberItemLinearLayout = ViewHolder.get(view, R.id.member_item_linear_layout);
        this.classTextView = ViewHolder.get(view, R.id.class_textView);
        this.idTextView = ViewHolder.get(view, R.id.id_textView);
        this.nameTextView = ViewHolder.get(view, R.id.name_textView);

        if( (boolean) this.list.get(i).get("checked") )
            this.memberItemLinearLayout.setBackgroundColor(Color.parseColor("#FFF2CC"));
        else
            this.memberItemLinearLayout.setBackgroundColor(Color.TRANSPARENT);

        this.classTextView.setText(this.list.get(i).get("class").toString().trim());
        this.idTextView.setText(this.list.get(i).get("documentId").toString().trim());
        this.nameTextView.setText(this.list.get(i).get("name").toString().trim());

        // Add Events
//        this.memberItemLinearLayout.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Log.d("memberItemLinearLayout", "Clicked : " + i);
//
//
//            }
//        });

        return view;
    }

}
