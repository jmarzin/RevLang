package fr.marzin.jacques.revlang;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;

import static java.util.Collections.max;

public class MiseAJour extends IntentService {

    private static final String ACTION_MAJ = "fr.marzin.jacques.revlang.action.MAJ";
    private static final String EXTRA_LANGUE = "fr.marzin.jacques.revlang.extra.LANGUE";
    public static final String EXTRA_MESSAGE = "fr.marzin.jacques.revlang.extra.MESSAGE";
    private static final String ACTION_RETOUR_MAJ = "fr.marzin.jacques.revlang.action.RETOUR_MAJ";
    public static SQLiteDatabase db;
    private String langue;
    private String debutHttp;
    private String dateMajCategories;
    private String dateMajMots;
    private String dateMajVerbes;
    private String dateMajFormes;
    private String dateMajVocabulaire;
    private String dateMajConjugaisons;
    private int nombreMaj;

    public static void startActionMaj(Context context, String langue) {
        Intent intent = new Intent(context, MiseAJour.class);
        intent.setAction(ACTION_MAJ);
        intent.putExtra(EXTRA_LANGUE, langue);
        context.startService(intent);
    }

    public MiseAJour() {
        super("MiseAJour");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MAJ.equals(action)) {
                langue = intent.getStringExtra(EXTRA_LANGUE).toLowerCase();
                debutHttp = "http://langues.jmarzin.fr/"+langue+"/api/v2/";
                try {
                    handleActionMaj(langue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void envoiMessage (String message) {
        Intent callevent = new Intent(ACTION_RETOUR_MAJ);
        callevent.setAction(ACTION_MAJ);
        callevent.putExtra(EXTRA_MESSAGE, message);
        sendBroadcast(callevent);
    }

    private void handleActionMaj(String langue) throws JSONException {
        MyDbHelper dbManager = new MyDbHelper(getBaseContext());
        db = dbManager.getWritableDatabase();
        ConnectivityManager connMgr = (ConnectivityManager)
                getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            nombreMaj = 0;
            dateMajCategories = lectureGet(debutHttp + "date_categories");
            dateMajMots = lectureGet(debutHttp + "date_mots");
            dateMajVerbes = lectureGet(debutHttp + "date_verbes");
            dateMajFormes = lectureGet(debutHttp + "date_formes");
            boolean okMajVoc = true, okMajConj = true;
            if (dateMajCategories == null || dateMajMots == null || dateMajVerbes == null || dateMajFormes == null) {
                envoiMessage("Problème de réseau. Mise à jour impossible.");
            } else {
                if (besoinMajVocabulaire()) {
                    okMajVoc = majVocabulaire();
                }
                if (besoinMajConjugaisons()) {
                    okMajConj = majConjugaisons();
                }
                if (okMajConj && okMajVoc) {
                    if (nombreMaj == 0) {
                        envoiMessage("Tout est à jour.");
                    } else if (nombreMaj == 1) {
                        envoiMessage("1 objet mis à jour");
                    } else {
                        envoiMessage(nombreMaj + " objets mis à jour.");
                    }
                } else {
                    envoiMessage("Problème de réseau. Mise à jour impossible.");
                }
            }
        } else {
            envoiMessage("Pas de réseau. Mise à jour impossible.");
        }
        db.close();
        stopSelf();
    }

    private boolean besoinMajVocabulaire() {
        if (dateMajCategories.compareTo(dateMajMots) <= 0) {
            dateMajVocabulaire = dateMajMots;
        } else {
            dateMajVocabulaire = dateMajCategories;
        }
        long nbThemes = DatabaseUtils.queryNumEntries(db, "\""+ThemeContract.ThemeTable.TABLE_NAME+"\"",
                ThemeContract.ThemeTable.COLUMN_NAME_LANGUE_ID + "= \"" + langue.substring(0, 2) + "\"");
        if (nbThemes == 0) {
            return true;
        }
        long nbMots = DatabaseUtils.queryNumEntries(db, "\""+MotContract.MotTable.TABLE_NAME+"\"",
                MotContract.MotTable.COLUMN_NAME_LANGUE_ID + "= \""+langue.substring(0,2)+"\"");
        if (nbMots == 0) {
            return true;
        }
        long nbThemesAnciens = DatabaseUtils.queryNumEntries(db, "\""+ThemeContract.ThemeTable.TABLE_NAME+"\"",
                ThemeContract.ThemeTable.COLUMN_NAME_LANGUE_ID + "= \"" + langue.substring(0, 2) + "\" AND " +
                ThemeContract.ThemeTable.COLUMN_NAME_DATE_MAJ + " < \""+dateMajCategories+"\"");
        long nbMotsAnciens = DatabaseUtils.queryNumEntries(db, "\""+MotContract.MotTable.TABLE_NAME+"\"",
                MotContract.MotTable.COLUMN_NAME_LANGUE_ID + "= \""+langue.substring(0,2)+"\" AND " +
                MotContract.MotTable.COLUMN_NAME_DATE_MAJ + " < \""+dateMajMots+"\"");
        if (nbThemesAnciens == 0 && nbMotsAnciens == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean majVocabulaire() throws JSONException {
        Hashtable tableId = new Hashtable();
        String reponse = lectureGet(debutHttp + "categories");
        if (reponse == null) {
            return false;
        }
        JSONArray tableCategories = new JSONArray(reponse);
        int nombreCategories = tableCategories.length();
        for (int i=0 ; i < nombreCategories ; i++ ) {
            JSONArray categorie = tableCategories.optJSONArray(i);
            tableId.put(categorie.getInt(0), majCategorie(categorie));
        }
        reponse = lectureGet(debutHttp + "mots");
        if (reponse == null) {
            return false;
        }
        JSONArray tableMots = new JSONArray(reponse);
        int nombreMots = tableMots.length();
        for (int i=0 ; i < nombreMots ; i++) {
            JSONArray mot = tableMots.optJSONArray(i);
            majMot(mot, (int) tableId.get(mot.getInt(1)));
        }
        db.execSQL("DELETE FROM " + ThemeContract.ThemeTable.TABLE_NAME +
            " WHERE " + ThemeContract.ThemeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0, 2) + "\" AND " +
                ThemeContract.ThemeTable.COLUMN_NAME_DATE_MAJ + " <  \""+dateMajVocabulaire+"\"");
        db.execSQL("DELETE FROM " + MotContract.MotTable.TABLE_NAME +
                " WHERE " + MotContract.MotTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0, 2) + "\" AND " +
                MotContract.MotTable.COLUMN_NAME_DATE_MAJ + " <  \""+dateMajVocabulaire+"\"");
        return true;
    }

    private int majCategorie(JSONArray categorie) throws JSONException {
        String selection = ThemeContract.ThemeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0,2) + "\"" +
                " AND " + ThemeContract.ThemeTable.COLUMN_NAME_DIST_ID + " = " + categorie.getInt(0);
        Theme theme = Theme.find_by(db,selection);
        if (theme == null) {
            theme = new Theme();
            theme.dist_id = categorie.getInt(0);
            theme.langue_id = langue.substring(0,2);
        }
        if (theme.numero != categorie.getInt(1) || !theme.langue.equals(categorie.getString(2))) {
            nombreMaj++;
            theme.numero = categorie.getInt(1);
            theme.langue = categorie.getString(2);
        }
        theme.date_maj = dateMajVocabulaire;
        theme.save(db);
        return theme._id;
    }

    private void majMot(JSONArray mot_dist, int theme_id) throws JSONException {
        String selection = MotContract.MotTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0,2) + "\"" +
                " AND " + MotContract.MotTable.COLUMN_NAME_DIST_ID + " = " + mot_dist.get(0);
        Mot mot = Mot.find_by(db,selection);
        if (mot == null) {
            mot = new Mot();
            mot.dist_id = mot_dist.getInt(0);
            mot.langue_id = langue.substring(0,2);
        }
        if (mot.theme == null) {
            mot.theme = new Theme();
        }
        mot.theme._id = theme_id;
        Boolean modifie = false;
        if (!mot.francais.equals(mot_dist.getString(2)) ||
                !mot.mot_directeur.equals(mot_dist.getString(3)) ||
                !mot.langue.equals(mot_dist.getString(4))) {
            modifie = true;
            mot.francais =  mot_dist.getString(2);
            mot.mot_directeur = mot_dist.getString(3);
            mot.langue = mot_dist.getString(4);
        }
        if (mot_dist.length() == 6 && !mot.prononciation.equals(mot_dist.getString(5))) {
            modifie = true;
            mot.prononciation = mot_dist.getString(5);
        }
        if (modifie) { nombreMaj++;}
        mot.date_maj = dateMajVocabulaire;
        mot.save(db);
    }

