package com.herault.comptecible.utils;


import com.herault.comptecible.R;
import com.herault.comptecible.Resultat_archer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class Stockage {

    protected Activity context;
    private SQLiteDatabase db = null;
    private Db_resultat base;

    public void onCreate(AppCompatActivity mainActivity) {
        this.context = mainActivity;
        base = new Db_resultat(mainActivity, Db_resultat.Constants.DATABASE_NAME, null, Db_resultat.Constants.DATABASE_VERSION);
        openDB();
    }

    public long showDB() {
        if (db == null)
            return (0);
        else
            return (1);
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
    }

    /*-----------------------------------record-------------------------------------------------*/
// Get all resultat for one archer for one Round
    public List<Resultat_archer> getResultatArrows(String archer, String roundName) {
        int value = 0;
        long Id = getArcherId(archer);
        final int cursorIdColNumber = 0;

        String table = Db_resultat.Constants.RESULTATS + "," + Db_resultat.Constants.ARCHERS;
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_VALUE,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ARROW,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_X,
                Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_Y};

        String selection = Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ROUND + " =? AND "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_COL_NAME + " = ? AND "
                + Db_resultat.Constants.RESULTATS + "." + Db_resultat.Constants.KEY_COL_ID_NAME + " = "
                + Db_resultat.Constants.ARCHERS + "." + Db_resultat.Constants.KEY_ID_COLUMN;
        String[] selectionArg = new String[]{roundName, archer};
        // The groupBy clause:
        //String groupBy = Constants.KEY_COL_EYES_COLOR;
        // The having clause
        String having = null;
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
        if (cursor.moveToFirst()) {
            // Browse the results list:

            int count = 0;
            do {
                Resultat_archer rArcher = new Resultat_archer();
                rArcher.name = cursor.getString(indexArrow);
                rArcher.value = cursor.getInt(indexValue);
                rArcher.x = cursor.getDouble(indexX);
                rArcher.y = cursor.getDouble(indexY);
                rValue.add(rArcher);
                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();

        }

        cursor.close();
        return rValue;

    }

    // Get for on Archer One arrow
    public Resultat_archer getResultatArrow(String archer, String roundName, long arrow) {
        int value = 0;
        long Id = getArcherId(archer);
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_COL_VALUE, Db_resultat.Constants.KEY_COL_X, Db_resultat.Constants.KEY_COL_Y};
        final int cursorIdColNumber = 0;

        String selection = Db_resultat.Constants.KEY_COL_ROUND + " =? AND " + Db_resultat.Constants.KEY_COL_ID_NAME + " = ? AND "
                + Db_resultat.Constants.KEY_COL_ARROW + " =?";
        String[] selectionArg = new String[]{roundName, String.valueOf(Id), String.valueOf(arrow)};
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
        Resultat_archer rArcher = new Resultat_archer();
        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexValue = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_VALUE);
            int indexX = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_X);
            int indexY = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_Y);
            // Browse the results list:
            int count = 0;
            do {
                rArcher.value = cursor.getInt(indexValue);
                rArcher.x = cursor.getDouble(indexX);
                rArcher.y = cursor.getDouble(indexY);
                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();

        }


        cursor.close();
        return rArcher;
    }
//-------------------------------------------------------------------
    // get for all archer  result for all round

    public List<Resultat_archer> getResultatAllRound(String name_archer) {
        Long archer_id = getArcherId(name_archer);

        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"SUM (" + Db_resultat.Constants.KEY_COL_VALUE + " )", Db_resultat.Constants.KEY_COL_ROUND};
        final int cursorIdColNumber = 0;

        String selection = Db_resultat.Constants.KEY_COL_ID_NAME + " =?  ";
        String[] selectionArg = new String[]{Long.toString(archer_id)};
        // The groupBy clause:
        String groupBy = Db_resultat.Constants.KEY_COL_ROUND;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = Db_resultat.Constants.KEY_COL_ROUND + "  ASC";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);

        Cursor cursor = db.query(Db_resultat.Constants.RESULTATS, projections, selection,
                selectionArg, groupBy, null, orderBy, null);
        // The elements to retrieve
        List<Resultat_archer> rArcher = new ArrayList<Resultat_archer>();

        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ROUND);
            Resultat_archer rowArcher;
            // Browse the results list:
            int count = 0;
            do {
                rowArcher = new Resultat_archer();
                rowArcher.value = cursor.getInt(0);
                rowArcher.name = cursor.getString(indexName);
                rowArcher.information = " nb 10= " + getResultatRoundCompte(cursor.getString(indexName), String.valueOf(getArcherId(name_archer)), "10")
                        + " nb 9= " + getResultatRoundCompte(cursor.getString(indexName), String.valueOf(getArcherId(name_archer)), "9")
                        + " X= " + getResultatRoundCompte(cursor.getString(indexName), String.valueOf(getArcherId(name_archer)), "0");
                rArcher.add(rowArcher);
                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();
        }
        cursor.close();
        return rArcher;
    }
