package com.herault.comptecible.utils;


import com.herault.comptecible.Note;
import com.herault.comptecible.R;
import com.herault.comptecible.Resultat_archer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class Stockage {

    private Activity context;
    private SQLiteDatabase db = null;
    private Db_resultat base;
     void initDB (SQLiteDatabase db) { this.db = db ;}
    public void onCreate(AppCompatActivity mainActivity) {
        this.context = mainActivity;
        base = new Db_resultat(mainActivity, Db_resultat.Constants.DATABASE_NAME, null, Db_resultat.Constants.DATABASE_VERSION);
        openDB();
    }

    public boolean showDB() {
        if (db == null) {
            return (false);
        } else {
            return (true);
        }
    }

    /**
     * * Open the database* *
     *
     * @throws SQLiteException
     */
    public void openDB() throws SQLiteException {
        try {
            db = base.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = base.getReadableDatabase();
        }
    }

    /**
     * Close Database
     */
    public void closeDB() {
        db.close();
        db=null;
    }

    /*-----------------------------------record-------------------------------------------------*/

    // -----------------------------Methode archer -----------------------------------------------------
// Get all resultat for one archer for one Round
    public List<Resultat_archer> getResultatArrows(String archer, String roundName) {


        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ARCHERS + "," + Db_resultat.Constants.ROUNDS;

        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_VALUE,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ARROW,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_X,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_Y,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_PLUS ,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ARROW_NUMBER };

        String selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =? AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_COL_NAME + " = ? AND "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME + " = "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_ID_ARCHERS + " AND "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND + " = "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS;
        String[] selectionArg = new String[]{roundName, archer};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
       // String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ARROW + " ASC";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);

        Cursor cursor = db.query(table, projections, selection,
                selectionArg, null, null, orderBy, null);
        // The elements to retrieve

        List<Resultat_archer> rValue = new ArrayList<Resultat_archer>();


        // The associated index within the cursor
        int indexValue = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_VALUE);
        int indexArrow = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ARROW);
        int indexX = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_X);
        int indexY = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_Y);
        int indexPlus = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_PLUS);
        int indexArrowName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ARROW_NUMBER);


        if (cursor.moveToFirst()) {
            // Browse the results list:

            int count = 0;
            do {
                Resultat_archer rArcher = new Resultat_archer();
                rArcher.name = cursor.getString(indexArrow);
                rArcher.value = cursor.getInt(indexValue);
                rArcher.x = cursor.getDouble(indexX);
                rArcher.y = cursor.getDouble(indexY);
                rArcher.dixPlus = cursor.getInt(indexPlus);
                rArcher.arrowName = cursor.getInt(indexArrowName);
                rValue.add(rArcher);
                count++;
            } while (cursor.moveToNext());

        }

        cursor.close();
        return rValue;

    }

    // Get for on Archer One arrow
    public Resultat_archer getResultatArrow(String archer, String roundName, long arrow) {
        int value = 0;
        long Id = getArcherId(archer);
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_COL_VALUE, Db_resultat.Constants.KEY_COL_X, Db_resultat.Constants.KEY_COL_Y, Db_resultat.Constants.KEY_COL_ARROW_NUMBER};
        final int cursorIdColNumber = 0;

        String selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =? AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_COL_NAME + " = ? AND "
                + Db_resultat.Constants.KEY_COL_ARROW + " =? AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + " = "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND + " AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_ID_ARCHERS + "="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME;
        String[] selectionArg = new String[]{roundName, archer, String.valueOf(arrow)};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = Db_resultat.Constants.KEY_COL_NAME ;
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ARCHERS + "," + Db_resultat.Constants.ROUNDS;
        Cursor cursor = db.query(table, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve
        Resultat_archer rArcher = new Resultat_archer();
        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexValue = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_VALUE);
            int indexX = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_X);
            int indexY = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_Y);
            int indexArrowName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ARROW_NUMBER);
            // Browse the results list:
            int count = 0;
            do {
                rArcher.value = cursor.getInt(indexValue);
                rArcher.x = cursor.getDouble(indexX);
                rArcher.y = cursor.getDouble(indexY);
                rArcher.arrowName= cursor.getInt(indexArrowName);
                count++;
            } while (cursor.moveToNext());

        }


        cursor.close();
        return rArcher;
    }
//-------------------------------------------------------------------
    // get for  archer  result for all round
