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
    private long timeLeftInMillis = 25*60*1000;
    private CountDownTimer timer;



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
        if(!isRunning){
            timer = new CountDownTimer(timeLeftInMillis, 100) {
                @Override
                public void onTick(long millistUntilFinished) {
                    timeLeftInMillis =millistUntilFinished;
                    updateTimerText();

                }

                @Override
                public void onFinish() {
                    isRunning=false;
                    updateTimerText();
                    binding.timerTextView.setText("Tiempo terminado!");


                }
            }.start();

            isRunning=true;

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
        int minutes = (int) (timeLeftInMillis / 1000) / 60; // Minutos
        int seconds = (int) (timeLeftInMillis / 1000) % 60; // Segundos
        int milliseconds = (int) (timeLeftInMillis % 1000) / 10; // Milisegundos (dividido por 10 para mostrar solo 2 dígitos)

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milliseconds);
        binding.timerTextView.setText(timeFormatted);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}