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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class VerbesActivity extends Activity {

    public JmSession maJmSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_verbes);
        maJmSession = new JmSession(null,getBaseContext());
        String langue = maJmSession.getLangue();
        if (langue.equals("Italien")) {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.italien);
        } else {
            getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.anglais);
        }
        this.setTitle("Verbes");

        final Cursor mCursor = Verbe.where(maJmSession.getDb(),"langue_id = \"" + langue.substring(0,2).toLowerCase() + "\"");

        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                mCursor,                                              // Pass in the cursor to bind to.
                new String[] {VerbeContract.VerbeTable.COLUMN_NAME_LANGUE},
                new int[] {android.R.id.text1},
                0);  // Parallel array of which template objects to bind to those columns.
        ListView listView = (ListView) findViewById(fr.marzin.jacques.revlang.R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                mCursor.moveToPosition(pos);
                int rowId = mCursor.getInt(mCursor.getColumnIndexOrThrow("_id"));
                maJmSession.setVerbeId(rowId);
                Intent intent = new Intent(getBaseContext(), FormesActivity.class);
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(fr.marzin.jacques.revlang.R.menu.menu_verbes, menu);
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
