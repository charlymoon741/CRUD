package com.example.mysqlcrud;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
public class ViewEmployee extends AppCompatActivity implements View.OnClickListener {
    private EditText txtID;
    private EditText txtName;
    private EditText txtDesig;
    private EditText txtSalary;
    private Button btnUpdate;
    private Button btnDelete;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);
        Intent intent = getIntent();
        id = intent.getStringExtra(Config.EMP_ID);
        txtID = (EditText) findViewById(R.id.txtID);
        txtName = (EditText) findViewById(R.id.txtName);
        txtDesig = (EditText) findViewById(R.id.txtDesig);
        txtSalary = (EditText) findViewById(R.id.txtSalary);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        txtID.setText(id);
        getEmployee();
    }
    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewEmployee.this,"Fetching...","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_EMP,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }
    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String name = c.getString(Config.TAG_NAME);
            String desg = c.getString(Config.TAG_DESG);
            String sal = c.getString(Config.TAG_SAL);
            txtName.setText(name);
            txtDesig.setText(desg);
            txtSalary.setText(sal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateEmployee(){
        final String name = txtName.getText().toString().trim();
        final String desg = txtDesig.getText().toString().trim();
        final String salary = txtSalary.getText().toString().trim();
        class UpdateEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewEmployee.this,"Updating...","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewEmployee.this,s,Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID,id);
                hashMap.put(Config.KEY_EMP_NAME,name);
                hashMap.put(Config.KEY_EMP_DESG,desg);
                hashMap.put(Config.KEY_EMP_SAL,salary);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP,hashMap);
                return s;
            }
        }
        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
        startActivity(new Intent(this, MainActivity.class));
    }
    private void deleteEmployee(){
        class DeleteEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewEmployee.this, "Updating...", "Wait...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewEmployee.this, s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_EMP, id);
                return s;
            }
        }
        startActivity(new Intent(this, MainActivity.class));
        DeleteEmployee de = new DeleteEmployee();
        de.execute();

    }
    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this employee?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEmployee();
                        startActivity(new Intent(ViewEmployee.this,MainActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == btnUpdate){
            updateEmployee();
        }
        if(v == btnDelete){
            confirmDeleteEmployee();
        }
    }
}
