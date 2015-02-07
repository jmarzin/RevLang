package fr.marzin.jacques.revlang;

import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

    public int reduit(SQLiteDatabase db, Session session) {
        majDateRev();
        int nRemove;
        int nouveauPoids;
        int facteur;
        if (this.getClass().getName().contains("Mot")) {
            facteur = 1;
        } else {
            facteur = -1;
        }
        if (poids == 1) {
            nouveauPoids = 1;
            nRemove = 1;
        } else {
            nouveauPoids = poids/2;
            nRemove = poids - nouveauPoids;
        }
        Integer valeur;
        for (int i=0 ; i < nRemove ; i++) {
            valeur = facteur*_id;
            session.liste.remove(valeur);
        }
        if (session.errMin > 0 && nb_err > 0) {
            nb_err -= 1;
        }
        poids = nouveauPoids;
        this.save(db);
        return nouveauPoids;
    }

    public int augmente(SQLiteDatabase db, Session session) {
        majDateRev();
        int facteur;
        if (this.getClass().getName().contains("Mot")) {
            facteur = 1;
        } else {
            facteur = -1;
        }
        int nouveauPoids = poids*2;
        int nAjout = nouveauPoids - poids;
        for (int i=0 ; i < nAjout ; i++) {
                session.liste.add((Integer) facteur*_id);
        }
        nb_err += 1;
        poids = nouveauPoids;
        this.save(db);
        return nouveauPoids;
    }
}
