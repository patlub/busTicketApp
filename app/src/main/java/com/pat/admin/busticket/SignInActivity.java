package com.pat.admin.busticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button signInBtn;
    private static final String REGISTER_URL = "http://192.168.43.134/BusTicket/signin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = (EditText) findViewById(R.id.signInEmail);
        editTextPassword = (EditText) findViewById(R.id.signInPassword);

        signInBtn = (Button) findViewById(R.id.signInBtn);

        signInBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signInBtn) {
            signInUser();
        }
    }

    private void signInUser() {
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        if(isValidEmail(email)) {
            login(email, password);
        }else{
            Toast.makeText(getApplicationContext(), "Enter Valid Email", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void login(String email, String password) {
        class LoginUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            ServerConnect ruc = new ServerConnect();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignInActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                String status = s.substring(0, 1);
                if (status.equals("1")) {
                    String[] creds = s.substring(1).split(" ");
                    MainActivity.UserEmail = creds[0];
                    MainActivity.UserName = creds[1];
                    MainActivity.id = creds[2];
                    MainActivity.phone = creds[3];
                    String msg = "Welcome " + MainActivity.UserName;
                    MainActivity.isLoggedIn = true;
                    Intent i = new Intent(getApplicationContext(), BookingActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();

                data.put("email", params[0]);
                data.put("password", params[1]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        LoginUser ru = new LoginUser();
        ru.execute(email, password);
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