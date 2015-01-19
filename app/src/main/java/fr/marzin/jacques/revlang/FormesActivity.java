package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class FormesActivity extends Activity {

    public JmSession maJmSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_formes);
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
        this.setTitle("Formes verbales");
        int verbe_id = maJmSession.getVerbeId();
        String cond2;
        final Cursor mCursor;
        if (verbe_id > 0) {
            cond2 = " AND " + FormeContract.FormeTable.COLUMN_NAME_VERBE_ID + " = " + verbe_id;
            SQLiteDatabase db = maJmSession.getDb();
            mCursor = db.rawQuery("select F._id, F.langue, V.langue as verbe from formes as F JOIN verbes as V ON F.verbe_id=V._id where F." +
                    FormeContract.FormeTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0, 2).toLowerCase() + "\"" + cond2 +
                    " ORDER BY V." + VerbeContract.VerbeTable.COLUMN_NAME_LANGUE + " ASC , F." +
                    FormeContract.FormeTable.COLUMN_NAME_FORME_ID + " ASC", null);
        } else {
            mCursor = maJmSession.getCursor("Formes");
        }
        ListAdapter adapter = new SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mCursor,
            new String[] {FormeContract.FormeTable.COLUMN_NAME_LANGUE},
            new int[] {android.R.id.text1},
            0);
        ListView listView = (ListView) findViewById(fr.marzin.jacques.revlang.R.id.listView2);
        listView.setAdapter(adapter);
        if (maJmSession.getFormeId() > 0) {
            listView.setSelection(maJmSession.getFormePos());
            maJmSession.setFormeId(0);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                mCursor.moveToPosition(pos);
                int rowId = mCursor.getInt(mCursor.getColumnIndexOrThrow("_id"));
                maJmSession.setFormeId(rowId);
                Intent intent = new Intent(getBaseContext(), FormeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        maJmSession.save();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_formes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
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
