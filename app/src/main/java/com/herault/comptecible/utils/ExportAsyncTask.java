package com.herault.comptecible.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.R;

import java.lang.ref.WeakReference;
import java.util.function.LongToIntFunction;

import static java.lang.Thread.sleep;

public class ExportAsyncTask extends AsyncTask<String, Integer, Long> {

    private final WeakReference<Listeners> callback;

    // Constructor
    public ExportAsyncTask(Listeners callback) {
        this.callback = new WeakReference<>(callback);
    }

    public void myPublishProgress(int i) {
        publishProgress(i);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.callback.get().onPreExecute(); // Call the related callback method
    }

    @Override
    protected void onPostExecute(Long success) {
        super.onPostExecute(success);
        this.callback.get().onPostExecute(success); // Call the related callback method

    }

    @Override
    protected Long doInBackground(String... name) {
        return this.callback.get().doInBackground(name); // Call the related callback method
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        this.callback.get().onProgressUpdate(values);

    }


    public interface Listeners {
        void onPreExecute();

        Long doInBackground(String... name);

        void onPostExecute(Long success);

        void onProgressUpdate(Integer... values);
    }
}