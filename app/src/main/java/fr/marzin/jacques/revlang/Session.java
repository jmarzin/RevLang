package fr.marzin.jacques.revlang;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jacques on 27/01/15.
 */
public class Session {
    public int _id;
    public String langue;
    public int derniereSession;
    public String modeRevision;
    public int poidsMin;
    public int errMin;
    public int ageRev;
    public int conserveStats;
    public int nbQuestions;
    public int nbErreurs;
    public int[] listeThemes;
    public int[] listeVerbes;
    public int themeId;
    public int motId;
    public int verbeId;
    public int formeId;
    public ArrayList<Integer> liste;

    public Session() {
        this.langue = null;
        this.derniereSession = 1;
        this.modeRevision = null;
        this.poidsMin = 0;
        this.errMin = 0;
        this.ageRev = 0;
        this.conserveStats = 0;
        this.nbQuestions = 0;
        this.nbErreurs = 0;
        this.listeThemes = new int[0];
        this.listeVerbes = new int[0];
        this.themeId = 0;
        this.motId = 0;
        this.verbeId = 0;
        this.formeId = 0;
        this.liste = new ArrayList<Integer>();
    }

    private static String serialize(int content[]) {
        return Arrays.toString(content);
    }

    private static Boolean myEqualsString(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        } else {
            for (int i = 0; i < s1.length() ; i++) {
                if (s1.charAt(i) != s2.charAt(i) ) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] deserialize(String content){
        if (content == null || myEqualsString(content,"[]") || myEqualsString(content,"")) {
            return new int[0];
        } else {
            String[] tableauString = content.substring(1, content.length()-1).split(",");
            int[] tableauInt = new int[tableauString.length];
            for (int i = 0; i < tableauString.length; i++) {
                tableauInt[i] = Integer.parseInt(tableauString[i].trim());
            }
            return tableauInt;
        }
    }

    public void save(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(SessionContract.SessionTable.COLUMN_NAME_LANGUE, this.langue);
        values.put(SessionContract.SessionTable.COLUMN_NAME_DERNIERE,this.derniereSession);
        values.put(SessionContract.SessionTable.COLUMN_NAME_MODE_REVISION,this.modeRevision);
        values.put(SessionContract.SessionTable.COLUMN_NAME_POIDS_MIN,poidsMin);
        values.put(SessionContract.SessionTable.COLUMN_NAME_ERR_MIN,errMin);
        values.put(SessionContract.SessionTable.COLUMN_NAME_AGE_REV,ageRev);
        values.put(SessionContract.SessionTable.COLUMN_NAME_CONSERVE_STATS,conserveStats);
        values.put(SessionContract.SessionTable.COLUMN_NAME_NB_QUESTIONS,nbQuestions);
        values.put(SessionContract.SessionTable.COLUMN_NAME_NB_ERREURS,nbErreurs);
        values.put(SessionContract.SessionTable.COLUMN_NAME_LISTE_THEMES,serialize(listeThemes));
        values.put(SessionContract.SessionTable.COLUMN_NAME_LISTE_VERBES,serialize(listeVerbes));
        values.put(SessionContract.SessionTable.COLUMN_NAME_THEME_ID,themeId);
        values.put(SessionContract.SessionTable.COLUMN_NAME_MOT_ID,motId);
        values.put(SessionContract.SessionTable.COLUMN_NAME_VERBE_ID,verbeId);
        values.put(SessionContract.SessionTable.COLUMN_NAME_FORME_ID,formeId);
        int[] listeb = new int[liste.size()];
        for (int i = 0 ; i < liste.size() ; i++) {
            listeb[i] = liste.get(i);
        }
        values.put(SessionContract.SessionTable.COLUMN_NAME_LISTE,serialize(listeb));

        if (this._id > 0) {
            String selection = SessionContract.SessionTable.COLUMN_NAME_ID + " = " + _id;
            int count = db.update(SessionContract.SessionTable.TABLE_NAME,values,selection,null);
        } else {
            this._id = (int) db.insert(SessionContract.SessionTable.TABLE_NAME,null,values);
        }
    }

    public static Session find_by(SQLiteDatabase db, String selection) {
        final Cursor mCursor = db.query(SessionContract.SessionTable.TABLE_NAME,null,selection,null,null,null,null);
        Session session = new Session();
        if (mCursor.moveToFirst()) {
            session._id = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_ID));
            session.langue = mCursor.getString(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_LANGUE));
            session.modeRevision = mCursor.getString(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_MODE_REVISION));
            session.poidsMin = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_POIDS_MIN));
            session.errMin = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_ERR_MIN));
            session.ageRev = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_AGE_REV));
            session.conserveStats = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_CONSERVE_STATS));
            session.nbQuestions = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_NB_QUESTIONS));
            session.nbErreurs = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_NB_ERREURS));
            session.listeVerbes = deserialize(mCursor.getString(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_LISTE_VERBES)));
            session.listeThemes = deserialize(mCursor.getString(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_LISTE_THEMES)));
            int[] listeb = deserialize(mCursor.getString(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_LISTE)));
            session.liste = new ArrayList<Integer>();
            for (int i = 0 ; i < listeb.length ; i++) {
                session.liste.add(listeb[i]);
            }
            session.themeId = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_THEME_ID));
            session.motId = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_MOT_ID));
            session.verbeId = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_VERBE_ID));
            session.formeId = mCursor.getInt(mCursor.getColumnIndexOrThrow(SessionContract.SessionTable.COLUMN_NAME_FORME_ID));
        } else {
            session = null;
        }
        mCursor.close();
        return session;
    }
}
