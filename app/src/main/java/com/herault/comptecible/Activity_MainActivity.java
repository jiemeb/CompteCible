package com.herault.comptecible;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.graphics.Bitmap;

import android.view.MotionEvent;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import com.herault.comptecible.utils.Stockage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Activity_MainActivity extends AppCompatActivity {

    private static final double CONSTANTE_nbDivisionCible = 10.;

    private TextView arrowValue = null;
    private TextView endNumber = null;
    private Spinner archer = null;
    private long archer_id = 0;
    private EditText result = null;
    private ImageView Cible = null;
    private Stockage stock = null;
    private List<String> lArcher;
    private boolean must_config = false;
    private int resultat_fleche;
    private ArrayAdapter adapter_archer;
    private Button next_archer = null;
    private Button previous_archer = null;
    private Button b10 = null;
    private Button b9 = null;
    private Button b8 = null;
    private Button b7 = null;
    private Button b6 = null;
    private Button b5 = null;
    private Button b4 = null;
    private Button b3 = null;
    private Button b2 = null;
    private Button b1 = null;
    private Button bManque = null;
    private Button bAnnul = null;
    private String roundName = null;
    private int NumberArrow = 0;
    private int NumberEndByRound = 0;
    private View.OnTouchListener onTouchCible = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            Bitmap bitmap;

            double xmax = v.getWidth();
            double ymax = v.getHeight();
            double Xscale, Yscale;
            String res = "";
            // On récupère la coordonnée sur l'abscisse (X) de l'évènement getWidth()
            Xscale = 10 / xmax;
            double x = ((event.getX() - (xmax / 2.)) * Xscale);

            // On récupère la coordonnée sur l'ordonnée (Y) de l'évènement getHeight()
            Yscale = 10 / ymax;
            double y = ((event.getY() - (ymax / 2.)) * Yscale);

            if (y > -CONSTANTE_nbDivisionCible / 2. && y < CONSTANTE_nbDivisionCible / 2. && x > -CONSTANTE_nbDivisionCible / 2. && x < CONSTANTE_nbDivisionCible / 2.) {
                resultat_fleche = (int) (CONSTANTE_nbDivisionCible - (int) (Math.sqrt(Math.pow(x, 2.) + Math.pow(y, 2.)) - (0.3)));//
                // Cible is on 6 to 10
                if (resultat_fleche < 6)
                    resultat_fleche = 0;
                res = convertColor(resultat_fleche);


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    updateView(resultat_fleche, x, y);
                    v.performClick();
                    res = "";
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    arrowValue.setText(Html.fromHtml(res, Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
                } else {
                    arrowValue.setText(Html.fromHtml(res), TextView.BufferType.SPANNABLE);
                }
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        stock = new Stockage();             // init de la classe interface de stockage
        stock.onCreate(this);

        roundName = stock.getValue("roundName");
        String snumberArrow = stock.getValue("numberArrow");
        String snumberEnd = stock.getValue("numberEnd");

// Initialisation minimun
        if (snumberArrow.length() == 0 || snumberEnd.length() == 0 || roundName.length() == 0) {

            snumberArrow = "3";
            stock.updateValue("numberArrow", snumberArrow);
            snumberEnd = "20";
            stock.updateValue("numberEnd", snumberEnd);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 1");
            roundName = sdf.format(new Date());
            stock.updateValue("roundName", roundName);
            must_config = true;
        }

        NumberArrow = Integer.parseInt(snumberArrow);
        NumberEndByRound = Integer.parseInt(snumberEnd);

        archer = findViewById(R.id.archer);
        lArcher = stock.getArchers(true);


        adapter_archer = new ArrayAdapter(
                this,
                R.layout.spinner_generale
        );

        for (int i = 0; i < lArcher.size(); i++) {
            adapter_archer.add(lArcher.get(i));
        }


        /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        adapter_archer.setDropDownViewResource(R.layout.spinner_generale);
        //Enfin on passe l'adapter au Spinner et c'est tout
        archer.setAdapter(adapter_archer);

        archer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                archer_id = stock.getArcherId((String) archer.getSelectedItem());
                if (archer_id < 0)
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.missing_archer) + archer.getSelectedItem(), Toast.LENGTH_SHORT).show();

// Init lors du changement Update

                updateviewOnly();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        arrowValue = findViewById(R.id.ArrowValue);
        endNumber = findViewById(R.id.endNumber);
        result = findViewById(R.id.result);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        b10 = findViewById(R.id.button10);
        bAnnul = findViewById(R.id.Annul);
        bManque = findViewById(R.id.Manque);

        Cible = findViewById(R.id.imageCible);
        Cible.setOnTouchListener(onTouchCible);
        //stock.showArchers();

        next_archer = findViewById(R.id.bNextArcher);
        next_archer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = archer.getSelectedItemPosition();
                pos += 1;
                if (pos < archer.getCount())
                    archer.setSelection(pos);

            }
        });
        previous_archer = findViewById(R.id.bPreviousArcher);
        previous_archer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = archer.getSelectedItemPosition();
                pos -= 1;
                if (pos >= 0)
                    archer.setSelection(pos);

            }
        });

        bAnnul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock.supResultat(archer.getSelectedItem().toString(), roundName);
                updateviewOnly();

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(1, 100, 100);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(2, 100, 100);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(3, 100, 100);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(4, 100, 100);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(5, 100, 100);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(6, 100, 100);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(7, 100, 100);
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(8, 100, 100);

            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(9, 100, 100);
            }
        });
        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(10, 100, 100);
            }
        });
        bManque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stock.addResultat(archer.getSelectedItem().toString(), roundName, 0, 100, 100);
                updateviewOnly();
            }
        });


        if (must_config) {
            Intent i = new Intent(this, Activity_Config_round.class);
            startActivity(i);
        }
    }

    private void updateView(int value, double X, double Y) {
        long arrowIndex = stock.getarrowIndex(archer.getSelectedItem().toString(), roundName);

        //     Log.d("CompteCible","Stock"+Integer.toString(value)+" "+Double.toString(X)+" "+Double.toString(Y));
        if (arrowIndex < NumberArrow * NumberEndByRound) {
            stock.addResultat(archer.getSelectedItem().toString(), roundName, value, X, Y);
        }
        updateviewOnly();
    }

    private void updateviewOnly() {
        if (archer.getCount() != 0) {
            int k = archer.getSelectedItemPosition();
            if (archer.getCount() <= k || k < 0)
                archer.setSelection(0);
            long arrowIndex = stock.getarrowIndex(archer.getSelectedItem().toString(), roundName);  // Number of Arrow
            endNumber.setText(Long.toString((arrowIndex / NumberArrow) + 1));

            if (!(arrowIndex < NumberArrow * NumberEndByRound)) {
                endNumber.setText(getResources().getString(R.string.EndRound));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.EndRound) + " " + archer.getSelectedItem(), Toast.LENGTH_SHORT).show();
            }



            List<Resultat_archer> resultat_fleches = null;
            resultat_fleches = stock.getResultatArrows(archer.getSelectedItem().toString(), roundName);

            Resultat_archer resultat_archer;
            StringBuilder resultTemp = new StringBuilder();
            result.setText("");

            int sumTotal = 0;
            for (int i = 0; i <= (arrowIndex / NumberArrow); i++) {
                int sumVole = 0;
                long boucle = 0;
                List<Long> end; //End
                end = new ArrayList<Long>();
                boucle = (i == arrowIndex / NumberArrow) ? arrowIndex % NumberArrow : NumberArrow;
                for (int j = 0; j < boucle; j++) {

                    resultat_archer = resultat_fleches.get((i * NumberArrow) + j);
                    sumVole += resultat_archer.getValue();
                    end.add(resultat_archer.getValue());
                }
                Collections.sort(end, Collections.reverseOrder());
                for (int j = 0; j < boucle; j++) {
                    resultTemp.append(convertColor(end.get(j).intValue()));

            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                result.append(Html.fromHtml(fleche, Html.FROM_HTML_MODE_COMPACT));
            else
                result.append(Html.fromHtml(fleche));
                */

                }
                if (boucle != 0) {
                    sumTotal += sumVole;
                    if (sumVole < 10)
                        resultTemp.append("= " + "0").append(Long.toString(sumVole, 10)).append("<br />");
                    else
                        resultTemp.append("= ").append(Long.toString(sumVole, 10)).append("<br />");
            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                result.append(Html.fromHtml(" = " + Long.toString(sumVole) + "<br />", Html.FROM_HTML_MODE_COMPACT));
            else
                result.append(Html.fromHtml(" = " + Long.toString(sumVole) + "<br />")); */
                }
            }
            resultTemp.append(Long.toString(sumTotal)).append("<br />");
  /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
         result.append(Html.fromHtml(Long.toString(sumTotal)+"<br />", Html.FROM_HTML_MODE_COMPACT));
     else
         result.append(Html.fromHtml(Long.toString(sumTotal)+"<br />")); */

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                result.append(Html.fromHtml(resultTemp.toString(), Html.FROM_HTML_MODE_COMPACT));
            else
                result.append(Html.fromHtml(resultTemp.toString()));


            redraw();
        }
  /*  else { // No archer must condif
        Intent i = new Intent(this, Activity_Config_round.class);
        startActivity(i);
    }*/
    }

    private String convertColor(int value) {
        String retour = "";
        switch (value) {
            case 0:
                retour = "&ensp <span style=\"background : white \"><B>0</B></span>";
                break;
            case 1:
                retour = "&ensp <span style=\"background : white \"><B>1</B></span>";
                break;
            case 2:
                //                       retour = "<span style=\"background : white \">   <B>" + value + " </B></span> ";
                retour = "&ensp <span style=\"background : white \"><B>2</B></span>";
                break;

            case 3:
//                        retour = "<span style=\"background : black \"> <font color=RED ><B> " + value + "</font></B> </font></span> ";
                retour = "&ensp <span style=\"background : black \"><font color=RED ><B>3</B></font></font></span>";
                // retour = "<span style=\"background : black ;float: ;left;width: 1em\"> <font color=RED ><B> " + value + "</font></B> </font></span> ";
                break;
            case 4:
                retour = "&ensp <span style=\"background : black \"><font color=RED ><B>4</B></font></font></span>";
                // retour = "<span style=\"background : black ;float: ;left;width: 1em\"> <font color=RED ><B> " + value + "</font></B> </font></span> ";
                break;

            case 5:
                //                       retour = "<span style=\"background : blue ; \">  <font color=RED >  <B>" + value + " </font></B></span> ";
                retour = "&ensp <span style=\"background : blue ; \"> <font color=RED ><B>5</B></font></span>";
                break;
            case 6:
                retour = "&ensp <span style=\"background : blue ; \"> <font color=RED ><B>6</B></font></span>";
                break;

            case 7:
                //                       retour = " <span style=\"background : red \">  <font color=BLACK>  <B>" + value + " </font></B> </span> ";
                retour = "&ensp <span style=\"background : red \"><font color=BLACK><B>7</B></font></span>";
                break;
            case 8:
                retour = "&ensp <span style=\"background : red \"><font color=BLACK><B>8</font></B></span>";
                break;

            case 9:
                //                       retour = "<span style=\"background : yellow  \">   <B>" + value + " </B></span> ";
                retour = "&ensp <span style=\"background : yellow  \"><B>9</B></span>";
                break;
            case 10:
                retour = " <span style=\"background : yellow  \"><B>10</B></span>";
                break;
        }
        return retour;
    }
