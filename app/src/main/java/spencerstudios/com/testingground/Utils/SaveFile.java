package spencerstudios.com.testingground.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import spencerstudios.com.testingground.Dialogs.DialogFactory;

public class SaveFile extends Thread {

    private Context ctx;
    private File sdcard;
    private String dir = "/ed-209";
    private int res_id;

    public SaveFile(Context context, int res_id) {
        ctx = context;
        sdcard = Environment.getExternalStorageDirectory();
        this.res_id = res_id;
    }

    public void run() {
        File file = new File(sdcard + dir);
        if (!file.exists()) {
            boolean mkd = file.mkdirs();
            Log.d("DIR_CREATED", String.valueOf(mkd));
        }
        copyResources(res_id);
    }

    private void copyResources(int resId) {
        InputStream in = ctx.getResources().openRawResource(resId);
        String filename = ctx.getResources().getResourceEntryName(resId);

        File f = new File(filename);

        if (!f.exists()) {
            try {
                OutputStream out = new FileOutputStream(new File(sdcard + dir, filename));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();

                DialogFactory.SavedDialog(ctx, sdcard + dir + File.separator + f.getPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
