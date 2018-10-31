package com.test.kani.outsidermanagement.Utilitiy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class LoadingDialog
{
    private Context context = null;
    private ProgressDialog progDialog;

    public LoadingDialog(Context context)
    {
        this.context = context;
        this.progDialog = new ProgressDialog(context);
        this.progDialog.setIndeterminate(true);
        this.progDialog.setCancelable(true);
        this.progDialog.setCanceledOnTouchOutside(false);
        this.progDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {

            }
        });
    }

    public boolean isShowing()
    {
        return this.progDialog.isShowing();
    }

    public void show(String msg)
    {
        if( this.context != null )
        {
            this.progDialog.setMessage(msg);
            this.progDialog.show();
        }
    }

    public void dismiss()
    {
        if( this.context != null )
            this.progDialog.dismiss();
    }
}
