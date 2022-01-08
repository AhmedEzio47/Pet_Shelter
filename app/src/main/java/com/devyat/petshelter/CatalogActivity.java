package com.devyat.petshelter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.devyat.petshelter.adapters.PetCursorAdapter;
import com.devyat.petshelter.data.PetContract;
import com.devyat.petshelter.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
            startActivity(intent);
        });
        LoaderManager.getInstance(this).initLoader(URL_LOADER, null, this);
        mPetsListView = findViewById(R.id.pets_list);
        View emptyView = findViewById(R.id.empty_view);
        mPetsListView.setEmptyView(emptyView);
        mPetCursorAdapter = new PetCursorAdapter(this, null);
        mPetsListView.setAdapter(mPetCursorAdapter);
    }
    ListView mPetsListView;
    PetCursorAdapter mPetCursorAdapter;

    private void insertDummyData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, "Garfield");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, "Dummy");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, 1);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 4);
        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);
    }

    private void deleteAllPets() {
        getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, null, null);
    }

    private void displayDatabaseInfo() {

        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, null, null, null, null);

            ListView petsListView = findViewById(R.id.pets_list);
            View emptyView = findViewById(R.id.empty_view);
            petsListView.setEmptyView(emptyView);
            petsListView.setAdapter(new PetCursorAdapter(this, cursor));
//            cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    static private final int  URL_LOADER = 0;
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id){
            case URL_LOADER:
                return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mPetCursorAdapter.swapCursor(data);
        mPetsListView.setAdapter(mPetCursorAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mPetCursorAdapter.swapCursor(null);
        mPetsListView.setAdapter(mPetCursorAdapter);
    }
}