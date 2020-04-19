package com.herault.comptecible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.utils.ExportAsyncTask;
import com.herault.comptecible.utils.Stockage;

import java.util.List;


public class Activity_maintenance extends AppCompatActivity {

    protected Activity context;
    private Button bSuppresArcher;
    private Button bCleaDataBase;
    private Button bSuppressRound;
    private Button bExportArcherRounds;
    private ProgressBar progressBarExport;
    private Stockage stock = null;
    private Spinner archer = null;
    private Spinner round = null;
    private List<String> lRound;
    private ArrayAdapter adapter;
    private ArrayAdapter adapterRound;

    private List<String> lArcher;
    long archer_id;
    private Activity_maintenance localActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localActivity = this;
        setContentView(R.layout.activity_maintenance);
        progressBarExport = findViewById(R.id.am_progressBar);

        archer = findViewById(R.id.am_sArcher);
        stock = new Stockage();             // init de la classe interface de stockage
        stock.onCreate(this);

        lArcher = stock.getArchers(false);
        adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item
        );

        for (int i = 0; i < lArcher.size(); i++) {
            adapter.add(lArcher.get(i));
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        archer.setAdapter(adapter);

// Suppress Archer

        bSuppresArcher = findViewById(R.id.am_bSuppresArcher);
        bSuppresArcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (archer.getCount() != 0) {
                    String name = archer.getSelectedItem().toString();

                    adapter.remove(archer.getSelectedItem());
                    stock.dropArcher(name);
                } else {
                    Intent i = new Intent(Activity_maintenance.this, Activity_Config_round.class);
                    startActivity(i);
                    Activity_maintenance.this.finish();
                }
            }
        });

        bCleaDataBase = findViewById(R.id.am_bCleaDataBase);
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
                        Intent j = new Intent(Activity_maintenance.this, Activity_Config_round.class);
                        startActivity(j);
                        Activity_maintenance.this.finish();
                    }
                });
                popupValidation.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;
                    }
                });
                popupValidation.show();

            }
        });

// export in file Archer for all Rounds

        bExportArcherRounds = findViewById(R.id.am_bexport_archer_rounds);
        bExportArcherRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // test Archer
                if (archer.getCount() != 0 && archer.getSelectedItemId() >= 0) {
                    // Start  aSynchrone task
                    // new ExportAsyncTask().execute();
                    String[] argv = new String[]{archer.getSelectedItem().toString()};
                    ExportAsyncTask task = new ExportAsyncTask(Activity_maintenance.this);
                    task.execute(argv);
                }
            }
        });


//-------------------------------------------------------------------------------------------------
        round = findViewById(R.id.sRound);
        lRound = stock.getRounds();
        adapterRound = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item
        );

        for (int i = 0; i < lRound.size(); i++) {
            adapterRound.add(lRound.get(i));
        }
        adapterRound.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        round.setAdapter(adapterRound);

        bSuppressRound = findViewById(R.id.am_bSup_round);
        bSuppressRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round.getCount() != 0) {
                    String name = round.getSelectedItem().toString();

                    adapterRound.remove(round.getSelectedItem());
                    stock.supRound(name);
                } else {
                    Intent i = new Intent(Activity_maintenance.this, Activity_Config_round.class);
                    startActivity(i);
                    Activity_maintenance.this.finish();
                }
            }
        });

    }


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
