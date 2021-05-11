package com.techroid.timer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView timer;
    EditText type;
    TextView history;
    private String mTime,mType;
    private int sec = 0;
    private boolean isRunning;
    private boolean wasRunning;
    public static  final String SHARED_PREF = "Shared Pref";
    public static  final String TIME ="Time";
    public static  final String TYPE ="Type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        mTime  = prefs.getString(TIME, "00:00");
        mType  = prefs.getString(TYPE,"push ups");
        setContentView(R.layout.activity_main);
        timer= findViewById(R.id.tvTimer);
        type = findViewById(R.id.edtType);
        history = findViewById(R.id.tvHistory);
        history.setText("You spent "+mTime+" on "+mType+" last time.");

        if (savedInstanceState != null)
        {

            sec = savedInstanceState.getInt("seconds");
            isRunning = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        startTimer();
    }

    public void onPlayClick(View view) {
        isRunning = true;
    }

    public void onPauseClick(View view) {
        isRunning = false;
    }

    public void onStopClick(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("Stopping Timer!");
        dialog.setMessage("Do you want to stop the timer");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                isRunning = false;
                sec = 0;
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString(TIME,timer.getText().toString());
                editor.putString(TYPE,type.getText().toString());
                editor.commit();

            }
        });

        dialog.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        }).show();


    }
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = isRunning;
        isRunning = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            isRunning = true;
        }
    }
    private void startTimer()
    {
        final TextView timer = findViewById(R.id.tvTimer);

        final Handler hd = new Handler();

        hd.post(new Runnable() {
            @Override

            public void run()
            {
                int hours_var = sec / 3600;
                int minutes_var = (sec % 3600) / 60;
                int secs_var = sec % 60;

                String time_value = String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours_var, minutes_var, secs_var);

                timer.setText(time_value);

                if (isRunning)
                {
                    sec++;
                }

                hd.postDelayed(this, 1000);
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", sec);
        savedInstanceState.putBoolean("running", isRunning);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }
}