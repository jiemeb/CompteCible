package com.herault.comptecible.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.core.content.FileProvider;

import com.herault.comptecible.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;


public class shareImage {
    final Context c;

    public shareImage(Context c) {
        this.c = c;
    }

    public void shareInt(View view, String msg, String name) {

        Bitmap bitmap = getScreenShot(view);
        Uri uri = getmageToShare(bitmap, name);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("*/*");
        c.startActivity(Intent.createChooser(intent, "Share Via").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap, String name) {

        File imagefolder = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Uri uri = null;

        try {
            if (imagefolder != null && !imagefolder.exists()) {
                if (!imagefolder.mkdirs()) {
                    Log.e("shareImage", "Failed to create directory: " + imagefolder);
                }
            }

            File file = new File(imagefolder, name + ".png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(c.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);

        } catch (Exception e) {
            String error = "Erreur acces: verifier les droits de l'application";
            Toast.makeText(c, error + e.getMessage(), Toast.LENGTH_LONG).show();
            error = "ou supprimer le fichier " + imagefolder + name;
            Toast.makeText(c, error, Toast.LENGTH_LONG).show();
        }
        return uri;
    }
    public static Bitmap getScreenShot(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);

        view.draw(canvas);
        return bitmap;
    }

    // For share sreenshot  -- Permission check

}

