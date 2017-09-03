package de.glaeseker_tom.sportfestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username, et_password;
    private Button btn_login;
    private TextView tv_register;
    private String urlString = "http://192.168.20.30:80/sportfest/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);

        //Für Testzwecke Todo Entfernen
        et_username.setText("Baum2");
        et_password.setText("123");

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        if(!username.isEmpty() || !password.isEmpty()) {
            AccountHandler accountHandler = new AccountHandler(this);
            accountHandler.execute(urlString, "login", username, password);
        }else{
            Toast.makeText(getApplicationContext(), "FEHLER: Bitte fülle alle Felder", Toast.LENGTH_SHORT).show();
        }
    }
}
