package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.app.SearchManager;
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


public class searchResultsActivity extends Activity {

    public JmSession maJmSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fr.marzin.jacques.revlang.R.layout.activity_mots);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onPause() {
        maJmSession.save();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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
                if (maJmSession.getThemeId() > 0) {
                    intent = new Intent(getBaseContext(), ThemesActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
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
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            maJmSession = new JmSession(null,getBaseContext());
            String langue = maJmSession.getLangue();
            if (langue.equals("Italien")) {
                getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.italien);
            } else {
                getActionBar().setIcon(fr.marzin.jacques.revlang.R.drawable.anglais);
            }
            this.setTitle("Mots trouvés");
            final Cursor mCursor;
            SQLiteDatabase db = maJmSession.getDb();
            String sortOrder =
                MotContract.MotTable.COLUMN_NAME_MOT_DIRECTEUR + " ASC";
            String selection = MotContract.MotTable.COLUMN_NAME_LANGUE_ID + " = \"" + langue.substring(0, 2).toLowerCase() + "\"" +
                " AND (" + MotContract.MotTable.COLUMN_NAME_FRANCAIS + " LIKE \"" + query + "\"" +
                " OR " + MotContract.MotTable.COLUMN_NAME_LANGUE + " LIKE \"" + query + "\"" +
                " OR " + MotContract.MotTable.COLUMN_NAME_MOT_DIRECTEUR + " LIKE \"" + query + "\")";
            mCursor = db.query(
                MotContract.MotTable.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                sortOrder
                );
            ListAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                mCursor,
                new String[] {MotContract.MotTable.COLUMN_NAME_FRANCAIS},
                new int[] {android.R.id.text1},
                0);
            ListView listView = (ListView) findViewById(fr.marzin.jacques.revlang.R.id.listView2);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                    mCursor.moveToPosition(pos);
//                    maJmSession.setMotPos(pos);
                    int rowId = mCursor.getInt(mCursor.getColumnIndexOrThrow("_id"));
                    maJmSession.setMotId(rowId);
                    Intent intent = new Intent(getBaseContext(), MotActivity.class);
                    startActivity(intent);
                }
            });
        }
        return;
    }

}
