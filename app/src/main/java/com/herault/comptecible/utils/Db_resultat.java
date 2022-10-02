package com.herault.comptecible.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

class Db_resultat extends SQLiteOpenHelper {


    /**
     * The static string to create the database.
     */
    private static final String DATABASE_CREATE_TABLE_ARCHERS = "create table "
            + Constants.ARCHERS + "(" + Constants.KEY_ID_ARCHERS
            + " INTEGER primary key autoincrement, "
            + Constants.KEY_COL_NAME + " TEXT UNIQUE ,"
            + Constants.KEY_COL_INFORMATION + " TEXT ,"
            + Constants.KEY_COL_ARCHER_BOW + " INTEGER DEFAULT 0 "
            + ")  ";

    private static final String DATABASE_CREATE_TABLE_ARCHERS_ROUND = "create table "
            + Constants.ARCHERS_ROUND + "(" + Constants.KEY_ID_ARCHERS
            + " INTEGER primary key autoincrement, "
            + Constants.KEY_COL_NAME + " TEXT  UNIQUE , "
            + Constants.KEY_COL_INFORMATION + " TEXT " + ")  ";



    private static final String DATABASE_CREATE_TABLE_ARCHER_NOTES = "create table "
            + Constants.ARCHERS_NOTE + "(" + Constants.KEY_ID_NOTE
            + " INTEGER primary key autoincrement, "
            + Constants.KEY_COL_ID_ARCHERS + " INTEGER, "
            + Constants.KEY_COL_NAME_NOTE + " TEXT, "
            + " CONSTRAINT c "
            + " FOREIGN KEY ( " + Constants.KEY_COL_ID_ARCHERS + " ) REFERENCES " + Constants.ARCHERS
            + " ( " + Constants.KEY_ID_ARCHERS + " ) ON DELETE CASCADE"+ ")  "
            ;


    private static final String DATABASE_CREATE_TABLE_ROUNDS = "create table "
            + Constants.ROUNDS + "(" + Constants.KEY_ID_ROUNDS
            + " INTEGER primary key autoincrement, " + Constants.KEY_COL_ROUND_NAME + " TEXT UNIQUE , "
            + Constants.KEY_COL_ROUND_TYPE + " TEXT ) ";

    private static final String DATABASE_CREATE_TABLE_RESULTATS = "create table "
            + Constants.RESULTATS + "("
            + Constants.KEY_COL_ID1
            + " INTEGER primary key autoincrement, "
            + Constants.KEY_COL_ID_ROUND + " INTEGER ,"
            + Constants.KEY_COL_ARROW + " INTEGER, "
            + Constants.KEY_COL_VALUE + " INTEGER, "
            + Constants.KEY_COL_X + " REAL, "
            + Constants.KEY_COL_Y + " REAL, "
            + Constants.KEY_COL_PLUS + " BIT, "
            + Constants.KEY_COL_ARROW_NUMBER + "INTEGER DEFAULT NULL,"
            + Constants.KEY_COL_ID_NAME + " INTEGER ,"
            + "CONSTRAINT a "
            + " FOREIGN KEY ( " + Constants.KEY_COL_ID_ROUND + " ) REFERENCES " + Constants.ROUNDS
            + " ( " + Constants.KEY_ID_ROUNDS + " ) ON DELETE CASCADE,"
            + "CONSTRAINT b"
            + " FOREIGN KEY ( " + Constants.KEY_COL_ID_NAME + " ) REFERENCES " + Constants.ARCHERS
            + " ( " + Constants.KEY_ID_ARCHERS + " ) ON DELETE CASCADE)";

    private static final String DATABASE_CREATE_TABLE_COMPTE_CIBLE_ = "create table "
            + Constants.COMPTE_CIBLE + "(" + Constants.KEY_ID_ARCHERS
            + " INTEGER primary key autoincrement, " + Constants.KEY_COL_COMPTE_NAME + " TEXT UNIQUE,"
            + Constants.KEY_COL_COMPTE_VALUE + " TEXT)  ";

    /**
     * @param context = the context of the caller
     * @param name    = Database's name
     * @param factory = null
     * @param version
     */