public List<Resultat_archer> getResultatAllRound(String name_archer) {
        String filter[]= {};
        return getResultatAllRound( name_archer ,  filter) ;
}

public List<Resultat_archer> getResultatAllRound(String name_archer , String[] filter) {
        Long archer_id = getArcherId(name_archer);

        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"SUM (" + Db_resultat.Constants.KEY_COL_VALUE + " )",
                Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME};
        final int cursorIdColNumber = 0;
        String filterRound ="";

    for (String filterI:filter
         ) {
        if (!filterI.isEmpty())
            filterRound += " AND "+Db_resultat.Constants.KEY_COL_ROUND_TYPE+" LIKE \"%"+filterI+"%\" " ;
    }
        String selection = Db_resultat.Constants.KEY_COL_ID_NAME + " =? AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + "="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND
                + filterRound;
        String[] selectionArg = new String[]{Long.toString(archer_id)};
        // The groupBy clause:
        String groupBy = Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + "  ASC";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ROUNDS;
        Cursor cursor = db.query(table, projections, selection,
                selectionArg, groupBy, null, orderBy, null);
        // The elements to retrieve
        List<Resultat_archer> rArcher = new ArrayList<Resultat_archer>();

        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ROUND_NAME);
            Resultat_archer rowArcher;
            // Browse the results list:
            int count = 0;
            do {
                rowArcher = new Resultat_archer();
                rowArcher.value = cursor.getInt(0);
                rowArcher.name = cursor.getString(indexName);
                int nb10Total =  getResultatRoundCompte(cursor.getString(indexName), String.valueOf(getArcherId(name_archer)), "10");
                rowArcher.information = " nb 10+= " + String.valueOf(nb10Total - getResultatRoundCompte(cursor.getString(indexName), String.valueOf(getArcherId(name_archer)), "10Plus"))
                        + " nb 10= " + String.valueOf(nb10Total)
                        + " nb 9= " + getResultatRoundCompte(cursor.getString(indexName), String.valueOf(getArcherId(name_archer)), "9")
                        + " X= " + getResultatRoundCompte(cursor.getString(indexName), String.valueOf(getArcherId(name_archer)), "0");
                rArcher.add(rowArcher);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return rArcher;
    }
