package spencerstudios.com.testingground;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.medialablk.easygifview.EasyGifView;

import spencerstudios.com.testingground.Constants.Const;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private TextView tv;

    Handler handler;
    Runnable runnable;

    int phrase_segments = 0;
    boolean phraseHasFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EasyGifView easyGifView = findViewById(R.id.easyGifView);
        tv = findViewById(R.id.tv);
        easyGifView.setGifFromResource(R.drawable.ed_gif);

        handler = new Handler();
    }

    public void btnPlay(View view) {
        playAudio();
    }

    private void playAudio() {

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        mp = MediaPlayer.create(this, Const.audio_resource_id);

        if (mp != null) {
            phrase_segments = 0;
            phraseHasFinished = false;

            if (handler != null) {
                handler.removeCallbacks(runnable);
            }

            initPhraseTextSequence();

            mp.start();
        }
    }

    @Override
    protected void onPause() {

        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }

        super.onPause();
    }

    void initPhraseTextSequence() {

        runnable = new Runnable() {
            @Override
            public void run() {

                if (!phraseHasFinished) {

                    handler.postDelayed(this, Const.DELAYS[phrase_segments]);

                    tv.setText(Const.ED_209_PHRASE[phrase_segments]);

                    phrase_segments++;

                    if (phrase_segments == Const.ED_209_PHRASE.length) {
                        phrase_segments = 0;
                        phraseHasFinished = true;
                        handler.removeCallbacks(this);
                    }
                }
            }
        };
        handler.post(runnable);
    }
}
