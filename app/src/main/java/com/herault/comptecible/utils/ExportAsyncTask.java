package com.herault.comptecible.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.R;

import java.util.function.LongToIntFunction;

import static java.lang.Thread.sleep;

public class ExportAsyncTask extends AsyncTask<String, Integer, Long> {
    private AppCompatActivity myActivity;

    public ExportAsyncTask(AppCompatActivity a) {
        myActivity = a;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressBar pb =  myActivity.findViewById(R.id.am_progressBar);
        pb.setVisibility(View.VISIBLE);
        Log.e("TAG", "AsyncTask is started.");
    }

    @Override
    protected void onPostExecute(Long success) {
        super.onPostExecute(success);
        ProgressBar pb =  myActivity.findViewById(R.id.am_progressBar);
        pb.setVisibility(View.GONE);


        Log.e("TAG", "AsyncTask is finished.");
    }

    @Override
    protected Long doInBackground(String... name) {
        Long j = 0L;
        for (int i = 0; i < 100; i++) {
            publishProgress(i);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Log.e("TAG", "AsyncTask doing some big work...");
        // 5 - Execute our task
        return 0L;
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar pb =  myActivity.findViewById(R.id.am_progressBar);
        pb.setProgress(values[0]);
    }
}