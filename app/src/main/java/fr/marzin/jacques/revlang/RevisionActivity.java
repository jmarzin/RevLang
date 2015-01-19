package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Hashtable;
import java.util.Random;


public class RevisionActivity extends Activity {

    public JmSession maJmSession;
    public Hashtable question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_revision);
    }

    @Override
    protected void onResume() {
        super.onResume();
        maJmSession = new JmSession(null,getBaseContext());
        String langue = maJmSession.getLangue();
        if (langue.equals("Italien")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.italien);
        } else {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.anglais);
        }
        this.setTitle("Révision");
        maJmSession.initRevision();
        if (JmSession.aleatoire == null) {
            JmSession.aleatoire = new Random();
        }
        poseQuestion();
    }

    @Override
    protected void onPause() {
        maJmSession.save();
        super.onResume();
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
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_themes:
                intent = new Intent(this, ThemesActivity.class);
                maJmSession.setThemeId(0);
                maJmSession.setMotId(0);
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_mots:
                intent = new Intent(this, MotsActivity.class);
                maJmSession.setThemeId(0);
                maJmSession.setMotId(0);
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_verbes:
                intent = new Intent(this, VerbesActivity.class);
                maJmSession.setVerbeId(0);
                maJmSession.setFormeId(0);
                startActivity(intent);
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_formes:
                intent = new Intent(this, FormesActivity.class);
                maJmSession.setVerbeId(0);
                maJmSession.setFormeId(0);
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
        if (maJmSession.getNbQuestions() > 0) {
            sousTitre += maJmSession.getNbErreurs() + " " + pluralize("erreur", maJmSession.getNbErreurs()) +
                    ", " + maJmSession.getNbQuestions() + " " + pluralize("question", maJmSession.getNbQuestions()) +
                    " (" + maJmSession.getNbErreurs() * 100 / maJmSession.getNbQuestions() + " %), ";
        }
        sousTitre += pluralize("reste", maJmSession.getNbTermesListe()) + " " + maJmSession.getNbTermesListe() + "(" +
                maJmSession.getTailleListe() + ")";
        getActionBar().setSubtitle(sousTitre);
    }

    public void poseQuestion() {
        ajusteSousTitre();
        TextView mBravo = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.bravoOuEchec);
        TextView mligne1 = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.ligne1Question);
        TextView mligne2 = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.ligne2Question);
        Button mBouton = (Button) findViewById(fr.marzin.jacques.revlang.R.id.boutonVerifierAutre);
        TextView mtexteReponse = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.texteReponse);
        EditText mReponse = (EditText) findViewById(fr.marzin.jacques.revlang.R.id.reponse);
        TableLayout mzoneQuestion = (TableLayout) findViewById(fr.marzin.jacques.revlang.R.id.zoneQuestion);
        question = maJmSession.question();
        if (question == null) {
            mBravo.setText("Plus de questions !");
            mBravo.setTextColor(0xFF000000);
            mligne1.setText("");
            mligne2.setText("");
            mzoneQuestion.setVisibility(View.INVISIBLE);
            mBouton.setVisibility(View.INVISIBLE);
        } else {
            mBravo.setText("");
            mligne1.setText((String) question.get("ligne1"));
            mligne2.setText((String) question.get("ligne2"));
            mBouton.setText("Vérifier");
            mtexteReponse.setText("");
            mReponse.setText("");
        }
    }

    public void clickBouton(View view) {
        Button mBouton = (Button) findViewById(fr.marzin.jacques.revlang.R.id.boutonVerifierAutre);

        if (mBouton.getText().equals("Vérifier")) {
            TextView mtexteReponse = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.texteReponse);
            String texte = question.get("reponse").toString();

            if (!question.get("prononciation").toString().equals("")) {
                texte += " [" + question.get("prononciation").toString() + "]";
            }
            int nouveauPoids;
            EditText mReponse = (EditText) findViewById(fr.marzin.jacques.revlang.R.id.reponse);
            maJmSession.setNbQuestions(maJmSession.getNbQuestions() + 1);
            if (egalite(mReponse.getText().toString(),question.get("reponse").toString())) {
                TextView mBravo = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.bravoOuEchec);
                mBravo.setText("Bravo !");
                mBravo.setTextColor(0xFE04CB05);
                mtexteReponse.setTextColor(0xFE04CB05);
                nouveauPoids = maJmSession.reduit(question);

            } else {
                maJmSession.setNbErreurs(maJmSession.getNbErreurs() + 1);
                TextView mBravo = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.bravoOuEchec);
                mBravo.setText("Erreur !");
                mBravo.setTextColor(0xFECB0403);
                mtexteReponse.setTextColor(0xFECB0403);
                nouveauPoids = maJmSession.augmente(question);
            }
            texte += " (" + nouveauPoids + ")";
            mtexteReponse.setText(eclate(texte));
            mBouton.setText("Autre question");
            ajusteSousTitre();
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
        String[] tableau = texte.split("/");
        texte_eclate = tableau[0];
        for (int i=1 ; i < tableau.length ; i++) {
            texte_eclate += " o\n"+tableau[i];
        }
        return texte_eclate;
    }
}
