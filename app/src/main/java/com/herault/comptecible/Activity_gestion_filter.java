package com.herault.comptecible;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.utils.Stockage;

import java.util.ArrayList;
import java.util.List;

public class Activity_gestion_filter extends AppCompatActivity {
    private Stockage stock = null;
    private TextView filterResult ;
    private GridView gridFilter ;
    private Activity_gestion_filter localActivity;
    private List<String> listGridValue;
    private  String filterTemplate ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            localActivity = this;
            setContentView(R.layout.activity_gestion_filter);
            stock = new Stockage();             // init de la classe interface de stockage
            stock.onCreate(this);

            filterResult = findViewById(R.id.filterResult);
   /*         final Intent intent = getIntent();
            filterResult.setText( intent.getStringExtra("before")); */
            filterResult.setText(stock.getValue("filterPrevious"));
            gridFilter= findViewById(R.id.gridChoice);
// Get All value
           listGridValue =new ArrayList<String>();
             filterTemplate =  stock.getValue("filterTemplate");
             if(!(filterTemplate.trim().length() > 0))
                 filterTemplate = "";
            if (filterTemplate.isEmpty()) {
                filterTemplate = "10m 18m 20m 30m 50m 60m 70m 3x10 3x20 6x6 6x12 °40 °60 °80 °122";
                stock.updateValue("filterTemplate",filterTemplate);
            }

            updateGridValue();

            gridFilter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                    String wordToDelete = listGridValue.get(position);
                    // dialog
                    String result = replaceWord(filterResult.getText().toString(),wordToDelete, "");
                    filterResult.setText(result);
                    result = filterResult.getText().toString().replace("  ", " ");
                    filterResult.setText(result);
                    stock.updateValue("filterPrevious",result);
                    // Make choice to delete from template
                    AlertDialog.Builder popupValidation = new AlertDialog.Builder(localActivity);
                    popupValidation.setMessage(getResources().getString(R.string.gf_dia_sup_template)+" "+wordToDelete);
                    popupValidation.setTitle(getResources().getString(R.string.gf_dia_template));
                    popupValidation.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            filterTemplate=replaceWord(filterTemplate,wordToDelete, "");
                            filterTemplate=filterTemplate.replace("  ", " ");
                            stock.updateValue("filterTemplate",filterTemplate);
                            updateGridValue();
                            }
                    });
                    popupValidation.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    popupValidation.show();



                    return true;
                }
            });


            gridFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   String select = listGridValue.get(position);
                   String filter = filterResult.getText().toString();
                   if(position == (listGridValue.size()-1))
                   {
                       AlertDialog.Builder builder = new AlertDialog.Builder(localActivity);
                       final EditText input = new EditText(localActivity);
                       builder
                               .setTitle(R.string.gf_dia_tittle_addsubject)
                               .setMessage(R.string.gf_dia_addsubject)
                               .setView(input)
                               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                   public void onClick(DialogInterface dialog, int which) {
                                       String value = input.getText().toString();
                                       value=value.replace(' ','_');
                                       if (value.toString().trim().length() == 0) {
                                           Toast.makeText(Activity_gestion_filter.this, R.string.gf_dia_input_empty, Toast.LENGTH_SHORT).show();
                                       } else {
                                            filterTemplate =  stock.getValue("filterTemplate");   // Add New data en new values in []
                                           if (filterTemplate.isEmpty())
                                               filterTemplate= value;
                                           if (!findWord (filterTemplate,value)) // Don't add if already down
                                               filterTemplate=filterTemplate+" "+value;
                                           stock.updateValue("filterTemplate",filterTemplate);
                                           updateGridValue();
                                       }
                                       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                       imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                                   }
                               })
                               .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                                   public void onClick(DialogInterface dialog, int which) {
                                       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                       imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                                   }
                               });

                       builder.show();
                       input.requestFocus();
                       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                       imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                   }
                   else {
                       if (filter.isEmpty())
                           filterResult.setText(select);
                       if (!findWord(filter,select)) // Don't add if already down
                           filterResult.setText(filter + " " + select);
                       stock.updateValue("filterPrevious",filterResult.getText().toString());
                   }
                }
            });


        Button bpValid = findViewById(R.id.gf_bp_valid);
        bpValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent();
                intent.putExtra("after", filterResult.getText().toString());
                setResult(123, intent);
                finish(); */
                onBackPressed();
            }
        });

        Button bp_annul = findViewById(R.id.gf_bp_cancel);
        bp_annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(-1, intent);
                finish();

            }
        });

        Button bpClearFilter = findViewById(R.id.gf_bp_clear_filter);
        bpClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterResult.setText("");
                stock.updateValue("filterPrevious","");
            }
        });


    }
    private boolean findWord (String listString,String toFind)
    {
        String result ="" ;
        String [] table =  listString.toString().split("\\s+");
        for (String t:table) {
            if(toFind.equals(t))
            {
                return true;
            }
        }
        return false ;
    }


        private String replaceWord (String listString,String toFind,String toReplace)
        {
            String result ="" ;
             String [] table =  listString.toString().split("\\s+");
              for (String t:table) {
                if(toFind.equals(t))
                {
                    t=toReplace;
                }
                if (result.isEmpty())
                result = t ;
                else
                result += " "+t ;
            }
              return result ;
        }

        private void updateGridValue()
        {
            String [] table =  filterTemplate.toString().split("\\s+");
            listGridValue.clear();
            for (String t:table
            ) {
                listGridValue.add(t);
            }
            listGridValue.add(getResources().getString(R.string.gf_toAdd));
            gridFilter.setAdapter(new ArrayAdapter<String>(this,R.layout.cell_grid_view, listGridValue));
        }

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

        Intent intent = new Intent();
        intent.putExtra("after", filterResult.getText().toString());
        setResult(123, intent);
        finish();

    }

}

