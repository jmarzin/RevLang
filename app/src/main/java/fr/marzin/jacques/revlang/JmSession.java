package fr.marzin.jacques.revlang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

/**
 * Created by jacques on 01/01/15.
 */
public class JmSession {

    private String langue;
    private MyDbHelper dbManager;
    private SQLiteDatabase db;
    private String dateRev;
    private Session session;

    public static Boolean dejaMaj;
    public static Random aleatoire;

    public JmSession(String langue,Context context) {
        this.langue = langue;
        if (dejaMaj == null) {
            dejaMaj = false;
        }
        this.dbManager = new MyDbHelper(context);
        this.db = this.dbManager.getWritableDatabase();
        String selection;
        if (langue == null) {
            selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        } else {
            selection = SessionContract.SessionTable.COLUMN_NAME_LANGUE + " = \"" + langue + "\"";
        }
        session = Session.find_by(db, selection);
        this.langue = session.langue;
        if (session == null) {
            session = new Session();
            session.langue = context.getString(fr.marzin.jacques.revlang.R.string.titre_langue);
            return;
        }
    }

    public String getSelection(String objets) {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        date.setTime(date.getTime() -session.ageRev*24*3600000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateRev = sdf.format(date);
        String cond1, cond2 = "", cond3 = "", cond4 = "", cond5 = "";
        if (objets == "Mots") {
            cond1 = MotContract.MotTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0, 2).toLowerCase() + "\"";
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
            cond1 = FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0,2).toLowerCase() + "\"";
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
        return cond1 + cond2 + cond3 + cond4 + cond5;
    }

    public void creerListe() {

        String id;
        String poids;
        Cursor c;
        if (session.modeRevision.equals("Vocabulaire")) {
            c = Mot.where(db, getSelection("Mots"));
            id = MotContract.MotTable.COLUMN_NAME_ID;
            poids = MotContract.MotTable.COLUMN_NAME_POIDS;
        } else {
            c = Forme.where(db, getSelection("Formes"));
            id = FormeContract.FormeTable.COLUMN_NAME_ID;
            poids = FormeContract.FormeTable.COLUMN_NAME_POIDS;
        }
        session.liste = new ArrayList<Integer>();
        for (int i = 0 ; i < c.getCount() ; i++) {
            c.moveToNext();
            int element = c.getInt(c.getColumnIndexOrThrow(id));
            int nb = c.getInt(c.getColumnIndexOrThrow(poids));
            for (int j = 1 ; j <= nb ; j++) {
                session.liste.add(element);
            }
        }
    }

    public void initRevision() {
        if (session.modeRevision == null) {
            session.modeRevision = "Vocabulaire";
            creerListe();
        };
    }

    public void majStats(int nbQuestions, int nbErreurs) {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateCourante = sdf.format(date);

        String cond = StatsContract.StatsTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0,2).toLowerCase() + "\"";
        cond += " AND " + StatsContract.StatsTable.COLUMN_NAME_DATE + " = \"" + dateCourante + "\"";
        Stats stats = Stats.find_by(db,cond);

        if (stats == null) {
            stats = new Stats();
            stats.date_rev = dateCourante;
            stats.langue_id = langue.substring(0,2).toLowerCase();
        }

        if (session.modeRevision.equals("Vocabulaire")) {
            stats.nb_questions_mots += nbQuestions;
            stats.nb_erreurs_mots += nbErreurs;
        } else {
            stats.nb_questions_formes += nbQuestions;
            stats.nb_erreurs_formes += nbErreurs;
        }

        stats.save(db);
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public Boolean getDerniereSession() {
        return (session.derniereSession == 1);
    }

    public void setDerniereSession(Boolean derniereSession) {
        if (derniereSession) {
            session.derniereSession = 1;
        } else {
            session.derniereSession = 0;
        }
    }

    public MyDbHelper getDbManager() {
        return dbManager;
    }

