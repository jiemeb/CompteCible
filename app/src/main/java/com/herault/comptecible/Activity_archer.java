package com.herault.comptecible;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.utils.Stockage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Activity_archer extends AppCompatActivity {

    private Stockage stock = null;
    private ListNote adapterNote;
    private Spinner bowType ;
    private Spinner SArcherName = null;
    private List<Note> listNote;
    private boolean archerInformationChanged = false;
    private TextView archer_information;
    private String oldArcherSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_archer);
        stock = new Stockage();             // init de la classe interface de stockage
        stock.onCreate(this);

        Button plusNote = findViewById(R.id.bPlus);
        plusNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Activity_archer.this, EditNoteActivity.class);
                myIntent.putExtra("archer", (String) SArcherName.getSelectedItem());
//                startActivityForResult(myIntent, 0);
                someActivityResultLauncher.launch(myIntent);
// ré-actualise Note
             }
        });



        /* List of Archers in Base */
        // --------------------
        SArcherName = findViewById(R.id.sp_archer);
        List<String> lArcherName = stock.getArchers(false);

        ArrayAdapter adapterArcherName = new ArrayAdapter(
                this,
                R.layout.spinner_generale
        );

        for (int i = 0; i < lArcherName.size(); i++) {
            adapterArcherName.add(lArcherName.get(i));
        }
        /*
        archer_information
         */
        archer_information = findViewById(R.id.archer_information);

        archer_information.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                archerInformationChanged = true;

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        adapterArcherName.setDropDownViewResource(R.layout.spinner_generale);
        //Enfin on passe l'adapter au Spinner et c'est tout
        SArcherName.setAdapter(adapterArcherName);
        // force item select with round

        SArcherName.setSelection(0); // select first Archer
        String archerSel = (String) SArcherName.getSelectedItem();
        archer_information.setText(stock.getArcherInformation(archerSel));
        SArcherName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (archerInformationChanged) {
                    stock.updateArcherInformation(oldArcherSelected, archer_information.getText().toString());
                    archerInformationChanged = false;
                }
                String archerSel = (String) SArcherName.getSelectedItem();
                oldArcherSelected = archerSel;
                archer_information.setText(stock.getArcherInformation(archerSel));
                bowType.setSelection(stock.getArcherBow(archerSel));
                refreshNotes();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ;  // your code here
            }
        });

        // -----------------------------------

        archerSel = (String) SArcherName.getSelectedItem();
        final ListView lNoteBase = findViewById(R.id.note);
        listNote = stock.getNotes(archerSel);
        ArrayList<Note> notes_archer = new ArrayList<Note>();

        adapterNote = new ListNote(this, notes_archer);

        refreshNotes();

        lNoteBase.setAdapter(adapterNote);

        lNoteBase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                Note myNote = adapterNote.getItem(position);

                Intent myIntent = new Intent(Activity_archer.this, EditNoteActivity.class);
                myIntent.putExtra("idNote", myNote.keyIdNote);
                myIntent.putExtra("archer", (String) SArcherName.getSelectedItem());
//                startActivityForResult(myIntent, 0);
                someActivityResultLauncher.launch(myIntent);
// ré-actualise Note
                return true;

            }
        });
// archer bow
        bowType = findViewById(R.id.id_archer_bow);
        bowType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stock.updateArcherBow( (String) SArcherName.getSelectedItem(),position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

// -----------------------------------

    private void refreshNotes() {
        adapterNote.clear();
        String ArcherSel = (String) SArcherName.getSelectedItem();
        listNote = stock.getNotes(ArcherSel);
        for (int i = 0; i < listNote.size(); i++) {
            adapterNote.add(listNote.get(i));
        }
    }

ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() != Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        //    doSomeOperations();
                      //  Toast.makeText(Activity_archer.this, "retour de l'activité appelante", Toast.LENGTH_SHORT).show();
                    }
                    stock.openDB();
                    refreshNotes();
                }
            });

 /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && requestCode == 0) {
            if (data.hasExtra("retour")) {
        //        Toast.makeText(this, data.getExtras().getString("retour"),
         //               Toast.LENGTH_SHORT).show();
            }
            stock.openDB();
            refreshNotes();
        }
    } */

    /*********************************************************************************/
    /** Managing LifeCycle and database open/close operations ************************/
    /*********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        stock.openDB();

    }

    @Override
    protected void onPause() {
        if (archerInformationChanged) {
            String archerSel = (String) SArcherName.getSelectedItem();
            stock.updateArcherInformation(archerSel, archer_information.getText().toString());
            archerInformationChanged = false;
        }
        super.onPause();

        stock.closeDB();

    }

}
