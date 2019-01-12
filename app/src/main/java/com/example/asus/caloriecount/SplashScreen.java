package com.example.asus.caloriecount;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        CustomFonts.setDefaultFont(this, "DEFAULT", "fonts/Gotham.ttf");
        CustomFonts.setDefaultFont(this, "MONOSPACE", "fonts/Gotham.ttf");
        CustomFonts.setDefaultFont(this, "SERIF", "fonts/Gotham.ttf");
        CustomFonts.setDefaultFont(this, "SANS_SERIF", "fonts/Gotham.ttf");

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SQLiteHandler db = new SQLiteHandler(getApplicationContext());

                if (!db.isUserEmpty()) {
                    Intent i = new Intent(SplashScreen.this, Data_Classess.class);
                    startActivity(i);
                    SplashScreen.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}