package fr.marzin.jacques.revlang;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jacques on 23/01/15.
 */
public class Mot extends ItemQuestionnable {

//    public String theme_langue;
    public String francais;
    public String mot_directeur;
    public String langue_niveau;
    public Theme theme;

    public Mot() {
        this.poids = 1;
        this.nb_err = 0;
        this.date_rev = "1900-01-01";
        this.date_maj = "";
        this.dist_id = 0;
        this.francais = "";
        this.langue = "";
        this.langue_id = "";
        this.mot_directeur = "";
        this.langue_niveau = "1";
        this.theme = new Theme();
    }

    public void save(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(MotContract.MotTable.COLUMN_NAME_POIDS, poids);
        values.put(MotContract.MotTable.COLUMN_NAME_NB_ERR, nb_err);
        values.put(MotContract.MotTable.COLUMN_NAME_DATE_REV, date_rev);
        values.put(MotContract.MotTable.COLUMN_NAME_DATE_MAJ,date_maj);
        values.put(MotContract.MotTable.COLUMN_NAME_DIST_ID,dist_id);
        values.put(MotContract.MotTable.COLUMN_NAME_FRANCAIS,francais);
        values.put(MotContract.MotTable.COLUMN_NAME_LANGUE,langue);
        values.put(MotContract.MotTable.COLUMN_NAME_LANGUE_ID,langue_id);
        values.put(MotContract.MotTable.COLUMN_NAME_MOT_DIRECTEUR,mot_directeur);
        values.put(MotContract.MotTable.COLUMN_NAME_LANGUE_NIVEAU,langue_niveau);
        values.put(MotContract.MotTable.COLUMN_NAME_THEME_ID,theme._id);

        if (this._id > 0) {
            String selection = MotContract.MotTable.COLUMN_NAME_ID + " = " + _id;
            int count = db.update(MotContract.MotTable.TABLE_NAME,values,selection,null);
        } else {
            this._id = (int) db.insert(MotContract.MotTable.TABLE_NAME,null,values);
        }
    }

    public static Mot find_by(SQLiteDatabase db, String selection) {
        final Cursor mCursor = db.query(MotContract.MotTable.TABLE_NAME,null,selection,null,null,null,null);
        Mot mot = new Mot();
        if (mCursor.moveToFirst()) {
            mot._id = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_ID));
            mot.langue_id = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_LANGUE_ID));
            int theme_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_THEME_ID));
            mot.theme = Theme.find(db,theme_id);
            mot.francais = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_FRANCAIS));
            mot.langue_niveau = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_LANGUE_NIVEAU));
            mot.mot_directeur = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_MOT_DIRECTEUR));
            mot.langue = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_LANGUE));
            mot.date_rev = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_DATE_REV));
            mot.poids = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_POIDS));
            mot.nb_err = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_NB_ERR));
            mot.dist_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_DIST_ID));
            mot.date_maj = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_DATE_MAJ));
        } else {
            mot = null;
        }
        mCursor.close();
        return mot;
    }

    public static Mot find(SQLiteDatabase db, int id) {
        String selection = MotContract.MotTable.COLUMN_NAME_ID + " = " + id;
        return Mot.find_by(db,selection);
    }

    public static Cursor where(SQLiteDatabase db, String selection) {
        String sortOrder = MotContract.MotTable.COLUMN_NAME_MOT_DIRECTEUR + " ASC";
        return db.query(MotContract.MotTable.TABLE_NAME,null,selection,null,null,null,sortOrder);
    }
}