// ------------------------------
// get for all archer all result for one round

    public int getResultatRoundCompte(String roundName, String Archer_ID, @NonNull String RefValue) {
        int value = 0;
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"COUNT (" + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_VALUE + " )"};
        final int cursorIdColNumber = 0;
        String selection ;
        String[] selectionArg;
        if (RefValue.compareTo("10Plus") != 0) {
         selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =?  AND "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME + " =? AND "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_VALUE + " =? AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + " = "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND;
        selectionArg = new String[]{roundName, Archer_ID, RefValue};
    }
    else
    {
         selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =?  AND "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME + " =? AND "
                 + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_VALUE + " = 10 AND "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_PLUS + " = 1 AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + "="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND;
        selectionArg = new String[]{roundName, Archer_ID};
    }


        // The groupBy clause:
        // String groupBy = Db_resultat.Constants.RESULTATS+"."+Db_resultat.Constants.KEY_COL_ID_ROUND;
        // The having clause
        //String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = "COUNT (" +Db_resultat.Constants.RESULTATS+"."+Db_resultat.Constants.KEY_COL_VALUE + " ) Desc";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ROUNDS;
        Cursor cursor = db.query(table, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve
        int numberOf = 0;

        if (cursor.moveToFirst()) {
            // The associated index within the cursor
            int indexValue = 0;
            // Browse the results list:
            int count = 0;
            numberOf = cursor.getInt(0);
            count++;
        }
        cursor.close();
        return numberOf;
    }

    //----------------- get resultat round for all archers
    public List<Resultat_archer> getResultatAll(String roundName) {
        int value = 0;
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"SUM (" + Db_resultat.Constants.KEY_COL_VALUE + " )", Db_resultat.Constants.KEY_COL_ID_NAME};
        final int cursorIdColNumber = 0;

        String selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =?  AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + "="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND;
        String[] selectionArg = new String[]{roundName};

        // The groupBy clause:
        String groupBy = Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = "SUM (" + Db_resultat.Constants.KEY_COL_VALUE + " ) Desc";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ROUNDS;
        Cursor cursor = db.query(table, projections, selection,
                selectionArg, groupBy, null, orderBy, null);
        // The elements to retrieve
        List<Resultat_archer> rArcher = new ArrayList<Resultat_archer>();

        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexValue = cursor.getColumnIndex("SUM (" + Db_resultat.Constants.KEY_COL_VALUE + " ) Desc");
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ID_NAME);
            Resultat_archer rowArcher;
            // Browse the results list:
            int count = 0;
            do {
                rowArcher = new Resultat_archer();
                rowArcher.value = cursor.getInt(0);
                rowArcher.name = getArcher(cursor.getInt(indexName));
                rowArcher.information = " nb10+= " + getResultatRoundCompte(roundName, String.valueOf(cursor.getInt(indexName)), "10Plus")
                        + " nb10= " + getResultatRoundCompte(roundName, String.valueOf(cursor.getInt(indexName)), "10")
                        + " nb9= " + getResultatRoundCompte(roundName, String.valueOf(cursor.getInt(indexName)), "9")
                        + " X= " + getResultatRoundCompte(roundName, String.valueOf(cursor.getInt(indexName)), "0");

                rArcher.add(rowArcher);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return rArcher;
    }

    //-------------------------------------------------------------------get last arrow index
    public long getarrowIndex(String archer, String roundName) {
        int arrow = 0;
        // get flecheIndex
        //select arrow  from resultat,round where rounds.name=roundname and _id1 =archer(id) and round.id=resultat.roundid
        long Id = getArcherId(archer);
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"MAX (" + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ARROW + ")"};
        final int cursorIdColNumber = 0;
        // To add a Where clause you can either do that:
        //qb.appendWhere(Db_resultat.Constants.KEY_COL_ROUND+ "=?"+Db_resultat.Constants.KEY_COL_ID1 +" = ?");
        // Or that:
        String selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =? AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_COL_NAME + " = ? AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + " ="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND + " AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_ID_ARCHERS + " ="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME;
        String[] selectionArg = new String[]{roundName, archer};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = Db_resultat.Constants.KEY_COL_NAME ;
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ARCHERS + "," + Db_resultat.Constants.ROUNDS;
        Cursor cursor = db.query(table, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrow = cursor.getInt(0);
        }

        cursor.close();
        return (arrow);
    }

    //---------------------------------- supression archer for one Round
    public void supResultat(String archer, String roundName) {
        int arrow = 0;
        // get flecheIndex
        //select round from resultat where round=roundname and _id1 =archer(id)
        long Id = getArcherId(archer);

        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"MAX (" + Db_resultat.Constants.KEY_COL_ARROW + ")", Db_resultat.Constants.KEY_COL_ID1};
        final int cursorIdColNumber = 0;
        // To add a Where clause you can either do that:
        //qb.appendWhere(Db_resultat.Constants.KEY_COL_ROUND+ "=?"+Db_resultat.Constants.KEY_COL_ID1 +" = ?");
        // Or that:
        String selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =? AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_COL_NAME + " = ? AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + " ="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND + " AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_ID_ARCHERS + " = "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME;

        String[] selectionArg = new String[]{roundName, archer};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = Db_resultat.Constants.KEY_COL_NAME ;
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ARCHERS + "," + Db_resultat.Constants.ROUNDS;
        int resutat_id = 0;
        Cursor cursor = db.query(table, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrow = cursor.getInt(0);
            resutat_id = cursor.getInt(1);
        }

        selection = Db_resultat.Constants.KEY_COL_ID1 + " =? AND " +
                Db_resultat.Constants.KEY_COL_ARROW + " =? ";

        selectionArg = new String[]{String.valueOf(resutat_id), String.valueOf(arrow)};

        // Insert the line in the database

        db.delete(Db_resultat.Constants.RESULTATS, selection, selectionArg);
        cursor.close();

    }

    //----------------------------- add resultat for on archer for one round
    public long addResultat(String archer, String roundName, int value, double X, double Y,int dixPlus)
    {
        return(addResultat( archer,  roundName,  value,  X,  Y, dixPlus,0));
    }
    public long addResultat(String archer, String roundName, int value, double X, double Y,int dixPlus,int arrow_name) {
        int arrow = 0;
        // get flecheIndex
        //select round from resultat where round=roundname and _id1 =archer(id)
        long Id = getArcherId(archer);
        Long idRound = getRoundId(roundName);
        if (idRound < 0)          // Create Round
        {
            idRound = addRound(roundName);
        }
        idRound = getRoundId(roundName);
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"MAX (" + Db_resultat.Constants.KEY_COL_ARROW + ")"};
        final int cursorIdColNumber = 0;
        // To add a Where clause you can either do that:
        //qb.appendWhere(Db_resultat.Constants.KEY_COL_ROUND+ "=?"+Db_resultat.Constants.KEY_COL_ID1 +" = ?");
        // Or that:
        String selection = Db_resultat.Constants.KEY_COL_ID_ROUND + " =? AND " + Db_resultat.Constants.KEY_COL_ID_NAME + " = ?";
        String[] selectionArg = new String[]{String.valueOf(idRound), String.valueOf(Id)};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = Db_resultat.Constants.KEY_COL_NAME ;
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);

        Cursor cursor = db.query(Db_resultat.Constants.RESULTATS, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            arrow = cursor.getInt(0);
            arrow++;
        }

        // -------------------
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_resultat.Constants.KEY_COL_ID_ROUND, idRound);
        contentValues.put(Db_resultat.Constants.KEY_COL_ARROW, arrow);
        contentValues.put(Db_resultat.Constants.KEY_COL_VALUE, value);
        contentValues.put(Db_resultat.Constants.KEY_COL_X, X);
        contentValues.put(Db_resultat.Constants.KEY_COL_Y, Y);
        contentValues.put(Db_resultat.Constants.KEY_COL_PLUS, dixPlus);
        contentValues.put(Db_resultat.Constants.KEY_COL_ARROW_NUMBER, arrow_name);
        contentValues.put(Db_resultat.Constants.KEY_COL_ID_NAME, Id);


        // Insert the line in the database

        db.insert(Db_resultat.Constants.RESULTATS, null, contentValues);
        cursor.close();
        return (arrow);
    }

    //------------------------------------------------------------------------------------------------
    /*---------------------------------------Fonction Member of CompteCible Value ---------------*/
