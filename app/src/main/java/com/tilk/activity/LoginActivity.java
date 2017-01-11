package com.tilk.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tilk.R;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;
import com.tilk.utils.SharedPreferencesManager;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmailText;
    private EditText etPasswordText;
    private Button btnLogin;

    private SharedPreferencesManager sessionManager;

    private DateFormat dateFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Logger.enableLog();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        sessionManager = new SharedPreferencesManager(LoginActivity.this);

        etEmailText = (EditText)findViewById(R.id.input_email);

        etEmailText.setText("yan.pierru@gmail.com");
        etPasswordText =(EditText)findViewById(R.id.input_password);
        etPasswordText.setText("pouley");
        btnLogin =(Button)findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {

        if (!validate()) {
            onLoginFailed(getString(R.string.login_bad_format));
            return;
        }

        String email = etEmailText.getText().toString();
        String password = etPasswordText.getText().toString();

        LoginCheck loginCheck = new LoginCheck(email,password);
        ArrayList<Integer> listData = new ArrayList<>();
        try {
            listData = loginCheck.execute().get();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(listData.get(0)==1){
            onLoginSuccess(listData.get(1),listData.get(2));
        }else{
            onLoginFailed(getString(R.string.login_failed));
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    public void onLoginSuccess(int id_user,int tilk_id) {
        // today
        Calendar calendar = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        sessionManager.setUserOnline();
        sessionManager.setUserId(id_user);
        sessionManager.setTilkId(tilk_id);
        sessionManager.setReferenceDate(calendar.getTime());

        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    public void onLoginFailed(String toPrint) {
        Toast.makeText(getBaseContext(), toPrint, Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String email = etEmailText.getText().toString();
        String password = etPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailText.setError(getString(R.string.login_bad_format));
            valid = false;
        } else {
            etEmailText.setError(null);
        }

        if (password.isEmpty()) {
            etPasswordText.setError(getString(R.string.login_empty_pwd));
            valid = false;
        } else {
            etPasswordText.setError(null);
        }

        return valid;
    }




    private class LoginCheck extends AsyncTask<Void,Void,ArrayList<Integer>> {

        private ProgressDialog progressDialog;
        private String email;
        private String password;

        public LoginCheck(String email, String password){
            this.email=email;
            this.password=password;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.progressdialog_label_connexion));
            progressDialog.show();
        }

        @Override
        protected ArrayList<Integer> doInBackground(Void... args) {
            ArrayList<Integer> listData = new ArrayList<>();
            try {

                String response = HttpPostManager.sendPost("email="+email+"&password="+password,Constants.URL_LOGIN);

                JSONObject jsonObject = new JSONObject(response);
                listData.add(jsonObject.getInt("code"));
                listData.add(jsonObject.getInt("tilk_id"));
                listData.add(jsonObject.getInt("id"));


            }catch(Exception e){
                e.printStackTrace();
            }

            return listData;
        }

        @Override
        protected void onPostExecute(ArrayList<Integer> result) {
            progressDialog.dismiss();
        }
    }
}
