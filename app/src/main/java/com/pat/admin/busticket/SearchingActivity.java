package com.pat.admin.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SearchingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextSearch;

    private Button searchBtn;
    private static final String REGISTER_URL = "http://192.168.43.134/BusTicket/search.php";

    String myJSON;
    JSONArray jsonArray = null;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUMBER = "number";
    private static final String TAG_NAME = "name";
    private static final String TAG_ROUTE = "route";
    private static final String TAG_LEVEL = "level";
    private static final String TAG_DATE = "date";
    private static final String TAG_USER_ID = "user_id";

    private TextView ticketNameView;
    private TextView ticketNameLabel;
    private TextView ticketRouteLabel;
    private TextView ticketRouteView;
    private TextView ticketLevelLabel;
    private TextView ticketdateLabel;
    private TextView ticketLevelView;
    private TextView ticketdateView;
    private TextView ticketNumberView;
    private TextView ticketNumberLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        ticketNumberView = (TextView) findViewById(R.id.ticketNumber);
        ticketNumberLabel = (TextView) findViewById(R.id.ticketNumberLabel);
        ticketNameView = (TextView) findViewById(R.id.ticketName);
        ticketNameLabel = (TextView) findViewById(R.id.ticketNameLabel);
        ticketRouteView = (TextView) findViewById(R.id.ticketRoute);
        ticketRouteLabel = (TextView) findViewById(R.id.ticketRouteLabel);
        ticketLevelView = (TextView) findViewById(R.id.ticketLevel);
        ticketLevelLabel = (TextView) findViewById(R.id.ticketLevelLabel);
        ticketdateView = (TextView) findViewById(R.id.ticketDate);
        ticketdateLabel = (TextView) findViewById(R.id.ticketDateLabel);

        editTextSearch = (EditText) findViewById(R.id.ticketSearchNumber);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn)
            searchTicket();
    }

    private void searchTicket() {
        String ticketNumber = editTextSearch.getText().toString().trim().toLowerCase();
        search(ticketNumber);
    }

    private void search(String ticketNumber) {
        class SearchTicket extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            ServerConnect ruc = new ServerConnect();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchingActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("0")) {
                    hideList();
                    Toast.makeText(getApplicationContext(), "No Ticket Found", Toast.LENGTH_LONG).show();
                } else {
                    myJSON = s;
                    showList();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("ticketNumber", params[0]);
                String result = ruc.sendPostRequest(REGISTER_URL, data);
                return result;
            }
        }
        SearchTicket ru = new SearchTicket();
        ru.execute(ticketNumber);
    }

    protected void showList() {

        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            JSONObject c = jsonArray.getJSONObject(0);
            String number = c.getString(TAG_NUMBER);
            String name = c.getString(TAG_NAME);
            String route = c.getString(TAG_ROUTE);
            String level = c.getString(TAG_LEVEL);
            String date = c.getString(TAG_DATE);
            String user_id = c.getString(TAG_USER_ID);

            ticketNumberView.setText(number);
            ticketNameView.setText(name);
            ticketRouteView.setText(route);
            ticketLevelView.setText(level);
            ticketdateView.setText(date);

            ticketNumberLabel.setVisibility(View.VISIBLE);
            ticketNameLabel.setVisibility(View.VISIBLE);
            ticketRouteLabel.setVisibility(View.VISIBLE);
            ticketLevelLabel.setVisibility(View.VISIBLE);
            ticketdateLabel.setVisibility(View.VISIBLE);

            ticketNumberView.setVisibility(View.VISIBLE);
            ticketNameView.setVisibility(View.VISIBLE);
            ticketRouteView.setVisibility(View.VISIBLE);
            ticketLevelView.setVisibility(View.VISIBLE);
            ticketdateView.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void hideList() {
        ticketNumberLabel.setVisibility(View.GONE);
        ticketNameLabel.setVisibility(View.GONE);
        ticketRouteLabel.setVisibility(View.GONE);
        ticketLevelLabel.setVisibility(View.GONE);
        ticketdateLabel.setVisibility(View.GONE);

        ticketNumberView.setVisibility(View.GONE);
        ticketNameView.setVisibility(View.GONE);
        ticketRouteView.setVisibility(View.GONE);
        ticketLevelView.setVisibility(View.GONE);
        ticketdateView.setVisibility(View.GONE);

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
