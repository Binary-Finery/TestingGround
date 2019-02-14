package spencerstudios.com.testingground;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.medialablk.easygifview.EasyGifView;

import spencerstudios.com.testingground.Constants.Const;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp;


    int currPos, prog = 0, currProgBar = 0;

    ProgressBar[] progressBars = new ProgressBar[3];

    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initProgressUpdater() {

        cdt = new CountDownTimer(Const.track_lengths[currProgBar], Const.UPDATE_AUDIO_PROGRESS_DELAY) {
            @Override
            public void onTick(long t) {
                progressBars[currProgBar].setProgress((int) t);
            }

            @Override
            public void onFinish() {
                progressBars[currProgBar].setProgress(0);
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
        }
    }

    public void btnPlay(View v) {

        for (int i = 0; i < Const.selected_track_view_id.length; i++) {
            if (v.getId() == Const.selected_track_view_id[i]) {
                currProgBar = i;
                break;
            }
        }

        resetProgBars();

        currPos = 0;
        prog = 0;

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

        if(cdt!=null){
            cdt.cancel();
            cdt = null;
        }
        super.onPause();
    }
}
