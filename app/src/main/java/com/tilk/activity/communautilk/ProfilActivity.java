package com.tilk.activity.communautilk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tilk.R;
import com.tilk.models.ProfilTilkeur;
import com.tilk.models.UserProfil;

import static android.view.View.GONE;

public class ProfilActivity extends AppCompatActivity {

    private Button btnModify;
    private Button btnValidate;
    private Button btnCancel;

    private TextView tvPseudo;
    private TextView tvDpt;
    private TextView tvNbAdults;
    private TextView tvNbKids;

    private EditText etPseudo;
    private EditText etDpt;
    private EditText etNbAdults;
    private EditText etNbKids;

    private UserProfil userProfil;
    private boolean firstUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        userProfil = new UserProfil(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mon profil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnModify = (Button)findViewById(R.id.btn_profil_modify);
        btnModify.setOnClickListener(onClickModify);
        btnValidate = (Button)findViewById(R.id.btn_profil_validate);
        btnValidate.setOnClickListener(onClickValidate);
        btnCancel = (Button)findViewById(R.id.btn_profil_cancel);
        btnCancel.setOnClickListener(onClickCancel);

        tvPseudo=(TextView)findViewById(R.id.tv_pseudo);
        tvDpt=(TextView)findViewById(R.id.tv_dpt);
        tvNbAdults=(TextView)findViewById(R.id.tv_nb_adults);
        tvNbKids=(TextView)findViewById(R.id.tv_nb_kids);

        etPseudo=(EditText)findViewById(R.id.et_pseudo);
        etDpt=(EditText)findViewById(R.id.et_dpt);
        etNbAdults=(EditText)findViewById(R.id.et_nb_adults);
        etNbKids=(EditText)findViewById(R.id.et_nb_kids);

        if(getIntent().getExtras().getBoolean("edit_mode")){
            editMode();
            btnCancel.setVisibility(View.GONE);
            firstUse=true;
        }else{
            viewMode();
            firstUse=false;
            fillTextView();
        }


    }

    private View.OnClickListener onClickModify = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editMode();
        }
    };

    private View.OnClickListener onClickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewMode();
        }
    };

    private View.OnClickListener onClickValidate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String strPseudo = etPseudo.getText().toString();
            String strDpt = etDpt.getText().toString();
            String strNbAdults= etNbAdults.getText().toString();
            String strNbKids=etNbKids.getText().toString();
            ProfilTilkeur currentProfil;
            boolean isModified=false;


            if (firstUse) {
                currentProfil = new ProfilTilkeur();
            } else {
                currentProfil = userProfil.getProfilTilkeur();
            }

            if (strPseudo.length() > 0) {
                isModified=true;
                currentProfil.setPseudo(strPseudo);
            }
            if (strDpt.length() > 0) {
                isModified=true;
                currentProfil.setDepartement(strDpt);
            }
            if (strNbAdults.length() > 0) {
                isModified=true;
                currentProfil.setNbAdults(Integer.valueOf(strNbAdults));
            }
            if (strNbKids.length() > 0) {
                isModified=true;
                currentProfil.setNbKids(Integer.valueOf(strNbKids));
            }



            if(isModified) {
                userProfil.setProfilTilkeur(currentProfil);
                //new SaveProfil().execute();
                viewMode();
                fillTextView();
            }


        }
    };

    private void fillTextView(){

        ProfilTilkeur currentProfil = userProfil.getProfilTilkeur();
        tvPseudo.setText(currentProfil.getPseudo());
        tvDpt.setText(currentProfil.getDepartement());
        tvNbAdults.setText(""+currentProfil.getNbAdults());
        tvNbKids.setText(""+currentProfil.getNbKids());
    }

    private void editMode(){
        btnModify.setVisibility(GONE);
        tvPseudo.setVisibility(GONE);
        tvDpt.setVisibility(GONE);
        tvNbAdults.setVisibility(GONE);
        tvNbKids.setVisibility(GONE);

        btnValidate.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        etPseudo.setVisibility(View.VISIBLE);
        etDpt.setVisibility(View.VISIBLE);
        etNbAdults.setVisibility(View.VISIBLE);
        etNbKids.setVisibility(View.VISIBLE);
    }

    private void viewMode(){
        btnModify.setVisibility(View.VISIBLE);
        tvPseudo.setVisibility(View.VISIBLE);
        tvDpt.setVisibility(View.VISIBLE);
        tvNbAdults.setVisibility(View.VISIBLE);
        tvNbKids.setVisibility(View.VISIBLE);

        btnValidate.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        etPseudo.setVisibility(View.GONE);
        etDpt.setVisibility(View.GONE);
        etNbAdults.setVisibility(View.GONE);
        etNbKids.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    /*private class SaveProfil extends AsyncTask<Void,Void,Void> {

        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProfilActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Sauvegarde...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            ProfilTilkeur currentProfil = userProfil.getProfilTilkeur();
            try {
                String response = HttpPostManager.sendPost("tilkeur="+currentProfil.getJson(userProfil.getUserId()), Constants.URL_UPDATE_PROFIL);

                Logger.logI(response);
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            progressDialog.dismiss();
        }
    }*/

}
