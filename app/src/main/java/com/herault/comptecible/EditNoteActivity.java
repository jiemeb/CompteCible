package com.herault.comptecible;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.herault.comptecible.utils.Stockage;

public class EditNoteActivity extends AppCompatActivity {

    protected Activity context;
    private Stockage stock = null;
    Button valid;
    Button annul;
    Button delete;
    EditText saisie;
    long idNote;
    String archer_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        stock = new Stockage();              // init de la classe interface de stockage
        stock.onCreate(this);
        idNote = this.getIntent().getLongExtra("idNote", -1);
        //       idNote =    Long.valueOf( this.getIntent().getStringExtra("idNote"));
        archer_name = this.getIntent().getStringExtra("archer");

        saisie = findViewById(R.id.saisie);
        if (idNote != -1) {   // Update
            saisie.setText(stock.getNote(idNote));
        }


        valid = findViewById(R.id.valid);
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idNote == -1) {
                    stock.addNote(archer_name, saisie.getText().toString());
                } else {
                    stock.updateNote(idNote, saisie.getText().toString());
                }
                Intent i2 = new Intent();
                i2.putExtra("retour", "valeur de retour");
                EditNoteActivity.this.setResult(0, i2);
                EditNoteActivity.this.finish();
            }
        });

        annul = findViewById(R.id.cancel);
        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent();
                i2.putExtra("retour", "valeur de retour");
                EditNoteActivity.this.setResult(0, i2);
                EditNoteActivity.this.finish();
            }
        });


        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idNote != -1) {
                    stock.deleteNote(idNote);
                }
                Intent i2 = new Intent();
                i2.putExtra("retour", "valeur de retour");
                EditNoteActivity.this.setResult(0, i2);
                EditNoteActivity.this.finish();
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

    public void onBackPressed() {
        Intent i2 = new Intent();
        i2.putExtra("retour", "valeur de retour");
        EditNoteActivity.this.setResult(0, i2);
        EditNoteActivity.this.finish();

    }


}