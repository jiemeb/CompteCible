package com.herault.comptecible;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.herault.comptecible.utils.Stockage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Activity_MainActivity extends AppCompatActivity {

    private static final double CONSTANTE_nbDivisionCible = 10.;

    double Xdecallage;
    double Ydecallage;

    private double pointageOffset = 2;
    private TextView arrowValue = null;
    private TextView endNumber = null;
    private TextView end = null;
    private Spinner archer = null;
    private long archer_id = 0;
    private EditText result = null;
    private ImageView Cible = null;
    private Stockage stock = null;
    private List<String> lArcher;
    private boolean must_config = false;
    private ArrayAdapter adapter_archer;
    private String roundName = null;
    private int NumberArrow = 0;
    private int NumberEndByRound = 0;
    private boolean orientationLand = false;
                                //
    private final View.OnTouchListener onTouchCible = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            double xmax = v.getWidth();
            double ymax = v.getHeight();
            double Xscale, Yscale;
            double x, y;
            int resultat_fleche = 0;
/*            bitmap = Bitmap.createBitmap((int) xmax, (int) ymax, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            //  canvas.translate((float) (xmax / 2), (float) ymax / 2);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
     */
            arrowValue.setVisibility(View.VISIBLE);


            // On récupère la coordonnée sur l'abscisse (X) de l'évènement getWidth()
            Xscale = 10 / xmax;
            Yscale = 10 / ymax;
            double xcurrent = event.getX() - xmax / 2.;
            double ycurrent = event.getY() - ymax / 2.;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // "Land"
                if (orientationLand) {

                    Ydecallage = 0.0;
                    if (xcurrent * Xscale < -1.0)
                        Xdecallage = -pointageOffset;
                    else
                        Xdecallage = pointageOffset;
                } else {
                    //Portrait
                    Xdecallage = 0.0;
                    Ydecallage = pointageOffset;
                }
            }

                x = (xcurrent * Xscale) - Xdecallage;
                y = (ycurrent * Yscale) - Ydecallage;

                if (y > -CONSTANTE_nbDivisionCible / 2. && y < CONSTANTE_nbDivisionCible / 2. && x > -CONSTANTE_nbDivisionCible / 2. && x < CONSTANTE_nbDivisionCible / 2.) {
                    resultat_fleche = (int) (CONSTANTE_nbDivisionCible - (int) (Math.sqrt(Math.pow(x, 2.) + Math.pow(y, 2.)) - (0.3)));//
                    // Cible is on 6 to 10
                    if (resultat_fleche < 6)
                        resultat_fleche = 0;
                }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                updateView(resultat_fleche, x, y);
                v.performClick();
                arrowValue.setVisibility(View.INVISIBLE);
            } else {
                GradientDrawable sd;

                switch (resultat_fleche) {
                    case 0:
                    case 1:
                    case 2:
                        //   arrowValue.setBackgroundColor(Color.WHITE);
                        sd = (GradientDrawable) arrowValue.getBackground().mutate();
                        sd.setColor(getResources().getColor(R.color.BlancCible));
                        sd.invalidateSelf();
                        break;

                    case 3:
                    case 4:
                        //    arrowValue.setBackgroundColor(Color.BLACK);
                        sd = (GradientDrawable) arrowValue.getBackground().mutate();
                        sd.setColor(getResources().getColor(R.color.NoirCible));
                        sd.invalidateSelf();
                        break;

                    case 5:
                    case 6:
                        //arrowValue.setBackgroundColor(getResources().getColor(R.color.BleuCible));
                        sd = (GradientDrawable) arrowValue.getBackground().mutate();
                        sd.setColor(getResources().getColor(R.color.BleuCible));
                        sd.invalidateSelf();
                        break;

                    case 7:
                    case 8:
                        //  arrowValue.setBackgroundColor(getResources().getColor(R.color.RougeCible));
                        sd = (GradientDrawable) arrowValue.getBackground().mutate();
                        sd.setColor(getResources().getColor(R.color.RougeCible));
                        sd.invalidateSelf();
                        break;

                    case 9:
                    case 10:
                        sd = (GradientDrawable) arrowValue.getBackground().mutate();
                        sd.setColor(getResources().getColor(R.color.JauneCible));
                        sd.invalidateSelf();
                        //     arrowValue.setBackgroundColor(getResources().getColor(R.color.JauneCible));
                        break;
                }


                Log.d("CompteCible", "onTouch " + Double.toString(x) + " " + Double.toString(y) + " " + Integer.toString(resultat_fleche));
                arrowValue.setText(String.valueOf(resultat_fleche));
                drawImpact(x, y);
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
        String sPointerOffset= stock.getValue("pointageOffset");
        if (sPointerOffset.isEmpty())
        {
            sPointerOffset = "2" ;
            stock.updateValue("pointageOffset",sPointerOffset);
        }
        pointageOffset = Double.parseDouble ( sPointerOffset);


        int iorientation = getResources().getConfiguration().orientation;
        if (iorientation == Configuration.ORIENTATION_LANDSCAPE) {
        orientationLand = true;
        }

        roundName = stock.getValue("roundName");
        String snumberArrow = stock.getValue("numberArrow");
        String snumberEnd = stock.getValue("numberEnd");
        archer = findViewById(R.id.archer);
        lArcher = stock.getArchers(true);

        int countArcher = lArcher.size();
// Initialisation minimun
        if (snumberArrow.isEmpty() || snumberEnd.isEmpty() || roundName.isEmpty() || countArcher == 0) {
            snumberArrow = "3";
            stock.updateValue("numberArrow", snumberArrow);
            snumberEnd = "20";
            stock.updateValue("numberEnd", snumberEnd);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 1");
            roundName = sdf.format(new Date());
            stock.updateValue("roundName", roundName);
            must_config = true;
        }

        adapter_archer = new ArrayAdapter(
                this,
                R.layout.spinner_generale
        );

        for (int i = 0; i < lArcher.size(); i++) {
            adapter_archer.add(lArcher.get(i));
        }

        NumberArrow = Integer.parseInt(snumberArrow);
        NumberEndByRound = Integer.parseInt(snumberEnd);



        /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        adapter_archer.setDropDownViewResource(R.layout.spinner_generale);
        //Enfin on passe l'adapter au Spinner et c'est tout
        archer.setAdapter(adapter_archer);
// why for somme one archer is empty !
        archer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 int spinnerCountElement =archer.getCount() ;
                 if (spinnerCountElement >= position+1) {
                     String ArcherSel = (String) archer.getSelectedItem();
                     archer_id = stock.getArcherId(ArcherSel);

                     if (archer_id < 0)
                         Toast.makeText(getApplicationContext(), getResources().getString(R.string.missing_archer) + archer.getSelectedItem(), Toast.LENGTH_SHORT).show();
// Init lors du changement Update
                     updateviewOnly();
                 }
                 else
                     Log.d("CompteCible", "onItemSelected: archer count "+ String.valueOf(spinnerCountElement));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        arrowValue = findViewById(R.id.ArrowValue);
        end = findViewById(R.id.textEnd);
        endNumber = findViewById(R.id.endNumber);
        result = findViewById(R.id.result);
        Button b1 = findViewById(R.id.button1);
        Button b2 = findViewById(R.id.button2);
        Button b3 = findViewById(R.id.button3);
        Button b4 = findViewById(R.id.button4);
        Button b5 = findViewById(R.id.button5);
        Button b6 = findViewById(R.id.button6);
        Button b7 = findViewById(R.id.button7);
        Button b8 = findViewById(R.id.button8);
        Button b9 = findViewById(R.id.button9);
        Button b10 = findViewById(R.id.button10);
        Button bAnnul = findViewById(R.id.Annul);
        Button bManque = findViewById(R.id.Manque);

        Cible = findViewById(R.id.imageCible);
        Cible.setOnTouchListener(onTouchCible);
        //stock.showArchers();

        Button next_archer = findViewById(R.id.bNextArcher);
        next_archer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = archer.getSelectedItemPosition();
                pos += 1;
                if (pos < archer.getCount())
                    archer.setSelection(pos);
                else
                    archer.setSelection(0);

            }
        });

        Button previous_archer = findViewById(R.id.bPreviousArcher);
        previous_archer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = archer.getSelectedItemPosition();
                pos -= 1;
                if (pos >= 0)
                    archer.setSelection(pos);
                else
                    archer.setSelection( archer.getCount() - 1);
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
                updateView(0, 100, 100);
            }
        });

        if (must_config) {
            Intent i = new Intent(this, Activity_config_round.class);
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
        if ((archer.getCount()) >= 0) {
            int k = archer.getSelectedItemPosition();
            if (k < 0) { // pas d'archer dans la base'
                must_config = true;
            } else {
                if (archer.getCount() <= k || k < 0)
                    archer.setSelection(0);
                Log.d("CompteCible", "updateview " + Integer.toString(k) + " " + roundName);

                long arrowIndex = stock.getarrowIndex(archer.getSelectedItem().toString(), roundName);  // Number of Arrow


                if (!(arrowIndex < NumberArrow * NumberEndByRound)) {
                    end.setText(getResources().getString(R.string.EndRound));
                    endNumber.setText("");
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.EndRound) + " " + archer.getSelectedItem(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    end.setText(getResources().getString(R.string.End));
                    endNumber.setText(Long.toString((arrowIndex / NumberArrow) + 1));
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
        } else {                                                              // No archer must condif
      /*  Intent i = new Intent(this, Activity_config_round.class);
        startActivity(i); */
            must_config = true;
        }
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

        double Xscale, Yscale;
        double xmax = Cible.getWidth();
        double ymax = Cible.getHeight();
        if (xmax == 0) {
            xmax = ymax = 100;
        }

        Xscale = 10.00 / xmax;
        Yscale = 10.00 / ymax;
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

    private void drawImpact(double x, double y) {

        //   ImageView fantomCible=findViewById(R.id.imageCible);
        Bitmap bitmap;

        double Xscale, Yscale;
        double xmax = Cible.getWidth();
        double ymax = Cible.getHeight();
        if (xmax == 0) {
            xmax = ymax = 100;
        }

        Xscale = 10.00 / xmax;
        Yscale = 10.00 / ymax;

        bitmap = Bitmap.createBitmap((int) xmax, (int) ymax, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //    canvas.translate((int) xmax / 2, (int) ymax / 2);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.CYAN);
        //      canvas.drawCircle((float) ((x) / Xscale), (float) ((y) / Yscale), (float) (0.3 / Xscale), paint);
        canvas.drawCircle((float) ((x + CONSTANTE_nbDivisionCible / 2.) / Xscale), (float) ((y + CONSTANTE_nbDivisionCible / 2.) / Yscale), (float) (0.3 / Xscale), paint);

        Cible.setImageBitmap(bitmap);


    }
    /*********************************************************************************
    ** Managing LifeCycle and database open/close operations ************************
    *********************************************************************************/
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
        String sPointerOffset= stock.getValue("pointageOffset");
        if (sPointerOffset.isEmpty())
        {
            sPointerOffset = "2" ;
            stock.updateValue("pointageOffset",sPointerOffset);
        }
        pointageOffset = Double.parseDouble ( sPointerOffset);


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

        if (id == R.id.config_round) {

            Intent i = new Intent(this, Activity_config_round.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.archerMenu) {

            Intent i = new Intent(this, Activity_archer.class);
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
        if (id == R.id.Exit) {
            this.finish();
            return true;
        }
        if (id == R.id.apropos) {
            Intent i = new Intent(this, Activity_apropos.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
