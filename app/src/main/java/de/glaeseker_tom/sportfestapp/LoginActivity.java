package de.glaeseker_tom.sportfestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Die LoginActivity steuert die Login-Maske und erstellt ein Objekt des AccountHandlers,
 * welcher die Kommunikation mit dem Server aufnimmt.
 */
public class LoginActivity extends AppCompatActivity {

    private String urlString = "http://192.168.20.30:80/sportfest/";
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        TextView tvRegister = (TextView) findViewById(R.id.tv_register);

        //Für Testzwecke Todo Entfernen
        etUsername.setText("Baum2");
        etPassword.setText("123");

        //Setzt OnClickListener für die TextView in der LoginMaske
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        //Login Button OnClickListener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        //Überprüft, ob Nutzer alle Eingabefelder gefüllt hat.
        if(!username.isEmpty() || !password.isEmpty()) {
            //erstellt ein neues Objekt vom AccountHandler und führt den AsyncTask aus.
            AccountHandler accountHandler = new AccountHandler(this);
            accountHandler.execute(urlString, "login", username, password);
        }else{
            Toast.makeText(getApplicationContext(), "FEHLER: Bitte fülle alle Felder", Toast.LENGTH_SHORT).show();
        }
    }
}
