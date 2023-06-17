package com.herault.comptecible;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.utils.FilterContainer;
import com.herault.comptecible.utils.FiltersContainer;
import com.herault.comptecible.utils.MyHandlerThread;
import com.herault.comptecible.utils.Stockage;


import java.util.ArrayList;
import java.util.List;

public class Activity_maintenance extends AppCompatActivity  {

    protected Activity context;


    private ProgressBar progressBarExport;
    private Stockage stock = null;
    private Spinner archer = null;
    private Spinner round = null;
    private ArrayAdapter adapter;
    private List<String> lRound ;
    private ArrayAdapter adapterRound;

    private EditText pointageOffset = null ;
    //private EditText resultFilter =null;

    private GridView resultFilter ;
    private FiltersContainer filtersResultContainer;

    private Activity_maintenance localActivity;
    private List<Resultat_archer> lresultat;
    private MyHandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localActivity = this;
        setContentView(R.layout.activity_maintenance);
        progressBarExport = findViewById(R.id.am_progressBar);
        this.configureHandlerThread();
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
// Initialize Filter
        resultFilter = findViewById(R.id.r_maintenance_filter);
        String SFilterResult = stock.getValue("filter");
        if(!(SFilterResult.trim().length() > 0))
            SFilterResult = "";
        filtersResultContainer= new FiltersContainer(SFilterResult);
        updateResultValue();
        resultFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Activity_gestion_filter.class);
                stock.updateValue("filterPrevious",filtersResultContainer.serialize());
                someActivityResultLauncher.launch(intent);
            }
        });

// Suppress Archer

        Button bSuppresArcher = findViewById(R.id.am_bSuppresArcher);
        bSuppresArcher.setOnClickListener(v -> {
            if (archer.getCount() != 0) {
                String name = archer.getSelectedItem().toString();

                AlertDialog.Builder popupValidation = new AlertDialog.Builder(localActivity);
                popupValidation.setMessage(getResources().getString(R.string.archerClean)+name);
                popupValidation.setTitle(getResources().getString(R.string.archerClean));
                popupValidation.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.remove(archer.getSelectedItem());
                        stock.dropArcher(name);

                        if (archer.getCount() == 0) {
                            Intent j = new Intent(Activity_maintenance.this, Activity_config_round.class);
                            startActivity(j);
                            Activity_maintenance.this.finish();
                        }
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

// Clear Database
        Button bCleaDataBase = findViewById(R.id.am_bCleaDataBase);
        bCleaDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupValidation = new AlertDialog.Builder(localActivity);
                popupValidation.setMessage(getResources().getString(R.string.baseAlertClean)+"database");
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
// Set or getValue of Optimisation Arrow
         CheckBox arrowNamed = findViewById(R.id.RadioArrow);
         String sNamedArrow = stock.getValue("NamedArrow");
       if(sNamedArrow.compareTo("true")==0)
         {
             arrowNamed.setChecked(true);
         }
         arrowNamed.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if (!arrowNamed.isChecked()) {
                     stock.updateValue("NamedArrow", "false");
                 }
                 else {
                     stock.updateValue("NamedArrow", "true");
                 }
             }
         });
// Set pointing Offset
        // Get Offset of Pointer
        pointageOffset = findViewById(R.id.pointageOffset);
        String sPointageOffset = stock.getValue("pointageOffset");
        if(sPointageOffset.isEmpty())
        {
            sPointageOffset = "2" ;
        }
        pointageOffset.setText(sPointageOffset);
        pointageOffset.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String sPointageOffset = pointageOffset.getText().toString().trim();
                if (!sPointageOffset.isEmpty()) {
                    switch (sPointageOffset)
                    {
                        case "0":
                        case "1":
                        case "2":
                        case "3": break ;
                        default :   sPointageOffset = "2" ;
                    }
                    stock.updateValue("pointageOffset", sPointageOffset);
                }           // you can call or do what you want with your EditText here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                    String name = new String(round.getSelectedItem().toString());
                    lresultat = stock.getResultatAll(name);
                    startHandlerThread(lresultat,name);


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

                 //   String[] argv = new String[]{archer.getSelectedItem().toString() + "_" + round.getSelectedItem().toString()};
                    lresultat = stock.getResultatArrows(archer.getSelectedItem().toString(), round.getSelectedItem().toString());
                  /*  task = new ExportAsyncTask(Activity_maintenance.this);
                    task.execute(argv);*/
                    String name = new String(archer.getSelectedItem().toString() + "_" + round.getSelectedItem().toString());
                    startHandlerThread(lresultat,name);

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
                    String name = new String(archer.getSelectedItem().toString());
                    lresultat =stock.getResultatAllRound(name, filtersResultContainer.getFilter());
                    name += "_all";
                    startHandlerThread(lresultat,name);

                }
            }
        });

