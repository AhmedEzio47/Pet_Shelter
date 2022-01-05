package com.devyat.petshelter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PetDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "shelter.db";
    public static final int DB_VERSION = 1;
    public enum PetGender {UNKNOWN, FEMALE, MALE}

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME
            + "(" + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
            + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
            + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
            + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PetContract.PetEntry.TABLE_NAME + ";";

    private static final String SQL_INSERT = "INSERT INTO " + PetContract.PetEntry.TABLE_NAME
            + "(name, breed, gender, weight) VALUES (?, ?, ?, ?);";

    public PetDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public long insertPet( String name, String breed, PetGender gender, int weight){
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, name);
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, breed);
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, mapGender(gender));
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, weight);
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.insert(PetContract.PetEntry.TABLE_NAME, null, values);
    }

    public void deleteAllPets(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PetContract.PetEntry.TABLE_NAME, null, null);
    }

    public static int mapGender(PetGender gender){
        switch (gender){
            case UNKNOWN:
                return PetContract.PetEntry.GENDER_UNKNOWN;
            case FEMALE:
                return PetContract.PetEntry.GENDER_FEMALE;
            case MALE:
                return PetContract.PetEntry.GENDER_MALE;
        }
        return PetContract.PetEntry.GENDER_UNKNOWN;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
