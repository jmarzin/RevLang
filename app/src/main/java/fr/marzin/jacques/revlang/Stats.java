package fr.marzin.jacques.revlang;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;

/**
 * Created by jacques on 26/01/15.
 */
public class Stats {

    public int _id;
    public String langue_id;
    public String date_rev;
    public int nb_questions_mots;
    public int nb_erreurs_mots;
    public int nb_questions_formes;
    public int nb_erreurs_formes;

    public Stats() {
        this.langue_id = "";
        this.date_rev = "1900-01-01";
        this.nb_questions_mots = 0;
        this.nb_erreurs_mots = 0;
        this.nb_questions_formes = 0;
        this.nb_erreurs_formes = 0;
    }

    public void save(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(StatsContract.StatsTable.COLUMN_NAME_LANGUE_ID, langue_id);
        values.put(StatsContract.StatsTable.COLUMN_NAME_DATE, date_rev);
        values.put(StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_MOTS, nb_questions_mots);
        values.put(StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_MOTS,nb_erreurs_mots);
        values.put(StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_FORMES,nb_questions_formes);
        values.put(StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_FORMES,nb_erreurs_formes);

        if (this._id > 0) {
            String selection = StatsContract.StatsTable.COLUMN_NAME_ID + " = " + _id;
            int count = db.update(StatsContract.StatsTable.TABLE_NAME,values,selection,null);
        } else {
            this._id = (int) db.insert(StatsContract.StatsTable.TABLE_NAME,null,values);
        }
    }

    public static Stats find_by(SQLiteDatabase db, String selection) {
        final Cursor mCursor = db.query(StatsContract.StatsTable.TABLE_NAME,null,selection,null,null,null,null);
        Stats stats = new Stats();
        if (mCursor.moveToFirst()) {
            stats._id = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_ID));
            stats.langue_id = mCursor.getString(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_LANGUE_ID));
            stats.date_rev = mCursor.getString(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_DATE));
            stats.nb_questions_mots = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_MOTS));
            stats.nb_erreurs_mots = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_MOTS));
            stats.nb_questions_formes = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_FORMES));
            stats.nb_erreurs_formes = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_FORMES));
        } else {
            stats = null;
        }
        mCursor.close();
        return stats;
    }

    public static Stats find(SQLiteDatabase db, int id) {
        String selection = StatsContract.StatsTable.COLUMN_NAME_ID + " = " + id;
        return Stats.find_by(db,selection);
    }

    public static Cursor where(SQLiteDatabase db, String selection) {
        String sortOrder = StatsContract.StatsTable.COLUMN_NAME_DATE + " ASC";
        return db.query(StatsContract.StatsTable.TABLE_NAME,null,selection,null,null,null,sortOrder);
    }
}

