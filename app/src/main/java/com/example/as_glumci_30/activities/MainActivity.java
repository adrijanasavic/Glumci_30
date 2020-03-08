package com.example.as_glumci_30.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.as_glumci_30.R;
import com.example.as_glumci_30.adapters.MyAdapter;
import com.example.as_glumci_30.db.DatabaseHelper;
import com.example.as_glumci_30.db.model.Glumac;
import com.example.as_glumci_30.dialog.AboutDialog;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    private DatabaseHelper databaseHelper;
    private SharedPreferences prefs;
    MyAdapter.OnItemClickListener listener;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static String GLUMAC_KEY = "GLUMAC_KEY";

    private AlertDialog dijalog;
    private Glumac g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        setupToolbar();

        prefs = PreferenceManager.getDefaultSharedPreferences( this );


        showGlumac();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_glumac:
                addGlumac();
                refresh();
                break;
            case R.id.settings:
                // Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
               //  startActivity(settings);
                break;
            case R.id.about_dialog:
                showDialog();
                break;
        }

        return super.onOptionsItemSelected( item );
    }


    private void refresh() {
        RecyclerView recyclerView = (RecyclerView) findViewById( R.id.lvList );

        if (recyclerView != null) {
            MyAdapter adapter = (MyAdapter) recyclerView.getAdapter();

            if (adapter != null) {

                try {
                    adapter.clear();
                    List<Glumac> list = getDatabaseHelper().getmGlumacDao().queryForAll();

                    adapter.addAll( list );

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void showGlumac() {


        recyclerView = findViewById( R.id.lvList );

        try {
            ArrayList<Glumac> list = (ArrayList<Glumac>) getDatabaseHelper().getmGlumacDao().queryForAll();

            adapter = new MyAdapter( MainActivity.this, list, MainActivity.this );
            recyclerView.setAdapter( adapter );

            layoutManager = new LinearLayoutManager( MainActivity.this );
            recyclerView.setLayoutManager( layoutManager );


        } catch (SQLException e) {
            e.printStackTrace();

        }

    }


    private void addGlumac() {
        final Dialog dialog = new Dialog( this );
        dialog.setContentView( R.layout.add_layout );
        dialog.setCanceledOnTouchOutside( false );


        Button add = dialog.findViewById( R.id.add_glumac );
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText glumacPrezime = dialog.findViewById( R.id.glumac_prezime );
                EditText glumacIme = dialog.findViewById( R.id.glumac_ime );
                EditText glumacBiografija = dialog.findViewById( R.id.glumac_biografija );
                EditText glumacDatum = dialog.findViewById( R.id.glumac_datum );
                EditText glumacRating = dialog.findViewById( R.id.glumac_rating );

                Glumac glumac = new Glumac();
                glumac.setmIme( glumacIme.getText().toString() );
                glumac.setmPrezime( glumacPrezime.getText().toString() );
                glumac.setmBiografija( glumacBiografija.getText().toString() );
                glumac.setmDatum( glumacDatum.getText().toString() );
                glumac.setmRating( Float.parseFloat( glumacRating.getText().toString() ) );

                try {
                    getDatabaseHelper().getmGlumacDao().create( glumac );


                    boolean toast = prefs.getBoolean( getString( R.string.toast_key ), false );
                    boolean notif = prefs.getBoolean( getString( R.string.notif_key ), false );

                    if (toast) {
                        Toast.makeText( MainActivity.this, "Unet nov glumac", Toast.LENGTH_LONG ).show();

                    }

                    if (notif) {
                        showNotification( "Unet nov glumac" );

                    }

                    refresh();

                } catch (NumberFormatException e) {
                    Toast.makeText( MainActivity.this, "Rating mora biti broj", Toast.LENGTH_SHORT ).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();


            }

        } );

        Button cancel = dialog.findViewById( R.id.cancel );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );

        dialog.show();
    }

    private void showDialog() {
        if (dijalog == null) {
            dijalog = new AboutDialog( MainActivity.this ).prepareDialog();
        } else {
            if (dijalog.isShowing()) {
                dijalog.dismiss();
            }
        }
        dijalog.show();
    }

    public void showNotification(String poruka) {

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        NotificationCompat.Builder builder = new NotificationCompat.Builder( MainActivity.this );
        builder.setSmallIcon( android.R.drawable.ic_input_add );
        builder.setContentTitle( "Glumci" );
        builder.setContentText( poruka );

        Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.mipmap.movies );


        builder.setLargeIcon( bitmap );
        notificationManager.notify( 1, builder.build() );
    }


    public void setupToolbar() {

        Toolbar toolbar = findViewById( R.id.toolbar );

        if (toolbar != null) {
            setSupportActionBar( toolbar );
        }

    }


    @Override
    protected void onResume() {

        super.onResume();
        refresh();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper( this, DatabaseHelper.class );
        }
        return databaseHelper;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState( savedInstanceState );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate( savedInstanceState );

    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged( configuration );

    }

    @Override
    public void OnItemClick(int position) {
        Glumac g = adapter.get( position );
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
         intent.putExtra(GLUMAC_KEY, g.getmId());
        startActivity(intent);
    }
}
