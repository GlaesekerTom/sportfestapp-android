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
*Die RegisterActivity steuert die Register-Maske und erstellt ein Objekt des AccountHandlers,
* welcher die Kommunikation mit dem Server aufnimmt.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText et_username, et_password, et_password_repeat;
    private String urlString = "http://192.168.20.30:80/sportfest/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_repeat = (EditText) findViewById(R.id.et_password_repeat);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        TextView tv_login = (TextView) findViewById(R.id.tv_login);

        //Setzt OnClickListener auf TextView "Anmelden" in der Register-Maske.
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        //Setzt OnClickListener auf "Registrieren-Button".
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
}

    private void register() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        String password_repeat = et_password_repeat.getText().toString();
        // Checkt, ob Eingabefelder gefüllt sind und Passwort und Passwort-Wiederholung übereinstimmen
        if(!(username.isEmpty() || password.isEmpty() || password_repeat.isEmpty())) {
            if(password.equals(password_repeat)) {
                //Neues Objekt des AccountHandlers und AsyncTask wird aufgerufen.
                AccountHandler accountHandler = new AccountHandler(this);
                accountHandler.execute(urlString, "register", username, password);
            }else {
                Toast.makeText(getApplicationContext(), "FEHLER: Passwörter stimmen nicht überein!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "FEHLER: Bitte fülle alle Felder!", Toast.LENGTH_SHORT).show();
        }
    }
}
