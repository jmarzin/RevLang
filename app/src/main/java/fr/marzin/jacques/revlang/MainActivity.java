package fr.marzin.jacques.revlang;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    public SQLiteDatabase db;
    public Session session;
    public Toast message;
    public Boolean dejaMaj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("RevLang");
        dejaMaj = false;
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_main);
        Context context = getApplicationContext();
        String text = getString(fr.marzin.jacques.revlang.R.string.erreurChoixLangue);
        int duration = Toast.LENGTH_LONG;
        message = Toast.makeText(context, text, duration);
        message.setGravity(Gravity.TOP, 0, 0);
        MyDbHelper dbManager = new MyDbHelper(getBaseContext());
        db = dbManager.getWritableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        session = Session.find_by(db, selection);
        if (session == null) {
            session = new Session();
            session.langue = getString(fr.marzin.jacques.revlang.R.string.titre_langue);
        }
        TextView mTexteLangue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue);
        mTexteLangue.setText(session.langue);
    }

    @Override
    protected void onPause() {
        session.save(db);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void changeLangue(String langue) {
        TextView mTexteLangue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue);
        if (mTexteLangue.getText().equals(langue)) {
            return;
        } else {
            session.derniereSession = 0;
            session.save(db);
            String selection = SessionContract.SessionTable.COLUMN_NAME_LANGUE + " = \"" + langue + "\"";
            session = Session.find_by(db, selection);
            if (session == null) {
                session = new Session();
                session.langue = langue;
            }
            dejaMaj = false;
            mTexteLangue.setText(langue);
        }
    }

    public void clickDrapeauItalien(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Italien));
    }

    public void clickDrapeauAnglais(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Anglais));
    }

    public void clickDrapeauEspagnol(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Espagnol));
    }

    public void clickDrapeauPortugais(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Portugais));
    }

    public void clickDrapeauOccitan(View view) {
        changeLangue("Occitan");
    }

    public void clickDrapeauLingvo(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Lingvo));
    }

    public void clickDrapeauAllemand(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Allemand));
    }

    private boolean Oklangue() {
        TextView mTexteLangue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue);
        String contenu = getString(fr.marzin.jacques.revlang.R.string.titre_langue);
        if (mTexteLangue.getText().equals(contenu)) {
            message.show();
            return false;
        } else {
            return true;
        }
    }

    public void lanceActivite(Intent intent) {
        if (!dejaMaj) {
            dejaMaj = true;
            MiseAJour.startActionMaj(getBaseContext(), session.langue);
        }
        startActivity(intent);
    }

    public void clickThemes(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, ThemesActivity.class);
            session.themeId = 0;
            session.motId = 0;
            lanceActivite(intent);
        }
    }

    public void clickMots(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, MotsActivity.class);
            session.themeId = 0;
            session.motId = 0;
            lanceActivite(intent);
        }
    }

    public void clickVerbes(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, VerbesActivity.class);
            session.verbeId = 0;
            session.formeId = 0;
            lanceActivite(intent);
        }
    }

    public void clickFormes(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, FormesActivity.class);
            session.verbeId = 0;
            session.formeId = 0;
            lanceActivite(intent);
        }
    }

    public void clickRevision(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, RevisionActivity.class);
            lanceActivite(intent);
        }
    }

    public void clickStatistiques(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, StatsActivity.class);
            lanceActivite(intent);
        }
    }

    public void clickParametrage(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, ParametrageActivity.class);
            lanceActivite(intent);
        }
    }

    public void clickQuitter(View view) {
        if (session.conserveStats == 0) {
            session.nbQuestions = 0;
            session.nbErreurs = 0;
        }
        dejaMaj = false;
        finish();
    }
}
