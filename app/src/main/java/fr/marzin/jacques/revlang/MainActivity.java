package fr.marzin.jacques.revlang;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    public JmSession maJmSession;
    public Toast message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("RevLang");
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_main);
        Context context = getApplicationContext();
        String text = getString(fr.marzin.jacques.revlang.R.string.erreurChoixLangue);
        int duration = Toast.LENGTH_LONG;
        message = Toast.makeText(context, text, duration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        maJmSession = new JmSession(null,getBaseContext());
        TextView mTexteLangue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue);
        mTexteLangue.setText(maJmSession.getLangue());
    }

    @Override
    protected void onPause() {
        maJmSession.save();
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
            maJmSession.setDerniereSession(false);
            maJmSession.save();
            maJmSession = new JmSession(langue,getBaseContext());
            JmSession.dejaMaj = false;
            mTexteLangue.setText(langue);
        }
    }

    public void clickDrapeauItalien(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Italien));
    }

    public void clickDrapeauAnglais(View view) {
        changeLangue(getString(fr.marzin.jacques.revlang.R.string.Anglais));
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
        if (!JmSession.dejaMaj) {
            JmSession.dejaMaj = true;
            MiseAJour.startActionMaj(getBaseContext(), maJmSession.getLangue());
        }
        startActivity(intent);
    }

    public void clickThemes(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, ThemesActivity.class);
            maJmSession.setThemeId(0);
            maJmSession.setMotId(0);
            lanceActivite(intent);
        }
    }

    public void clickMots(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, MotsActivity.class);
            maJmSession.setThemeId(0);
            maJmSession.setMotId(0);
            lanceActivite(intent);
        }
    }

    public void clickVerbes(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, VerbesActivity.class);
            maJmSession.setVerbeId(0);
            maJmSession.setFormeId(0);
            lanceActivite(intent);
        }
    }

    public void clickFormes(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, FormesActivity.class);
            maJmSession.setVerbeId(0);
            maJmSession.setFormeId(0);
            lanceActivite(intent);
        }
    }

    public void clickRevision(View view) {
        if (Oklangue()) {
            Intent intent = new Intent(this, RevisionActivity.class);
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
        if (maJmSession.getConserveStats() == 0) {
            maJmSession.setNbQuestions(0);
            maJmSession.setNbErreurs(0);
        }
        this.finish();
    }
}
