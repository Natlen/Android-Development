package com.natlen.ex1;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int RL =1;
    public static final int RL_G =2;
    public static final int FL_CLOCK =3;
    public static final int RL_T =4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

    }
    public void set_layout(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.act1:
                intent = new Intent(this, rl.class);
                startActivityForResult(intent,RL);
                break;
            case R.id.act2:
                intent = new Intent(this, rl_g.class);
                startActivityForResult(intent,RL_G);
                break;
            case R.id.act3:
                intent = new Intent(this, fl_clock.class);
                startActivityForResult(intent,FL_CLOCK);
                break;
            case R.id.act4:
                intent = new Intent(this, rl_t.class);
                startActivityForResult(intent,RL_T);
                break;
        }
    }
}
