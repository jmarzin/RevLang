package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class FormeActivity extends Activity {

    public JmSession maJmSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_forme);
        maJmSession = new JmSession(null,getBaseContext());
        String langue = maJmSession.getLangue();
        if (langue.equals("Italien")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.italien);
        } else {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.anglais);
        }
        this.setTitle("Forme verbale");
        int forme_id = maJmSession.getFormeId();
        Forme forme = Forme.find(maJmSession.getDb(),forme_id);

        TextView t_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_id);
        t_id.setText(""+forme_id);
        TextView l_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.l_langue);
        l_langue.setText("en "+langue);

        if (forme != null) {

            TextView t_langue_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue_id);
            t_langue_id.setText(forme.langue_id);

            TextView t_verbe_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_verbe_langue);
            if (forme.verbe.langue != null) {
                t_verbe_langue.setText(forme.verbe.langue);
            } else {
                t_verbe_langue.setText("");
            }

            TextView t_forme = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_forme);
            t_forme.setText(forme.forme_text);

            TextView t_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue);
            t_langue.setText(forme.langue);

            TextView t_prononciation = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_prononciation);
            t_prononciation.setText(forme.prononciation);

            TextView t_date_rev = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_date_rev);
            t_date_rev.setText(forme.date_rev);

            TextView t_poids = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_poids);
            t_poids.setText(""+forme.poids);

            TextView t_nberr = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_nberr);
            t_nberr.setText(""+forme.nb_err);

            TextView t_dist_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_dist_id);
            t_dist_id.setText(""+forme.dist_id);

            TextView t_date_maj = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_date_maj);
            t_date_maj.setText(forme.date_maj);
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
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_forme, menu);
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
