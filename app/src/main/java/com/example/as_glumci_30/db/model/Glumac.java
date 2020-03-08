package com.example.as_glumci_30.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Glumac.TABLE_NAME_ACTOR)
public class Glumac {

    public static final String TABLE_NAME_ACTOR = "glumac";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_IME = "ime";
    public static final String FIELD_NAME_PREZIME = "prezime";
    public static final String FIELD_NAME_BIOGRAFIJA = "biografija";
    public static final String FIELD_NAME_RATING = "rating";
    public static final String FIELD_NAME_DATUM = "datum";
    public static final String FIELD_NAME_FILMOVI = "filmovi";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_IME)
    private String mIme;

    @DatabaseField(columnName = FIELD_NAME_PREZIME)
    private String mPrezime;

    @DatabaseField(columnName = FIELD_NAME_BIOGRAFIJA)
    private String mBiografija;

    @DatabaseField(columnName = FIELD_NAME_RATING)
    private float mRating;

    @DatabaseField(columnName = FIELD_NAME_DATUM)
    private String mDatum;

    @ForeignCollectionField(columnName = Glumac.FIELD_NAME_FILMOVI,eager = true)
    private ForeignCollection<Filmovi> mFilmovi;


    public Glumac() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmIme() {
        return mIme;
    }

    public void setmIme(String mIme) {
        this.mIme = mIme;
    }

    public String getmPrezime() {
        return mPrezime;
    }

    public void setmPrezime(String mPrezime) {
        this.mPrezime = mPrezime;
    }

    public String getmBiografija() {
        return mBiografija;
    }

    public void setmBiografija(String mBiografija) {
        this.mBiografija = mBiografija;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }

    public String getmDatum() {
        return mDatum;
    }

    public void setmDatum(String mDatum) {
        this.mDatum = mDatum;
    }

    public ForeignCollection<Filmovi> getmFilmovi() {
        return mFilmovi;
    }

    public void setmFilmovi(ForeignCollection<Filmovi> mFilmovi) {
        this.mFilmovi = mFilmovi;
    }

    @Override
    public String toString() {
        return mIme + " " + mPrezime;
    }
}

