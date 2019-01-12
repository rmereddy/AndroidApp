package com.example.asus.caloriecount;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.id;
import static com.example.asus.caloriecount.R.id.entry;

public class Data_Classess extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView result1;
    boolean doubleBackToExitPressedOnce = false;

    private Context context;
    private List<LogEntry> logEntries;
    private DisplayLogActivity disp;
    private CalorieSummary calSum;
    private boolean entry1;
    private  double ototal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__classess);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CustomFonts.setDefaultFont(this, "DEFAULT", "fonts/Gotham.ttf");
        CustomFonts.setDefaultFont(this, "MONOSPACE", "fonts/Gotham.ttf");
        CustomFonts.setDefaultFont(this, "SERIF", "fonts/Gotham.ttf");
        CustomFonts.setDefaultFont(this, "SANS_SERIF", "fonts/Gotham.ttf");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Data_Classess.this, AddNewEntryActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        retrieveCalorieLog();
        displayCalorieSummary();
        displaycal();



        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        Users user = db.getUser();

        //Bundle a = getIntent().getExtras();



        String namevr = user.getName();
        String resulttext = user.getResult();

        result1 = (TextView) findViewById(R.id.resulttext);


        getSupportActionBar().setTitle("Welcome " + namevr + "!");

        result1.setText(resulttext);


        db.close();
    }



    private void displaycal() {
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        int flag = 0;


        Users user = db.getUser();
        float str1 = calSum.getTotalCalories();
        float str = calSum.getTotalcalories1();



        double total = (double) str;
        double ntotal = (double) str1;

        double result;
        double tcal = user.getCalories();
        double daily = user.getDaily();
        double dispCal = tcal;

        String date1 = user.getDates();



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = calendar.getTime();
        String date2 = dateFormat.format(today);



        //     daily = daily - add1;
        //     db.update_daily(user.getCalories(), user.getName());


        if (user.getTempo() == ntotal && (date2.equals(user.getDates()))) {
            flag = 1;
        } else if (user.getTempo() == ntotal && (!date2.equals(user.getDates()))){
            flag = 2;
        } else if (user.getTempo() != ntotal && (date2.equals(user.getDates()))) {
            flag = 3;
        } else if (user.getTempo() != ntotal && (!date2.equals(user.getDates()))){
            flag = 4;
        }
        switch (flag) {
            case 1:

                dispCal = user.getDaily();
                break;
            case 2:

                dispCal = user.getCalories();
                //result = user.getCalories();


                break;
            case 3:

                dispCal = user.getDaily() - total;


                break;
            case 4:

                dispCal = user.getCalories() - total;

                break;
        }
        db.update_tempo(ntotal, user.getName());
        db.update_daily(dispCal, user.getName());
        db.update_Users(date2, user.getName());


        daily = user.getDaily();
        TextView textView = (TextView) findViewById(R.id.delete);
        if (daily < 0) {
            textView.setText( String.format( "%.1f",  Math.abs(daily)) + "\n" + "calories over");
        } else textView.setText( String.format( "%.1f",  daily ) + "\n" + "calories left");


        db.close();
    }



    private void retrieveCalorieLog() {
        LogManager.loadCalorieLog(this);
    }

    private void displayCalorieSummary() {
        calSum = new CalorieSummary(LogManager.getLogEntries());
        calSum.display(this);
    }


    protected void onResume() {
        super.onResume();

        displayCalorieSummary();
        displaycal();
        displaycal();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    public void reset(View view) {

        Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to reset your daily calorie?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
                SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                Users user = db.getUser();
                user.getCalories();
                db.update_daily(user.getCalories(), user.getName());
                displaycal();
                db.close();   // stop chronometer here

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.data__classess, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            {Context context = this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();   // stop chronometer here

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit) {
            Context context = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to remove and reenter your info?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                    Intent intent = new Intent(Data_Classess.this, MainActivity.class);
                    startActivity(intent);
                    db.clear();
                    finish();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == entry) {
            Intent intent = new Intent(this, AddNewEntryActivity.class);
            startActivity(intent);
        } else if (id == R.id.log) {
            Intent intent = new Intent(this, DisplayLogActivity.class);
            startActivity(intent);

        } else if (id == R.id.exit) {Context context = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    finish();   // stop chronometer here

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
