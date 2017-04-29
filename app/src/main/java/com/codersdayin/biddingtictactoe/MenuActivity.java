package com.codersdayin.biddingtictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GameStatus nextTurn;
    Button b11, b12, b13, b21, b22, b23, b31, b32, b33;
    EditText currentBidA;
    TextView currentBidBText, remainingBidAText, remainingBidBText;
    boolean bidNotReceived = true;
    Integer remainingBidA = 100, remainingBidB = 100;

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

        currentBidBText = (TextView) findViewById(R.id.currentBidB);
        remainingBidAText = (TextView) findViewById(R.id.remainingBidA);
        remainingBidBText = (TextView) findViewById(R.id.remainingBidB);

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

        currentBidA = (EditText) findViewById(R.id.currentBidA);
        setBidListener(currentBidA);
        toggleView();
    }

    private void setBidListener(final EditText currBid) {
        currBid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Integer currentBidA = Integer.parseInt(currBid.getText().toString());
                    currBid.setText("");
                    Double randomBid = (Math.random() * (remainingBidB - 1)) + 1;
                    Integer currentBidB = randomBid.intValue();
                    currentBidBText.setText(currentBidB.toString());
                    if(currentBidA > currentBidB) {
                        nextTurn = GameStatus.PLAYER_A_TURN;
                        remainingBidA -= currentBidA;
                        remainingBidB += currentBidA;
                        bidNotReceived = false;
                    } else if (currentBidA < currentBidB) {
                        nextTurn = GameStatus.PLAYER_B_TURN;
                        remainingBidB -= currentBidB;
                        remainingBidA += currentBidB;
                        makeAMove();
                    } else {
                        // TODO: bid again
                    }
                    updateBids();
                    toggleView();
                    handled = true;
                }
                return handled;
            }
        } );
    }

    private void makeAMove() {
        Button[][] buttons = {{b11, b12, b13}, {b21, b22, b23}, {b31, b32, b33}};
        GameStatus[][] table = getCurrentGameStatus();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(table[i][j] == GameStatus.BLANK) {
                    setText(buttons[i][j]);
                    return;
                }
            }
        }
    }

    private void updateBids() {
        remainingBidAText.setText(remainingBidA.toString());
        remainingBidBText.setText(remainingBidB.toString());
    }

    private void toggleView() {

        currentBidA.setEnabled(bidNotReceived);

        b11.setEnabled(!bidNotReceived);
        b12.setEnabled(!bidNotReceived);
        b13.setEnabled(!bidNotReceived);

        b21.setEnabled(!bidNotReceived);
        b22.setEnabled(!bidNotReceived);
        b23.setEnabled(!bidNotReceived);

        b31.setEnabled(!bidNotReceived);
        b32.setEnabled(!bidNotReceived);
        b33.setEnabled(!bidNotReceived);
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
        bidNotReceived = true;
        toggleView();
        GameStatus status = updateGameStatus();
        if(status != GameStatus.BLANK) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    private GameStatus updateGameStatus() {

        GameStatus[][] table = getCurrentGameStatus();

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

    @NonNull
    private GameStatus[][] getCurrentGameStatus() {
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
        return table;
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
