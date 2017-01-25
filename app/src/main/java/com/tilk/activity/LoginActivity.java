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
import com.tilk.models.FriendTilkeur;
import com.tilk.models.ProfilTilkeur;
import com.tilk.models.UserProfil;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;
import com.tilk.utils.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmailText;
    private EditText etPasswordText;
    private Button btnLogin;

    private UserProfil userProfil;

    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Logger.enableLog();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        userProfil = new UserProfil(LoginActivity.this);

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

        email = etEmailText.getText().toString();

        Bundle bundle = new Bundle();
        try {
            bundle = new LoginCheck().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(bundle.getInt("returnCode")==1){
            onLoginSuccess(bundle);
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

    public void onLoginSuccess(Bundle bundle) {
        userProfil.setUserOnline();
        userProfil.setUserEmail(email);
        userProfil.setUserId(bundle.getInt("id_user"));
        userProfil.setTilkId(bundle.getInt("id_tilk"));
        userProfil.setUserSurname(bundle.getString("surname"));

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


    private class LoginCheck extends AsyncTask<Void,Void,Bundle> {

        private ProgressDialog progressDialog;
        private String password;
        private Bundle bundle;

        public LoginCheck() {
            this.password = etPasswordText.getText().toString();
            bundle = new Bundle();
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
        protected Bundle doInBackground(Void... args) {
            try {

                String response = HttpPostManager.sendPost("email=" + email + "&password=" + password, Constants.URL_LOGIN);

                JSONObject jsonObject = new JSONObject(response);

                bundle.putInt("returnCode", jsonObject.getInt("code"));

                if(jsonObject.getInt("code")==1) {
                    bundle.putInt("id_tilk", jsonObject.getInt("id_tilk"));
                    bundle.putInt("id_user", jsonObject.getInt("id_user"));
                    bundle.putString("surname", jsonObject.getString("surname"));
                    bundle.putInt("ct_status", jsonObject.getInt("ct_status"));


                    if(jsonObject.getInt("ct_status")==1) {
                        response = HttpPostManager.sendPost("id_user=" + jsonObject.getInt("id_user"), Constants.URL_GET_FRIENDS);

                        JSONObject jsonObject2 = new JSONObject(response);
                        JSONArray array = jsonObject2.getJSONArray("friends");
                        ArrayList<FriendTilkeur> listFriends = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            listFriends.add(new FriendTilkeur(array.getJSONObject(i).getInt("friend_id"), array.getJSONObject(i).getString("friend_pseudo"), array.getJSONObject(i).getInt("friend_conso")));
                        }


                        ProfilTilkeur profilTilkeur = userProfil.getProfilTilkeur();
                        profilTilkeur.setListFriends(listFriends);
                        profilTilkeur.setPseudo(jsonObject.getString("ct_pseudo"));
                        profilTilkeur.setDepartement(jsonObject.getString("ct_dpt"));
                        profilTilkeur.setNbAdults(jsonObject.getInt("ct_nbAdults"));
                        profilTilkeur.setNbKids(jsonObject.getInt("ct_nbKids"));
                        userProfil.setProfilTilkeur(profilTilkeur);


                        userProfil = new UserProfil(LoginActivity.this);

                        Logger.logI(""+userProfil.getProfilTilkeur().getListFriends().size());

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            super.onPostExecute(bundle);
            progressDialog.dismiss();
        }
    }
}
