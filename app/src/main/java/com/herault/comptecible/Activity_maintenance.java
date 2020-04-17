package com.herault.comptecible;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.utils.ExportAsyncTask;
import com.herault.comptecible.utils.Stockage;

import java.util.List;


public class Activity_maintenance extends AppCompatActivity {

    protected Activity context;
    Button bSuppresArcher;
    Button bCleaDataBase;
    Button bSuppressRound;
    Button bExportArcherRounds;
    ProgressBar progressBarExport;
    Stockage stock = null;
    Spinner archer = null;
    Spinner round = null;
    List<String> lRound;
    ArrayAdapter adapter;
    ArrayAdapter adapterRound;

    List<String> lArcher;
    long archer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        progressBarExport=findViewById(R.id.am_progressBar);

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
                    Intent i = new Intent(Activity_maintenance.this, Config_round.class);
                    startActivity(i);
                    Activity_maintenance.this.finish();
                }
            }
        });

        bCleaDataBase = findViewById(R.id.am_bCleaDataBase);
        bCleaDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock.dropArchers(false);
                Intent i = new Intent(Activity_maintenance.this, Config_round.class);
                startActivity(i);
                Activity_maintenance.this.finish();
            }
        });

// export in file Archer for all Rounds

        bExportArcherRounds = findViewById(R.id.am_bexport_archer_rounds);
        bExportArcherRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

    // test Archer
            if(archer.getCount() != 0 && archer.getSelectedItemId()>=0) {
                // Start  aSynchrone task
              // new ExportAsyncTask().execute();
               String argv[] =  new String[] {archer.getSelectedItem().toString()};
                ExportAsyncTask task =   new ExportAsyncTask(Activity_maintenance.this) ;
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
                    Intent i = new Intent(Activity_maintenance.this, Config_round.class);
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
