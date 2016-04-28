package net.bradbowie.alain.generator;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.bradbowie.alain.R;
import net.bradbowie.alain.SimpleSubscriber;
import net.bradbowie.alain.databinding.GeneratorActivityBinding;
import net.bradbowie.alain.model.Idea;
import net.bradbowie.alain.model.PartialIdea;
import net.bradbowie.alain.tts.SystemTts;
import net.bradbowie.alain.tts.TtsProvider;
import net.bradbowie.alain.util.LOG;
import net.bradbowie.alain.util.SerializationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class GeneratorActivity extends AppCompatActivity {
    private static final String TAG = LOG.tag(GeneratorActivity.class);

    private static final String BUNDLE_GENERATED_IDEAS = "generated_ideas";
    private static final String BUNDLE_TTS_ENABLED = "tts_enabled";

    private static final long CONFIG_IDEA_INTERVAL = 4000;
    private static final long CONFIG_IDEA_INTERVAL_INTIAL = 2500;
    private static final long CONFIG_INTRO_DRAMATIC_PAUSE = 1000;

    private GeneratorActivityBinding binding;
    private IdeaListAdapter adapter;
    private Subscription generatorSub;
    private IdeaGenerator generator;
    private TtsProvider tts;
    private String ttsIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.generator_activity);
        initToolbar();
        initList();
        initGenerator();
    }

    private void initToolbar() {
        setSupportActionBar(binding.generatorToolbar);
    }

    private void initList() {
        adapter = new IdeaListAdapter();
        adapter.setHeaderText(getString(R.string.generator_default_header_text));
        binding.generatorIdeaList.setAdapter(adapter);
        binding.generatorIdeaList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initGenerator() {
        generator = new IdeaGenerator();
        generator.setPartialIdeas(getDefaultPartialIdeals());
        generator.setColors(getDefaultColors());
    }

    private List<PartialIdea> getDefaultPartialIdeals() {
        String json;
        try {
            InputStream is = getAssets().open("words.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            LOG.e(TAG, "Failed to load assets", ex);
            return null;
        }

        return SerializationUtils.fromJsonToListSafe(json, PartialIdea.class);
    }

    private List<Integer> getDefaultColors() {
        return Arrays.asList(
                ContextCompat.getColor(this, R.color.c_red),
                ContextCompat.getColor(this, R.color.c_orange),
                ContextCompat.getColor(this, R.color.c_yellow),
                ContextCompat.getColor(this, R.color.c_green),
                ContextCompat.getColor(this, R.color.c_blue),
                ContextCompat.getColor(this, R.color.c_indigo),
                ContextCompat.getColor(this, R.color.c_violet)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        onGeneratorStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyTts();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(BUNDLE_GENERATED_IDEAS, adapter.getGreatestIdeasEver());
        savedInstanceState.putBoolean(BUNDLE_TTS_ENABLED, tts != null);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        List<Idea> previouslyGenerated = savedInstanceState.getParcelableArrayList(BUNDLE_GENERATED_IDEAS);
        adapter.addAllIdeas(previouslyGenerated);

        if(savedInstanceState.getBoolean(BUNDLE_TTS_ENABLED)) initTts();

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_generator, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_generator_enable_tts).setChecked(tts != null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_generator_clear_all:
                onClearGeneratedIdeas();
                return true;
            case R.id.menu_generator_enable_tts:
                if(tts == null) {
                    initTts();
                    item.setChecked(true);
                } else {
                    destroyTts();
                    item.setChecked(false);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initTts() {
        if(tts == null) {
            ttsIntro = getString(R.string.generator_default_header_text);
            tts = new SystemTts(this);
        }
    }

    private void destroyTts() {
        if(tts != null) {
            tts.destroy();
            tts = null;
        }
    }

    public void onPlayClick(View v) {
        onGeneratorStart();
    }

    public void onPauseClick(View v) {
        onGeneratorStop();
    }

    public void onGeneratorStart() {
        if (generatorSub != null && !generatorSub.isUnsubscribed()) {
            LOG.i(TAG, "Generator already started");
            return;
        }

        generatorSub = Observable.interval(CONFIG_IDEA_INTERVAL_INTIAL, CONFIG_IDEA_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        onGeneratorStarted();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        onGeneratorStopped();
                    }
                })
                .subscribe(new SimpleSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        onNextIdea(generator.next());
                    }
                });
    }

    private void onGeneratorStarted() {
        binding.generatorPauseButton.setVisibility(View.VISIBLE);
        binding.generatorPlayButton.setVisibility(View.GONE);

        if(tts != null) tts.say(ttsIntro);

        adapter.setGenerating(true);
    }

    private void onNextIdea(Idea i) {
        adapter.addIdea(i);

        if(tts != null) {
            tts.say(i.getLeft() + " " + i.getRight());
            tts.dramaticPause(CONFIG_INTRO_DRAMATIC_PAUSE);
            tts.say(ttsIntro);
        }
    }

    public void onGeneratorStop() {
        if (generatorSub != null && !generatorSub.isUnsubscribed()) {
            generatorSub.unsubscribe();

            if(tts != null) tts.stopSpeaking();
        }
    }

    private void onGeneratorStopped() {
        binding.generatorPauseButton.setVisibility(View.GONE);
        binding.generatorPlayButton.setVisibility(View.VISIBLE);

        adapter.setGenerating(false);
    }

    private void onClearGeneratedIdeas() {
        adapter.clearIdeas();
    }
}
