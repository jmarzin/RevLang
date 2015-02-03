package fr.marzin.jacques.revlang;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.getActivity;


public class StatsActivity extends Activity {

    public SQLiteDatabase db;
    public Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

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

        this.setTitle("Statistiques");

        Cursor mCursor = Stats.where(db,"langue_id = \"" + session.langue.substring(0,2).toLowerCase() + "\"");

        GraphView graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> questions = new LineGraphSeries<DataPoint>();
        questions.setColor(Color.GREEN);
        LineGraphSeries<DataPoint> erreurs = new LineGraphSeries<DataPoint>();
        erreurs.setColor(Color.RED);
        DataPoint point;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0 ; i < mCursor.getCount() ; i++) {
            mCursor.moveToNext();
            String dateS = mCursor.getString(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_DATE));
            Date date = new Date();
            try {
                date = formatter.parse(dateS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int nbQuestionsMots = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_MOTS));
            int nbErreursMots = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_MOTS));
            int nbQuestionsFormes = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_FORMES));
            int nbErreursFormes = mCursor.getInt(mCursor.getColumnIndexOrThrow(StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_FORMES));
            point = new DataPoint(date,nbQuestionsMots+nbQuestionsFormes);
            questions.appendData(point, true, mCursor.getCount());
            point = new DataPoint(date,nbErreursFormes+nbErreursMots);
            erreurs.appendData(point,true,mCursor.getCount());
        }
        graph.addSeries(questions);
        graph.addSeries(erreurs);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space


        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                fr.marzin.jacques.revlang.R.layout.ligne_liste_stats,
                mCursor,
                new String[] {StatsContract.StatsTable.COLUMN_NAME_DATE,
                        StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_MOTS,
                        StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_MOTS,
                        StatsContract.StatsTable.COLUMN_NAME_NB_QUESTIONS_FORMES,
                        StatsContract.StatsTable.COLUMN_NAME_NB_ERREURS_FORMES},
                new int[] {fr.marzin.jacques.revlang.R.id.text1,
                        fr.marzin.jacques.revlang.R.id.text2,
                        fr.marzin.jacques.revlang.R.id.text3,
                        fr.marzin.jacques.revlang.R.id.text4,
                        fr.marzin.jacques.revlang.R.id.text5},
                0);
        ListView listView = (ListView) findViewById(fr.marzin.jacques.revlang.R.id.listView);
        listView.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.menu_stats, menu);
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
            case fr.marzin.jacques.revlang.R.id.action_parametrage:
                intent = new Intent(this, ParametrageActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}