package fr.marzin.jacques.revlang;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by jacques on 24/01/15.
 */
public class Question {

    public ItemQuestionnable item;
    public String ligne1;
    public String ligne2;

    public Question(JmSession maJmSession) {
        this.item = null;
        ArrayList<Integer> liste = maJmSession.getListe();
        while (liste.size() != 0 && this.item == null) {
            int id_tire = liste.get(JmSession.aleatoire.nextInt(liste.size()));
            if (maJmSession.getModeRevision().equals("Vocabulaire")) {
                Mot mot = Mot.find(maJmSession.getDb(), id_tire);
                if (mot != null) {
                    this.item = mot;
                    if (mot.theme != null) {
                        this.ligne1 = mot.theme.langue;
                    } else {
                        this.ligne1 = "";
                    }
                    this.ligne2 = mot.francais;
                } else {
                    while (liste.remove((Integer) id_tire)) { };
                }
            } else {
                Forme forme = Forme.find(maJmSession.getDb(), id_tire);
                if (forme != null) {
                    this.item = forme;
                    if (forme.verbe != null) {
                        this.ligne1 = forme.verbe.langue;
                    } else {
                        this.ligne1 = "";
                    }
                    this.ligne2 = forme.forme_text;
                } else {
                    while (liste.remove((Integer) id_tire)) { };
                }
            }
        }
    }
}
