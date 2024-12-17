package com.example.androidstudytimeapp.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidstudytimeapp.R;
import com.example.androidstudytimeapp.databinding.FragmentTimerBinding;

import java.util.Locale;


public class FragmentTimer extends Fragment {

    FragmentTimerBinding binding;
    private boolean isRunning;
    private long timeLeftInMillis = 30 * 1000;
    private CountDownTimer timer;
    private int sessionCount=0;
    private static final long STUDY_TIME = 30 * 1000;// 25 minutos
    private static final long SHORT_BREAK_TIME = 30 * 1000; // 5 minutos
    private static final long LONG_BREAK_TIME = 30 * 1000; // 15 minutos



    public FragmentTimer() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTimerBinding.inflate(inflater, container, false);


        binding.startButton.setOnClickListener(v->startTimer());
        binding.pauseButton.setOnClickListener(v->pauseTimer());
        binding.resetButton.setOnClickListener(v->resetTimer());

        updateTimerText();

        return binding.getRoot();
    }

    private void startTimer() {
        if (!isRunning) {
            timer = new CountDownTimer(timeLeftInMillis, 10) { // Intervalo de 10ms para precisión
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    isRunning = false;
                    sessionCount++;

                    // Determinar la siguiente fase
                    if (sessionCount % 8 == 0) { // Descanso largo después de 4 estudios
                        timeLeftInMillis = LONG_BREAK_TIME;
                        binding.timerTypeTextView.setText("LONG BREAK TIME");
                    } else if (sessionCount % 2 == 0) { // Descanso corto después de cada estudio
                        timeLeftInMillis = SHORT_BREAK_TIME;
                        binding.timerTypeTextView.setText("SHORT BREAK TIME");
                    } else { // Fase de estudio
                        timeLeftInMillis = STUDY_TIME;
                        binding.timerTypeTextView.setText("TIME TO STUDY");
                    }
                    startTimer(); // Iniciar automáticamente la siguiente fase
                }
            }.start();
            isRunning = true;
        }
    }

    private void pauseTimer() {
        if(isRunning){
            timer.cancel();
            isRunning=false;
        }
    }

    private void resetTimer() {
        if(isRunning){
            timer.cancel();
            isRunning=false;
            timeLeftInMillis = 25*60*1000;
            updateTimerText();

        }
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        int milliseconds = (int) (timeLeftInMillis % 1000) / 10;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milliseconds);
        binding.timerTextView.setText(timeFormatted);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}