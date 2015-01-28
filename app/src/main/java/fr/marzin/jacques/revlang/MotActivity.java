package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class MotActivity extends Activity {

    public JmSession maJmSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_mot);
        maJmSession = new JmSession(null,getBaseContext());
        String langue = maJmSession.getLangue();
        if (langue.equals("Italien")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.italien);
        } else {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.anglais);
        }
        this.setTitle("Mot");
        int mot_id = maJmSession.getMotId();
        Mot mot = Mot.find(maJmSession.getDb(),mot_id);

        TextView t_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_id);
        t_id.setText(""+mot_id);
        TextView l_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.l_langue);
        l_langue.setText("en "+langue);

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

            TextView t_prononciation = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_prononciation);
            t_prononciation.setText(mot.prononciation);

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

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        maJmSession = new JmSession(null,getBaseContext());
    }

    @Override
    protected void onPause() {
        maJmSession.save();
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
            case fr.marzin.jacques.revlang.R.id.action_revision:
                intent = new Intent(this, RevisionActivity.class);
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
