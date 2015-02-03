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

    public SQLiteDatabase db;
    public Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_forme);
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

        this.setTitle("Forme verbale");
        Forme forme = Forme.find(db,session.formeId);

        TextView t_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_id);
        t_id.setText(""+session.formeId);
        TextView l_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.l_langue);
        l_langue.setText("en "+session.langue);

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
        String selection = SessionContract.SessionTable.COLUMN_NAME_DERNIERE + " = 1";
        session = Session.find_by(db, selection);
    }

    @Override
    protected void onPause() {
        session.save(db);
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
