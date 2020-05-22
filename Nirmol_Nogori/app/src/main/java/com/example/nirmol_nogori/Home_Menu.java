package com.example.nirmol_nogori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nirmol_nogori.Ui.MainActivity;

public class Home_Menu extends AppCompatActivity {

    private long backpressed;
    private Toast backtost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__menu);

    }

    //for Double press for Exit
    @Override
    public void onBackPressed() {

        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(Home_Menu.this, "press BACK again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }

        backpressed = System.currentTimeMillis();

    }
}
