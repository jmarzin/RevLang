package fr.marzin.jacques.revlang;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jacques on 24/01/15.
 */
public class Question {

    public ItemQuestionnable item;
    public String ligne1;
    public String ligne2;
    public String prononciation;

    public Question(SQLiteDatabase db,Session session, Random aleatoire) {
        this.item = null;

        while (session.liste.size() != 0 && this.item == null) {
            int id_tire = session.liste.get(aleatoire.nextInt(session.liste.size()));
            if (id_tire > 0) {
                Mot mot = Mot.find(db, id_tire);
                if (mot != null) {
                    this.item = mot;
                    if (mot.theme != null) {
                        this.ligne1 = mot.theme.langue;
                    } else {
                        this.ligne1 = "";
                    }
                    this.ligne2 = mot.francais;
                    this.prononciation = mot.pronunciation;
                } else {
                    while (session.liste.remove((Integer) id_tire)) { };
                }
            } else {
                Forme forme = Forme.find(db, -id_tire);
                if (forme != null) {
                    this.item = forme;
                    if (forme.verbe != null) {
                        this.ligne1 = forme.verbe.langue;
                    } else {
                        this.ligne1 = "";
                    }
                    this.ligne2 = forme.forme_text;
                } else {
                    while (session.liste.remove((Integer) (-id_tire))) { };
                }
            }
        }
    }
}