//-------------------------------------------------------------------------------------------------

        round = findViewById(R.id.sRound);
         lRound = stock.getRounds(filtersResultContainer.getFilter());
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
                if (round.getCount() != 0 && round.getSelectedItemId() >= 0) {
                    String name = round.getSelectedItem().toString();
                    AlertDialog.Builder popupValidation = new AlertDialog.Builder(localActivity);
                    popupValidation.setMessage(getResources().getString(R.string.roundClean) + name);
                    popupValidation.setTitle(getResources().getString(R.string.roundClean));
                    popupValidation.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapterRound.remove(round.getSelectedItem());
                            stock.supRound(name);

                            if (round.getCount() == 0) {
                                Intent j = new Intent(Activity_maintenance.this, Activity_config_round.class);
                                startActivity(j);
                                Activity_maintenance.this.finish();
                            }
                        }
                    });
                    popupValidation.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    popupValidation.show();

                }
            }
        });



    }
    //--------------------------gestion Filter--------------------------
    private void updateResultValue()
    {
        //       String [] table =  filterTemplate.toString().split("\\s+");
        //listResultValue.clear();
        arrayFilter arrayAdapter ;
        if(filtersResultContainer.getLength() == 0)
        {
            ArrayList<FilterContainer> dummyFilter = new ArrayList<>();
            dummyFilter.add(new FilterContainer("_Black_",getString(R.string.gf_no_filter)));
            arrayAdapter = new arrayFilter(this, dummyFilter);
        }
        else
        {
            arrayAdapter = new arrayFilter(this, filtersResultContainer.getListFilterContainer());
        }

        resultFilter.setAdapter( arrayAdapter );


    }
    //gestion activity with return
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                stock.openDB();
                if (result.getResultCode() == 123) {
                    // There are no request codes

                    filtersResultContainer= new FiltersContainer( result.getData().getStringExtra("after") );
                    stock.updateValue("filter", filtersResultContainer.serialize());
                    updateResultValue();

                    //refress  listround
                    adapterRound.clear();
                    lRound = stock.getRounds(filtersResultContainer.getFilter());
                    int selection = 0;
                    for (int i = 0; i < lRound.size(); i++) {
                        String tempo = lRound.get(i);
                        adapterRound.add(lRound.get(i));
                    }
                    // Update resultat adaptateur

                    if ( adapterRound.isEmpty())
                    {

                        Toast.makeText(this, getString(R.string.pasRun),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

//---------------------------------------
    // -----------------
    // CONFIGURATION
    // -----------------

    // 2 - Configuring the HandlerThread
    private void configureHandlerThread(){
        handlerThread = new MyHandlerThread(this,"MyAwesomeHandlerThread", this.progressBarExport);
    }

    // -------------------------------------------
    // BACKGROUND TASK (HandlerThread & AsyncTask)
    // -------------------------------------------

    // 4 - EXECUTE HANDLER THREAD
    private void startHandlerThread(List <Resultat_archer>lresultat,String name){

        handlerThread.startHandler(lresultat,name);
    }

//--------------------------------------
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

    @Override
    protected void onDestroy() {
        // 3 - QUIT HANDLER THREAD (Free precious resources)
        handlerThread.quit();
        super.onDestroy();
    }


}
