package com.app.raceanalyzer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

    private EditText mUsernameField;
    private EditText mPasswordField;
    private TextView mErrorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.raceanalyzer.R.layout.activity_login);
        UI();
    }

    private void UI(){

        mUsernameField = (EditText) findViewById(com.app.raceanalyzer.R.id.login_username);
        mPasswordField = (EditText) findViewById(com.app.raceanalyzer.R.id.login_password);
        mErrorField = (TextView) findViewById(com.app.raceanalyzer.R.id.error_messages);

    }

    //click signIn for execute asyncTask login (in background)
    public void signIn(final View v){
        new loginTask().execute();

    }


    //click register button for go to intent register page
     public void toRegistration(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //insert menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.app.raceanalyzer.R.menu.login, menu);
        return true;
    }

    private class loginTask extends AsyncTask<Void, Integer, Void> {

        ProgressDialog pd;

        protected void onPreExecute() {

            pd = new ProgressDialog(LoginActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setTitle("Login");
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setProgress(0);
//            pd.show();

        }

        protected Void doInBackground(Void... params) {
            ParseUser.logInInBackground(mUsernameField.getText().toString(), mPasswordField.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {


                    if (user != null) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Signup failed. Look at the ParseException to see what happened.
                        switch(e.getCode()){
                            case ParseException.USERNAME_TAKEN:
                                mErrorField.setText("Sorry, this username has already been taken.");
                                break;
                            case ParseException.USERNAME_MISSING:
                                mErrorField.setText("Sorry, you must supply a username to register.");
                                break;
                            case ParseException.PASSWORD_MISSING:
                                mErrorField.setText("Sorry, you must supply a password to register.");
                                break;
                            case ParseException.OBJECT_NOT_FOUND:
                                mErrorField.setText("Sorry, those credentials were invalid.");
                                break;
                            default:
                                mErrorField.setText(e.getLocalizedMessage());
                              //  Log.e("Login Activity" , String.valueOf(e));
                                break;
                        }
                    }


                }
            });

            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            pd.setProgress(values[0]);
        }

        protected void onPostExecute(Void result) {


        }
    }




}
