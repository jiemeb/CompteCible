package com.herault.comptecible;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.utils.FilterContainer;
import com.herault.comptecible.utils.FiltersContainer;
import com.herault.comptecible.utils.Stockage;

import java.util.ArrayList;
import java.util.List;


public class Activity_resultat_round extends AppCompatActivity {
    private Spinner archer = null;
    private Spinner round = null;
    private List<String> lArcher;

    private String roundName = "";
    private ArrayAdapter adapter_archer;
    private ArrayAdapter adapter_round;

    private Stockage stock = null;

    private List<Resultat_archer> lresultat;
    private ListRound adapter_resultat;

    private GridView resultFilter ;
    private String SFilterResult="";
    private FiltersContainer filtersResultContainer;

    private List<String> lRound  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_round);

        stock = new Stockage();             // init de la classe interface de stockage
        stock.onCreate(this);


        resultFilter = findViewById(R.id.res_round_filter);


   //     resultFilter.setText(stock.getValue("filter"));
        SFilterResult=stock.getValue("filter");
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

     /*   resultFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_gestion_filter.class);
                stock.updateValue("filterPrevious",filtersResultContainer.serialize());
                someActivityResultLauncher.launch(intent);
            }
        });*/



        //spinner Rounds
        round = findViewById(R.id.res_spinner_round);

        adapter_round = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item
        );
        roundName = stock.getValue("roundName");
        lRound = stock.getRounds(filtersResultContainer.getFilter());
        int selection = -1;
        for (int i = 0; i < lRound.size(); i++) {
            String tempo = lRound.get(i);
            if (tempo.contentEquals(roundName))
                selection = i;
            adapter_round.add(lRound.get(i));
        }

        adapter_round.setDropDownViewResource(R.layout.spinner_generale);
        round.setAdapter(adapter_round);
        if (selection == -1)        // no round after filter
        {
            roundName = "";
            Toast.makeText(this, getString(R.string.pasRun),
                    Toast.LENGTH_SHORT).show();
        }
        else
            round.setSelection(selection);                                  // Select RoundName from Round current
// Round select ----------------------------
        round.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                roundName = round.getSelectedItem().toString();
                // Update Archer List

                adapter_archer.clear();
                lArcher = stock.getArchers(false); // get modification  on Database after config Round
       //         lArcher = stock.getArchers(roundName); // get modification  on Database after config Round
                lArcher.add("***");
                for (int i = 0; i < lArcher.size(); i++) {
                    adapter_archer.add(lArcher.get(i));
                }
                archer.setSelection(archer.getCount() - 1);
                //          Toast.makeText(getApplicationContext(), " " + archer.getSelectedItem(), Toast.LENGTH_SHORT).show();
                resultat_round();                                                                         // Update for *** archer in round

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //spinner Archer
        archer = findViewById(R.id.res_spinner_archer);
        lArcher = stock.getArchers(roundName);
        lArcher.add("***");
        adapter_archer = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item
        );

        for (int i = 0; i < lArcher.size(); i++) {
            adapter_archer.add(lArcher.get(i));
        }

        /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        adapter_archer.setDropDownViewResource(R.layout.spinner_generale);
        archer.setAdapter(adapter_archer);
        archer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!archer.getSelectedItem().toString().equals("***"))
                    resultat_archer();
                else
                    resultat_round();

                //       Toast.makeText(getApplicationContext(), "Archer" + archer.getSelectedItem(), Toast.LENGTH_SHORT).show();
                // Init lors du changement Update

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