// ------------------------------
// get for all archer all result for one round

    public String getResultatRoundCompte(String roundName, String Archer_ID, String RefValue) {
        int value = 0;
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"COUNT (" + Db_resultat.Constants.KEY_COL_VALUE + " )"};
        final int cursorIdColNumber = 0;

        String selection = Db_resultat.Constants.KEY_COL_ROUND + " =?  AND " + Db_resultat.Constants.KEY_COL_ID_NAME + " =? AND "
                + Db_resultat.Constants.KEY_COL_VALUE + " =? ";
        String[] selectionArg = new String[]{roundName, Archer_ID, RefValue};

        // The groupBy clause:
        String groupBy = Db_resultat.Constants.KEY_COL_ROUND;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = "COUNT (" + Db_resultat.Constants.KEY_COL_VALUE + " ) Desc";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);

        Cursor cursor = db.query(Db_resultat.Constants.RESULTATS, projections, selection,
                selectionArg, null, null, null, null);
        // The elements to retrieve
        String numberOf = "";

        if (cursor.moveToFirst()) {
            // The associated index within the cursor
            int indexValue = 0;
            // Browse the results list:
            int count = 0;
            numberOf = String.valueOf(cursor.getInt(0));
            count++;
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();


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

        String selection = Db_resultat.Constants.KEY_COL_ROUND + " =?  ";
        String[] selectionArg = new String[]{roundName};
        // The groupBy clause:
        String groupBy = Db_resultat.Constants.KEY_COL_ID_NAME;
        // The having clause
        String having = null;
        // The order by clause (column name followed by Asc or DESC)
        String orderBy = "SUM (" + Db_resultat.Constants.KEY_COL_VALUE + " ) Desc";
        // Maximal size of the results list
        //  String maxResultsListSize = "60";
        //Cursor cursor = db.query(db_resultat.Constants.ARCHERS, projections, selection,
        //	selectionArg, groupBy, having, orderBy, maxResultsListSize);

        Cursor cursor = db.query(Db_resultat.Constants.RESULTATS, projections, selection,
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
                rowArcher.information = " nb10= " + getResultatRoundCompte(roundName, String.valueOf(cursor.getInt(indexName)), "10")
                        + " nb9= " + getResultatRoundCompte(roundName, String.valueOf(cursor.getInt(indexName)), "9")
                        + " X= " + getResultatRoundCompte(roundName, String.valueOf(cursor.getInt(indexName)), "0");

                rArcher.add(rowArcher);
                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();

        }
        cursor.close();
        return rArcher;
    }

    //-------------------------------------------------------------------get last arrow index
    public long getarrowIndex(String archer, String roundName) {
        int arrow = 0;
        // get flecheIndex
        //select round from resultat where round=roundname and _id1 =archer(id)
        long Id = getArcherId(archer);
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"MAX (" + Db_resultat.Constants.KEY_COL_ARROW + ")"};
        final int cursorIdColNumber = 0;
        // To add a Where clause you can either do that:
        //qb.appendWhere(Db_resultat.Constants.KEY_COL_ROUND+ "=?"+Db_resultat.Constants.KEY_COL_ID1 +" = ?");
        // Or that:
        String selection = Db_resultat.Constants.KEY_COL_ROUND + " =? AND " + Db_resultat.Constants.KEY_COL_ID_NAME + " = ?";
        String[] selectionArg = new String[]{roundName, String.valueOf(Id)};
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
        }

        cursor.close();
        return (arrow);
    }

    //---------------------------------- supression archer for pone Round
    public void supResultat(String archer, String roundName) {
        int arrow = 0;
        // get flecheIndex
        //select round from resultat where round=roundname and _id1 =archer(id)
        long Id = getArcherId(archer);
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"MAX (" + Db_resultat.Constants.KEY_COL_ARROW + ")"};
        final int cursorIdColNumber = 0;
        // To add a Where clause you can either do that:
        //qb.appendWhere(Db_resultat.Constants.KEY_COL_ROUND+ "=?"+Db_resultat.Constants.KEY_COL_ID1 +" = ?");
        // Or that:
        String selection = Db_resultat.Constants.KEY_COL_ROUND + " =? AND " + Db_resultat.Constants.KEY_COL_ID_NAME + " = ?";
        String[] selectionArg = new String[]{roundName, String.valueOf(Id)};
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
        }
        selection = Db_resultat.Constants.KEY_COL_ROUND + " =? AND " + Db_resultat.Constants.KEY_COL_ID_NAME + " = ? AND " +
                Db_resultat.Constants.KEY_COL_ARROW + " =? ";

        selectionArg = new String[]{roundName, String.valueOf(Id), String.valueOf(arrow)};

        // Insert the line in the database

        db.delete(Db_resultat.Constants.RESULTATS, selection, selectionArg);
        cursor.close();

    }

    //----------------------------- add resultat for on archer for one round
    public long addResultat(String archer, String roundName, int value, double X, double Y) {
        int arrow = 0;
        // get flecheIndex
        //select round from resultat where round=roundname and _id1 =archer(id)
        long Id = getArcherId(archer);
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{"MAX (" + Db_resultat.Constants.KEY_COL_ARROW + ")"};
        final int cursorIdColNumber = 0;
        // To add a Where clause you can either do that:
        //qb.appendWhere(Db_resultat.Constants.KEY_COL_ROUND+ "=?"+Db_resultat.Constants.KEY_COL_ID1 +" = ?");
        // Or that:
        String selection = Db_resultat.Constants.KEY_COL_ROUND + " =? AND " + Db_resultat.Constants.KEY_COL_ID_NAME + " = ?";
        String[] selectionArg = new String[]{roundName, String.valueOf(Id)};
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
        contentValues.put(Db_resultat.Constants.KEY_COL_ROUND, roundName);
        contentValues.put(Db_resultat.Constants.KEY_COL_ARROW, arrow);
        contentValues.put(Db_resultat.Constants.KEY_COL_VALUE, value);
        contentValues.put(Db_resultat.Constants.KEY_COL_X, X);
        contentValues.put(Db_resultat.Constants.KEY_COL_Y, Y);
        contentValues.put(Db_resultat.Constants.KEY_COL_ID_NAME, Id);


        // Insert the line in the database

        db.insert(Db_resultat.Constants.RESULTATS, null, contentValues);
        cursor.close();
        return (arrow);
    }

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

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();
        }
        cursor.close();
        return (value);

    }

    /*----------------------Table Archer and ArcherRound    ---------------------------------*/
    public long addArcher(String name, boolean round) {

        // -------------------
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_resultat.Constants.KEY_COL_NAME, name);

        // Insert the line in the database
        if (round) {
            return (db.insert(Db_resultat.Constants.ARCHERS_ROUND, null, contentValues));
        } else {
            return (db.insert(Db_resultat.Constants.ARCHERS, null, contentValues));
        }
    }

    public void dropArchers(boolean round) {
        if (!round) {
            db.delete(Db_resultat.Constants.ARCHERS, null, null);
            db.delete(Db_resultat.Constants.RESULTATS, null, null);
            db.delete(Db_resultat.Constants.COMPTE_CIBLE, null, null);

        }
        db.delete(Db_resultat.Constants.ARCHERS_ROUND, null, null);
    }

    public void dropArcher(String name) {

        long id_name = getArcherId(name);

        db.delete(Db_resultat.Constants.RESULTATS, Db_resultat.Constants.KEY_COL_ID_NAME + "= ?", new String[]{Integer.toString((int) id_name)});
        db.delete(Db_resultat.Constants.ARCHERS_ROUND, Db_resultat.Constants.KEY_COL_NAME + "= ?", new String[]{name});
        db.delete(Db_resultat.Constants.ARCHERS, Db_resultat.Constants.KEY_COL_NAME + "= ?", new String[]{name});
    }

    public String showArchers() {
// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_COLUMN,
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

    public long insertArray(ArrayList<String> list, boolean round) {
        long max, error = 0;
        int i;
        max = list.size();
        for (i = 0; i < max; i++) {
            error = addArcher(list.get(i), true);
        }
        return error;
    }


    public long getArcherId(String name) {

        //select id fron archers where name=name
        //Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_COLUMN,
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
        @SuppressLint("Recycle") Cursor cursor = db.query(Db_resultat.Constants.ARCHERS, projections, selection,
                selectionArg, null, null, orderBy, null);
        // The elements to retrieve
        int colId = -1;
        if (cursor.moveToFirst()) {

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_COLUMN);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);
                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();
        }
        cursor.close();
        return (colId);
    }

    //---------
    public String getArcher(int colId) {

        //select name fron archers where id=colId
        //Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{
                Db_resultat.Constants.KEY_COL_NAME};

        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        // To add a Where clause you can either do that:
        // qb.appendWhere(Constants.KEY_COL_HAIR_COLOR+ "=blond");
        // Or that:
        String selection = Db_resultat.Constants.KEY_ID_COLUMN + "=?";
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
        @SuppressLint("Recycle") Cursor cursor = db.query(Db_resultat.Constants.ARCHERS, projections, selection,
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

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();
        }
        cursor.close();
        return (name);
    }


    public ArrayList getArchers(Boolean round) {
        ArrayList retour = new ArrayList();
        Cursor cursor = null;
// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_ID_COLUMN,
                Db_resultat.Constants.KEY_COL_NAME};
        // And then store the column index answered by the request (we present
        // an other way to
        // retireve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;
        if (round) {
            cursor = db.query(Db_resultat.Constants.ARCHERS_ROUND, projections, null,
                    null, null, null, null, null);
            //displayResults(cursor);
        } else {
            cursor = db.query(Db_resultat.Constants.ARCHERS, projections, null,
                    null, null, null, null, null);
            // displayResults(cursor);
        }

        if (cursor.moveToFirst()) {
            // The elements to retrieve
            int colId;
            String name;

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_COLUMN);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_NAME);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);
                retour.add(name);

                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();
        }
        cursor.close();
        return (retour);
    }

    //----------------------------------------------------------------------------------------------------------------------------------
    public ArrayList getArchers(String round) {
        ArrayList retour = new ArrayList();
        Cursor cursor = null;
// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_COL_ID_NAME};
        String selection = Db_resultat.Constants.KEY_COL_ROUND + " =? ";
        String[] selectionArg = new String[]{round};
        String groupBy = Db_resultat.Constants.KEY_COL_ID_NAME;
        // And then store the column index answered by the request (we present
        // an other way to
        // retireve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;

        cursor = db.query(Db_resultat.Constants.RESULTATS, projections, selection, selectionArg, groupBy, null, null, null);
        // displayResults(cursor);


        if (cursor.moveToFirst()) {
            // The elements to retrieve
            int colId;

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ID_NAME);

            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);

                retour.add(getArcher(colId));

                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();
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
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_ID_COLUMN);
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


    public ArrayList getRounds() {
        ArrayList retour = new ArrayList();
        Cursor cursor = null;
// Using man made query
        // The projection define what are the column you want to retrieve
        String[] projections = new String[]{Db_resultat.Constants.KEY_COL_ROUND,
                Db_resultat.Constants.KEY_COL_ID1};
        String groupBy = Db_resultat.Constants.KEY_COL_ROUND;
        // And then store the column index answered by the request (we present
        // an other way to
        // retireve data)
        final int cursorIdColNumber = 0, cursorNameColNumber = 1;

        cursor = db.query(Db_resultat.Constants.RESULTATS, projections, null,
                null, groupBy, null, null, null);
        // displayResults(cursor);     }

        if (cursor.moveToFirst()) {
            // The elements to retrieve
            int colId;
            String name;

            // The associated index within the cursor
            int indexId = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ID1);
            int indexName = cursor.getColumnIndex(Db_resultat.Constants.KEY_COL_ROUND);
            // Browse the results list:
            int count = 0;
            do {
                colId = cursor.getInt(indexId);
                name = cursor.getString(indexName);
                retour.add(name);

                count++;
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Noelement), Toast.LENGTH_LONG)
                    .show();

        }
        cursor.close();
        return (retour);
    }

    public void supRound(String roundName) {

        String selection;
        String[] selectionArg;

        selection = Db_resultat.Constants.KEY_COL_ROUND + " =?  ";

        selectionArg = new String[]{roundName};

        // Insert the line in the database

        db.delete(Db_resultat.Constants.RESULTATS, selection, selectionArg);

    }

}

