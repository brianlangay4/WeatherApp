package com.builtin.weatherapp;
/*creator Brian Barnabas Langay
        email brianlangay0@gmail.com*/

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    private ThemeViewModel themeViewModel;
    private SharedPreferences sharedPreferences;


    private String selectedTheme;
    private String pendingThemeChange;
    private String originalTheme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        // status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // Set the status bar to be transparent
            window.setStatusBarColor(Color.TRANSPARENT);

            // Make the content appear behind the status bar
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Change bottom nav color based on app's night mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            int navColor;

            if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                // Night mode
                navColor = ContextCompat.getColor(this, R.color.dark);
            } else {
                // Light or dark mode
                navColor = ContextCompat.getColor(this, R.color.d1);
            }

            window.setNavigationBarColor(navColor);
        }


        List<String> themes = new ArrayList<>();
        themes.add("default");
        themes.add("brown");
        themes.add("navy");


        //themeViewModel = new ViewModelProvider(this).get(ThemeViewModel.class);
        sharedPreferences = getSharedPreferences("ThemePreferences", MODE_PRIVATE);
        Spinner themeSpinner = findViewById(R.id.themeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.theme_options, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        themeSpinner.setAdapter(adapter);

        //Set the selected item based on the stored theme value
        String storedTheme = sharedPreferences.getString("selectedTheme", "default");
        int position = adapter.getPosition(storedTheme);
        themeSpinner.setSelection(position);

        // In your activity or fragment
        StringValueManager stringManager = new StringValueManager(this);

        // To set a new value
        stringManager.setValue("New Value");




        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private String selectedTheme;
            private String newSelectedTheme;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTheme = parent.getItemAtPosition(position).toString();
                updateTheme(selectedTheme);
                Log.d("track theme value :","value = "+selectedTheme);
                //get the value here
                // To set a new value
                stringManager.setValue(selectedTheme);
                String newSelectedTheme = parent.getItemAtPosition(position).toString();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stringManager.setValue(selectedTheme);
                checkAndHandleThemeChange(newSelectedTheme);

            }

        });


    }
    public void onBackPressed() {
        // Perform your custom actions when the back button is pressed
        // For example, you can call a method or execute code here
        Intent intent = new Intent(this, WeatherAcivity.class);
        finish(); // Finish the current activity
        startActivity(intent);

        // Call the superclass method to handle the default back button behavior
        super.onBackPressed();
    }


    private void updateTheme(String selectedTheme) {
       // Update SharedPreferences with the new theme value
        sharedPreferences.edit().putString("selectedTheme", selectedTheme).apply();

    }

    private void checkAndHandleThemeChange(String newTheme) {
        // In your activity or fragment
        StringValueManager stringManager = new StringValueManager(this);
        if (selectedTheme != null && selectedTheme.equals(newTheme)) {
            // Theme hasn't changed, do nothing
            return;
        }

        // Theme has changed, show confirmation popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Theme Change");
        builder.setMessage("Do you want to change the theme to " + newTheme + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, change the theme
                selectedTheme = newTheme;
                updateTheme(selectedTheme);
                stringManager.setValue(selectedTheme);
                // You can also include any additional optimizations like caching or memory management if needed
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User declined, reset pendingThemeChange
                pendingThemeChange = null;
                // Optionally, you could also reset the selection in the spinner to the original value
                // themeSpinner.setSelection(getPositionOfTheme(selectedTheme));
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Dismissed the dialog, reset pendingThemeChange
                pendingThemeChange = null;
                // Optionally, you could also reset the selection in the spinner to the original value
                // themeSpinner.setSelection(getPositionOfTheme(selectedTheme));
            }
        });

        // Store the newTheme as pending, in case the user confirms later
        pendingThemeChange = newTheme;

        builder.show();
    }
}
