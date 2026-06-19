package com.herault.comptecible.utils;

import android.content.Context;
import android.util.Log;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.herault.comptecible.BuildConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class DatabaseBackup {

    public static File getBackupFile(Context context) {
        File exportDir = context.getExternalFilesDir(null);
        if (exportDir != null && !exportDir.exists()) exportDir.mkdirs();
        return new File(exportDir, "backup_" + Db_resultat.Constants.DATABASE_NAME);
    }

    public static boolean backupDatabase(Context context) {
        try {
            File dbFile = context.getDatabasePath(Db_resultat.Constants.DATABASE_NAME);
            File backupFile = getBackupFile(context);

            if (dbFile.exists()) {
                try (FileChannel src = new FileInputStream(dbFile).getChannel();
                     FileChannel dst = new FileOutputStream(backupFile).getChannel()) {
                    dst.transferFrom(src, 0, src.size());
                }
                return true;
            }
        } catch (Exception e) {
            Log.e("DatabaseBackup", "Error during backup", e);
        }
        return false;
    }

    public static void shareBackup(Context context) {
        if (backupDatabase(context)) {
            File backupFile = getBackupFile(context);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", backupFile);
            
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/x-sqlite3");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            context.startActivity(Intent.createChooser(intent, "Sauvegarder vers Drive, Email, etc."));
        }
    }

    public static boolean restoreDatabase(Context context, Uri sourceUri) {
        try {
            File dbFile = context.getDatabasePath(Db_resultat.Constants.DATABASE_NAME);

            try (InputStream is = context.getContentResolver().openInputStream(sourceUri);
                 FileOutputStream fos = new FileOutputStream(dbFile)) {
                
                if (is == null) return false;

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("DatabaseBackup", "Error during restore from Uri", e);
        }
        return false;
    }

    public static boolean restoreDatabase(Context context) {
        try {
            File exportDir = context.getExternalFilesDir(null);
            File backupFile = new File(exportDir, "backup_" + Db_resultat.Constants.DATABASE_NAME);
            File dbFile = context.getDatabasePath(Db_resultat.Constants.DATABASE_NAME);

            if (backupFile.exists()) {
                try (FileChannel src = new FileInputStream(backupFile).getChannel();
                     FileChannel dst = new FileOutputStream(dbFile).getChannel()) {
                    dst.transferFrom(src, 0, src.size());
                }
                return true;
            }
        } catch (Exception e) {
            Log.e("DatabaseBackup", "Error during restore", e);
        }
        return false;
    }
}
