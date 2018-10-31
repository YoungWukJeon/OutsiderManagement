package com.test.kani.outsidermanagement.Utilitiy;

import android.app.Dialog;
import android.content.Context;

public class BaseDialog extends Dialog
{
    protected UpdateComponentListener updateComponentListener;

    public BaseDialog(Context context, int themeResId)
    {
        super(context, themeResId);
        this.setCancelable(false);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        dismiss();
    }

    public void setUpdateComponentListener(UpdateComponentListener listener)
    {
        this.updateComponentListener = listener;
    }

    public interface UpdateComponentListener
    {
        void updateComponent(Object obj);
    }
}
