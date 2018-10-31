package com.test.kani.outsidermanagement.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.kani.outsidermanagement.R;
import com.test.kani.outsidermanagement.Utilitiy.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class OutsiderManagementListAdapter extends BaseAdapter
{
    TextView classTextView, nameTextView, outsiderDuringTextView, outsiderReasonTextView;
    CheckBox selectCheckBox;
    LinearLayout outsiderManagementItemLinearLayout;

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;
    private int layout;

    public OutsiderManagementListAdapter(Context context, int layout, ArrayList<HashMap<String, Object>> list)
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
        this.outsiderManagementItemLinearLayout = ViewHolder.get(view, R.id.outsider_management_item_linear_layout);
        this.selectCheckBox = ViewHolder.get(view, R.id.select_checkBox);
        this.classTextView = ViewHolder.get(view, R.id.class_textView);
        this.nameTextView = ViewHolder.get(view, R.id.name_textView);
        this.outsiderDuringTextView = ViewHolder.get(view, R.id.outsider_during_textView);
        this.outsiderReasonTextView = ViewHolder.get(view, R.id.outsider_reason_textView);

        // Set attributes
        if ("관심".equals(this.list.get(i).get("outsiderType").toString().trim()))
            this.outsiderManagementItemLinearLayout.setBackgroundColor(Color.parseColor("#FB6F53"));
        else if ("배려".equals(this.list.get(i).get("outsiderType").toString().trim()))
            this.outsiderManagementItemLinearLayout.setBackgroundColor(Color.parseColor("#FFF2CC"));
        else if ("일반".equals(this.list.get(i).get("outsiderType").toString().trim()))
            this.outsiderManagementItemLinearLayout.setBackgroundColor(Color.parseColor("#A9D18E"));

        if( this.list.get(i).get("checked") instanceof Boolean )
        {
            this.selectCheckBox.setChecked((boolean) this.list.get(i).get("checked"));
            this.selectCheckBox.setVisibility(CheckBox.VISIBLE);
        }
        else
            this.selectCheckBox.setVisibility(CheckBox.GONE);

        this.classTextView.setText(this.list.get(i).get("class").toString().trim());
        this.nameTextView.setText(this.list.get(i).get("name").toString().trim());
        this.outsiderDuringTextView.setText(this.list.get(i).get("outsiderDuring").toString().trim());
        this.outsiderReasonTextView.setText(this.list.get(i).get("outsiderReason").toString().trim());

        // Add Events
        this.selectCheckBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("selectCheckBox", "Clicked : ");
                list.get(i).put("checked", ((CheckBox) v).isChecked());
            }
        });

        return view;
    }
}
