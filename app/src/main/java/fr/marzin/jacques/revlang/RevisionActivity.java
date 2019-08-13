package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;


public class RevisionActivity extends Activity {

    public SQLiteDatabase db;
    public Session session;
    public Question question;
    public Random aleatoire;
    private TextToSpeech ttobj;
    private Locale locale;
    private ImageButton imSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_revision);
        MyDbHelper dbManager = new MyDbHelper(getBaseContext());
        db = dbManager.getWritableDatabase();
        String selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        session = Session.find_by(db, selection);

        getActionBar().setIcon(Utilitaires.drapeau(session.langue));
        locale = Utilitaires.setLocale(session.langue);

        this.setTitle("Révision");

        Utilitaires.initRevision(db, session);
        if (aleatoire == null) {
            aleatoire = new Random();
        }
        poseQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        session = Session.find_by(db, selection);
        ttobj=new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            if (locale != null) {
                                ttobj.setLanguage(locale);
                            }
                        }
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        session.save(db);
        if(ttobj !=null){
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_revision, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
//                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_themes:
                intent = new Intent(this, ThemesActivity.class);
                session.themeId = 0;
                session.motId = 0;
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_mots:
                intent = new Intent(this, MotsActivity.class);
                session.themeId = 0;
                session.motId = 0;
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_verbes:
                intent = new Intent(this, VerbesActivity.class);
                session.verbeId = 0;
                session.formeId = 0;
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_formes:
                intent = new Intent(this, FormesActivity.class);
                session.verbeId = 0;
                session.formeId = 0;
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_statistiques:
                intent = new Intent(this, StatsActivity.class);
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_parametrage:
                intent = new Intent(this, ParametrageActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String pluralize(String mot, int nombre) {
        if (nombre < 2) {
            return mot;
        } else {
            if (mot.equals("reste")) {
                return mot + "nt";
            } else {
                return mot + "s";
            }
        }
    }

    private void ajusteSousTitre() {
        String sousTitre = "";
        if (session.nbQuestions > 0) {
            sousTitre += session.nbErreurs + " " + pluralize("erreur", session.nbErreurs) +
                    ", " + session.nbQuestions + " " + pluralize("question", session.nbQuestions) +
                    " (" + session.nbErreurs * 100 / session.nbQuestions + " %), ";
        }
        int nbTermesListe = session.getNbTermesListe();
        sousTitre += pluralize("reste", nbTermesListe) + " " + nbTermesListe + "(" +
                session.liste.size() + ")";
        getActionBar().setSubtitle(sousTitre);
    }

    public void poseQuestion() {
        ajusteSousTitre();
        TextView mBravo = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.bravoOuEchec);
        TextView mligne1 = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.ligne1Question);
        TextView mligne2 = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.ligne2Question);
        Button mBouton = (Button) findViewById(fr.marzin.jacques.revlang.R.id.boutonVerifierAutre);
        TextView mtexteReponse = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.texteReponse);
        TextView mPrononciation = (TextView) findViewById(R.id.prononciation);
        EditText mReponse = (EditText) findViewById(fr.marzin.jacques.revlang.R.id.reponse);
        TableLayout mzoneQuestion = (TableLayout) findViewById(fr.marzin.jacques.revlang.R.id.zoneQuestion);
        imSpeaker = (ImageButton) findViewById(R.id.im_speaker);
        question = new Question(db,session,aleatoire);
        imSpeaker.setVisibility(View.INVISIBLE);
        if (question.item == null) {
            mBravo.setText("Plus de questions !");
            mBravo.setTextColor(0xFF000000);
            mligne1.setText("");
            mligne2.setText("");
            mzoneQuestion.setVisibility(View.INVISIBLE);
            mBouton.setVisibility(View.INVISIBLE);
        } else {
            mBravo.setText("");
            mligne1.setText((String) question.ligne1);
            mligne2.setText((String) question.ligne2);
            mBouton.setText("Vérifier");
            mtexteReponse.setText("");
            mPrononciation.setText("");
            mReponse.setText("");
        }
    }

    public void clickSpeaker(View view) {
        ttobj.speak(eclate(question.item.langue), TextToSpeech.QUEUE_FLUSH, null);
    }

    public void clickBouton(View view) {
        Button mBouton = (Button) findViewById(fr.marzin.jacques.revlang.R.id.boutonVerifierAutre);

        if (mBouton.getText().equals("Vérifier")) {
            TextView mtexteReponse = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.texteReponse);
            TextView mPrononciation = (TextView) findViewById(R.id.prononciation);
            String texte = question.item.langue;

            int nouveauPoids;
            EditText mReponse = (EditText) findViewById(fr.marzin.jacques.revlang.R.id.reponse);
            session.nbQuestions++;
            if (egalite(mReponse.getText().toString(),question.item.langue)) {
                TextView mBravo = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.bravoOuEchec);
                mBravo.setText("Bravo !");
                mBravo.setTextColor(0xFE04CB05);
                mtexteReponse.setTextColor(0xFE04CB05);
                nouveauPoids = question.item.reduit(db,session);
                majStats(1,0);

            } else {
                session.nbErreurs++;
                TextView mBravo = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.bravoOuEchec);
                mBravo.setText("Erreur !");
                mBravo.setTextColor(0xFECB0403);
                mtexteReponse.setTextColor(0xFECB0403);
                nouveauPoids = question.item.augmente(db,session);
                majStats(1,1);
            }
            texte += " (" + nouveauPoids + ")";
            mtexteReponse.setText(eclate(texte));
            mPrononciation.setText(question.prononciation);
            mBouton.setText("Autre question");
            if (locale != null) {
                imSpeaker.setVisibility(View.VISIBLE);
            }
            ajusteSousTitre();
            if (session.parleAuto == 1) {
                clickSpeaker(view);
            }
        } else {
            poseQuestion();
        }
    }

    private boolean egalite(String reponse, String reponse_attendue) {
        reponse = reponse.toLowerCase();
        reponse_attendue = reponse_attendue.toLowerCase();
        for (String retval: reponse_attendue.split("/")){
            if (retval.equals(reponse)) {
                return true;
            }
        }
        return false;
    }

    private String eclate(String texte) {
        String texte_eclate;
        String ou;
        switch (question.item.langue_id) {
            case "it" :  ou = "o";
                break;
            case "an":  ou = "or";
                break;
            case "es":  ou = "o";
                break;
            case "po":  ou = "ou";
                break;
            case "li":  ou = "aŭ";
                break;
            case "oc":  ou = "o";
                break;
            case "al": ou = "oder";
            default: ou = "/";
                break;
        }
        String[] tableau = texte.split("/");
        texte_eclate = tableau[0];
        for (int i=1 ; i < tableau.length ; i++) {
            texte_eclate += " "+ou+" "+tableau[i];
        }
        return texte_eclate;
    }

    public void majStats(int nbQuestions, int nbErreurs) {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateCourante = sdf.format(date);

        String cond = StatsContract.StatsTable.COLUMN_NAME_LANGUE_ID + " = \"" + session.langue.substring(0,2).toLowerCase() + "\"";
        cond += " AND " + StatsContract.StatsTable.COLUMN_NAME_DATE + " = \"" + dateCourante + "\"";
        Stats stats = Stats.find_by(db,cond);

        if (stats == null) {
            stats = new Stats();
            stats.date_rev = dateCourante;
            stats.langue_id = session.langue.substring(0,2).toLowerCase();
        }

        if (question.item.getClass().getName().contains("Mot")) {
            stats.nb_questions_mots += nbQuestions;
            stats.nb_erreurs_mots += nbErreurs;
        } else {
            stats.nb_questions_formes += nbQuestions;
            stats.nb_erreurs_formes += nbErreurs;
        }

        stats.save(db);
    }
}
