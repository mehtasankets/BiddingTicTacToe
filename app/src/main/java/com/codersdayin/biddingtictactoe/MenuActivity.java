package com.codersdayin.biddingtictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GameStatus nextTurn;
    Button b11, b12, b13, b21, b22, b23, b31, b32, b33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nextTurn = GameStatus.PLAYER_A_TURN;

        b11 = (Button) findViewById(R.id.b11);
        b12 = (Button) findViewById(R.id.b12);
        b13 = (Button) findViewById(R.id.b13);

        b21 = (Button) findViewById(R.id.b21);
        b22 = (Button) findViewById(R.id.b22);
        b23 = (Button) findViewById(R.id.b23);

        b31 = (Button) findViewById(R.id.b31);
        b32 = (Button) findViewById(R.id.b32);
        b33 = (Button) findViewById(R.id.b33);

        setButtonListener(b11);
        setButtonListener(b12);
        setButtonListener(b13);

        setButtonListener(b21);
        setButtonListener(b22);
        setButtonListener(b23);

        setButtonListener(b31);
        setButtonListener(b32);
        setButtonListener(b33);
    }

    private void setButtonListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(button);
            }
        });
    }

    private void setText(Button button) {
        if(button.getText() == null || !button.getText().equals("")) {
            // nothing to be done here.
            return;
        }
        if(nextTurn == GameStatus.PLAYER_A_TURN) {
            button.setText("O");
            nextTurn = GameStatus.PLAYER_B_TURN;
        } else {
            button.setText("X");
            nextTurn = GameStatus.PLAYER_A_TURN;
        }
        GameStatus status = updateGameStatus();
        if(status != GameStatus.BLANK) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    private GameStatus updateGameStatus() {

        GameStatus[][] table = {
                {GameStatus.BLANK, GameStatus.BLANK, GameStatus.BLANK},
                {GameStatus.BLANK, GameStatus.BLANK, GameStatus.BLANK},
                {GameStatus.BLANK, GameStatus.BLANK, GameStatus.BLANK}};

        table[0][0] = getCellStatus(b11);
        table[0][1] = getCellStatus(b12);
        table[0][2] = getCellStatus(b13);

        table[1][0] = getCellStatus(b21);
        table[1][1] = getCellStatus(b22);
        table[1][2] = getCellStatus(b23);

        table[2][0] = getCellStatus(b31);
        table[2][1] = getCellStatus(b32);
        table[2][2] = getCellStatus(b33);

        GameStatus finalGameStatus = GameStatus.BLANK;
        // row checking
        for(int i=0; i<3; i++) {
            if(table[i][0] == table[i][1] && table[i][1] == table[i][2] && table[i][0] != GameStatus.BLANK) {
                finalGameStatus = table[i][0];
            }
        }

        // column checking
        for(int i=0; i<3; i++) {
            if(table[0][i] == table[1][i] && table[1][i] == table[2][i] && table[0][i] != GameStatus.BLANK) {
                finalGameStatus = table[0][i];
            }
        }

        // cross checking
        if(table[0][0] == table[1][1] && table[1][1] == table[2][2] && table[0][0] != GameStatus.BLANK) {
            finalGameStatus = table[0][0];
        }
        if(table[0][2] == table[1][1] && table[1][1] == table[2][0] && table[0][2] != GameStatus.BLANK) {
            finalGameStatus = table[0][2];
        }

        if(finalGameStatus != GameStatus.BLANK) {
            if(finalGameStatus == GameStatus.ZERO) {
                Toast.makeText(this, "Player A Won", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Player B Won", Toast.LENGTH_SHORT).show();
            }
        }
        return finalGameStatus;
    }

    private GameStatus getCellStatus(Button button) {
        if(button.getText().equals("O")) {
            return GameStatus.ZERO;
        } if(button.getText().equals("X")) {
            return GameStatus.CROSS;
        }
        return GameStatus.BLANK;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