    private boolean besoinMajConjugaisons() {
        if (dateMajVerbes.compareTo(dateMajFormes) <= 0) {
            dateMajConjugaisons = dateMajFormes;
        } else {
            dateMajConjugaisons = dateMajVerbes;
        }
        long nbVerbes = DatabaseUtils.queryNumEntries(db, "\""+ VerbeContract.VerbeTable.TABLE_NAME+"\"",
                VerbeContract.VerbeTable.COLUMN_NAME_LANGUE_ID + "= \"" + langue.substring(0, 2) + "\"");
        if (nbVerbes == 0) {
            return true;
        }
        long nbFormes = DatabaseUtils.queryNumEntries(db, "\""+ FormeContract.FormeTable.TABLE_NAME+"\"",
                FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID + "= \""+langue.substring(0,2)+"\"");
        if (nbFormes == 0) {
            return true;
        }
        long nbVerbesAnciens = DatabaseUtils.queryNumEntries(db, "\""+ VerbeContract.VerbeTable.TABLE_NAME+"\"",
                VerbeContract.VerbeTable.COLUMN_NAME_LANGUE_ID + "= \"" + langue.substring(0, 2) + "\" AND " +
                        VerbeContract.VerbeTable.COLUMN_NAME_DATE_MAJ + " < \""+dateMajVerbes+"\"");
        long nbFormesAnciennes = DatabaseUtils.queryNumEntries(db, "\""+ FormeContract.FormeTable.TABLE_NAME+"\"",
                FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID + "= \""+langue.substring(0,2)+"\" AND " +
                        FormeContract.FormeTable.COLUMN_NAME_DATE_MAJ + " < \""+dateMajFormes+"\"");
        if (nbVerbesAnciens == 0 && nbFormesAnciennes == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean majConjugaisons() throws JSONException {
        Hashtable tableId = new Hashtable();
        String reponse = lectureGet(debutHttp + "verbes");
        if (reponse == null) {
            return false;
        }
        JSONArray tableVerbes = new JSONArray(reponse);
        int nombreVerbes = tableVerbes.length();
        for (int i=0 ; i < nombreVerbes ; i++ ) {
            JSONArray verbe = tableVerbes.optJSONArray(i);
            tableId.put(verbe.getInt(0), majVerbe(verbe));
        }
        reponse = lectureGet(debutHttp + "formes");
        if (reponse == null) {
            return false;
        }
        JSONArray tableFormes = new JSONArray(reponse);
        int nombreFormes = tableFormes.length();
        for (int i=0 ; i < nombreFormes ; i++) {
            JSONArray forme = tableFormes.optJSONArray(i);
            majForme(forme, (int) tableId.get(forme.getInt(1)));
        }
        db.execSQL("DELETE FROM " + VerbeContract.VerbeTable.TABLE_NAME +
                " WHERE " + VerbeContract.VerbeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0, 2) + "\" AND " +
                VerbeContract.VerbeTable.COLUMN_NAME_DATE_MAJ + " <  \""+dateMajConjugaisons+"\"");
        db.execSQL("DELETE FROM " + FormeContract.FormeTable.TABLE_NAME +
                " WHERE " + FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0, 2) + "\" AND " +
                FormeContract.FormeTable.COLUMN_NAME_DATE_MAJ + " <  \""+dateMajConjugaisons+"\"");
        return true;
    }

