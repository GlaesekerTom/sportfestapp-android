package de.glaeseker_tom.sportfestapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.glaeseker_tom.sportfestapp.models.TeamModel;

/*
 * Die LoginActivity steuert die Login-Maske und erstellt ein Objekt des AccountHandlers,
 * welcher die Kommunikation mit dem Server für den Loginprozess aufnimmt.
 */
public class LoginActivity extends AppCompatActivity {

    private String urlString;
    private EditText etUsername, etPassword;
    private CheckBox checkBox;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Context context = getApplicationContext();
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        checkBox = (CheckBox) findViewById(R.id.cb_remember_me);
        ImageView serverSettings = (ImageView) findViewById(R.id.iv_server_settings);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        TextView tvRegister = (TextView) findViewById(R.id.tv_register);

        sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        urlString = sharedPreferences.getString("serverUrl","");
        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        if(!password.isEmpty()){
            checkBox.setChecked(true);
        }

        etUsername.setText(username);
        etPassword.setText(password);
        prefEditor = sharedPreferences.edit();
        prefEditor.apply();
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

        //AlertDialog zum setzen der Serverurl. Muss beim ersten Start getan werden.
        // Die Serveradresse kann jedes mal geändert werden, ansonsten wird die alte verzeichnete verwendet.
        serverSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Serveradresse ändern:");
                EditText tn = new EditText(LoginActivity.this);
                tn.setHint("Neue Serveradresse");
                final LinearLayout ll = new LinearLayout(LoginActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(tn);
                builder.setView(ll);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText et1 = (EditText) ll.getChildAt(0);
                        String serverUrl = et1.getText().toString();
                        //Url muss mit http:// beginnen. Wenn der Benutzer, dies nicht getan hat wird es nachträglich ergänzt.
                        if(!serverUrl.isEmpty()){
                            if(serverUrl.charAt(0) != 'h'){
                                serverUrl = "http://"+serverUrl;
                            }
                        }
                        urlString = serverUrl;
                        //Url wird abgespeichert, sodass nach Neustart der App die Url einfach geladen werden kann.
                        prefEditor.putString("serverUrl",serverUrl);
                        prefEditor.commit();
                        dialogInterface.dismiss();

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }
    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        //Überprüft, ob Nutzer alle Eingabefelder gefüllt hat.
        if(!username.isEmpty() || !password.isEmpty()) {
            //Wenn die "Angemeldet bleiben"-Checkbox ausgewählt ist, werden Benutzername und Passwort abgespeichert
            // wird sich angemeldet ohne dabei die Checkbox ausgewählt zu haben, werden die Daten gelöscht.
            if(checkBox.isChecked()){
                prefEditor.putString("username",username);
                prefEditor.putString("password",password);
            }else{
                prefEditor.remove("username");
                prefEditor.remove("password");
            }
            prefEditor.commit();
            //erstellt ein neues Objekt vom AccountHandler und führt den AsyncTask aus.
            AccountHandler accountHandler = new AccountHandler(this);
            accountHandler.execute(urlString, "login", username, password);
        }else{
            Toast.makeText(getApplicationContext(), "FEHLER: Bitte fülle alle Felder", Toast.LENGTH_SHORT).show();
        }
    }
}