// Store Value for all Activity
    public void updateValue(String name, String value) {
        ContentValues cv = new ContentValues();
        cv.put(Db_resultat.Constants.KEY_COL_COMPTE_VALUE, value);

        if (db.update(Db_resultat.Constants.COMPTE_CIBLE, cv, Db_resultat.Constants.KEY_COL_COMPTE_NAME + "= ?", new String[]{name}) <= 0) {

            cv.put(Db_resultat.Constants.KEY_COL_COMPTE_NAME, name);
            db.insert(Db_resultat.Constants.COMPTE_CIBLE, null, cv);
        }

    }

    //  get Value
    public String getValue(String name) {

        //select value fron Comptecible where name=name
        //Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_COL_COMPTE_NAME,
                Db_resultat.Constants.KEY_COL_COMPTE_VALUE};

        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        // To add a Where clause you can either do that:
        // qb.appendWhere(Constants.KEY_COL_HAIR_COLOR+ "=blond");
        // Or that:
        String selection = Db_resultat.Constants.KEY_COL_COMPTE_NAME + "=?";
        String[] selectionArg = new String[]{name};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        //String orderBy = Db_resultat.Constants.KEY_COL_NAME ;
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);

        Cursor cursor = db.query(Db_resultat.Constants.COMPTE_CIBLE, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve
        String value = "";
        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexValue = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_COMPTE_VALUE);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_COMPTE_NAME);
            // Browse the results list:
            int count = 0;
            do {
                value = cursor.getString(indexValue);
                name = cursor.getString(indexName);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (value);

    }

    //------------------------------------------------------------------------------------------------
    /*----------------------Table Archer and ArcherRound    ---------------------------------*/

    // add archer in database or current round
    public long addArcher(String name, boolean round) {

        // -------------------
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_resultat.Constants.KEY_COL_NAME, name);
        contentValues.put(Db_resultat.Constants.KEY_COL_INFORMATION, "");

        // Insert the line in the database
        if (round) {
            return (db.insert(Db_resultat.Constants.ARCHERS_ROUND, null, contentValues));
        } else {
            return (db.insert(Db_resultat.Constants.ARCHERS, null, contentValues));
        }
    }
    // Drop all Archers from database or round
    public void dropArchers(boolean round) {
        if (!round) {
            db.delete(Db_resultat.Constants.ARCHERS, null, null);
            db.delete(Db_resultat.Constants.RESULTATS, null, null);
            db.delete(Db_resultat.Constants.COMPTE_CIBLE, null, null);
            db.delete(Db_resultat.Constants.ARCHERS_NOTE, null, null);

        }
        db.delete(Db_resultat.Constants.ARCHERS_ROUND, null, null);
    }

    // Suppress one Archer in database
    //-----------
    public void dropArcher(String name) {

        long id_name = getArcherId(name);
        db.delete(Db_resultat.Constants.RESULTATS, Db_resultat.Constants.KEY_COL_ID_NAME + "= ?", new String[]{Integer.toString((int) id_name)});
        db.delete(Db_resultat.Constants.ARCHERS_ROUND, Db_resultat.Constants.KEY_COL_NAME + "= ?", new String[]{name});
        db.delete(Db_resultat.Constants.ARCHERS_NOTE, Db_resultat.Constants.KEY_ID_ARCHERS + "= ?", new String[]{Integer.toString((int) id_name)});
        db.delete(Db_resultat.Constants.ARCHERS, Db_resultat.Constants.KEY_COL_NAME + "= ?", new String[]{name});

    }
    // Get all Archer from database
    //---------
    public String showArchers() {
// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_ARCHERS,
                Db_resultat.Constants.KEY_COL_NAME};
        // And then store the column index answered by the request (we present
        // an other way to
        // retireve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        // To add a Where clause you can either do that:
        // qb.appendWhere(Constants.KEY_COL_HAIR_COLOR+ "=blond");
        // Or that:
	/*String selection = //Constants.KEY_COL_HAIR_COLOR + "=?";
	String[] selectionArg = new String[] { "blond" };
	// The groupBy clause:
	String groupBy = Constants.KEY_COL_EYES_COLOR;
	// The having clause
	String having = null;*/
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.KEY_COL_NAME + "  ASC";
        // Maximal size of the results list
        String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        Cursor cursor = db.query(Db_resultat.Constants.ARCHERS, projections, null,
                null, null, null, orderBy, maxResultsListSize);
        displayResults(cursor);

	/*// Using the QueryBuilder
	// Create a Query SQLite object
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(db_resultat.Constants.ARCHERS);
	// qb.appendWhere(Constants.KEY_COL_HAIR_COLOR+ "=blond");
	qb.setDistinct(true);
	cursor = qb.query(db, projections, selection, selectionArg, groupBy,
		having, orderBy);
	displayResults(cursor); */
        cursor.close();
        return ("OK");

    }

    public long insertArrayArchers(ArrayList<String> list, boolean round) {
        long max, error = 0;
        int i;
        max = list.size();
        for (i = 0; i < max; i++) {
            error = addArcher(list.get(i), true);
        }
        return error;
    }

    // Get id archer form his name
    //---------
    public long getArcherId(String name) {

        //select id fron archers where name=name
        //Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_ARCHERS,
                Db_resultat.Constants.KEY_COL_NAME};

        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        // To add a Where clause you can either do that:
        // qb.appendWhere(Constants.KEY_COL_HAIR_COLOR+ "=blond");
        // Or that:
        String selection = Db_resultat.Constants.KEY_COL_NAME + "=?";
        String[] selectionArg = new String[]{name};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.KEY_COL_NAME + "  ASC";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        Cursor cursor = db.query(Db_resultat.Constants.ARCHERS, projections, selection,
                selectionArg, null, null, orderBy, null);
        // The elements to retrieve
        int colId = -1;
        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_ARCHERS);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (colId);
    }

    // Get archer form his ID
    //---------
    private String getArcher(int colId) {

        //select name fron archers where id=colId
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{
                Db_resultat.Constants.KEY_COL_NAME};

        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        // To add a Where clause you can either do that:
        // qb.appendWhere(Constants.KEY_COL_HAIR_COLOR+ "=blond");
        // Or that:
        String selection = Db_resultat.Constants.KEY_ID_ARCHERS + "=?";
        String[] selectionArg = new String[]{Integer.toString(colId)};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.KEY_COL_NAME + "  ASC";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        Cursor cursor = db.query(Db_resultat.Constants.ARCHERS, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve
        String name = "";
        if (cursor.moveToFirst()) {

            // The associated index within the cursor

            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME);
            // Browse the results list:
            int count = 0;
            do {

                name = cursor.getString(indexName);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (name);
    }

    // Get archers from database or current round
    //
    public ArrayList getArchers(Boolean round) {
        ArrayList retour = new ArrayList();
        Cursor cursor = null;

// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_ARCHERS,
                Db_resultat.Constants.KEY_COL_NAME};
        // And then store the column index answered by the request (we present
        // an other way to
        // retireve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        if (round) {
            cursor = db.query(Db_resultat.Constants.ARCHERS_ROUND, projections, null,
                    null, null, null, Db_resultat.Constants.KEY_ID_ARCHERS, null);
            //displayResults(cursor);
        } else {
            cursor = db.query(Db_resultat.Constants.ARCHERS, projections, null,
                    null, null, null, Db_resultat.Constants.KEY_COL_NAME, null);
            // displayResults(cursor);
        }

        if (cursor.moveToFirst()) {
            // The elements to retrieve
            int colId;
            String name;

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_ARCHERS);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);
                retour.add(name);

                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (retour);
    }

    // Get all archers for one round
    //----------------------------------------------------------------------------------------------------------------------------------
    public ArrayList getArchers(String round) {
        ArrayList retour = new ArrayList();
        Cursor cursor = null;

// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_COL_NAME};
        String selection = Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_COL_ROUND_NAME + " =? AND "
                + Db_resultat.Constants.ROUNDS + "." + Db_resultat.Constants.KEY_ID_ROUNDS + " ="
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_ROUND + " AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_ID_ARCHERS + " = "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME;

        String[] selectionArg = new String[]{round};
        String groupBy = Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME;

        // And then store the column index answered by the request (we present

        // an other way to
        // retireve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ARCHERS + "," + Db_resultat.Constants.ROUNDS;

        cursor = db.query(table, projections, selection, selectionArg, groupBy, null, null, null);
        // displayResults(cursor);


        if (cursor.moveToFirst()) {
            // The elements to retrieve
            int colId;

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME);

            // Browse the results list:
            int count = 0;
            do {
                retour.add(cursor.getString(indexId));
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (retour);
    }


    //----------------------------------------------------------------------------------------------------------------------------------


    private void displayResults(Cursor cursor) {
        // then browse the result:
        if (cursor.moveToFirst()) {
            // The elements to retrieve
            int colId;
            String name;

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_ARCHERS);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);

                Toast.makeText(
                        context,
                        "Retrieve element :" + name + "," + " ("
                                + colId + ")", Toast.LENGTH_LONG).show();
                count++;
            } while (cursor.moveToNext());
            Toast.makeText(context,
                    "The number of elements retrieved is " + count,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();
        }
        cursor.close();
    }

    //------------------------------------------------------------------------------------------------------------
    // Round method
    // getId of one round
    public long getRoundId(String name) {

        //select id fron archers where name=name
        //Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_ROUNDS,
                Db_resultat.Constants.KEY_COL_ROUND_NAME};

        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        // To add a Where clause you can either do that:
        // qb.appendWhere(Constants.KEY_COL_HAIR_COLOR+ "=blond");
        // Or that:
        String selection = Db_resultat.Constants.KEY_COL_ROUND_NAME + "=?";
        String[] selectionArg = new String[]{name};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.KEY_COL_ROUND_NAME + "  ASC";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        Cursor cursor = db.query(Db_resultat.Constants.ROUNDS, projections, selection,
                selectionArg, null, null, orderBy, null);
        // The elements to retrieve
        int colId = -1;
        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_ROUNDS);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ROUND_NAME);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (colId);
    }

    // return an array of round name

    public ArrayList getRounds() {
        String filter[] = new String[] {""};
        return getRounds( filter);
    }

    public ArrayList getRounds(@NonNull String[] filter) {

        ArrayList retour = new ArrayList();
        Cursor cursor = null;
        String filterRound ="";
        if (filter.length > 0) {
            for (String filterI : filter) {
                if(!filterI.isEmpty())
                {
                    if (filterRound != "")
                        filterRound += " AND " + Db_resultat.Constants.KEY_COL_ROUND_TYPE + " LIKE \"%" + filterI + "%\" ";
                    else
                        filterRound += Db_resultat.Constants.KEY_COL_ROUND_TYPE + " LIKE \"%" + filterI + "%\" ";
                }
           }
        }

// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_COL_ROUND_NAME,
                Db_resultat.Constants.KEY_ID_ROUNDS};
        String selection =  filterRound;

        String groupBy = Db_resultat.Constants.KEY_COL_ROUND_NAME;

        // And then store the column index answered by the request (we present
        // an other way to
        // retrieve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;

        cursor = db.query(Db_resultat.Constants.ROUNDS, projections,selection,
                null, groupBy, null, null, null);
        // displayResults(cursor);     }

        if (cursor.moveToFirst()) {
            // The elements to retrieve
            int colId;
            String name;

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_ROUNDS);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ROUND_NAME);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);
                retour.add(name);

                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (retour);
    }
    // delete one round by  name
    public void supRound(String roundName) {
        long idRound = getRoundId(roundName);
        String selection;
        String[] selectionArg;

        selection = Db_resultat.Constants.KEY_COL_ID_ROUND + " =?  ";
        selectionArg = new String[]{String.valueOf(idRound)};

        // Insert the line in the database

        db.delete(Db_resultat.Constants.RESULTATS, selection, selectionArg);
        selection = Db_resultat.Constants.KEY_ID_ROUNDS + " =?  ";
        db.delete(Db_resultat.Constants.ROUNDS, selection, selectionArg);

    }
    // Create a round without qualifier
    public long addRound(String roundName) {

        // -------------------
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_resultat.Constants.KEY_COL_ROUND_NAME, roundName);
        // Insert the line in the database
        return (db.insert(Db_resultat.Constants.ROUNDS, null, contentValues));

    }
    // Get qualifier by round
    public String getRoundQualifier(String roundName) {
        Cursor cursor = null;
// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{ Db_resultat.Constants.KEY_COL_ROUND_TYPE};

        String selection = Db_resultat.Constants.KEY_COL_ROUND_NAME + "=?";
        String[] selectionArg = new String[]{roundName};
        //String groupBy = Db_resultat.Constants.KEY_COL_ROUND_NAME;
        // And then store the column index answered by the request (we present
        // an other way to
        // retrieve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;

        cursor = db.query(Db_resultat.Constants.ROUNDS, projections, selection,
                selectionArg, null, null, null, null);
        // displayResults(cursor);     }
        String colType ="";
        if (cursor.moveToFirst()) {
            // The elements to retrieve
            String name;
            // The associated index within the cursor
            int roundType = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ROUND_TYPE);

            // Browse the results list:
            int count = 0;
            do {
                colType = cursor.getString(roundType);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (colType);
    }
    //

    // update or create a round with qualifier
    public long updateRound(String roundName, String qualifier) {
        Long idRound = getRoundId(roundName); // verify existing round
        if (idRound < 0)          // Create Round
        {
            idRound = addRound(roundName);
        }
        idRound = getRoundId(roundName);
        // -------------------
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_resultat.Constants.KEY_COL_ROUND_TYPE, qualifier);
        //Update Rounds
        return(db.update(Db_resultat.Constants.ROUNDS, contentValues, Db_resultat.Constants.KEY_ID_ROUNDS + "= ?", new String[]{String.valueOf(idRound)}) );

    }


