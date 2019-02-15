package spencerstudios.com.testingground;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.medialablk.easygifview.EasyGifView;

import spencerstudios.com.testingground.Constants.Const;
import spencerstudios.com.testingground.Utils.SaveFile;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private int currProgBar = 0, id = 0;
    private ProgressBar[] progressBars = new ProgressBar[3];
    private ImageView playButtons [] = new ImageView[3];
    private CountDownTimer cdt;

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initProgressUpdater() {

        final int total = Const.track_lengths[currProgBar];

        cdt = new CountDownTimer(total, Const.UPDATE_AUDIO_PROGRESS_DELAY) {
            @Override
            public void onTick(long t) {
                progressBars[currProgBar].setProgress(total - (int) t);
            }

            @Override
            public void onFinish() {
                progressBars[currProgBar].setProgress(Const.track_lengths[currProgBar]);
                //resetProgBars();
                Log.d("COUNTDOWN TIMER", "onFinish() called");
            }
        };

        cdt.start();
    }

    private void initViews() {

        EasyGifView gifView = findViewById(R.id.easyGifView);
        gifView.setGifFromResource(R.drawable.ed_gif);

        for (int i = 0; i < progressBars.length; i++) {
            progressBars[i] = findViewById(Const.progBarIds[i]);
            progressBars[i].setProgress(0);
            progressBars[i].setMax(Const.track_lengths[i]);
            playButtons[i] = findViewById(Const.play_btn_ids[i]);
        }
    }

    public void btnPlay(View v) {

        for (int i = 0; i < Const.selected_track_view_id.length; i++) {
            if (v.getId() == Const.selected_track_view_id[i]) {
                currProgBar = i;
                playButtons[i].startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.play_anim));
                break;
            }
        }

        resetProgBars();

        playAudio(Const.resource_ids[currProgBar]);
    }

    private void resetProgBars() {
        for (ProgressBar pb : progressBars) {
            pb.setProgress(0);
        }
    }

    private void playAudio(int res_id) {

        if (mp != null) {
            mp.stop();
            mp.reset();
            mp.release();
        }

        mp = MediaPlayer.create(this, res_id);

        if (mp != null) {
            if (cdt != null) {
                cdt.cancel();
                cdt = null;
            }
            mp.start();

            initProgressUpdater();
        }
    }

    @Override
    protected void onPause() {

        resetProgBars();

        if (mp != null) {
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }

        if (cdt != null) {
            cdt.cancel();
            cdt = null;
        }
        super.onPause();
    }

    public void save(View v) {

        switch (v.getId()) {
            case R.id.iv_res_two_save:
                id = 1;
                break;
            case R.id.iv_res_three_save:
                id = 2;
                break;
            default:
                id = 0;
        }

        if(checkPermission()) initSaveAudio();
        else requestPermission();

    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission is required, " +
                    "please enable in app settings", Toast
                    .LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted");
                    initSaveAudio();
                } else {
                    Log.e("value", "Permission Denied");
                }
                break;
        }
    }

    private void initSaveAudio(){
        SaveFile saveFile = new SaveFile(this, Const.resource_ids[id]);
        saveFile.run();
    }
}
