package com.herault.comptecible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.herault.comptecible.utils.ExportAsyncTask;
import com.herault.comptecible.utils.Stockage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.lang.Thread.sleep;


public class Activity_maintenance extends AppCompatActivity implements ExportAsyncTask.Listeners {

    protected Activity context;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ProgressBar progressBarExport;
    private Stockage stock = null;
    private Spinner archer = null;
    private Spinner round = null;
    private ArrayAdapter adapter;
    private ArrayAdapter adapterRound;

    private ExportAsyncTask task;
    long archer_id;
    private Activity_maintenance localActivity;
    private List<Resultat_archer> lresultat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localActivity = this;
        setContentView(R.layout.activity_maintenance);

   /*     if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        } else {
            // Code for Below 23 API Oriented Device
            // Do next code
        } */


        progressBarExport = findViewById(R.id.am_progressBar);

        archer = findViewById(R.id.am_sArcher);
        stock = new Stockage();             // init de la classe interface de stockage
        stock.onCreate(this);

        List<String> lArcher = stock.getArchers(false);
        adapter = new ArrayAdapter(
                this,
                R.layout.spinner_generale);

        for (int i = 0; i < lArcher.size(); i++) {
            adapter.add(lArcher.get(i));
        }
        adapter.setDropDownViewResource(R.layout.spinner_generale);
        archer.setAdapter(adapter);

// Suppress Archer

        Button bSuppresArcher = findViewById(R.id.am_bSuppresArcher);
        bSuppresArcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (archer.getCount() != 0) {
                    String name = archer.getSelectedItem().toString();

                    adapter.remove(archer.getSelectedItem());
                    stock.dropArcher(name);
                } else {
                    Intent i = new Intent(Activity_maintenance.this, Activity_config_round.class);
                    startActivity(i);
                    Activity_maintenance.this.finish();
                }
            }
        });

        Button bCleaDataBase = findViewById(R.id.am_bCleaDataBase);
        bCleaDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupValidation = new AlertDialog.Builder(localActivity);
                popupValidation.setMessage(getResources().getString(R.string.baseAlertClean));
                popupValidation.setTitle(getResources().getString(R.string.baseClean));

                popupValidation.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stock.dropArchers(false);
                        Intent j = new Intent(Activity_maintenance.this, Activity_config_round.class);
                        startActivity(j);
                        Activity_maintenance.this.finish();
                    }
                });
                popupValidation.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                popupValidation.show();

            }
        });
        // export in file Round for all archers

        Button bExportRoundArchers = findViewById(R.id.am_bexport_round_archers);
        bExportRoundArchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // test Archer
                if (round.getCount() != 0 && round.getSelectedItemId() >= 0) {
                    // 3 - We create and start our AsyncTask
                    String[] argv = new String[]{round.getSelectedItem().toString()};
                    lresultat = stock.getResultatAll(argv[0]);
                    task = new ExportAsyncTask(Activity_maintenance.this);
                    task.execute(argv);

                }
            }
        });

// Export resultat for one archer for a round

        Button bExportRoundArcher = findViewById(R.id.am_bexport_round_archer);
        bExportRoundArcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // test Archer
                if (round.getCount() != 0 && round.getSelectedItemId() >= 0 && archer.getCount() != 0 && archer.getSelectedItemId() >= 0) {

                    String[] argv = new String[]{archer.getSelectedItem().toString() + "_" + round.getSelectedItem().toString()};
                    lresultat = stock.getResultatArrows(archer.getSelectedItem().toString(), round.getSelectedItem().toString());
                    task = new ExportAsyncTask(Activity_maintenance.this);
                    task.execute(argv);

                }
            }
        });



// export in file Archer for all Rounds

        Button bExportArcherRounds = findViewById(R.id.am_bexport_archer_rounds);
        bExportArcherRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // test Archer
                if (archer.getCount() != 0 && archer.getSelectedItemId() >= 0) {
                    // 3 - We create and start our AsyncTask
                    String[] argv = new String[]{archer.getSelectedItem().toString()};
                    lresultat = stock.getResultatAllRound(argv[0]);
                    task = new ExportAsyncTask(Activity_maintenance.this);
                    task.execute(argv);

                }
            }
        });


//-------------------------------------------------------------------------------------------------
        round = findViewById(R.id.sRound);
        List<String> lRound = stock.getRounds();
        adapterRound = new ArrayAdapter(
                this,
                R.layout.spinner_generale
        );

        for (int i = 0; i < lRound.size(); i++) {
            adapterRound.add(lRound.get(i));
        }
        adapterRound.setDropDownViewResource(R.layout.spinner_generale);
        round.setAdapter(adapterRound);

        Button bSuppressRound = findViewById(R.id.am_bSup_round);
        bSuppressRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round.getCount() != 0) {
                    String name = round.getSelectedItem().toString();

                    adapterRound.remove(round.getSelectedItem());
                    stock.supRound(name);
                } else {
                    Intent i = new Intent(Activity_maintenance.this, Activity_config_round.class);
                    startActivity(i);
                    Activity_maintenance.this.finish();
                }
            }
        });

    }
    //-------------------------------------------------------------------
    // -------------------------------------------
    // BACKGROUND TASK (HandlerThread & AsyncTask)
    // -------------------------------------------


    // ------


    // 2 - Override methods of callback
    @Override
    public void onPreExecute() {
        // 2.1 - We update our UI before task (starting ProgressBar)
        this.updateUIBeforeTask();
    }

    @Override
    public Long doInBackground(String... name) {

        // Create a path where we will place our private file on external
        // storage.
        File file = new File(getExternalFilesDir(""), name[0] + ".csv");


        try {
            OutputStream os = new FileOutputStream(file);
            //          byte[] data = new byte[is.available()];

            String formCsv = "";
            Double current;
            Double ratio = lresultat.size() / 100.;
            int i = 0;
            for (Resultat_archer r : lresultat) {
                formCsv = r.getName() + "," + r.getValue() +
                        "," + r.getY() + "," + r.getX() + "\n\r";
                os.write(formCsv.getBytes());
                i++;
                current = i / ratio;
                if (0 == (current - ((int) Math.round(current)))) {
                    task.myPublishProgress((int) Math.round(current));
                }
            }

            os.close();
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);

        }
        return 0L;
    }

    @Override
    public void onProgressUpdate(Integer... values) {
        progressBarExport.setProgress(values[0]);
    }

    @Override
    public void onPostExecute(Long taskEnd) {
        // 2.2 - We update our UI before task (stopping ProgressBar)
        this.updateUIAfterTask(taskEnd);
    }

    // -----------------
    // UPDATE UI
    // -----------------

    private void updateUIBeforeTask() {
        progressBarExport.setVisibility(View.VISIBLE);
    }

    private void updateUIAfterTask(Long taskEnd) {
        progressBarExport.setVisibility(View.GONE);
        //   Toast.makeText(this, "Task is finally finished at : " + taskEnd + " !", Toast.LENGTH_SHORT).show();
    }

    //-----------------fin call back ExportASyncTAsk
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Activity_maintenance.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Activity_maintenance.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Activity_maintenance.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Activity_maintenance.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

//---------------------------------------

    /*********************************************************************************/
    /** Managing LifeCycle and database open/close operations ************************/
    /*********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        // Open stockage
        stock.openDB();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close stockage
        stock.closeDB();
    }


}
