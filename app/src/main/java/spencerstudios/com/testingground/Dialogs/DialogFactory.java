package spencerstudios.com.testingground.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import spencerstudios.com.testingground.R;

public class DialogFactory {

    public static void SavedDialog(Context ctx, String filePath){

        final AlertDialog builder = new AlertDialog.Builder(ctx).create();
        @SuppressLint("InflateParams") View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_file_saved, null);
        TextView tvPath = v.findViewById(R.id.tv_file_path);
        builder.setView(v);

        tvPath.setText(filePath);

        builder.setButton(DialogInterface.BUTTON_POSITIVE, "okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.dismiss();
            }
        });

        builder.show();
    }
}
