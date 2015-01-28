package fr.marzin.jacques.revlang;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jacques on 24/01/15.
 */
public class Theme extends Groupe{
    public int numero;

    public Theme() {
        this.dist_id = 0;
        this.langue = "";
        this.numero = 0;
        this.date_maj = "";
        this.langue_id = "";
    }

    public void save(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(ThemeContract.ThemeTable.COLUMN_NAME_DATE_MAJ,date_maj);
        values.put(ThemeContract.ThemeTable.COLUMN_NAME_DIST_ID,dist_id);
        values.put(ThemeContract.ThemeTable.COLUMN_NAME_LANGUE,langue);
        values.put(ThemeContract.ThemeTable.COLUMN_NAME_LANGUE_ID,langue_id);
        values.put(ThemeContract.ThemeTable.COLUMN_NAME_NUMERO,numero);

        if (this._id > 0) {
            String selection = ThemeContract.ThemeTable.COLUMN_NAME_ID + " = " + _id;
            int count = db.update(ThemeContract.ThemeTable.TABLE_NAME,values,selection,null);
        } else {
            this._id = (int) db.insert(ThemeContract.ThemeTable.TABLE_NAME,null,values);
        }
    }

    public static Theme find_by(SQLiteDatabase db, String selection) {
        final Cursor mCursor = db.query(ThemeContract.ThemeTable.TABLE_NAME,null,selection,null,null,null,null);
        Theme theme = new Theme();
        if (mCursor.moveToFirst()) {
            theme._id = mCursor.getInt(mCursor.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_ID));
            theme.langue_id = mCursor.getString(mCursor.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_LANGUE_ID));
            theme.numero = mCursor.getInt(mCursor.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_NUMERO));
            theme.langue = mCursor.getString(mCursor.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_LANGUE));
            theme.dist_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_DIST_ID));
            theme.date_maj = mCursor.getString(mCursor.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_DATE_MAJ));
        } else {
            theme = null;
        }
        mCursor.close();
        return theme;
    }

    public static Theme find(SQLiteDatabase db, int id) {
        String selection = ThemeContract.ThemeTable.COLUMN_NAME_ID + " = " + id;
        return find_by(db,selection);
    }

    public static Cursor where(SQLiteDatabase db, String selection) {
        Cursor mCursor;
        String sortOrder = ThemeContract.ThemeTable.COLUMN_NAME_NUMERO + " ASC";
        mCursor = db.query(ThemeContract.ThemeTable.TABLE_NAME,null,selection,null,null,null,sortOrder);
        return mCursor;
    }
}
