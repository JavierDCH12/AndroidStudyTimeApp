package com.example.androidstudytimeapp.UI;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidstudytimeapp.R;
import com.example.androidstudytimeapp.databinding.FragmentTimerBinding;

import java.util.Locale;


public class FragmentTimer extends Fragment {

    FragmentTimerBinding binding;
    private boolean isRunning;
    private long timeLeftInMillis;
    private CountDownTimer timer;
    private int sessionCount = 1;

    private int studyTime;
    private int shortBreakTime;
    private int longBreakTime;

    public FragmentTimer() {
        // Constructor vacío
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recibir los tiempos desde el Bundle
        if (getArguments() != null) {
            studyTime = getArguments().getInt("studyTime", 25) * 60 * 1000; // Convertir a milisegundos
            shortBreakTime = getArguments().getInt("shortBreakTime", 5) * 60 * 1000; // Convertir a milisegundos
            longBreakTime = getArguments().getInt("longBreakTime", 15) * 60 * 1000; // Convertir a milisegundos
            Log.d("FragmentTimer", "Estudio: " + studyTime + " minutos");
            Log.d("FragmentTimer", "Descanso corto: " + shortBreakTime + " minutos");
            Log.d("FragmentTimer", "Descanso largo: " + longBreakTime + " minutos");
        }

        Log.d("FragmentTimer", "Estudio: " + studyTime + " minutos");
        Log.d("FragmentTimer", "Descanso corto: " + shortBreakTime + " minutos");
        Log.d("FragmentTimer", "Descanso largo: " + longBreakTime + " minutos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater, container, false);

        // Inicializar con la fase de estudio (asegúrate de que el primer paso sea siempre estudio)
        setPhase(studyTime, "TIME TO STUDY", R.color.study_color);

        binding.startButton.setOnClickListener(v -> startTimer());
        binding.pauseButton.setOnClickListener(v -> pauseTimer());
        binding.resetButton.setOnClickListener(v -> resetTimer());

        return binding.getRoot();
    }

    private void startTimer() {
        if (!isRunning) {
            binding.progressBar.setMax((int) timeLeftInMillis);
            timer = new CountDownTimer(timeLeftInMillis, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    binding.progressBar.setProgress((int) timeLeftInMillis);
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    isRunning = false;
                    sessionCount++;

                    ObjectAnimator pulse = ObjectAnimator.ofFloat(binding.progressBar, "scaleX", 1f, 1.1f, 1f);
                    pulse.setRepeatCount(3);
                    pulse.setRepeatMode(ValueAnimator.REVERSE);
                    pulse.setDuration(300);
                    pulse.start();

                    handleNextPhase();
                }
            }.start();
            isRunning = true;
        }
    }

    private void vibratePhone(int mili) {
        Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(mili);
        }
    }

    private void vibratePhonePattern() {
        Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 200, 200, 200, 100, 100};
            vibrator.vibrate(pattern, -1);
        }
    }

    private void handleNextPhase() {
        if (sessionCount % 8 == 0) {
            setPhase(longBreakTime, "LONG BREAK TIME", R.color.long_break_color);
            vibratePhone(2000);

            showEndOfCycle();
        } else if (sessionCount % 2 == 0) {
            setPhase(shortBreakTime, "SHORT BREAK TIME", R.color.short_break_color);
            vibratePhone(1000);
        } else { // Fase de estudio
            setPhase(studyTime, "TIME TO STUDY", R.color.study_color);
            vibratePhonePattern();
        }

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(binding.timerTypeTextView, "alpha", 0f, 1f);
        fadeIn.setDuration(600); // Duración del fade-in
        fadeIn.start();

        startTimer();
    }

    private void setPhase(long phaseTime, String phaseTitle, int colorRes) {
        timeLeftInMillis = phaseTime;
        binding.timerTypeTextView.setText(phaseTitle);

        int color = ContextCompat.getColor(requireContext(), colorRes);
        binding.progressBar.setProgressTintList(ColorStateList.valueOf(color));
        binding.timerTypeTextView.setTextColor(color);

        binding.progressBar.setMax((int) phaseTime);
        binding.progressBar.setProgress((int) phaseTime);
        updateTimerText();
    }

    public void updateTimerSettings() {
        Log.d("FragmentTimer", "Estudio: " + studyTime + " minutos");
        Log.d("FragmentTimer", "Descanso corto: " + shortBreakTime + " minutos");
        Log.d("FragmentTimer", "Descanso largo: " + longBreakTime + " minutos");

        timeLeftInMillis = studyTime;  // Establecer el tiempo inicial del temporizador
        updateTimerText();
    }

    private void pauseTimer() {
        if (isRunning) {
            timer.cancel();
            isRunning = false;
        }
    }

    private void resetTimer() {
        if (isRunning) {
            timer.cancel();
            isRunning = false;
        }
        sessionCount = 0;
        setPhase(studyTime, "TIME TO STUDY", R.color.study_color);
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        binding.timerTextView.setText(timeFormatted);
    }

    private void showEndOfCycle() {
        pauseTimer();

        new AlertDialog.Builder(requireContext())
                .setTitle("Ciclo de estudio completado")
                .setMessage("Has completado 4 sesiones de estudio. ¿Quieres seguir o acabar por hoy?")
                .setPositiveButton("Estudiar", (dialog, which) -> {
                    sessionCount = 0;
                    timeLeftInMillis = studyTime;
                    updateTimerText();
                    startTimer();
                })
                .setNegativeButton("Acabar por hoy", (dialog, which) -> {
                    binding.timerTextView.setText("¡Bien hecho! Estudio finalizado por hoy.");
                    timeLeftInMillis = 0;
                    updateTimerText();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