    public void setDbManager(MyDbHelper dbManager) {
        this.dbManager = dbManager;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void save() {
        session.save(db);
    }


    public Hashtable getListeTousThemes () {
        Cursor c = Theme.where(db,"langue_id = \"" + langue.substring(0,2).toLowerCase() + "\"");
        Hashtable reponse = new Hashtable();
        int[] listeId = new int[c.getCount()];
        String[] listeNoms = new String[c.getCount()];

        for (int i = 0 ; i < c.getCount() ; i++) {
            c.moveToNext();
            listeId[i] = c.getInt(c.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_ID));
            listeNoms[i] = (String.format("%03d ",c.getInt(c.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_NUMERO))) +
                    c.getString(c.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_LANGUE)));
        }
        reponse.put("ids",listeId);
        reponse.put("noms",listeNoms);
        return reponse;
    }

    public Hashtable getListeTousVerbes () {

        Cursor c = Verbe.where(db,"langue_id = \"" + langue.substring(0,2).toLowerCase() + "\"");

        Hashtable reponse = new Hashtable();
        int[] listeId = new int[c.getCount()];
        String[] listeNoms = new String[c.getCount()];

        for (int i = 0 ; i < c.getCount() ; i++) {
            c.moveToNext();
            listeId[i] = c.getInt(c.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_ID));
            listeNoms[i] = c.getString(c.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_LANGUE));
        }
        reponse.put("ids",listeId);
        reponse.put("noms",listeNoms);
        return reponse;

    }

    public ArrayList<Integer> getListe() { return session.liste; }

    public void setListe(ArrayList<Integer> liste) { session.liste = liste; }

    public int[] getListeThemes() { return session.listeThemes; }

    public void setListeThemes(int[] listeThemes) {
        session.listeThemes = listeThemes;
    }

    public int[] getListeVerbes() {
        return session.listeVerbes;
    }

    public void setListeVerbes(int[] listeVerbes) {
        session.listeVerbes = listeVerbes;
    }

    public int getTailleListe() { return session.liste.size(); }

    public int getNbTermesListe() {
        Set<Integer> uniqueListe = new HashSet<Integer>(session.liste);
        return uniqueListe.size();
    }

    public int getPoidsMin() {
        return session.poidsMin;
    }

    public void setPoidsMin(int poidsMin) {
        session.poidsMin = poidsMin;
    }

    public int getErrMin() {
        return session.errMin;
    }

    public void setErrMin(int errMin) {
        session.errMin = errMin;
    }

    public int getAgeRev() {
        return session.ageRev;
    }

    public void setAgeRev(int ageRev) {
        session.ageRev = ageRev;
    }

    public String getModeRevision() {
        return session.modeRevision;
    }

    public void setModeRevision(String modeRevision) {
        session.modeRevision = modeRevision;
    }

    public String getDateRev() {
        return dateRev;
    }

    public void setDateRev(String dateRev) {
        this.dateRev = dateRev;
    }

    public int getConserveStats() {
        return session.conserveStats;
    }

    public void setConserveStats(int conserveStats) {
        session.conserveStats = conserveStats;
    }

    public int getNbQuestions() {
        return session.nbQuestions;
    }

    public void setNbQuestions(int nbQuestions) {
        session.nbQuestions = nbQuestions;
    }

    public int getNbErreurs() {
        return session.nbErreurs;
    }

    public void setNbErreurs(int nbErreurs) {
        session.nbErreurs = nbErreurs;
    }

    public void setThemeId(int row) {session.themeId = row; }

    public int getThemeId() { return session.themeId; }

    public void setMotId(int row) {session.motId = row;}

    public int getMotId() { return session.motId; }

    public void setVerbeId(int row) {session.verbeId = row;}

    public int getVerbeId() { return session.verbeId; }

    public void setFormeId(int row) {session.formeId = row;}

    public int getFormeId() { return session.formeId; }

}
