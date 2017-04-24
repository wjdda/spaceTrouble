package com.divanstudio.spaceTrouble;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseIntArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WJ_DDA on 22.04.2017.
 */

public class SoundManager {
    // TODO 1) Сейчас музыка играется в треде и там запускается цикл по поиску трека в менеджере
    // TODO Можно оптимизировать поиск, если использовать State здесь или в треде.

    // TODO 2) Можно попробовать реализовать SFX так же как и все остальные проигрыватели.

    private static volatile SoundManager instance;

    /** Проигрыватель звуковых эффектов*/
    private SoundPool sfxPlayer = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    private float sfx_vol = 0.6f;

    /** Cловарь ресурсов для SFX. Формат: {"Метка звука": added_res_id} */
    private Map<String, Integer> sfxSources = new HashMap<>();

    /** Хэш стримов активных звуков, которые играются. Формат: {res_id, stream_id} */
    private SparseIntArray sfxStreams = new SparseIntArray();

    /** Словарь фоновых звуковых эффектов (AmbienceSFX). Формат: {"Метка звука": MediaPlayer} */
    // Особенность этого плейлиста в том, что можно играть несколько звуков одновременно
    private Map<String, MediaPlayer> sfxAmbiencePlaylist = new HashMap<>();
    private float sfx_ambience_vol = 0.4f;

    /** Словарь музыкальных композиций. Формат: {"Метка звука": MediaPlayer} */
    // Особенность этого плейлиста в том, что играется только один музыкальный трек
    private Map<String, MediaPlayer> musicPlaylist = new HashMap<>();
    private float music_vol = 1.0f;

    private static final String TAG = SoundManager.class.getSimpleName();


