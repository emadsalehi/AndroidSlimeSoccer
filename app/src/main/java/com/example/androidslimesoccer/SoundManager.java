package com.example.androidslimesoccer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundManager extends Activity
{
    static SoundPool soundPool;
    static int[] sm;

    public static void InitSound(Context context) {

        int maxStreams = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(maxStreams)
                    .build();
        } else {
            soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 10);
        }

        sm = new int[1];
        // fill your sounds
        sm[0] = soundPool.load(context, R.raw.post_hit, 1);
//        sm[1] = soundPool.load(context, R.raw.sound_2, 1);
//        sm[2] = soundPool.load(context, R.raw.sound_3, 1);
    }

    public static void playSound(int sound, float volume) {
        soundPool.play(sm[sound], volume, volume, 1, 0, 1f);
    }

    public final void cleanUpIfEnd() {
        sm = null;
        soundPool.release();
        soundPool = null;
    }
}