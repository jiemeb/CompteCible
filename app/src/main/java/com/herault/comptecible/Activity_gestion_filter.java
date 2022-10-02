package com.herault.comptecible;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.herault.comptecible.utils.FilterContainer;
import com.herault.comptecible.utils.FiltersContainer;
import com.herault.comptecible.utils.Stockage;

import java.util.ArrayList;

public class Activity_gestion_filter extends AppCompatActivity {
    private Stockage stock = null;
    private GridView resultFilter;
    private GridView gridFilter ;
    private Activity_gestion_filter localActivity;
    private ArrayList<FilterContainer> listGridValue;
    private  ArrayList<FilterContainer> listResultValue;

    private String SFilterTemplate ="";
    private String SFilterResult="";
    private FiltersContainer filtersContainer;
    private FiltersContainer filtersResultContainer;
    private String currentColor ="_blue_" ;

    RadioButton r_blue,r_red, r_yellow,r_black ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            localActivity = this;
            setContentView(R.layout.activity_gestion_filter);
            stock = new Stockage();             // init de la classe interface de stockage
            stock.onCreate(this);
            resultFilter = findViewById(R.id.filterResult);

//Initiate Filter Result
        listResultValue = new ArrayList<FilterContainer>();
        SFilterResult=stock.getValue("filterPrevious");
        if(!(SFilterResult.trim().length() > 0))
            SFilterResult = "";
        filtersResultContainer= new FiltersContainer(SFilterResult);
            // Initiate  Filter Grid
            gridFilter= findViewById(R.id.gridChoice);
            updateResultValue();

// Get All value
           listGridValue =new ArrayList<FilterContainer>();
           SFilterTemplate =  stock.getValue("filterTemplate");
            if(!(SFilterTemplate.trim().length() > 0))
                 SFilterTemplate = "";
            if (SFilterTemplate.isEmpty()) {
                SFilterTemplate = "_yellow_ 10m _yellow_ 18m _yellow_ 20m _yellow_ 25m _yellow_ 30m  _yellow_ 40m _yellow_ 50m _yellow_ 60m _yellow_ 70m _blue_ 3x10 _blue_ 3x20 _blue_ 6x6 _blue_ 6x12 _black_ Ø40 _black_ Ø60 _black_ Ø80 _black_ Ø122";
                stock.updateValue("filterTemplate", SFilterTemplate);
            }
            filtersContainer = new FiltersContainer(SFilterTemplate);
            updateGridValue();

            gridFilter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                    String wordToDelete = listGridValue.get(position).getValue();
                    // Delete value dans listResultValue
                    filtersResultContainer.deleteValue( wordToDelete) ;
                    updateResultValue();

                    // Make dialog to delete fiter ?
                    // Make choice to delete from template
                    AlertDialog.Builder popupValidation = new AlertDialog.Builder(localActivity);
                    popupValidation.setMessage(getResources().getString(R.string.gf_dia_sup_template)+" "+wordToDelete);
                    popupValidation.setTitle(getResources().getString(R.string.gf_dia_template));
                    popupValidation.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            filtersContainer.deleteValue( wordToDelete) ;
                            stock.updateValue("filterTemplate", filtersContainer.serialize());
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
                   String select = listGridValue.get(position).getValue();
                   String color = currentColor;

                   if(position == (listGridValue.size()-1)) // Click on new filter
                   {
                       AlertDialog.Builder builder = new AlertDialog.Builder(localActivity);
                       final EditText input = new EditText(localActivity);
                       builder
                               .setTitle(R.string.gf_dia_tittle_addsubject)
                               .setMessage(R.string.gf_dia_addsubject)
                               .setView(input)
                               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                   public void onClick(DialogInterface dialog, int which) {
                                       String color = currentColor;
                                       String value = input.getText().toString();
                                       value=value.replace(' ','_');
                                       if (value.toString().trim().length() == 0) {
                                           Toast.makeText(Activity_gestion_filter.this, R.string.gf_dia_input_empty, Toast.LENGTH_SHORT).show();
                                       } else {
                                           filtersContainer.addValue(new FilterContainer(color,value));
                                           stock.updateValue("filterTemplate", filtersContainer.serialize());
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
                       imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
                   }
                   else {

                       filtersResultContainer.addValue(listGridValue.get(position));
                       updateResultValue();
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
            filtersResultContainer.clear();

                updateResultValue();
            }
        });

    RadioGroup r_group_color = findViewById(R.id.gf_gb_checkColor);
        r_blue = findViewById(R.id.gf_rb_blue);
        r_red = findViewById(R.id.gf_rb_red);
        r_yellow = findViewById(R.id.gf_rb_yellow);
        r_black = findViewById(R.id.gf_rb_black);
        r_blue = new RadioButton(this);
        r_red  = new RadioButton(this);
        r_yellow = new RadioButton(this);
        r_black= new RadioButton(this);
        r_group_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.gf_rb_blue:
                         currentColor ="_blue_" ;
                        break;
                    case R.id.gf_rb_red:
                        currentColor ="_red_" ;
                        break;
                    case R.id.gf_rb_yellow:
                        currentColor ="_yellow_" ;
                        break;
                    case R.id.gf_rb_black:
                        currentColor ="_black_" ;
                        break;
                }
            }
        });

    }


    private boolean findWord (String listString,String toFind)
    {

        String [] table =  listString.split("\\s+");
        for (String t:table) {
            if(toFind.equals(t))
            {
                return true;
            }
        }
        return false ;
    }



    private void updateResultValue()
    {
        //       String [] table =  filterTemplate.toString().split("\\s+");
        listResultValue.clear();
        arrayFilter arrayAdapter = new arrayFilter(this, listResultValue);


        for (int i = 0; i < filtersResultContainer.getLength(); i++)
        {
            String[] filter = filtersResultContainer.getTextview(i);
            listResultValue.add(new FilterContainer(filter[0], filter[1]));
        }
        resultFilter.setAdapter( arrayAdapter );
    }
        private void updateGridValue()
        {
     //       String [] table =  filterTemplate.toString().split("\\s+");
            listGridValue.clear();
            arrayFilter arrayAdapter = new arrayFilter(this,  listGridValue);


            for (int i = 0; i < filtersContainer.getLength(); i++)
            {
               listGridValue.add( filtersContainer.get(i));
            }
            listGridValue.add(new FilterContainer("_white_",getResources().getString(R.string.gf_toAdd)));
            gridFilter.setAdapter( arrayAdapter );
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
        intent.putExtra("after", filtersResultContainer.serialize());
        setResult(123, intent);
        finish();

    }

}

