package com.herault.comptecible;


import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

//import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.herault.comptecible.utils.Stockage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class Activity_config_round extends AppCompatActivity {


    private Stockage stock = null;
    private Button bLertGo;
    private ListArchers adapterBase;
    private ListArchers adapterRound;
    private EditText newArcher = null;
    private EditText roundName = null;
    private EditText INumberArrow = null;
    private EditText INumberEndByRound = null;
    private TextView roundQualifier = null ;

    private Button bAddArcher;
    private ListView lArcherRound, lArcherBase;
    private Spinner SRoundName = null;
    private List<String> lRoundName;
    private ArrayAdapter adapterRoundName;
    private Button bArchersUp;
    private int archerRoundSelectedPosition=0;

    //   private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config_round);


        stock = new Stockage();             // init de la classe interface de stockage
        stock.onCreate(this);



        bLertGo = findViewById(R.id.bLetGo);
        bLertGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sauvegarde archer_round database and test value before ending
                if (adapterRound.getCount() != 0 && roundName.getText().toString().trim().length() != 0 && INumberArrow.getText().toString().trim().length() != 0 && INumberEndByRound.getText().toString().trim().length() != 0) {
                    cleanSelectedArcherRound();
                    stock.dropArchers(true);
                    stock.insertArrayArchers(adapterRound._archers, true);
                    if (roundQualifier.getText().length() != 0) {
                        stock.updateRound(roundName.getText().toString(), roundQualifier.getText().toString());
                    }
                    Activity_config_round.this.finish(); // Kill config_run
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.configFill), Toast.LENGTH_SHORT).show();
                    if (adapterRound.getCount() == 0)
                        lArcherRound.setBackgroundColor(Color.YELLOW);
                    else
                        lArcherRound.setBackgroundColor(Color.WHITE);

                    if (roundName.getText().toString().trim().length() == 0)
                        roundName.setBackgroundColor(Color.YELLOW);
                    else
                        roundName.setBackgroundColor(Color.WHITE);

                    if (INumberArrow.getText().toString().trim().length() == 0)
                        INumberArrow.setBackgroundColor(Color.YELLOW);
                    else
                        INumberArrow.setBackgroundColor(Color.WHITE);

                    if (INumberEndByRound.getText().toString().trim().length() == 0)
                        INumberEndByRound.setBackgroundColor(Color.YELLOW);
                    else
                        INumberEndByRound.setBackgroundColor(Color.WHITE);

                }
            }
        });


        //      h = new Handler();


        lArcherBase = findViewById(R.id.archersBase);
        lArcherRound = findViewById(R.id.archersRound);
        newArcher = findViewById(R.id.newArcher);

        bAddArcher = findViewById(R.id.bAddArcher);

        bAddArcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nArcher = null;
                nArcher = newArcher.getText().toString().trim();
                if (!nArcher.isEmpty() && (stock.getArcherId(nArcher) < 0)) {
                    stock.addArcher(nArcher, false); //If Ok in Base
                    adapterRound.add(nArcher);  // put in list
                    newArcher.setText("");  //Clear Text
                }
                else
                {
                    newArcher.setText("");  //Clear Text}
                }
            }
        });

        bArchersUp = findViewById(R.id.archersUp);
        bArchersUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (archerRoundSelectedPosition > 0 && archerRoundSelectedPosition < (adapterRound.getCount() )) {
                Object archer = lArcherRound.getItemAtPosition(archerRoundSelectedPosition);
                adapterRound._archers.set(archerRoundSelectedPosition,(String) lArcherRound.getItemAtPosition(archerRoundSelectedPosition - 1));
                adapterRound._archers.set(archerRoundSelectedPosition - 1, (String) archer);
                archerRoundSelectedPosition -= 1;
                lArcherRound.setAdapter(adapterRound);
                }
            }
        });
        Button bArchersDown = findViewById(R.id.archersDown);
        bArchersDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (archerRoundSelectedPosition >= 0 && archerRoundSelectedPosition < (adapterRound.getCount() -1))
                {
                Object archer = lArcherRound.getItemAtPosition(archerRoundSelectedPosition);
                adapterRound._archers.set(archerRoundSelectedPosition,(String) lArcherRound.getItemAtPosition(archerRoundSelectedPosition + 1));
                adapterRound._archers.set(archerRoundSelectedPosition + 1, (String) archer);
                archerRoundSelectedPosition += 1;
                lArcherRound.setAdapter(adapterRound);
                }

            }
        });


        /* Get Round Name */

        roundName = findViewById(R.id.IRoundName);
        String round = stock.getValue("roundName");
        roundName.setText(round);
        roundName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String name = roundName.getText().toString().trim();
                if (!name.isEmpty())
                    stock.updateValue("roundName", name);
                String qualif =stock.getRoundQualifier(name.toString());
                roundQualifier.setText(stock.getRoundQualifier(name.toString()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        Button bConcours = findViewById(R.id.bConcours);
        bConcours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set spinner visible and roundName non Visible
        /*           roundName.setVisibility(View.VISIBLE);
                SRoundName.setVisibility(View.INVISIBLE);

           SRoundName.setFocusable(true);
                SRoundName.setFocusableInTouchMode(true);
                SRoundName.requestFocusFromTouch();
                SRoundName.requestFocus(View.FOCUS_DOWN); */

                SRoundName.performClick();
            }
        });

        SRoundName = findViewById(R.id.SRoundName);
        lRoundName = stock.getRounds();

        adapterRoundName = new ArrayAdapter(
                this,
                R.layout.spinner_generale
        );
        int indexRound = 0;
        for (int i = 0; i < lRoundName.size(); i++) {
            adapterRoundName.add(lRoundName.get(i));
            if (lRoundName.get(i).equals(round))
                indexRound = i;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 1");
        adapterRoundName.add(sdf.format(new Date()));

        adapterRoundName.setDropDownViewResource(R.layout.spinner_generale);
        //Enfin on passe l'adapter au Spinner et c'est tout
        SRoundName.setAdapter(adapterRoundName);
        // force item select with round

        SRoundName.setSelection(indexRound);

        SRoundName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                roundName.setText((String) SRoundName.getSelectedItem());
             /*                                    roundName.setVisibility(View.VISIBLE);
                                                 SRoundName.setVisibility(View.INVISIBLE); */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ;  // your code here
            }
        });

        /* Get Number of Arrow By END */

        INumberArrow = findViewById(R.id.INumberArrow);
        String NumberArrow = stock.getValue("numberArrow");

        INumberArrow.setText(NumberArrow);
        INumberArrow.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String NumberArrow = INumberArrow.getText().toString().trim();
                if (!NumberArrow.isEmpty())
                    stock.updateValue("numberArrow", NumberArrow); // you can call or do what you want with your EditText here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        /*Get Number of Ends by Round   */
        INumberEndByRound = findViewById(R.id.INumberEndByRound);
        String NumberEnd = stock.getValue("numberEnd");

        INumberEndByRound.setText(NumberEnd);
        INumberEndByRound.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String NumberEnd = INumberEndByRound.getText().toString().trim();
                if (!NumberEnd.isEmpty())
                    stock.updateValue("numberEnd", NumberEnd);// you can call or do what you want with your EditText here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });



        // List of archers in round
        adapterRound = new ListArchers(this, stock.getArchers(true));
        lArcherRound.setAdapter(adapterRound);
        actualizeAdapterBase();

        lArcherBase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //   Toast.makeText(getApplicationContext(), "L'index de cet élément est : " + position, Toast.LENGTH_SHORT).show();
                //   Toast.makeText(getApplicationContext(), "valeur : " + adapterBase.getItem(position).getName(), Toast.LENGTH_SHORT).show();
                String name = adapterBase.getItem(position);
                cleanSelectedArcherRound();
                adapterRound.add(name);
                adapterBase.remove(adapterBase.getItem(position));
                return true;

            }
        });




        lArcherRound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String archer ="";
                cleanSelectedArcherRound();

                archer= "*"+adapterRound.getItem(position);
                archerRoundSelectedPosition = position;
                adapterRound._archers.set(position,archer);
                lArcherRound.setAdapter(adapterRound);

            }



        });
        lArcherRound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapterRound.getItem(position);
                cleanSelectedArcherRound();
  // Insert sorting here              adapterBase.sort(name  );
                adapterRound.remove(adapterRound.getItem(position));
                adapterBase.clear();
                actualizeAdapterBase();
                return true;
            }
        });


        roundQualifier = findViewById(R.id.roundQualifier);
        roundQualifier.setText(stock.getRoundQualifier(roundName.toString()));
        roundQualifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Activity_gestion_filter.class);
   //             i.putExtra("before",roundQualifier.getText().toString());
                stock.updateValue("filterPrevious",roundQualifier.getText().toString());
                someActivityResultLauncher.launch(i);

            }
        });

    }

    private void cleanSelectedArcherRound() {

        String archer ="";
        for (int i = 0 ;i <adapterRound.getCount();i++) {
            archer = adapterRound.getItem(i);
            archer = archer.replace("*", "");
            adapterRound._archers.set(i, archer);
        }


        }


    void actualizeAdapterBase()
    {
        //Supress Archer in adapterBase present in adapterRound
        /* List of Archers in Base */
      //  adapterBase.clear();
        adapterBase = new ListArchers(this, stock.getArchers(false));
        lArcherBase.setAdapter(adapterBase);

        int i;
        boolean found;

        for (i = 0; i < adapterRound._archers.size(); i++) {
            int j;
            found = false;
            String aRoundName = adapterRound.getItem(i);
            aRoundName = aRoundName.replace("*", "");// Supress * in name
            for (j = 0; j < adapterBase._archers.size(); j++) {
                String aBaseName = adapterBase.getItem(j);

                if (aBaseName.equals(aRoundName)) {
                    adapterBase._archers.remove(j);
                    found = true;
                    break; //trouvé
                }
            }
            if (!found)
                adapterRound._archers.remove(i);
        }



    }

    /* Waiting from child activity */
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                stock.openDB();
                if (result.getResultCode() == 123) {
                    // There are no request codes

                    roundQualifier.setText( result.getData().getStringExtra("after") );
                    stock.updateRound(roundName.getText().toString(), roundQualifier.getText().toString());

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

    }


    @Override
    protected void onPause() {
        super.onPause();
        //Close stockage
        stock.closeDB();
    }

    public void onBackPressed() {

        if (roundName.getText().toString().trim().length() != 0 && INumberArrow.getText().toString().trim().length() != 0 && INumberEndByRound.getText().toString().trim().length() != 0) {
            if (adapterRound.isEmpty()) {
                stock.addArcher(getResources().getString(R.string.me), false); //If Ok in Base
                adapterRound.add(getResources().getString(R.string.me));  // put in list
            }
            cleanSelectedArcherRound();
            stock.dropArchers(true);
            stock.insertArrayArchers(adapterRound._archers, true);
            if (roundQualifier.getText().length() != 0) {
                stock.updateRound(roundName.getText().toString(), roundQualifier.getText().toString());
            }
            Activity_config_round.this.finish(); // Kill config_run
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.configFill), Toast.LENGTH_SHORT).show();
            if (adapterRound.getCount() == 0)
                lArcherRound.setBackgroundColor(Color.YELLOW);
            else
                lArcherRound.setBackgroundColor(Color.WHITE);

            if (roundName.getText().toString().trim().length() == 0)
                roundName.setBackgroundColor(Color.YELLOW);
            else
                roundName.setBackgroundColor(Color.WHITE);

            if (INumberArrow.getText().toString().trim().length() == 0)
                INumberArrow.setBackgroundColor(Color.YELLOW);
            else
                INumberArrow.setBackgroundColor(Color.WHITE);

            if (INumberEndByRound.getText().toString().trim().length() == 0)
                INumberEndByRound.setBackgroundColor(Color.YELLOW);
            else
                INumberEndByRound.setBackgroundColor(Color.WHITE);

        }
        //     Toast.makeText(this, getResources().getString(R.string.useButton) + " : \"" + getResources().getString(R.string.let_sgo) + "\"",
        //            Toast.LENGTH_SHORT).show();
    }

}
