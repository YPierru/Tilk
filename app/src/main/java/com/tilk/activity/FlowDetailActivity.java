package com.tilk.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.tilk.R;
import com.tilk.adapter.FlowDetailAdapter;
import com.tilk.models.FlowDetail;
import com.tilk.models.UserProfil;
import com.tilk.models.WaterLoad;
import com.tilk.utils.Constants;
import com.tilk.utils.HttpPostManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FlowDetailActivity extends AppCompatActivity {

    private WaterLoad waterLoad;
    private ArrayList<FlowDetail> listFlowDetails;
    private UserProfil userProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_detail);

        waterLoad = (WaterLoad)getIntent().getExtras().getSerializable("load");

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Détails - "+waterLoad.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listFlowDetails = new ArrayList<>();

        userProfil = new UserProfil(this);
        try {
            listFlowDetails=new GetData().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ListView lvFlowDetail = (ListView)findViewById(R.id.lv_details);

        FlowDetailAdapter adapter = new FlowDetailAdapter(listFlowDetails,this);
        lvFlowDetail.setAdapter(adapter);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    private class GetData extends AsyncTask<Void,Void,ArrayList<FlowDetail>>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FlowDetailActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.progressdialog_label_connexion));
            progressDialog.show();
        }

        @Override
        protected ArrayList<FlowDetail> doInBackground(Void... voids) {

            ArrayList<FlowDetail> listFlowDetails = new ArrayList<>();

            try {
                String received = HttpPostManager.sendPost("load_id=" + waterLoad.getId()+"&tilk_id="+userProfil.getTilkId(), Constants.URL_GET_DATA);

                //Logger.logI(received);

                //JSONObject jsonObject = new JSONObject(received);
                JSONArray array = new JSONArray(received);

                long st,et;
                int total,avg;

                for(int i=0;i<array.length();i++){
                    st=array.getJSONObject(i).getLong("start_time");
                    et=array.getJSONObject(i).getLong("end_time");
                    total=array.getJSONObject(i).getInt("total_water")/1000;
                    avg=array.getJSONObject(i).getInt("average_flow")*60/1000;

                    listFlowDetails.add(new FlowDetail(st,et,total,avg));

                }

                //Logger.logI(""+listFlowDetails.size());

            }catch(Exception e){
                e.printStackTrace();
            }


            return listFlowDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<FlowDetail> aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
