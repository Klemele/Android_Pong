package icelab.pong;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

public class SoundMeter {

    private MediaRecorder recorder = null;
    private int minSize;

    public void start() {
        try {
            String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/audiorecordtest.3gp";

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(mFileName);
            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("MediaRecorder", "prepare() failed");
            }
            recorder.start();   // Recording is now started
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public int getAmplitude() {
        return recorder.getMaxAmplitude();
    }

}
