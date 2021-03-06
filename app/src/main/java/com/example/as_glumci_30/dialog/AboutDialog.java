package com.example.as_glumci_30.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.as_glumci_30.R;


public class AboutDialog extends AlertDialog.Builder {

    public AboutDialog(Context context) {
        super(context);
        setTitle( R.string.aboutDijalog);
        setMessage("App name: Glumci\nby: Adrijana Savic");
        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public AlertDialog prepareDialog(){
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