//--------------------------------

    private void redraw() {

        //   ImageView fantomCible=findViewById(R.id.imageCible);
        Bitmap bitmap;
        //   double xmax = fantomCible.getWidth() ;
        double xmax = 1000;
        //   double ymax = fantomCible.getHeight() ;
        double ymax = 1000;
        double Xscale, Yscale;

        // On récupère la coordonnée sur l'abscisse (X) de l'évènement getWidth()
        Xscale = 10 / xmax;


        // On récupère la coordonnée sur l'ordonnée (Y) de l'évènement getHeight()
        Yscale = 10 / ymax;
        bitmap = Bitmap.createBitmap((int) xmax, (int) ymax, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate((int) xmax / 2, (int) ymax / 2);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resultat_archer resultat_archer;

        double moyX = 0, moyY = 0, nb_valeur_moyenne = 0.;
        long arrowIndex = stock.getarrowIndex(archer.getSelectedItem().toString(), roundName);
        long boucle = (arrowIndex % NumberArrow != 0) ? arrowIndex % NumberArrow : NumberArrow;
        for (int i = 0; i < boucle; i++) {

            resultat_archer = stock.getResultatArrow(archer.getSelectedItem().toString(), roundName, (arrowIndex - i));
            if (resultat_archer.x < 100) {
                nb_valeur_moyenne += 1;
                moyX += resultat_archer.x;
                moyY += resultat_archer.y;
                paint.setColor(Color.BLACK);
                canvas.drawCircle((float) (resultat_archer.x / Xscale), (float) (resultat_archer.y / Yscale), (float) (0.3 / Xscale), paint);
//                Log.d("CompteCible","trace"+Long.toString(resultat_archer.arrow)+" "+Double.toString(resultat_archer.x)+" "+Double.toString(resultat_archer.y));
            }
        }
        paint.setColor(Color.GREEN);
        canvas.drawCircle((float) (moyX / nb_valeur_moyenne / Xscale), (float) (moyY / nb_valeur_moyenne / Yscale), (float) (0.2 / Xscale), paint);
        Cible.setImageBitmap(bitmap);


    }
    /*********************************************************************************/
    /** Managing LifeCycle and database open/close operations ************************/
    /*********************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        // Open stockage
        stock.openDB();
        adapter_archer.clear();
        lArcher = stock.getArchers(true); // get modification  on Database after config Round
        for (int i = 0; i < lArcher.size(); i++) {
            adapter_archer.add(lArcher.get(i));
        }
        roundName = stock.getValue("roundName");
        String snumberArrow = stock.getValue("numberArrow");
        String snumberEnd = stock.getValue("numberEnd");
        NumberArrow = Integer.parseInt(snumberArrow);
        NumberEndByRound = Integer.parseInt(snumberEnd);
        updateviewOnly();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close stockage
        stock.closeDB();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.config_round) {

            Intent i = new Intent(this, Activity_Config_round.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.maintenance) {

            Intent i = new Intent(this, Activity_maintenance.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.resultat_round) {

            Intent i = new Intent(this, Activity_resultat_round.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
