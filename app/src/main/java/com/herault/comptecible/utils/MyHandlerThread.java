package com.herault.comptecible.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.FileProvider;

import com.herault.comptecible.BuildConfig;
import com.herault.comptecible.Resultat_archer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

public class MyHandlerThread extends HandlerThread {
    private final WeakReference<Context> weakReferenceContext;
    private final WeakReference<ProgressBar> progressBarWeakReference;
    private WeakReference<List<Resultat_archer>> lresultat;

    // 1 - Constructor
    public MyHandlerThread(Context context, String name, ProgressBar progressBar) {
        super(name);
        progressBarWeakReference = new WeakReference<>(progressBar);
        weakReferenceContext = new WeakReference<>(context);
    }

    // 2 - Public method that will start handler
    public void startHandler(List<Resultat_archer> lResultat, String name) {

        lresultat = new WeakReference<>(lResultat);
        // 2.1 - Checking if progressbar is accessible, and setting it visible
        ProgressBar progressBar = progressBarWeakReference.get();
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        // 2.2 - Checking if handlerThread is already alive, else we start it.
        if (!this.isAlive()) this.start();

        // 2.3 - Creating a new Handler and setting it the looper of handlerThread
        Handler handler = new Handler(this.getLooper());

        // 2.4 - Executing a new Runnable
        handler.post(() -> {
            Context context = weakReferenceContext.get();
            List<Resultat_archer> results = lresultat.get();

            if (context == null || results == null) {
                return;
            }

            File file = new File(context.getExternalFilesDir(""), name + ".csv");
            try (OutputStream os = new FileOutputStream(file)) {

                double ratio = results.size() / 100.0;
                int i = 0;
                for (Resultat_archer r : results) {
                    String formCsv = r.getName() + "," + r.getValue() +
                            "," + r.getY() + "," + r.getX() + "\n";
                    os.write(formCsv.getBytes());
                    i++;
                    if (ratio > 0) {
                        double current = i / ratio;
                        if (0 == (current - ((int) Math.round(current)))) {
                            ProgressBar pb = progressBarWeakReference.get();
                            if (pb != null) {
                                pb.setProgress((int) Math.round(current));
                            }
                        }
                    }
                }

                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, name);
                intent.setType("*/*");
                context.startActivity(Intent.createChooser(intent, "Share Via").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            } catch (IOException e) {
                // Unable to create file, likely because external storage is
                // not currently mounted.
                Log.w("Storage", "Error writing " + file, e);
            }

            // 2.6 - Update UI after task finished (In Main Thread)
            new Handler(Looper.getMainLooper()).post(() -> {
                ProgressBar pb = progressBarWeakReference.get();
                if (pb != null) {
                    pb.setVisibility(View.GONE);
                }
            });
        });
    }
}
