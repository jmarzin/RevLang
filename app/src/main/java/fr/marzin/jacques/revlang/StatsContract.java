package fr.marzin.jacques.revlang;

import android.provider.BaseColumns;

/**
 * Created by jacques on 21/01/15.
 */
public class StatsContract {
    public StatsContract() {}

    public static abstract class StatsTable implements BaseColumns {
        public static final String TABLE_NAME = "stats";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_LANGUE_ID = "langue_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NB_QUESTIONS_MOTS = "nb_questions_mots";
        public static final String COLUMN_NAME_NB_ERREURS_MOTS = "nb_erreurs_mots";
        public static final String COLUMN_NAME_NB_QUESTIONS_FORMES = "nb_questions_formes";
        public static final String COLUMN_NAME_NB_ERREURS_FORMES = "nb_erreurs_formes";
    }
}
