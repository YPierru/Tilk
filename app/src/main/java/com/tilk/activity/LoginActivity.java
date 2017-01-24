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
import com.tilk.models.ProfilTilkeur;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;
import com.tilk.utils.SharedPreferencesManager;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmailText;
    private EditText etPasswordText;
    private Button btnLogin;

    private SharedPreferencesManager sessionManager;

    private String email;
    private int returnCode;
    private int id_tilk;
    private int id_user;
    private String surname;
    private int ctStatus;
    private String ctDpt;
    private String ctPseudo;
    private int ctNbAdults;
    private int ctNbKids;

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
            onLoginFailed(getString(R.string.login_bad_format),null);
            return;
        }

        email = etEmailText.getText().toString();

        new LoginCheck().execute();
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

    public void onLoginSuccess(ProgressDialog progressDialog) {
        sessionManager.setUserOnline();
        sessionManager.setUserEmail(email);
        sessionManager.setUserId(id_user);
        sessionManager.setTilkId(id_tilk);
        sessionManager.setUserSurname(surname);
        if(ctStatus==1) {
            sessionManager.setProfilTilkeur(new ProfilTilkeur(ctPseudo,ctDpt,ctNbAdults,ctNbKids));
        }

        progressDialog.dismiss();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    public void onLoginFailed(String toPrint,ProgressDialog progressDialog) {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
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




    private class LoginCheck extends AsyncTask<Void,Void,Void> {

        private ProgressDialog progressDialog;
        private String password;

        public LoginCheck() {
            this.password = etPasswordText.getText().toString();
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
        protected Void doInBackground(Void... args) {
            try {

                String response = HttpPostManager.sendPost("email=" + email + "&password=" + password, Constants.URL_LOGIN);

                JSONObject jsonObject = new JSONObject(response);
                returnCode = jsonObject.getInt("code");

                if (returnCode == 1) {
                    id_tilk = jsonObject.getInt("id_tilk");
                    id_user = jsonObject.getInt("id_user");
                    surname = jsonObject.getString("surname");
                    ctStatus = jsonObject.getInt("ct_status");
                    ctPseudo = jsonObject.getString("ct_pseudo");
                    ctDpt = jsonObject.getString("ct_dpt");
                    ctNbAdults = jsonObject.getInt("ct_nbAdults");
                    ctNbKids = jsonObject.getInt("ct_nbKids");

                    onLoginSuccess(progressDialog);
                } else {
                    onLoginFailed(getString(R.string.login_failed), progressDialog);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
