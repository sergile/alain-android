package net.bradbowie.alain.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import net.bradbowie.alain.util.LOG;
import net.bradbowie.alain.util.StringUtils;

/**
 * Created by bradbowie on 4/27/16.
 */
public class SystemTts implements TtsProvider, TextToSpeech.OnInitListener {

    private static final String TAG = LOG.tag(SystemTts.class);

    private boolean ready;
    private TextToSpeech tts;

    public SystemTts(Context context) {
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int i) {
        ready = TextToSpeech.SUCCESS == i;

        if(!ready) {
            LOG.e(TAG, "Failed to initialize system TTS. Err code: " + i);
        }
    }

    @Override
    public void say(String toSay) {
        if(ready && tts != null && StringUtils.isValid(toSay)) {
            tts.speak(toSay, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    public void dramaticPause(long millis) {
        if(ready && tts != null && millis > 0) {
            tts.playSilence(millis, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    public void stopSpeaking() {
        if(tts != null && tts.isSpeaking()) tts.stop();
    }

    @Override
    public void destroy() {
        ready = false;

        if(tts != null) {
            tts.shutdown();
            tts = null;
        }
    }
}
