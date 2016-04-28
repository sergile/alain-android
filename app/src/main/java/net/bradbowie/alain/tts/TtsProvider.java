package net.bradbowie.alain.tts;

/**
 * Created by bradbowie on 4/27/16.
 */
public interface TtsProvider {
    void say(String toSay);

    void dramaticPause(long millis);

    void stopSpeaking();

    void destroy();
}
