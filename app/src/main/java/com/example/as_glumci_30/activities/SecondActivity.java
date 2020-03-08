package com.example.as_glumci_30.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.as_glumci_30.R;
import com.example.as_glumci_30.db.DatabaseHelper;
import com.example.as_glumci_30.db.model.Filmovi;
import com.example.as_glumci_30.db.model.Glumac;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SharedPreferences prefs;
    private Glumac glumac;


    private EditText ime;
    private EditText prezime;
    private EditText godinaRodjenja;
    private EditText biografija;
    private RatingBar ocena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setupToolbar();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int key = getIntent().getExtras().getInt(MainActivity.GLUMAC_KEY);

        try {
            glumac = getDatabaseHelper().getmGlumacDao().queryForId(key);

            ime = findViewById(R.id.second_ime);
            prezime = findViewById(R.id.second_prezime);
            godinaRodjenja = findViewById(R.id.second_godina);
            biografija = findViewById(R.id.second_biografija);
            ocena = findViewById(R.id.second_ocena);

            ime.setText(glumac.getmIme());
            prezime.setText(glumac.getmPrezime());
            godinaRodjenja.setText(glumac.getmDatum());
            biografija.setText(glumac.getmBiografija());
            ocena.setRating(glumac.getmRating());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = findViewById(R.id.second_listView);

        try {
            List<Filmovi> list = getDatabaseHelper().getmFilmDao().queryBuilder()
                    .where()
                    .eq(Filmovi.FIELD_NAME_GLUMCI, glumac.getmId())
                    .query();



            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Filmovi filmovi = (Filmovi) listView.getItemAtPosition(position);
                    Toast.makeText(SecondActivity.this, filmovi.getmNaziv() + " (" + filmovi.getmGodina() + ") - " + filmovi.getmZanr(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_item_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                editGlumac();
                break;
            case R.id.delete:
                deleteGlumac();
                refresh();
                break;
            case R.id.add_film:
                addFilm();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteGlumac() {
        try {
            getDatabaseHelper().getmGlumacDao().delete(glumac);
            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean toast = prefs.getBoolean(getString(R.string.toast_key), false);
        boolean notif = prefs.getBoolean(getString(R.string.notif_key), false);

        if (toast) {
            Toast.makeText(this, "Glumac obrisan", Toast.LENGTH_LONG).show();
        }

        if (notif) {
            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setSmallIcon(android.R.drawable.ic_menu_delete);
            builder.setContentTitle("Glumci");
            builder.setContentText("Glumac obrisan");

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.glumci_logo);

            builder.setLargeIcon(bitmap);
            notificationManager.notify(1, builder.build());
        }


    }

    private void editGlumac() {
        glumac.setmIme(ime.getText().toString());
        glumac.setmPrezime(prezime.getText().toString());
        glumac.setmDatum(godinaRodjenja.getText().toString());
        glumac.setmRating(ocena.getRating());
        glumac.setmBiografija(biografija.getText().toString());


        try {
            getDatabaseHelper().getmGlumacDao().update(glumac);

            boolean toast = prefs.getBoolean(getString(R.string.toast_key), false);
            boolean notif = prefs.getBoolean(getString(R.string.notif_key), false);

            if (toast) {
                Toast.makeText(this, "Update-ovani podaci o glumcu", Toast.LENGTH_LONG).show();
            }

            if (notif) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                builder.setSmallIcon(android.R.drawable.ic_menu_edit);
                builder.setContentTitle("Glumci");
                builder.setContentText("Update-ovani podaci o glumcu");

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.glumci_logo);

                builder.setLargeIcon(bitmap);
                notificationManager.notify(1, builder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addFilm() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_film_layout);
        dialog.setCanceledOnTouchOutside(false);

        Button add = dialog.findViewById(R.id.add_film_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText naziv = dialog.findViewById(R.id.film_naziv);
                EditText zanr = dialog.findViewById(R.id.film_zanr);
                EditText godina = dialog.findViewById(R.id.film_godina);

                Filmovi filmovi = new Filmovi();
                filmovi.setmNaziv(naziv.getText().toString());
                filmovi.setmZanr(zanr.getText().toString());
                filmovi.setmGodina(godina.getText().toString());
                filmovi.setmGlumac(glumac);

                try {
                    getDatabaseHelper().getmFilmDao().create(filmovi);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                refresh();

                boolean toast = prefs.getBoolean(getString(R.string.toast_key), false);
                boolean notif = prefs.getBoolean(getString(R.string.notif_key), false);

                if (toast) {
                    Toast.makeText(SecondActivity.this, "Dodat novi film", Toast.LENGTH_LONG).show();
                }

                if (notif) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(SecondActivity.this);

                    builder.setSmallIcon(android.R.drawable.ic_input_add);
                    builder.setContentTitle("Glumci");
                    builder.setContentText("Dodat novi film");

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.glumci_logo);

                    builder.setLargeIcon(bitmap);
                    notificationManager.notify(1, builder.build());
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.show();
        }
    }


    private void refresh() {
        ListView listview = findViewById(R.id.second_listView);

        if (listview != null) {
            ArrayAdapter<Filmovi> adapter = (ArrayAdapter<Filmovi>) listview.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<Filmovi> list = getDatabaseHelper().getmFilmDao().queryBuilder()
                            .where()
                            .eq(Filmovi.FIELD_NAME_GLUMCI, glumac.getmId())
                            .query();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}

