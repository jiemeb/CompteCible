package com.herault.comptecible.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;


import androidx.core.content.FileProvider;


import com.herault.comptecible.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;


public class shareImage {
   private final Context c;
    public shareImage(Context c) {this.c = c;   }

    public void shareInt(View  view, String msg, String name) {

        Bitmap bitmap = getScreenShot(view) ;
        Uri uri = getmageToShare(bitmap, name);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("*/*");
        c.startActivity(Intent.createChooser(intent, "Share Via").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap, String name) {

    //    String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Screenshots";
    //    File imagefolder = new File(str);

        String str = Environment.getExternalStorageDirectory()+ "/Pictures/Screenshots";
        File imagefolder = new File(str);
     //  File imagefolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"Screenshot");
        Uri uri = null;

        try {
            if (! imagefolder.exists()) {
                imagefolder.mkdirs();
                imagefolder.setWritable(true);
            }

            File file = new File(imagefolder, name+".png");
            file.setWritable(true);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(c.getApplicationContext(),   BuildConfig.APPLICATION_ID + ".fileprovider",file);

        } catch (Exception e) {
            Toast.makeText(c, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
    public static Bitmap getScreenShot(View view) {
        View rootView = view.getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return createBitmap;
    }

    // For share sreenshot  -- Permission check

}

