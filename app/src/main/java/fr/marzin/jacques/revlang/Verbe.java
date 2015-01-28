package fr.marzin.jacques.revlang;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jacques on 24/01/15.
 */
public class Verbe extends Groupe {

    public Verbe() {
        this.langue_id = "";
        this.langue = "";
        this.date_maj = "";
        this.dist_id = 0;
    }

    public void save(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(VerbeContract.VerbeTable.COLUMN_NAME_DATE_MAJ,date_maj);
        values.put(VerbeContract.VerbeTable.COLUMN_NAME_DIST_ID,dist_id);
        values.put(VerbeContract.VerbeTable.COLUMN_NAME_LANGUE,langue);
        values.put(VerbeContract.VerbeTable.COLUMN_NAME_LANGUE_ID,langue_id);

        if (this._id > 0) {
            String selection = VerbeContract.VerbeTable.COLUMN_NAME_ID + " = " + _id;
            int count = db.update(VerbeContract.VerbeTable.TABLE_NAME,values,selection,null);
        } else {
            this._id = (int) db.insert(VerbeContract.VerbeTable.TABLE_NAME,null,values);
        }
    }

    public static Verbe find_by(SQLiteDatabase db, String selection) {
        final Cursor mCursor = db.query(VerbeContract.VerbeTable.TABLE_NAME,null,selection,null,null,null,null);
        Verbe verbe = new Verbe();
        if (mCursor.moveToFirst()) {
            verbe._id = mCursor.getInt(mCursor.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_ID));
            verbe.langue_id = mCursor.getString(mCursor.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_LANGUE_ID));
            verbe.langue = mCursor.getString(mCursor.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_LANGUE));
            verbe.dist_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_DIST_ID));
            verbe.date_maj = mCursor.getString(mCursor.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_DATE_MAJ));
        } else {
            verbe = null;
        }
        mCursor.close();
        return verbe;
    }

    public static Verbe find(SQLiteDatabase db, int id) {
        String selection = VerbeContract.VerbeTable.COLUMN_NAME_ID + " = " + id;
        return Verbe.find_by(db,selection);
    }

    public static Cursor where(SQLiteDatabase db, String selection) {
        Cursor mCursor;
        String sortOrder = VerbeContract.VerbeTable.COLUMN_NAME_LANGUE + " ASC";
        mCursor = db.query(VerbeContract.VerbeTable.TABLE_NAME,null,selection,null,null,null,sortOrder);
        return mCursor;
    }
}
