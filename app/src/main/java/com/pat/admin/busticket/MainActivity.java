package com.pat.admin.busticket;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static boolean isLoggedIn;
    public static String UserEmail;
    public static String UserName;
    public static String id;

    private Button homeSignInBtn;
    private Button homeSignUpBtn;
    private Button homeBookTicketBtn;
    private Button homeSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isLoggedIn){
            isLoggedIn = false;
        }
        setContentView(R.layout.activity_main);
        homeSignInBtn = (Button) findViewById(R.id.homeSignIn);
        homeSignUpBtn = (Button) findViewById(R.id.homeSignUp);
        homeBookTicketBtn = (Button) findViewById(R.id.homeBooking);
        homeSearchBtn = (Button) findViewById(R.id.homeSearch);

        homeSearchBtn.setOnClickListener(this);
        homeSignInBtn.setOnClickListener(this);
        homeSignUpBtn.setOnClickListener(this);
        homeBookTicketBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == homeSignInBtn) {
            if(isLoggedIn){
                Toast.makeText(getApplicationContext(), "Please logout current user", Toast.LENGTH_LONG).show();
            }else {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        }
        if (v == homeSignUpBtn) {
            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);
        }
        if (v == homeBookTicketBtn) {
            if(isLoggedIn){
                Intent i = new Intent(getApplicationContext(), BookingActivity.class);
                startActivity(i);
            }else{
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }

        }
        if (v == homeSearchBtn) {
            if(isLoggedIn){
                Intent i = new Intent(getApplicationContext(), SearchingActivity.class);
                startActivity(i);
            }else{
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (isLoggedIn) {
            MenuItem signInItem = menu.findItem(R.id.signIn);
            MenuItem signUpItem = menu.findItem(R.id.signUp);

            signInItem.setVisible(false);
            signUpItem.setVisible(false);
            this.invalidateOptionsMenu();
        } else {
            MenuItem signOutItem = menu.findItem(R.id.signOut);
            signOutItem.setVisible(false);
            this.invalidateOptionsMenu();
        }
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
        } else if (id == R.id.signIn) {
            Intent i = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(i);
        } else if (id == R.id.signUp) {
            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);
        } else if (id == R.id.book) {
            if (!isLoggedIn) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), BookingActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.search) {

            if (!isLoggedIn) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), SearchingActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.signOut) {
            isLoggedIn = false;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
