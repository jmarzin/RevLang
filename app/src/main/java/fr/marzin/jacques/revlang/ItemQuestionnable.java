package fr.marzin.jacques.revlang;

import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by jacques on 24/01/15.
 */
abstract class ItemQuestionnable extends TermeBase {
    public String prononciation;
    public String date_rev;
    public int poids;
    public int nb_err;

    abstract void save(SQLiteDatabase db);

    private void majDateRev() {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date_rev = sdf.format(date);
    }

    public int reduit(JmSession maJmSession) {
        majDateRev();
        int nRemove;
        int nouveauPoids;
        if (poids == 1) {
            nouveauPoids = 1;
            nRemove = 1;
        } else {
            nouveauPoids = poids/2;
            nRemove = poids - nouveauPoids;
        }
        for (int i=0 ; i < nRemove ; i++) {
            maJmSession.getListe().remove((Integer) _id);
        }
        if (maJmSession.getErrMin() > 0 && nb_err > 0) {
            nb_err -= 1;
        }
        poids = nouveauPoids;
        this.save(maJmSession.getDb());
        maJmSession.majStats(1, 0);
        return nouveauPoids;
    }

    public int augmente(JmSession maJmSession) {
        majDateRev();
        int nouveauPoids = poids*2;
        int nAjout = nouveauPoids - poids;
        for (int i=0 ; i < nAjout ; i++) {
                maJmSession.getListe().add((Integer) _id);
        }
        nb_err += 1;
        poids = nouveauPoids;
        this.save(maJmSession.getDb());
        maJmSession.majStats(1, 1);
        return nouveauPoids;
    }
}
