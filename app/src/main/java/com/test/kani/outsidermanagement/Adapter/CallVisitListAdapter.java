package com.test.kani.outsidermanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.kani.outsidermanagement.R;
import com.test.kani.outsidermanagement.Utilitiy.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class CallVisitListAdapter extends BaseAdapter
{
    TextView classTextView, nameTextView, reportDateTextView, outsiderDuringTextView;
    ImageButton telImageButton;
    LinearLayout callVisitItemLinearLayout;

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> list;
    private int layout;

    public CallVisitListAdapter(Context context, int layout, ArrayList<HashMap<String, Object>> list)
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
        this.callVisitItemLinearLayout = ViewHolder.get(view, R.id.call_visit_item_linear_layout);
        this.classTextView = ViewHolder.get(view, R.id.class_textView);
        this.nameTextView = ViewHolder.get(view, R.id.name_textView);
        this.reportDateTextView = ViewHolder.get(view, R.id.report_date_textView);
        this.outsiderDuringTextView = ViewHolder.get(view, R.id.outsider_during_textView);
        this.telImageButton = ViewHolder.get(view, R.id.tel_imageBtn);

        // Set attributes
        if ("관심".equals(this.list.get(i).get("outsiderType").toString().trim()))
            this.callVisitItemLinearLayout.setBackgroundColor(Color.parseColor("#FB6F53"));
        else if ("배려".equals(this.list.get(i).get("outsiderType").toString().trim()))
            this.callVisitItemLinearLayout.setBackgroundColor(Color.parseColor("#FFF2CC"));
        else if ("일반".equals(this.list.get(i).get("outsiderType").toString().trim()))
            this.callVisitItemLinearLayout.setBackgroundColor(Color.parseColor("#A9D18E"));

        this.classTextView.setText(this.list.get(i).get("class").toString().trim());
        this.nameTextView.setText(this.list.get(i).get("name").toString().trim());
        this.reportDateTextView.setText(this.list.get(i).get("reportDate").toString().trim());
        this.outsiderDuringTextView.setText(this.list.get(i).get("outsiderDuring").toString().trim());

        // Add Events
        this.telImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("telImageButton", "Clicked : " + list.get(i).get("tel").toString());

                Intent intent  = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + list.get(i).get("tel").toString()));

                context.startActivity(intent);
            }
        });

        return view;
    }
}
