package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class MotActivity extends Activity {

    public JmSession maJmSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_mot);
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
        this.setTitle("Mot");
        int mot_id = maJmSession.getMotId();
        SQLiteDatabase db = maJmSession.getDb();
        String selection = MotContract.MotTable.COLUMN_NAME_ID + " = " + mot_id;
        TextView t_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_id);
        t_id.setText(""+mot_id);
        final Cursor mCursor = db.query(
                MotContract.MotTable.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null
        );
        if (mCursor.moveToFirst()) {
            String langue_id = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_LANGUE_ID));
            TextView t_langue_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue_id);
            t_langue_id.setText(langue_id);

            int theme_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_THEME_ID));
            final Cursor vCursor = db.rawQuery("select langue from themes where _id = " + theme_id,null);
            String theme_langue;
            if (vCursor.moveToFirst()) {
                theme_langue = vCursor.getString(vCursor.getColumnIndexOrThrow(ThemeContract.ThemeTable.COLUMN_NAME_LANGUE));
            } else {
                theme_langue = "";
            }
            vCursor.close();
            TextView t_theme_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_theme_langue);
            t_theme_langue.setText(theme_langue);

            String francais = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_FRANCAIS));
            TextView t_francais = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_francais);
            t_francais.setText(francais);
            String directeur = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_MOT_DIRECTEUR));
            TextView t_directeur = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_directeur);
            t_directeur.setText(directeur);
            TextView l_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.l_langue);
            l_langue.setText("en "+langue);
            String mlangue = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_LANGUE));
            TextView t_langue = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_langue);
            t_langue.setText(mlangue);
            String prononciation = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_PRONONCIATION));
            TextView t_prononciation = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_prononciation);
            t_prononciation.setText(prononciation);
            String date_rev = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_DATE_REV));
            TextView t_date_rev = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_date_rev);
            t_date_rev.setText(date_rev);
            int poids = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_POIDS));
            TextView t_poids = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_poids);
            t_poids.setText(""+poids);
            int nberr = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_NB_ERR));
            TextView t_nberr = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_nberr);
            t_nberr.setText(""+nberr);
            int dist_id = mCursor.getInt(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_DIST_ID));
            TextView t_dist_id = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_dist_id);
            t_dist_id.setText(""+dist_id);
            String date_maj = mCursor.getString(mCursor.getColumnIndexOrThrow(MotContract.MotTable.COLUMN_NAME_DATE_MAJ));
            TextView t_date_maj = (TextView) findViewById(fr.marzin.jacques.revlang.R.id.t_date_maj);
            t_date_maj.setText(date_maj);
            mCursor.close();
        }

    }

    @Override
    protected void onPause() {
        maJmSession.save();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_mot, menu);
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
//                moveTaskToBack(true);
//                UiDevice.resetWatcherTriggers ();
//                UiDevice.pressBack();
                this.finish();
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
