package fr.marzin.jacques.revlang;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jacques on 24/01/15.
 */
public class Forme extends ItemQuestionnable {

    public Verbe verbe;
//    public int verbe_id;
//    public String verbe_langue;
    public int forme_id;
    public String forme_text;

    public Forme() {
        this.poids = 1;
        this.nb_err = 0;
        this.date_rev = "1900-01-01";
        this.date_maj = "";
        this.dist_id = 0;
        this.forme_id = 0;
        this.langue = "";
        this.langue_id = "";
        this.verbe = new Verbe();
    }

    public void save(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(FormeContract.FormeTable.COLUMN_NAME_POIDS, poids);
        values.put(FormeContract.FormeTable.COLUMN_NAME_NB_ERR, nb_err);
        values.put(FormeContract.FormeTable.COLUMN_NAME_DATE_REV, date_rev);
        values.put(FormeContract.FormeTable.COLUMN_NAME_DATE_MAJ,date_maj);
        values.put(FormeContract.FormeTable.COLUMN_NAME_DIST_ID,dist_id);
        values.put(FormeContract.FormeTable.COLUMN_NAME_FORME_ID,forme_id);
        values.put(FormeContract.FormeTable.COLUMN_NAME_LANGUE,langue);
        values.put(FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID,langue_id);
        values.put(FormeContract.FormeTable.COLUMN_NAME_VERBE_ID,verbe._id);

        if (this._id > 0) {
            String selection = FormeContract.FormeTable.COLUMN_NAME_ID + " = " + _id;
            int count = db.update(FormeContract.FormeTable.TABLE_NAME,values,selection,null);
        } else {
            this._id = (int) db.insert(FormeContract.FormeTable.TABLE_NAME,null,values);
        }
    }

    public static Forme find_by(SQLiteDatabase db, String selection) {
        final Cursor mCursor = db.query(FormeContract.FormeTable.TABLE_NAME,null,selection,null,null,null,null);
        Forme forme = new Forme();
        if (mCursor.moveToFirst()) {
            forme._id = mCursor.getInt(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_ID));
            forme.langue_id = mCursor.getString(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID));
            int verbe_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_VERBE_ID));
            forme.verbe = Verbe.find(db, verbe_id);
            forme.forme_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_FORME_ID));
            forme.forme_text = FormeContract.forme_ids.get(forme.langue_id)[forme.forme_id - 1][0];
            forme.langue = mCursor.getString(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_LANGUE));
            forme.date_rev = mCursor.getString(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_DATE_REV));
            forme.poids = mCursor.getInt(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_POIDS));
            forme.nb_err = mCursor.getInt(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_NB_ERR));
            forme.dist_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_DIST_ID));
            forme.date_maj = mCursor.getString(mCursor.getColumnIndexOrThrow(FormeContract.FormeTable.COLUMN_NAME_DATE_MAJ));
        } else {
            forme = null;
        }
        mCursor.close();
        return forme;
    }

    public static Forme find(SQLiteDatabase db, int id) {
        String selection = FormeContract.FormeTable.COLUMN_NAME_ID + " = " + id;
        return Forme.find_by(db,selection);
    }

    public static Cursor where(SQLiteDatabase db, String selection) {
        String sortOrder = FormeContract.FormeTable.COLUMN_NAME_VERBE_ID + "," +
                FormeContract.FormeTable.COLUMN_NAME_FORME_ID + " ASC";
        return db.query(FormeContract.FormeTable.TABLE_NAME,null,selection,null,null,null,sortOrder);
    }
}