// ------- Get resultat for round

        ListView resultat = findViewById(R.id.res_lis_resultat);
        lresultat = stock.getResultatAll(roundName);

        ArrayList<Resultat_archer> resultat_archers = new ArrayList<Resultat_archer>();
        adapter_resultat = new ListRound(
                this, resultat_archers
        );

        for (int i = 0; i < lresultat.size(); i++) {
            adapter_resultat.add(lresultat.get(i));
        }
        resultat.setAdapter(adapter_resultat);
        resultat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Resultat_archer rArcher = lresultat.get(position);
                if (archer.getSelectedItem().toString().equals("***")) //if true  result Round
                {
                    // Start
                    if (!adapter_round.isEmpty())
                    {
                        Intent intent = new Intent(Activity_resultat_round.this, Activity_resultat_image.class);
                        intent.putExtra("round", round.getSelectedItem().toString());
                        intent.putExtra("name", rArcher.name);
                        startActivity(intent);
                    }
                } else                                                    // resultat Archer
                {
                   // ImageView imageView = findViewById(R.id.res_image_resultat);
                    // Here I must get round name in lresultat  and Archer in archer spinner
                    //            drawResultRound( round.getSelectedItem().toString(),rArcher.name) ;

                    Intent intent = new Intent(Activity_resultat_round.this, Activity_resultat_image.class);
                    intent.putExtra("round", rArcher.name);       // content rarcher are round name
                    intent.putExtra("name", archer.getSelectedItem().toString());
                    startActivity(intent);

                }

            }
        });
    }


    private void resultat_archer() {
        if (filtersResultContainer.getLength() != 0)
        lresultat = stock.getResultatAllRound(archer.getSelectedItem().toString(),filtersResultContainer.getFilter()); // regex \s = space in Java must be escape
        else
        lresultat = stock.getResultatAllRound(archer.getSelectedItem().toString());

        adapter_resultat.clear();
        for (int i = 0; i < lresultat.size(); i++) {
            adapter_resultat.add(lresultat.get(i));
        }
    }

    private void resultat_round() {

        lresultat = stock.getResultatAll(roundName);
        adapter_resultat.clear();
        for (int i = 0; i < lresultat.size(); i++) {
            adapter_resultat.add(lresultat.get(i));
        }
    }

    private void updateView() {

        adapter_round.getPosition(roundName);
        adapter_archer.clear();
        lArcher = stock.getArchers(roundName); // get modification  on Database after config Round
        lArcher.add("***");
        for (int i = 0; i < lArcher.size(); i++) {
            adapter_archer.add(lArcher.get(i));
        }
        //  String res = archer.getSelectedItem().toString();
        resultat_round();

    }
    private void updateResultValue()
    {
        //       String [] table =  filterTemplate.toString().split("\\s+");
        //listResultValue.clear();
         arrayFilter arrayAdapter ;
       if(filtersResultContainer.getLength() == 0)
       {
            ArrayList <FilterContainer>  dummyFilter = new ArrayList<>();
            dummyFilter.add(new FilterContainer("_Black_",getString(R.string.gf_no_filter)));
            arrayAdapter = new arrayFilter(this, dummyFilter);
       }
        else
       {
         arrayAdapter = new arrayFilter(this, filtersResultContainer.getListFilterContainer());
       }

        resultFilter.setAdapter( arrayAdapter );


    }
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

                    adapter_round.clear();
                    lRound = stock.getRounds(filtersResultContainer.getFilter());
                    int selection = 0;
                    for (int i = 0; i < lRound.size(); i++) {
                        String tempo = lRound.get(i);
                        if (tempo.contentEquals(roundName))
                            selection = i;
                        adapter_round.add(lRound.get(i));
                    }
                // Update resultat adaptateur

                if ( adapter_round.isEmpty())
                    {
                     adapter_resultat.clear();
                     roundName="" ;
                    Toast.makeText(this, getString(R.string.pasRun),
                          Toast.LENGTH_SHORT).show();
                    }
                updateView();
                }
            });



    /*********************************************************************************/
    /** Managing LifeCycle and database open/close operations ************************/
    /*********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        // Open stockage
        stock.openDB();
        updateView();

        archer.setSelection(archer.getCount() - 1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close stockage
        stock.closeDB();
    }
}

