package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Hashtable;


public class ParametrageActivity extends Activity {

    public SQLiteDatabase db;
    public Session session;
    private TextView mt_poidsMin;
    private TextView mt_errMin;
    private TextView mt_ageMin;
    private Switch mt_conserveStats;
    private RadioButton mRadioVocabulaire;
    private RadioButton mRadioConjugaisons;
    private RadioButton mRadioMixte;
    private LinearLayout mLayoutThemes;
    private LinearLayout mLayoutVerbes;
    private ListView lThemes;
    private ListView lVerbes;
    private int[] tableauIdThemes;
    String[] tableauThemes;
    private int[] tableauIdVerbes;
    private String[] tableauVerbes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_parametrage);
        MyDbHelper dbManager = new MyDbHelper(getBaseContext());
        db = dbManager.getWritableDatabase();
        String selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        session = Session.find_by(db, selection);

        if (session.langue.equals("Italien")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.italien);
        } else if (session.langue.equals("Anglais")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.anglais);
        } else if (session.langue.equals("Espagnol")) {
            getActionBar().setIcon(R.drawable.espagnol);
        } else {
            getActionBar().setIcon(R.drawable.lingvo);
        }

        this.setTitle("Paramétrage");
        Utilitaires.initRevision(db,session);
        mt_poidsMin = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_poidsMin);
        mt_poidsMin.setText(""+session.poidsMin);
        mt_ageMin = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_ageMin);
        mt_ageMin.setText(""+session.ageRev);
        mt_errMin = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_errMin);
        mt_errMin.setText(""+session.errMin);
        mt_conserveStats = (Switch) findViewById(fr.marzin.jacques.revlang.R.id.t_conserveStats);
        if (session.conserveStats == 1) {
            mt_conserveStats.setChecked(true);
        } else {
            mt_conserveStats.setChecked(false);
        }

        mRadioVocabulaire = (RadioButton) findViewById(fr.marzin.jacques.revlang.R.id.t_vocabulaire);
        mRadioConjugaisons = (RadioButton) findViewById(fr.marzin.jacques.revlang.R.id.t_conjugaisons);
        mRadioMixte = (RadioButton) findViewById(fr.marzin.jacques.revlang.R.id.t_mixte);
        if (session.modeRevision.equals("Vocabulaire")) {
            mRadioVocabulaire.setChecked(true);
        } else if (session.modeRevision.equals("Conjugaisons")){
            mRadioConjugaisons.setChecked(true);
        } else {
            mRadioMixte.setChecked(true);
        }

        onChangeChoix(mRadioVocabulaire);

        Cursor c = Theme.where(db,"langue_id = \"" + session.langue.substring(0,2).toLowerCase() + "\"");
        tableauThemes = new String[c.getCount()];
        tableauIdThemes = new int[c.getCount()];
        for (int i = 0 ; i < c.getCount() ; i++) {
            c.moveToNext();
            tableauIdThemes[i] = c.getInt(c.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_ID));
            tableauThemes[i] = (String.format("%03d ",c.getInt(c.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_NUMERO))) +
                    c.getString(c.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_LANGUE)));
        }

        c = Verbe.where(db,"langue_id = \"" + session.langue.substring(0,2).toLowerCase() + "\"");
        tableauIdVerbes = new int[c.getCount()];
        tableauVerbes = new String[c.getCount()];
        for (int i = 0 ; i < c.getCount() ; i++) {
            c.moveToNext();
            tableauIdVerbes[i] = c.getInt(c.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_ID));
            tableauVerbes[i] = c.getString(c.getColumnIndexOrThrow(VerbeContract.VerbeTable.COLUMN_NAME_LANGUE));
        }

        lThemes = (ListView) findViewById(fr.marzin.jacques.revlang.R.id.l_themes);
        lVerbes = (ListView) findViewById(fr.marzin.jacques.revlang.R.id.l_verbes);
        ArrayAdapter<String> themesAdapter =
                new ArrayAdapter<String>(this, fr.marzin.jacques.revlang.R.layout.choix_multiple, tableauThemes);
        ArrayAdapter<String> verbesAdapter =
                new ArrayAdapter<String>(this, fr.marzin.jacques.revlang.R.layout.choix_multiple, tableauVerbes);

        lThemes.setAdapter(themesAdapter);
        int[] tableauIdThemesSel = session.listeThemes;
        Arrays.sort(tableauIdThemesSel);
        for (int i = 0 ; i < tableauIdThemes.length ; i++) {
            if (Arrays.binarySearch(tableauIdThemesSel,tableauIdThemes[i]) >= 0) {
                lThemes.setItemChecked(i , true);
            }
        }

        lVerbes.setAdapter(verbesAdapter);
        int[] tableauIdVerbesSel = session.listeVerbes;
        Arrays.sort(tableauIdVerbesSel);
        for (int i = 0 ; i < tableauIdVerbes.length ; i++) {
            if (Arrays.binarySearch(tableauIdVerbesSel,tableauIdVerbes[i]) >= 0) {
                lVerbes.setItemChecked(i , true);
            }
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
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_parametrage, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void onChangeChoix(View view) {
        mRadioVocabulaire = (RadioButton) findViewById(fr.marzin.jacques.revlang.R.id.t_vocabulaire);
        mRadioConjugaisons = (RadioButton) findViewById(R.id.t_conjugaisons);
        mLayoutThemes = (LinearLayout) findViewById(fr.marzin.jacques.revlang.R.id.layout_themes);
        mLayoutVerbes = (LinearLayout) findViewById(fr.marzin.jacques.revlang.R.id.layout_verbes);
        if (mRadioVocabulaire.isChecked()) {
            mLayoutThemes.setVisibility(View.VISIBLE);
            mLayoutVerbes.setVisibility(View.GONE);
        } else if (mRadioConjugaisons.isChecked()) {
            mLayoutThemes.setVisibility(View.GONE);
            mLayoutVerbes.setVisibility(View.VISIBLE);
        } else {
            mLayoutThemes.setVisibility(View.VISIBLE);
            mLayoutVerbes.setVisibility(View.VISIBLE);
        }
    }

    public void onChangeConserveStats(View view) {
        if (mt_conserveStats.isChecked()) {
            session.conserveStats = 1;
        } else {
            session.conserveStats = 0;
        }
    }

    public void onPrepareListe(View view) {
        session.poidsMin = Integer.parseInt(mt_poidsMin.getText().toString());
        session.ageRev = Integer.parseInt(mt_ageMin.getText().toString());
        session.errMin = Integer.parseInt(mt_errMin.getText().toString());
        if (mRadioVocabulaire.isChecked()) {
            session.modeRevision = "Vocabulaire";
        } else if (mRadioConjugaisons.isChecked()) {
            session.modeRevision = "Conjugaisons";
        } else {
            session.modeRevision = "Mixte";
        }

        int[] tableauIdThemesChecked = new int[lThemes.getCheckedItemCount()];
        int j = 0;
        for (int i = 0; i < lThemes.getCount(); i++) {
            if (lThemes.isItemChecked(i)) {
                tableauIdThemesChecked[j]= tableauIdThemes[i];
                j++;
            }
        }
        session.listeThemes = tableauIdThemesChecked;

        int[] tableauIdVerbesChecked = new int[lVerbes.getCheckedItemCount()];
        j = 0;
        for (int i = 0 ; i < lVerbes.getCount() ; i++) {
            if (lVerbes.isItemChecked(i)) {
                tableauIdVerbesChecked[j] = tableauIdVerbes[i];
                j++;
            }
        }
        session.listeVerbes = tableauIdVerbesChecked;

        Utilitaires.creerListe(db,session);
        String sousTitre = "Liste de " + session.getNbTermesListe() + "terme(s) (" + session.liste.size() + ")";
        getActionBar().setSubtitle(sousTitre);
    }
}
