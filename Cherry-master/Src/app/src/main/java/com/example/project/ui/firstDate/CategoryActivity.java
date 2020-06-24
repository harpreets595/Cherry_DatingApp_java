package com.example.project.ui.firstDate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

public class CategoryActivity extends AppCompatActivity {

    private String categorySelected;
    private Uri gmmIntentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.map_imageView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(CategoryActivity.this, Choose_Place.class);
//                intent.putExtra("CategorySelected", CategoryActivityFragment.category);
//
//                startActivity(intent);

                categorySelected = CategoryActivityFragment.category;
                switch (categorySelected) {
                    case "Food":
                        Snackbar.make(view, categorySelected, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        gmmIntentUri = Uri.parse("geo:45.5017,-73.5673?z=10&q=Restaurants");
                        changeToMap();
                        break;
                    case "Movies":
                        Snackbar.make(view, categorySelected, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        gmmIntentUri = Uri.parse("geo:45.5017,-73.5673?z=10&q=Movies");
                        changeToMap();
                        break;
                    case "Indoor Activities":
                        Snackbar.make(view, categorySelected, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        gmmIntentUri = Uri.parse("geo:45.5017,-73.5673?z=10&q=Indoor Activities");
                        changeToMap();
                        break;
                    case "Outdoors Activities":
                        Snackbar.make(view, categorySelected, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        gmmIntentUri = Uri.parse("geo:45.5017,-73.5673?z=10&q=Outdoor Activities");
                        changeToMap();
                        break;
                }
            }
        });
        
    }

    private void changeToMap() {
        if (gmmIntentUri != null) {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivityForResult(mapIntent, 0);
        }
    }
}


