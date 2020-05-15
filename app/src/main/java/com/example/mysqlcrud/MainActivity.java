package com.example.mysqlcrud;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

import android.os.Bundle;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Defining views
    private EditText txtName;
    private EditText txtDesig;
    private EditText txtSalary;
    private Button btnAdd;
    private Button btnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing views
        txtName = (EditText) findViewById(R.id.txtName);
        txtDesig = (EditText) findViewById(R.id.txtDesig);
        txtSalary = (EditText) findViewById(R.id.txtSalary);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnView = (Button) findViewById(R.id.btnView);
        //Setting listeners to button
        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
    }
    //Adding an employee
    private void addEmployee(){
        final String name = txtName.getText().toString().trim();
        final String desg = txtDesig.getText().toString().trim();
        final String sal = txtSalary.getText().toString().trim();
        class AddEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Adding...","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_EMP_NAME,name);
                params.put(Config.KEY_EMP_DESG,desg);
                params.put(Config.KEY_EMP_SAL,sal);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();
    }
    @Override
    public void onClick(View v) {
        if(v == btnAdd){
            addEmployee();
        }
        if(v == btnView){
            startActivity(new Intent(this, ViewAllEmployee.class));
        }
    }
}