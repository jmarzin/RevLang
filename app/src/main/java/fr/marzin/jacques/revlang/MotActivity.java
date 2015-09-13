package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;


public class MotActivity extends Activity {

    public SQLiteDatabase db;
    public Session session;
    private TextToSpeech ttobj;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_mot);

        MyDbHelper dbManager = new MyDbHelper(getBaseContext());
        db = dbManager.getWritableDatabase();
        String selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        session = Session.find_by(db, selection);

        if (session.langue.equals("Italien")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.italien);
            locale = Locale.ITALIAN;
        } else if (session.langue.equals("Anglais")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.anglais);
            locale = Locale.ENGLISH;
        } else if (session.langue.equals("Espagnol")) {
            getActionBar().setIcon(R.drawable.espagnol);
            locale = new Locale("es", "ES");
        } else if (session.langue.equals("Occitan")) {
            getActionBar().setIcon(R.drawable.occitan);
            locale = null;
        } else {
            getActionBar().setIcon(R.drawable.lingvo);
            locale = null;
        }

        this.setTitle("Mot");
        final Mot mot = Mot.find(db,session.motId);

        TextView t_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_id);
        t_id.setText(""+session.motId);
        TextView l_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.l_langue);
        l_langue.setText("en "+session.langue);

        if (mot != null) {

            TextView t_langue_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue_id);
            t_langue_id.setText(mot.langue_id);

            TextView t_theme_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_theme_langue);
            if (mot.theme != null) {
                t_theme_langue.setText(mot.theme.langue);
            } else {
                t_theme_langue.setText("");
            }

            TextView t_francais = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_francais);
            t_francais.setText(mot.francais);

            TextView t_directeur = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_directeur);
            t_directeur.setText(mot.mot_directeur);

            TextView t_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue);
            t_langue.setText(mot.langue);

            TextView t_prononciation = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue_niveau);
            t_prononciation.setText(mot.langue_niveau);

            TextView t_date_rev = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_date_rev);
            t_date_rev.setText(mot.date_rev);

            TextView t_poids = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_poids);
            t_poids.setText(""+mot.poids);

            TextView t_nberr = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_nberr);
            t_nberr.setText(""+mot.nb_err);

            TextView t_dist_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_dist_id);
            t_dist_id.setText(""+mot.dist_id);

            TextView t_date_maj = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_date_maj);
            t_date_maj.setText(mot.date_maj);

            ttobj=new TextToSpeech(getApplicationContext(),
                    new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR){
                                if (locale != null) {
                                    ttobj.setLanguage(locale);
                                    ttobj.speak(mot.langue, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        }
                    }
            );

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        session = Session.find_by(db, selection);
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
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_mot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case fr.marzin.jacques.revlang.R.id.action_themes:
                intent = new Intent(this, ThemesActivity.class);
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
            case fr.marzin.jacques.revlang.R.id.action_revision:
                intent = new Intent(this, RevisionActivity.class);
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
}
