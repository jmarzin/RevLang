package fr.marzin.jacques.revlang;

import android.provider.BaseColumns;

import java.util.Hashtable;

/**
 * Created by jacques on 01/01/15.
 */
public final class FormeContract {

    public FormeContract() {}

    public static Hashtable<String,String[][]> forme_ids = new Hashtable() {
        { put("it",  new String [][] {
                {"Gérondif"},
                {"Participe passé"},
                {"1ère pers sing. présent indic."},
                {"2ème pers sing. présent indic."},
                {"3ème pers sing. présent indic."},
                {"1ère pers plur. présent indic."},
                {"2ème pers plur. présent indic."},
                {"3ème pers plur. présent indic."},
                {"1ère pers sing. imparfait indic."},
                {"2ème pers sing. imparfait indic."},
                {"3ème pers sing. imparfait indic."},
                {"1ère pers plur. imparfait indic."},
                {"2ème pers plur. imparfait indic."},
                {"3ème pers plur. imparfait indic."},
                {"1ère pers sing. passé simple indic."},
                {"2ème pers sing. passé simple indic."},
                {"3ème pers sing. passé simple indic."},
                {"1ère pers plur. passé simple indic."},
                {"2ème pers plur. passé simple indic."},
                {"3ème pers plur. passé simple indic."},
                {"1ère pers sing. futur indic."},
                {"2ème pers sing. futur indic."},
                {"3ème pers sing. futur indic."},
                {"1ère pers plur. futur indic."},
                {"2ème pers plur. futur indic."},
                {"3ème pers plur. futur indic."},
                {"1ère pers sing. présent cond."},
                {"2ème pers sing. présent cond."},
                {"3ème pers sing. présent cond."},
                {"1ère pers plur. présent cond."},
                {"2ème pers plur. présent cond."},
                {"3ème pers plur. présent cond."},
                {"1ère pers sing. présent subj."},
                {"2ème pers sing. présent subj."},
                {"3ème pers sing. présent subj."},
                {"1ère pers plur. présent subj."},
                {"2ème pers plur. présent subj."},
                {"3ème pers plur. présent subj."},
                {"1ère pers sing. imparfait subj."},
                {"2ème pers sing. imparfait subj."},
                {"3ème pers sing. imparfait subj."},
                {"1ère pers plur. imparfait subj."},
                {"2ème pers plur. imparfait subj."},
                {"3ème pers plur. imparfait subj."},
                {"2ème pers sing. impératif"},
                {"3ème pers sing. impératif"},
                {"1ère pers plur. impératif"},
                {"2ème pers plur. impératif"},
                {"3ème pers plur. impératif"}});
            put("po",  new String [][] {
                    {"Gérondif"},
                    {"Participe passé"},
                    {"1ère pers sing. présent indic."},
                    {"2ème pers sing. présent indic."},
                    {"3ème pers sing. présent indic."},
                    {"1ère pers plur. présent indic."},
                    {"2ème pers plur. présent indic."},
                    {"3ème pers plur. présent indic."},
                    {"1ère pers sing. imparfait indic."},
                    {"2ème pers sing. imparfait indic."},
                    {"3ème pers sing. imparfait indic."},
                    {"1ère pers plur. imparfait indic."},
                    {"2ème pers plur. imparfait indic."},
                    {"3ème pers plur. imparfait indic."},
                    {"1ère pers sing. plus que parfait indic."},
                    {"2ème pers sing. plus que parfait indic."},
                    {"3ème pers sing. plus que parfait indic."},
                    {"1ère pers plur. plus que parfait indic."},
                    {"2ème pers plur. plus que parfait indic."},
                    {"3ème pers plur. plus que parfait indic."},
                    {"1ère pers sing. passé simple indic."},
                    {"2ème pers sing. passé simple indic."},
                    {"3ème pers sing. passé simple indic."},
                    {"1ère pers plur. passé simple indic."},
                    {"2ème pers plur. passé simple indic."},
                    {"3ème pers plur. passé simple indic."},
                    {"1ère pers sing. futur indic."},
                    {"2ème pers sing. futur indic."},
                    {"3ème pers sing. futur indic."},
                    {"1ère pers plur. futur indic."},
                    {"2ème pers plur. futur indic."},
                    {"3ème pers plur. futur indic."},
                    {"1ère pers sing. présent cond."},
                    {"2ème pers sing. présent cond."},
                    {"3ème pers sing. présent cond."},
                    {"1ère pers plur. présent cond."},
                    {"2ème pers plur. présent cond."},
                    {"3ème pers plur. présent cond."},
                    {"1ère pers sing. présent subj."},
                    {"2ème pers sing. présent subj."},
                    {"3ème pers sing. présent subj."},
                    {"1ère pers plur. présent subj."},
                    {"2ème pers plur. présent subj."},
                    {"3ème pers plur. présent subj."},
                    {"1ère pers sing. imparfait subj."},
                    {"2ème pers sing. imparfait subj."},
                    {"3ème pers sing. imparfait subj."},
                    {"1ère pers plur. imparfait subj."},
                    {"2ème pers plur. imparfait subj."},
                    {"3ème pers plur. imparfait subj."},
                    {"1ère pers sing. futur subj."},
                    {"2ème pers sing. futur subj."},
                    {"3ème pers sing. futur subj."},
                    {"1ère pers plur. futur subj."},
                    {"2ème pers plur. futur subj."},
                    {"3ème pers plur. futur subj."},
                    {"2ème pers sing. impératif"},
                    {"3ème pers sing. impératif"},
                    {"1ère pers plur. impératif"},
                    {"2ème pers plur. impératif"},
                    {"3ème pers plur. impératif"},
                    {"1ère pers sing. infinitif pers."},
                    {"2ème pers sing. infinitif pers."},
                    {"3ème pers sing. infinitif pers."},
                    {"1ère pers plur. infinitif pers."},
                    {"2ème pers plur. infinitif pers."},
                    {"3ème pers plur. infinitif pers."}});
            put("oc",  new String [][] {
                {"Participe présent"},
                {"Participe passé"},
                {"1ère pers sing. présent indic."},
                {"2ème pers sing. présent indic."},
                {"3ème pers sing. présent indic."},
                {"1ère pers plur. présent indic."},
                {"2ème pers plur. présent indic."},
                {"3ème pers plur. présent indic."},
                {"1ère pers sing. imparfait indic."},
                {"2ème pers sing. imparfait indic."},
                {"3ème pers sing. imparfait indic."},
                {"1ère pers plur. imparfait indic."},
                {"2ème pers plur. imparfait indic."},
                {"3ème pers plur. imparfait indic."},
                {"1ère pers sing. passé simple indic."},
                {"2ème pers sing. passé simple indic."},
                {"3ème pers sing. passé simple indic."},
                {"1ère pers plur. passé simple indic."},
                {"2ème pers plur. passé simple indic."},
                {"3ème pers plur. passé simple indic."},
                {"1ère pers sing. futur indic."},
                {"2ème pers sing. futur indic."},
                {"3ème pers sing. futur indic."},
                {"1ère pers plur. futur indic."},
                {"2ème pers plur. futur indic."},
                {"3ème pers plur. futur indic."},
                {"1ère pers sing. présent cond."},
                {"2ème pers sing. présent cond."},
                {"3ème pers sing. présent cond."},
                {"1ère pers plur. présent cond."},
                {"2ème pers plur. présent cond."},
                {"3ème pers plur. présent cond."},
                {"1ère pers sing. présent subj."},
                {"2ème pers sing. présent subj."},
                {"3ème pers sing. présent subj."},
                {"1ère pers plur. présent subj."},
                {"2ème pers plur. présent subj."},
                {"3ème pers plur. présent subj."},
                {"1ère pers sing. imparfait subj."},
                {"2ème pers sing. imparfait subj."},
                {"3ème pers sing. imparfait subj."},
                {"1ère pers plur. imparfait subj."},
                {"2ème pers plur. imparfait subj."},
                {"3ème pers plur. imparfait subj."},
                {"2ème pers sing. impératif pos."},
                {"1ère pers plur. impératif pos."},
                {"2ème pers plur. impératif pos."},
                {"2ème pers sing. impératif nég."},
                {"1ère pers plur. impératif nég."},
                {"2ème pers plur. impératif nég."}});
            put("an", new String [][] {
                    {"Preterit"},
                    {"Participe passé"}});
            put("es",  new String [][] {
                    {"Gérondif"},
                    {"Participe passé"},
                    {"1ère pers sing. présent indic."},
                    {"2ème pers sing. présent indic."},
                    {"3ème pers sing. présent indic."},
                    {"1ère pers plur. présent indic."},
                    {"2ème pers plur. présent indic."},
                    {"3ème pers plur. présent indic."},
                    {"1ère pers sing. imparfait indic."},
                    {"2ème pers sing. imparfait indic."},
                    {"3ème pers sing. imparfait indic."},
                    {"1ère pers plur. imparfait indic."},
                    {"2ème pers plur. imparfait indic."},
                    {"3ème pers plur. imparfait indic."},
                    {"1ère pers sing. passé simple indic."},
                    {"2ème pers sing. passé simple indic."},
                    {"3ème pers sing. passé simple indic."},
                    {"1ère pers plur. passé simple indic."},
                    {"2ème pers plur. passé simple indic."},
                    {"3ème pers plur. passé simple indic."},
                    {"1ère pers sing. futur indic."},
                    {"2ème pers sing. futur indic."},
                    {"3ème pers sing. futur indic."},
                    {"1ère pers plur. futur indic."},
                    {"2ème pers plur. futur indic."},
                    {"3ème pers plur. futur indic."},
                    {"1ère pers sing. présent cond."},
                    {"2ème pers sing. présent cond."},
                    {"3ème pers sing. présent cond."},
                    {"1ère pers plur. présent cond."},
                    {"2ème pers plur. présent cond."},
                    {"3ème pers plur. présent cond."},
                    {"1ère pers sing. présent subj."},
                    {"2ème pers sing. présent subj."},
                    {"3ème pers sing. présent subj."},
                    {"1ère pers plur. présent subj."},
                    {"2ème pers plur. présent subj."},
                    {"3ème pers plur. présent subj."},
                    {"1ère pers sing. imparfait 1 subj."},
                    {"2ème pers sing. imparfait 1 subj."},
                    {"3ème pers sing. imparfait 1 subj."},
                    {"1ère pers plur. imparfait 1 subj."},
                    {"2ème pers plur. imparfait 1 subj."},
                    {"3ème pers plur. imparfait 1 subj."},
                    {"1ère pers sing. imparfait 2 subj."},
                    {"2ème pers sing. imparfait 2 subj."},
                    {"3ème pers sing. imparfait 2 subj."},
                    {"1ère pers plur. imparfait 2 subj."},
                    {"2ème pers plur. imparfait 2 subj."},
                    {"3ème pers plur. imparfait 2 subj."},
                    {"1ère pers sing. futur subj."},
                    {"2ème pers sing. futur subj."},
                    {"3ème pers sing. futur subj."},
                    {"1ère pers plur. futur subj."},
                    {"2ème pers plur. futur subj."},
                    {"3ème pers plur. futur subj."},
                    {"2ème pers sing. impératif"},
                    {"3ème pers sing. impératif"},
                    {"1ère pers plur. impératif"},
                    {"2ème pers plur. impératif"},
                    {"3ème pers plur. impératif"}});
            put("li",  new String [][] {
                    {"1ère pers sing. présent"},
                    {"2ème pers sing. présent"},
                    {"3ème pers sing. présent"},
                    {"1ère pers plur. présent"},
                    {"2ème pers plur. présent"},
                    {"3ème pers plur. présent"},
                    {"1ère pers sing. passé"},
                    {"2ème pers sing. passé"},
                    {"3ème pers sing. passé"},
                    {"1ère pers plur. passé"},
                    {"2ème pers plur. passé"},
                    {"3ème pers plur. passé"},
                    {"1ère pers sing. futur"},
                    {"2ème pers sing. futur"},
                    {"3ème pers sing. futur"},
                    {"1ère pers plur. futur"},
                    {"2ème pers plur. futur"},
                    {"3ème pers plur. futur"},
                    {"1ère pers sing. fictif"},
                    {"2ème pers sing. fictif"},
                    {"3ème pers sing. fictif"},
                    {"1ère pers plur. fictif"},
                    {"2ème pers plur. fictif"},
                    {"3ème pers plur. fictif"},
                    {"1ère pers sing. volutif"},
                    {"2ème pers sing. volutif"},
                    {"3ème pers sing. volutif"},
                    {"1ère pers plur. volutif"},
                    {"2ème pers plur. volutif"},
                    {"3ème pers plur. volutif"},
                    {"Participe actif présent"},
                    {"Participe actif passé"},
                    {"Participe actif futur"},
                    {"Participe passif présent"},
                    {"Participe passif passé"},
                    {"Participe passif futur"}});
        }};


    public static abstract class FormeTable implements BaseColumns {
        public static final String TABLE_NAME = "formes";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_VERBE_ID = "verbe_id";
        public static final String COLUMN_NAME_FORME_ID = "forme_id";
        public static final String COLUMN_NAME_DIST_ID = "dist_id";
        public static final String COLUMN_NAME_LANGUE_ID = "langue_id";
        public static final String COLUMN_NAME_LANGUE = "langue";
        public static final String COLUMN_NAME_PRONONCIATION = "prononciation";
        public static final String COLUMN_NAME_DATE_MAJ = "date_maj";
        public static final String COLUMN_NAME_DATE_REV = "date_rev";
        public static final String COLUMN_NAME_POIDS = "poids";
        public static final String COLUMN_NAME_NB_ERR = "nb_err";


    }
}
