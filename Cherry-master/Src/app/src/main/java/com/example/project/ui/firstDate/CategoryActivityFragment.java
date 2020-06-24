package com.example.project.ui.firstDate;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class CategoryActivityFragment extends Fragment {
    public static String category;
    private View root;


    public CategoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_category, container, false);

        final Spinner dateCategoriesSpinner = root.findViewById(R.id.Date_Categories_Spinner);
        List<String> categoriesList = new ArrayList<>();

        categoriesList.add("Food");
        categoriesList.add("Movies");
        categoriesList.add("Indoor Activities");
        categoriesList.add("Outdoors Activities");

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(getContext(), R.layout.categories_layout, R.id.category_TextView);

        catAdapter.addAll(categoriesList);
        dateCategoriesSpinner.setAdapter(catAdapter);

        dateCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categorySelected = dateCategoriesSpinner.getSelectedItem().toString();

                ImageView categoryImage = root.findViewById(R.id.category_imageView);

                if(categorySelected == "Food"){
                    categoryImage.setImageResource(R.mipmap.pizza);
                    category = categorySelected;

                }
                if(categorySelected == "Movies"){
//                    categoryImage.setVisibility(View.INVISIBLE)
                    categoryImage.setImageResource(R.mipmap.movies);
                    category = categorySelected;
                }
                if(categorySelected == "Indoor Activities"){
                    categoryImage.setImageResource(R.mipmap.indoor);
                    category = categorySelected;

                }
                if(categorySelected == "Outdoors Activities"){
                    categoryImage.setImageResource(R.mipmap.outdoor);
                    category = categorySelected;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }
}
