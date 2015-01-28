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
            put("an", new String [][] {
                    {"Preterit"},
                    {"Participe passé"}});
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