    private int majVerbe(JSONArray verbe_dist) throws JSONException {
        String selection = VerbeContract.VerbeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0,2) + "\"" +
                " AND " + VerbeContract.VerbeTable.COLUMN_NAME_DIST_ID + " = " + verbe_dist.getInt(0);
        Verbe verbe = Verbe.find_by(db,selection);
        if (verbe == null) {
            verbe = new Verbe();
            verbe.dist_id = verbe_dist.getInt(0);
            verbe.langue_id = langue.substring(0,2);
        }
        if (!verbe.langue.equals(verbe_dist.getString(1))) {
            nombreMaj++;
            verbe.langue = verbe_dist.getString(1);
        }
        verbe.date_maj = dateMajConjugaisons;
        verbe.save(db);
        return verbe._id;
    }

    private void majForme(JSONArray forme_dist, int verbe_id) throws JSONException {
        String selection = FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0,2) + "\"" +
                " AND " + FormeContract.FormeTable.COLUMN_NAME_DIST_ID + " = " + forme_dist.getInt(0);
        Forme forme = Forme.find_by(db,selection);
        if (forme == null) {
            forme = new Forme();
            forme.dist_id = forme_dist.getInt(0);
            forme.langue_id = langue.substring(0,2);
        }
        if (forme.verbe == null) {
            forme.verbe = new Verbe();
        }
        Boolean modifie = false;
        forme.verbe._id = verbe_id;
        if (forme.forme_id != forme_dist.getInt(2) ||
                !forme.langue.equals(forme_dist.getString(3))) {
            modifie = true;
            forme.forme_id = forme_dist.getInt(2);
            forme.langue = forme_dist.getString(3);
        }
        if (forme_dist.length() == 5 && !forme.prononciation.equals(forme_dist.getString(4))) {
            modifie = true;
            forme.prononciation = forme_dist.getString(4);
        }
        if (modifie) {nombreMaj++;}
        forme.date_maj = dateMajConjugaisons;
        forme.save(db);
    }

    private String lectureGet(String url) {
        StringBuilder response = new StringBuilder();
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpGet get = new HttpGet();
        get.setURI(uri);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity messageEntity = httpResponse.getEntity();
            InputStream is = null;
            try {
                is = messageEntity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        } else {
            return null;
        }
    }
}
