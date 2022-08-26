package com.herault.comptecible.utils;


import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.FileProvider;

import com.herault.comptecible.Activity_maintenance;
import com.herault.comptecible.BuildConfig;
import com.herault.comptecible.Resultat_archer;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

public class MyHandlerThread extends HandlerThread {
    private WeakReference<Context> WeakReferenceContext ;
    private WeakReference<ProgressBar> progressBarWeakReference;
    private WeakReference<List<Resultat_archer>> lresultat;
    // 1 - Constructor
    public MyHandlerThread(Context Context,String name, ProgressBar progressBar) {
        super(name);
        progressBarWeakReference = new WeakReference<>(progressBar);
        WeakReferenceContext= new WeakReference<>(Context);
    }

    // 2 - Public method that will start handler
    public void startHandler(List <Resultat_archer> lResultat,String name){

        lresultat = new WeakReference <>(lResultat);
        // 2.1 - Checking if progressbar is accessible, and setting it visible
        if (progressBarWeakReference.get() != null) progressBarWeakReference.get().setVisibility(View.VISIBLE);

        // 2.2 - Checking if handlerThread is already alive, else we start it.
        if (!this.isAlive()) this.start();

        // 2.3 - Creating a new Handler and setting it the looper of handlerThread
        Handler handler = new Handler(this.getLooper());

        // 2.4 - Executing a new Runnable
        handler.post(new Runnable(){
            @Override
            public void run() {


                //     File file = new File(getExternalFilesDir(""), name[0] + ".csv");
                File file;
                  //  file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), name + ".csv");
                file = new File(WeakReferenceContext.get().getExternalFilesDir(""),name + ".csv");
                try {
                    OutputStream os = new FileOutputStream(file);
                    //          byte[] data = new byte[is.available()];

                    String formCsv;
                    double current;
                    double ratio = lresultat.get().size() / 100.;
                    int i = 0;
                    for (Resultat_archer r : lresultat.get()) {
                        formCsv = r.getName() + "," + r.getValue() +
                                "," + r.getY() + "," + r.getX() + "\n";
                        os.write(formCsv.getBytes());
                        i++;
                        current = i / ratio;
                        if (0 == (current - ((int) Math.round(current)))) {
                            progressBarWeakReference.get().setProgress((int) Math.round(current));
                        }
                    }
                    os.close();
                    Uri uri =  FileProvider.getUriForFile(WeakReferenceContext.get(),   BuildConfig.APPLICATION_ID + ".fileprovider",file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_TEXT, name);
                    intent.setType("*/*");
                    WeakReferenceContext.get().startActivity(Intent.createChooser(intent, "Share Via").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));



                } catch (IOException e) {
                    // Unable to create file, likely because external storage is
                    // not currently mounted.
                    Log.w("Storage", "Error writing " + file, e);

                }

                // 2.6 - Update UI after task finished (In Main Thread)
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (progressBarWeakReference.get() != null) progressBarWeakReference.get().setVisibility(View.GONE);
                });
            }
        });
    }
}