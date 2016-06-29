package com.pat.admin.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerRoute;
    private Spinner spinnerLevel;

    private Button bookTicketBtn;
    private static final String REGISTER_URL = "http://192.168.43.134/BusTicket/booking.php";
    private Spinner routeDropdown;
    private Spinner levelDropdown;
    private TextView computedPrice;
    private int ticketCost;
    private int levelTicketCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        computedPrice = (TextView) findViewById(R.id.computePrice);
        routeDropdown = (Spinner) findViewById(R.id.routeSpinner);
        levelDropdown = (Spinner) findViewById(R.id.levelSpinner);

        String[] routeItems = new String[]{"Kampala to Kireka", "Ntinda to Kampala", "Kampala to Banda",
                "Ntinda to Byeyogerere", "Nakawa to Kampala", "Kampala to Gayaza", "Zana to Kampala"};
        String[] levelItems = new String[]{"Single", "Double"};
        String[] paymentItems = new String[]{"Mobile Money", "PayPal", "Credit Card"};

        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, routeItems);
        routeDropdown.setAdapter(routeAdapter);

        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, levelItems);
        levelDropdown.setAdapter(levelAdapter);

        spinnerRoute = (Spinner) findViewById(R.id.routeSpinner);
        spinnerLevel = (Spinner) findViewById(R.id.levelSpinner);

        bookTicketBtn = (Button) findViewById(R.id.bookBtn);

        bookTicketBtn.setOnClickListener(this);
        spinnerRoute.setSelection(0,false);
        spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                switch (pos) {
                    case 0:
                        ticketCost = 2000;
                        break;
                    case 1:
                        ticketCost = 1500;
                        break;
                    case 2:
                        ticketCost = 2000;
                        break;
                    case 3:
                        ticketCost = 2500;
                        break;
                    case 4:
                        ticketCost = 1000;
                        break;
                    case 5:
                        ticketCost = 3000;
                        break;
                    case 6:
                        ticketCost = 1000;
                        break;
                }
                String cost = String.valueOf(ticketCost);
                computedPrice.setText(cost);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerLevel.setSelection(0,false);
        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                switch (pos) {
                    case 0:
                        levelTicketCost = ticketCost;
                        break;
                    case 1:
                        levelTicketCost = ticketCost * 2;
                        break;
                }
                String cost = String.valueOf(levelTicketCost);
                computedPrice.setText(cost);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == bookTicketBtn) {
            book();
        }
    }

    private void book() {
        String route = spinnerRoute.getSelectedItem().toString().trim().toLowerCase();
        String level = spinnerLevel.getSelectedItem().toString().trim().toLowerCase();

//        String route = routeDropdown.getSelectedItem().toString().trim().toLowerCase();
//        String level = levelDropdown.getSelectedItem().toString().trim().toLowerCase();
//        String payment = paymentDropdown.getSelectedItem().toString().trim().toLowerCase();

        bookTicket(route, level, MainActivity.UserName, MainActivity.UserEmail, MainActivity.id);
    }

    private void bookTicket(String route, String level, String name, String email, String id) {
        class LoginUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            ServerConnect ruc = new ServerConnect();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BookingActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();

                data.put("route", params[0]);
                data.put("level", params[1]);
                data.put("UserName", params[2]);
                data.put("UserEmail", params[3]);
                data.put("id", params[4]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        LoginUser ru = new LoginUser();
        ru.execute(route, level, name, email, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (MainActivity.isLoggedIn) {
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
            if (!MainActivity.isLoggedIn) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), BookingActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.search) {

            if (!MainActivity.isLoggedIn) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), SearchingActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.signOut) {
            MainActivity.isLoggedIn = false;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