    // Задаём класс как Singleton
    public static SoundManager getInstance() {
        SoundManager localInstance = instance;
        if (localInstance == null) {
            synchronized (SoundManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SoundManager();
                }
            }
        }
        return  localInstance;
    }


    // Конструктор
    private SoundManager () {}


    /* Методы SFX плеера */
    // Загружает звук в плеер и записываем в хэш ресурсов его ID
    public void addSFX (Context context, String soundName, int id_res) {
        try {
            this.sfxSources.put(soundName, this.sfxPlayer.load(context, id_res, 1));
            Log.i(TAG, "SFX Sound " + soundName + " added");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    // Задаёт громкость SFX
    public void setVolumeSFX (float vol) {
        this.sfx_vol = vol;
        Log.i(TAG, "SFX Sound volume setted = " + this.sfx_vol);
    }


    // Проигрывает SFX звук
    public void playSFX (String soundName, int loops_count) {
        // Играем звук и запоминаем stream_id
        int sounds_stream_id = this.sfxPlayer.play(
                this.sfxSources.get(soundName), this.sfx_vol, this.sfx_vol, 0, loops_count, 1.0f
        );

        // Складываем проигрываемые звуки в хэш стримов, если они циклические
        if (loops_count == -1) {
            this.sfxStreams.append(this.sfxSources.get(soundName), sounds_stream_id);
        }

        Log.i(TAG, "SFX Sound " + soundName + " starts playing");
    }


    // Останавливает проигрывание SFX звука и удаляет его из хэша стримов
    public void stopSFX (String soundName) {
        this.sfxPlayer.stop(this.sfxStreams.get(this.sfxSources.get(soundName)));
        this.sfxStreams.delete(this.sfxSources.get(soundName));

        Log.i(TAG, "SFX Sound " + soundName + " stops playing");
    }


    // Очистка SFX плеера
    public void clearSFX () {
        if (this.sfxPlayer != null) {
            // Остановка всех стримов
            for (int i = 0; i < this.sfxStreams.size(); i++) {
                this.sfxPlayer.stop(this.sfxStreams.get(i));
            }

            // Освобождение ресурсов SFX плеера
            this.sfxPlayer.release();
            this.sfxPlayer = null;
        }

        // Очистка хэша стримов
        this.sfxStreams.clear();

        // Очистка хэша ресурсов
        this.sfxSources.clear();

        Log.i(TAG, "SFX player destroyed");
    }
    /* -------- */


    /* Методы AmbienceSFX плеера */
    // Добавляет плеер звука с ресурсом в плейлист
    public void addAmbienceSFX (Context context, String soundName, int id_res) {
        this.sfxAmbiencePlaylist.put(soundName, MediaPlayer.create(context, id_res));

        Log.i(TAG, "Ambience SFX Sound " + soundName + " added");
    }


    // Устанавливает громкость фоновых звуков
    public void setAmbienceVolumeSFX (float volume) { this.sfx_ambience_vol = volume; }


    // Играет фоновый звук
    public void playAmbienceSFX(String soundName) {
        this.sfxAmbiencePlaylist.get(soundName).setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.sfxAmbiencePlaylist.get(soundName).setLooping(true);
        this.sfxAmbiencePlaylist.get(soundName).setVolume(this.sfx_ambience_vol, this.sfx_ambience_vol);
        this.sfxAmbiencePlaylist.get(soundName).start();

        Log.i(TAG, "Ambience SFX Sound " + soundName + " playing");
    }


    // Останавливает фоновый звук
    public void stopAmbienceSFX(String soundName) {
        if (this.sfxAmbiencePlaylist.get(soundName).isPlaying()) {
            // Останавливаем проигрыватель
            this.sfxAmbiencePlaylist.get(soundName).stop();

            // Подготавливаем проигрыватель
            try {
                this.sfxAmbiencePlaylist.get(soundName).prepare();
            }
            catch (IOException e) {
                Log.e(TAG, "Opening resource error");
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                Log.e(TAG, "Illegal State of player");
                e.printStackTrace();
            }

            // Трек ставим сначала
            this.sfxAmbiencePlaylist.get(soundName).seekTo(0);
        }
    }


    // Останавливает все фоновые звуки
    public void stopAllAmbienceSFX () {
        for (String Key : this.sfxAmbiencePlaylist.keySet()) {
            if (this.sfxAmbiencePlaylist.get(Key).isPlaying()) {
                // Останавливаем проигрыватель
                this.sfxAmbiencePlaylist.get(Key).stop();

                // Подготавливаем проигрыватель
                try {
                    this.sfxAmbiencePlaylist.get(Key).prepare();
                }
                catch (IOException e) {
                    Log.e(TAG, "Opening resource error");
                    e.printStackTrace();
                }
                catch (IllegalStateException e) {
                    Log.e(TAG, "Illegal State of player");
                    e.printStackTrace();
                }

                // Трек ставим сначала
                this.sfxAmbiencePlaylist.get(Key).seekTo(0);
            }
        }
    }
    /* -------- */


    /* Методы музыкального плеера */
    // Добавляет трек с ресурсом в плейлист
    public void addMusic (Context context, String soundName, int id_res) {
        this.musicPlaylist.put(soundName, MediaPlayer.create(context, id_res));

        Log.i(TAG, "Music track " + soundName + " added");
    }


    // Играет трек
    public void playMusic (String soundName) {
        // Останавливаем любой играющийся трек
        this.stopMusic();

        this.musicPlaylist.get(soundName).setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.musicPlaylist.get(soundName).setLooping(true);
        this.musicPlaylist.get(soundName).setVolume(this.music_vol, this.music_vol);
        this.musicPlaylist.get(soundName).start();

        Log.i(TAG, "Music track " + soundName + " playing");
    }


    // Устанавливает громкость музыки
    public void setMusicVolume (float volume) { this.music_vol = volume; }


    // Останавливает музыку
    public void stopMusic () {
        for (String Key : this.musicPlaylist.keySet()) {
            if (this.musicPlaylist.get(Key).isPlaying()) {
                // Останавливаем проигрыватель
                this.musicPlaylist.get(Key).stop();

                // Подготавливаем проигрыватель
                try {
                    this.musicPlaylist.get(Key).prepare();
                }
                catch (IOException e) {
                    Log.e(TAG, "Opening resource error");
                    e.printStackTrace();
                }
                catch (IllegalStateException e) {
                    Log.e(TAG, "Illegal State of player");
                    e.printStackTrace();
                }

                // Трек ставим сначала
                this.musicPlaylist.get(Key).seekTo(0);
            }
        }
    }


    // Паузит музыку
    public void pauseMusic() {
        for (String Key : this.musicPlaylist.keySet()) {
            if (this.musicPlaylist.get(Key).isPlaying()) {
                // Останавливаем проигрыватель
                this.musicPlaylist.get(Key).pause();
            }
        }
    }
    // Глобально ищет звуки во всех данных менеджера.
    // Если находит, играет звук и возвращает 0. Или возвращает 1, в случае отстутствия звука
    // Опции старта проигрывания:
    //   is_reset         - Сбросить проигрывание звука.
    //                      true  - начинает играть найденный звук заново.
    //                      false - игнорирует звук, если он играется
    //   is_solo_ambient  - Является ли звук окружения одиночным.
    //                      true  - Проигрыватель фоновых звуков играет только назначенный звук.
    //                              При этом останавливаются все звуки данного проигрывателя.
    //                      false - Проигрыватель играет фоновый звук без прерывания останльных.
    public int play(String soundName, boolean is_reset, boolean is_solo_ambient) {
        // Поиск в хэше SFX. Играем нужный звук.
        for (String Key : this.sfxSources.keySet()) {
            if (soundName.equals(Key)) {
                if (!is_reset) {
                    // Поиск в хэше активных стримов. Если стрим найден, не нужно играть SFX
                    if (this.sfxStreams.get(this.sfxSources.get(Key), -1) != -1) { return 0; }
                }

                Log.i(TAG, "Playing " + Key + " SFX sound");

                this.playSFX(Key, 0);

                return 0;
            }
        }

        // Поиск в хэше AmbienceSFX. Запоминаем найденный звук
        String foundKey = "";
        for (String Key : this.sfxAmbiencePlaylist.keySet()) {
            // Если найшли нужный звук, ничего с ним не делаем...
            if (soundName.equals(Key)) {
                foundKey = Key;
                Log.i(TAG, "Found " + Key + " ambience SFX sound");
                continue;
            }

            // ... остальные звуки останавливаем, если задан solo
            if (is_solo_ambient) { this.stopAmbienceSFX(Key); }
        }

        // Если звук нашли, играем его
        if (foundKey.length() > 0) {
            // Если звук окружения играется и его не нужно сбрасывать, то ничего не делаем
            if (!is_reset && this.sfxAmbiencePlaylist.get(foundKey).isPlaying()) { return 0; }

            // Если звук нужно сбросить, когда он играется, сбрасываем
            if (is_reset && this.sfxAmbiencePlaylist.get(foundKey).isPlaying()) {
                this.playAmbienceSFX(foundKey);
            }

            // Если звук не играется - играем его
            if (!this.sfxAmbiencePlaylist.get(foundKey).isPlaying()) {
                Log.i(TAG, "Playing " + foundKey + " ambience SFX");
                this.playAmbienceSFX(foundKey);
            }

            return 0;
        }

        // Поиск в хэше MusicSFX
        foundKey = "";
        for (String Key : this.musicPlaylist.keySet()) {
            if (soundName.equals(Key)) {
                foundKey = Key;
                Log.i(TAG, "Found " + Key + " music track");
            }
        }

        // Если трек нашли, играем его
        if (foundKey.length() > 0) {
            // Если музыкальный трек играется и его не нужно сбрасывать, то ничего не делаем
            if (!is_reset && this.musicPlaylist.get(foundKey).isPlaying()) { return 0; }

            // Если музыкальный трек нужно сбросить, когда он играется, сбрасываем
            if (is_reset && this.musicPlaylist.get(foundKey).isPlaying()) {
                this.playAmbienceSFX(foundKey);
            }

            // Если музыкальный трек не играется - играем его
            if (!this.musicPlaylist.get(foundKey).isPlaying()) {
                Log.i(TAG, "Playing " + foundKey + " music track");

                // Останавливаем музыку в любом случае
                this.stopMusic();

                // Играем назначенный музыкальный трек
                this.playMusic(foundKey);
            }

            return 0;
        }

        return 1;
    }


    public int play(String soundName, boolean is_reset) {
        return this.play(soundName, is_reset, false);
    }


    public int play(String soundName) {
        return this.play(soundName, false);
    }


    // Глобально ищет звук.
    // Если находит, останавливает звук и возвращает 0. Или возвращает 1, в случае отстутствия звука
    public int stop(String soundName) {
        // Поиск в хэше SFX
        for (String Key : this.sfxSources.keySet()) {
            if (soundName.equals(Key)) {
                // Поиск в хэше активных стримов. Если стрим не найден, не нужно останавливать звук
                if (this.sfxStreams.get(this.sfxSources.get(Key), -1) == -1) { return 0; }

                this.stopSFX(Key);

                // Удаляем стрим звука
                this.sfxStreams.delete(this.sfxSources.get(Key));

                return 0;
            }
        }

        // Поиск в хэше AmbienceSFX
        for (String Key : this.sfxAmbiencePlaylist.keySet()) {
            if (soundName.equals(Key)) {
                // Если звук окружения не играется, его не нужно останавливать
                if (!this.sfxAmbiencePlaylist.get(Key).isPlaying()) { return 0; }

                this.stopAmbienceSFX(Key);

                return 0;
            }
        }

        // Поиск в хэше MusicSFX
        for (String Key : this.musicPlaylist.keySet()) {
            if (soundName.equals(Key)) {
                // Если музыка не играется, её не нужно останавливать
                if (!this.musicPlaylist.get(Key).isPlaying()) { return 0; }

                this.stopMusic();

                return 0;
            }
        }

        return 1;
    }

    // Очищает все ресурсы
    public void allClear() {
        this.clearSFX();

        this.sfxAmbiencePlaylist = null;
        this.musicPlaylist       = null;
    }
/* -------- */
}