// -----------------------------------------------------------------------------------------
// Notes archers

    public List <Note> getNotes(String archer) {
        ArrayList retour = new ArrayList();
        Cursor cursor = null;

        long archer_id = getArcherId(archer);
// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_NOTE,
                Db_resultat.Constants.KEY_COL_NAME_NOTE};
        // And then store the column index answered by the request (we present
        // an other way to
        // retrieve data)

        String selection = Db_resultat.Constants.KEY_COL_ID_ARCHERS + "=?";
        String[] selectionArg = new String[]{String.valueOf(archer_id)};


        // The groupBy clause:


        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.KEY_ID_NOTE + "  DESC";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);

        //________________________

        List<Note> rValue = new ArrayList<Note>();
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;

        cursor = db.query(Db_resultat.Constants.ARCHERS_NOTE, projections, selection,
                selectionArg, null, null, orderBy, null);
        // displayResults(cursor);


        if (cursor.moveToFirst()) {
            // The elements to retrieve


            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_NOTE);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME_NOTE);
            // Browse the results list:
            int count = 0;
            do {
                Note note = new Note() ;
                note.setIdNote(cursor.getInt(indexId))  ;
                note.setNote(cursor.getString(indexName));
                rValue.add(note);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (rValue);
    }
    //---------------------------------------
    // Get Note from id_note
    // Note archers

    public String getNote(long note_id) {
        String retour = "";
        Cursor cursor = null;


// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{
                Db_resultat.Constants.KEY_COL_NAME_NOTE};
        // And then store the column index answered by the request (we present
        // an other way to
        // retireve data)

        String selection = Db_resultat.Constants.KEY_ID_NOTE + "=?";
        String[] selectionArg = new String[]{String.valueOf(note_id)};

        // The groupBy clause:

        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = null;
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        //________________________


        cursor = db.query(Db_resultat.Constants.ARCHERS_NOTE, projections, selection,
                selectionArg, null, null, orderBy, null);



        if (cursor.moveToFirst()) {
            // The elements to retrieve
            // The associated index within the cursor

            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME_NOTE);
            // Browse the results list:
            int count = 0;
            do {
                retour=cursor.getString(indexName);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (retour);
    }


    // Update Value note
    public void updateNote(Long note_id, String value) {
        ContentValues cv = new ContentValues();
        cv.put(Db_resultat.Constants.KEY_COL_NAME_NOTE, value);
        db.update(Db_resultat.Constants.ARCHERS_NOTE, cv, Db_resultat.Constants.KEY_ID_NOTE + "= ?", new String[]{String.valueOf(note_id)}) ;
    }

    // add Value note
    public void addNote(String archer, String value) {
        long archer_id = getArcherId(archer);
        ContentValues cv = new ContentValues();
        cv.put(Db_resultat.Constants.KEY_COL_ID_ARCHERS, archer_id);
        cv.put(Db_resultat.Constants.KEY_COL_NAME_NOTE, value);
        db.insert(Db_resultat.Constants.ARCHERS_NOTE, null, cv);
    }

    public void deleteNote(Long note_id) {

        String selection;
        String[] selectionArg;

        selection = Db_resultat.Constants.KEY_ID_NOTE + " =?  ";
        selectionArg = new String[]{String.valueOf(note_id)};
        db.delete(Db_resultat.Constants.ARCHERS_NOTE, selection, selectionArg);

    }

    // Update Value Information Archer
    public void updateArcherInformation(String archer, String value) {
        ContentValues cv = new ContentValues();
        cv.put(Db_resultat.Constants.KEY_COL_INFORMATION, value);
        db.update(Db_resultat.Constants.ARCHERS, cv, Db_resultat.Constants.KEY_COL_NAME + "= ?", new String[]{archer}) ;
    }

    // get archer bow
    public Integer   getArcherBow(String archer) {
            Integer retour = 0;
            Cursor cursor = null;

            // The projection define what are the column you want to retrieve
            String[] projections = new String[]{
                    Db_resultat.Constants.KEY_COL_ARCHER_BOW};

            String selection = Db_resultat.Constants.KEY_COL_NAME + "=?";
            String[] selectionArg = new String[]{archer};

            // The groupBy clause:
            //String groupBy = Constants.KEY_COL_EYES_COLOR;
            // The having clause
            String having = null;
            // The order by clause (column name followed by Asc or DESC)
            String orderBy = null;

            cursor = db.query(Db_resultat.Constants.ARCHERS, projections, selection,
                    selectionArg, null, null, orderBy, null);

            if (cursor.moveToFirst()) {
                // The elements to retrieve
                // The associated index within the cursor
                int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ARCHER_BOW);
                // Browse the results list:
                int count = 0;
                do {
                    retour=cursor.getInt(indexName);
                    count++;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return (retour);
        }

    // Set archer bow

        public void updateArcherBow(String archer, Integer value) {
            ContentValues cv = new ContentValues();
            cv.put(Db_resultat.Constants.KEY_COL_ARCHER_BOW, value);
            db.update(Db_resultat.Constants.ARCHERS, cv, Db_resultat.Constants.KEY_COL_NAME + "= ?", new String[]{archer}) ;
        }

    // Update Value note
    public String   getArcherInformation(String archer) {
        String retour = "";
        Cursor cursor = null;
        // Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{
                Db_resultat.Constants.KEY_COL_INFORMATION};

        String selection = Db_resultat.Constants.KEY_COL_NAME + "=?";
        String[] selectionArg = new String[]{archer};

        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = null;
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);
        //________________________


        cursor = db.query(Db_resultat.Constants.ARCHERS, projections, selection,
                selectionArg, null, null, orderBy, null);

        if (cursor.moveToFirst()) {
            // The elements to retrieve
            // The associated index within the cursor

            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_INFORMATION);
            // Browse the results list:
            int count = 0;
            do {
                retour=cursor.getString(indexName);
                count++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return (retour);
    }

}