    public Db_resultat(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the new database using the SQL string Database_create

        db.execSQL(DATABASE_CREATE_TABLE_ARCHERS);
        db.execSQL(DATABASE_CREATE_TABLE_ARCHER_NOTES);
        db.execSQL(DATABASE_CREATE_TABLE_ARCHERS_ROUND);
        db.execSQL(DATABASE_CREATE_TABLE_COMPTE_CIBLE_);
        db.execSQL(DATABASE_CREATE_TABLE_ROUNDS);
        db.execSQL(DATABASE_CREATE_TABLE_RESULTATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     /*  Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion
                + " vers la version " + newVersion
                + ", les anciennes données seront détruites "); */

        switch (oldVersion) {
            case 23 :
                db.execSQL("ALTER TABLE "+ Constants.ARCHERS + " ADD "  + Constants.KEY_COL_INFORMATION + " TEXT"  );
                db.execSQL("ALTER TABLE "+ Constants.ARCHERS_ROUND + " ADD "  + Constants.KEY_COL_INFORMATION + " TEXT"  );
                db.execSQL(DATABASE_CREATE_TABLE_ARCHER_NOTES);
            case 24 :
                db.execSQL( "create table TEMPORAIRE(" + Constants.KEY_ID_ROUNDS
                    + " INTEGER primary key autoincrement, " + Constants.KEY_COL_ROUND_NAME + " TEXT UNIQUE , "
                    + Constants.KEY_COL_ROUND_TYPE + " TEXT ) ");
                db.execSQL("INSERT INTO TEMPORAIRE SELECT * FROM "+Constants.ROUNDS);
                db.execSQL("DROP TABLE "+Constants.ROUNDS);
                db.execSQL("ALTER TABLE TEMPORAIRE RENAME TO "+ Constants.ROUNDS);
            case 25 :
                db.execSQL("alter table "+ Constants.ARCHERS +" ADD "+ Constants.KEY_COL_ARCHER_BOW+" INTEGER DEFAULT 0");
            case 26 :
                db.execSQL("alter table "+ Constants.RESULTATS +" ADD "+ Constants.KEY_COL_PLUS+" BIT DEFAULT 0");
            case 27 :
                db.execSQL("alter table "+ Constants.RESULTATS +" ADD "+ Constants.KEY_COL_ARROW_NUMBER + "INTEGER DEFAULT NULL" );
                // We must transform all Round qualifier
                Stockage stock = null;
                stock = new Stockage();
                stock.initDB(db);
                List<String> allRound = stock.getRounds() ;
                for (String roundName:allRound
                     ) {

                    String  oldFilter = "";
                    oldFilter =stock.getRoundQualifier (roundName);
                    if (oldFilter != null) {
                        String[] oldFilterArray = oldFilter.split("\\s+");
                        String newFilter = "";
                        for (String currentFilter : oldFilterArray
                        ) {

                            if (!currentFilter.isEmpty()) {
                                if (newFilter.isEmpty())
                                    newFilter = newFilter + "_black_" + " " + currentFilter;
                                else
                                    newFilter = newFilter + " _black_" + " " + currentFilter;
                            }
                        }
                        stock.updateRound(roundName, newFilter);
                    }
                }
            case 28 :

            break;
            default:
                // Drop the old database
                db.execSQL("DROP TABLE IF EXISTS " + Constants.ARCHERS);
                db.execSQL("DROP TABLE IF EXISTS " + Constants.ARCHERS_NOTE);
                db.execSQL("DROP TABLE IF EXISTS " + Constants.ARCHERS_ROUND);
                db.execSQL("DROP TABLE IF EXISTS " + Constants.RESULTATS);
                db.execSQL("DROP TABLE IF EXISTS " + Constants.COMPTE_CIBLE);
                db.execSQL("DROP TABLE IF EXISTS " + Constants.ROUNDS);
                // Create the new one
                onCreate(db);

        }

        // or do a smartest stuff
    }

    /**
     * @goals This class aims to show the constant to use for the DBOpenHelper
     */
    public static class Constants implements BaseColumns {

        // The database name
        public static final String DATABASE_NAME = "resultat.db";
        // The database version
        public static final int DATABASE_VERSION = 28;
// -----------------------
        // The table Name
        public static final String ARCHERS = "archers";

        // ## Column name ##
        // My Column ID and the associated explanation for end-users
        public static final String KEY_ID_ARCHERS = "_id";// Mandatory
        // My Column Name and the associated explanation for end-users
        public static final String KEY_COL_NAME = "name";
        public static final  String KEY_COL_INFORMATION = "information" ;
        public static final  String KEY_COL_ARCHER_BOW = "bow" ;

        // -----------------------
        // The table Name
        public static final String ARCHERS_NOTE = "archers_notes";

        // ## Column name ##
        // My Column ID and the associated explanation for end-users
        public static final String KEY_ID_NOTE = "_id";// Mandatory
        public static final  String KEY_COL_ID_ARCHERS = "_id_archers" ;
        // My Column Name and the associated explanation for end-users
        public static final String KEY_COL_NAME_NOTE = "note";


// -----------------------
        // The table Name
        public static final String ARCHERS_ROUND = "archers_round";

        // The table Name
        public static final String ROUNDS = "rounds";
        // ## Column name ##
        // My Column ID and the associated explanation for end-users
        public static final String KEY_ID_ROUNDS = "_id_round";// Mandatory
        // My Column Name and the associated explanation for end-users
        public static final String KEY_COL_ROUND_NAME = "name_round";

        public static final String KEY_COL_ROUND_TYPE = "type_round";

// -----------------------
        // All single value for application like Number of arrow by round
        // The table Name
        public static final String COMPTE_CIBLE = "compteCible";
        // ## Column name ##
        // My Column ID
        // public static final String KEY_ID_COLUMN = "_id"; // Mandatory
        // My Column round tempory
        public static final String KEY_COL_COMPTE_NAME = "name";
        // Type of round
        public static final String KEY_COL_COMPTE_VALUE = "value";

 // -----------------------
        // The table Name
        public static final String RESULTATS = "resultats";
        // ## Column name ##
        // My Column ID
        public static final String KEY_COL_ID1 = "_id1_resultat";// Mandatory
        // My Column ROUND SERIAL
        public static final String KEY_COL_ID_ROUND = "id_round";
        // My Column arrow Index of arrow
        public static final String KEY_COL_ARROW = "arrow";
        // My Column Value of Arrow
        public static final String KEY_COL_VALUE = "value";
        // My Column Value of Arrow
        public static final String KEY_COL_X = "X";
        // My Column Value of Arrow
        public static final String KEY_COL_Y = "Y";
        public static final String KEY_COL_ARROW_NUMBER = "arrow_name" ;
        // My column arrow plus
        public static final String KEY_COL_PLUS = "plus";
        // My Column id_Name
        public static final String KEY_COL_ID_NAME = "id_name";

 // -----------------------



    }

}
