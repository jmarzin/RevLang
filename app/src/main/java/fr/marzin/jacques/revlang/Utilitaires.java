package fr.marzin.jacques.revlang;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jacques on 28/01/15.
 */
public class Utilitaires {

    public static int drapeau(String langue) {
        if (langue.equals("Italien")) {
            return R.drawable.italien;
        } else if (langue.equals("Anglais")) {
            return R.drawable.anglais;
        } else if (langue.equals("Espagnol")) {
            return R.drawable.espagnol;
        } else if (langue.equals("Occitan")) {
            return R.drawable.occitan;
        } else if (langue.equals("Portugais")) {
            return R.drawable.portugais;
        } else if (langue.equals("Allemand")) {
            return R.drawable.allemand;
        } else {
            return R.drawable.lingvo;
        }
    }

    public static Locale setLocale(String langue) {
        if (langue.equals("Italien")) {
            return Locale.ITALIAN;
        } else if (langue.equals("Anglais")) {
            return Locale.ENGLISH;
        } else if (langue.equals("Espagnol")) {
            return new Locale("es", "ES");
        } else if (langue.equals("Occitan")) {
            return null;
        } else if (langue.equals("Portugais")) {
            return new Locale("pt", "PT");
        } else if (langue.equals("Allemand")) {
            return new Locale("de", "DE");
        } else {
            return null;
        }
    }

    public static String getSelection(Session session, String objets) {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        date.setTime(date.getTime() -session.ageRev*24*3600000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateRev = sdf.format(date);
        String cond1, cond2 = "", cond3 = "", cond4 = "", cond5 = "", cond6 = "";
        if (objets == "Mots") {
            cond1 = MotContract.MotTable.COLUMN_NAME_LANGUE_ID + " = \"" + session.langue.substring(0, 2).toLowerCase() + "\"";
            cond6 = " AND " + MotContract.MotTable.COLUMN_NAME_LANGUE_NIVEAU + " <= \"" + session.nivMax + "\"";
            if (session.ageRev != 0) {
                cond2 = " AND " + MotContract.MotTable.COLUMN_NAME_DATE_REV + " <= \"" + dateRev + "\"";
            }
            if (session.poidsMin > 1) {
                cond3 = " AND " + MotContract.MotTable.COLUMN_NAME_POIDS + " >= " + session.poidsMin;
            }
            if (session.errMin > 0) {
                cond4 = " AND " + MotContract.MotTable.COLUMN_NAME_NB_ERR + " >= " + session.errMin;
            }
            if (session.listeThemes.length > 0) {
                cond5 = " AND " + MotContract.MotTable.COLUMN_NAME_THEME_ID + " IN (";
                for (int i = 0; i < session.listeThemes.length; i++) {
                    if (i != 0) {
                        cond5 += ",";
                    }
                    cond5 += session.listeThemes[i];
                }
                cond5 += ")";
            }
        } else {
            cond1 = FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID + " = \"" + session.langue.substring(0,2).toLowerCase() + "\"";
            if (session.ageRev != 0) {
                cond2 = " AND " + FormeContract.FormeTable.COLUMN_NAME_DATE_REV + " <= \"" + dateRev + "\"";
            }
            if (session.poidsMin > 1) {
                cond3 = " AND " + FormeContract.FormeTable.COLUMN_NAME_POIDS + " >= " + session.poidsMin;
            }
            if (session.errMin > 0) {
                cond4 = " AND " + FormeContract.FormeTable.COLUMN_NAME_NB_ERR + " >= " + session.errMin;
            }
            if (session.listeVerbes.length > 0) {
                cond5 = " AND " + FormeContract.FormeTable.COLUMN_NAME_VERBE_ID + " IN (";
                for (int i=0 ; i < session.listeVerbes.length ; i++) {
                    if (i != 0) {cond5 += ",";}
                    cond5 += session.listeVerbes[i];
                }
                cond5 += ")";
            }
        }
        return cond1 + cond2 + cond3 + cond4 + cond5 + cond6;
    }

    public static void creerListe(SQLiteDatabase db, Session session) {

        String id;
        String poids;
        Cursor c;
        session.liste = new ArrayList<Integer>();
        if (session.modeRevision.equals("Vocabulaire") || session.modeRevision.equals("Mixte")) {
            c = Mot.where(db, getSelection(session, "Mots"));
            id = MotContract.MotTable.COLUMN_NAME_ID;
            poids = MotContract.MotTable.COLUMN_NAME_POIDS;
            for (int i = 0 ; i < c.getCount() ; i++) {
                c.moveToNext();
                int element = c.getInt(c.getColumnIndexOrThrow(id));
                int nb = c.getInt(c.getColumnIndexOrThrow(poids));
                for (int j = 1 ; j <= nb ; j++) {
                    session.liste.add(element);
                }
            }
            c.close();
        }
        if (session.modeRevision.equals("Conjugaisons") || session.modeRevision.equals("Mixte")) {
            c = Forme.where(db, getSelection(session,"Formes"));
            id = FormeContract.FormeTable.COLUMN_NAME_ID;
            poids = FormeContract.FormeTable.COLUMN_NAME_POIDS;
            for (int i = 0 ; i < c.getCount() ; i++) {
                c.moveToNext();
                int element = c.getInt(c.getColumnIndexOrThrow(id));
                int nb = c.getInt(c.getColumnIndexOrThrow(poids));
                for (int j = 1 ; j <= nb ; j++) {
                    session.liste.add(-element);
                }
            }
            c.close();
        }
    }

    public static void initRevision(SQLiteDatabase db, Session session) {
        if (session.modeRevision == null) {
            session.modeRevision = "Vocabulaire";
            creerListe(db,session);
            session.save(db);
        };
    }
}
