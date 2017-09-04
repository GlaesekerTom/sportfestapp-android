package de.glaeseker_tom.sportfestapp.fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.glaeseker_tom.sportfestapp.R;
import de.glaeseker_tom.sportfestapp.models.TeamModel;

public class ManageTournamentFragment extends Fragment implements View.OnClickListener {

    private String serverUrl;
    private Button btnEndTime;
    private TextView tvStartTime, tvEndTime, tvMatchTime;
    private CheckBox cb;
    private ListAdapterTournament adapter;
    private String startTime, endTime, matchTime;

    public ManageTournamentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_tournament, container, false);
        if(getArguments() != null){
            serverUrl = getArguments().getString("serverUrl");
        }
        Button btnStartTime = v.findViewById(R.id.btn_start_time);
        btnEndTime = v.findViewById(R.id.btn_end_time);
        tvStartTime = v.findViewById(R.id.tv_start_time);
        tvEndTime = v.findViewById(R.id.tv_end_time);
        tvMatchTime = v.findViewById(R.id.tv_match_time);
        Button btnCreateTournament = v.findViewById(R.id.btn_create_tournament);
        FloatingActionButton fab = v.findViewById(R.id.floating_button);
        cb = v.findViewById(R.id.cb_no_end_time);
        btnCreateTournament.setOnClickListener(this);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()){
                    btnEndTime.setVisibility(View.GONE);
                    tvEndTime.setVisibility(View.GONE);
                    tvMatchTime.setVisibility(View.VISIBLE);
                    tvEndTime.setText("");
                    endTime = "";
                }else{
                    btnEndTime.setVisibility(View.VISIBLE);
                    tvEndTime.setVisibility(View.VISIBLE);
                    tvMatchTime.setVisibility(View.GONE);
                    tvMatchTime.setText("");
                }
            }
        });
        btnCreateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(startTime!=null && !startTime.isEmpty() && adapter.teamModelList.size()>1) {
                        matchTime = tvMatchTime.getText().toString();
                        if(endTime == null || endTime.isEmpty()) {
                            endTime = "ignore";
                        }
                        if(matchTime == null || matchTime.isEmpty()){
                            matchTime= "ignore";
                        }
                        new SendTournamentData().execute();
                    }else{
                        Toast.makeText(getContext(), "FEHLER: Bitte alle Daten ausfüllen", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        fab.setOnClickListener(this);
        ListView lvMatches = v.findViewById(R.id.team_list_view);
        ArrayList<TeamModel> tm = new ArrayList<>();
        adapter = new ListAdapterTournament(getActivity(), R.layout.team_list_item,tm);
        lvMatches.setAdapter(adapter);

        return v;
    }

    public void onClick(View v){
        System.out.println("ID: "+v.getId());
        switch (v.getId()){
            case R.id.btn_start_time:{
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        tvStartTime.setText("Die Startzeit wurde auf "+String.valueOf(hour)+":"+String.valueOf(minute)+" Uhr gesetzt.");
                        startTime = hour+":"+minute;
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
                break;
            }
            case R.id.btn_end_time:{
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        tvEndTime.setText("Die Endzeit wurde auf "+String.valueOf(hour)+":"+String.valueOf(minute)+" Uhr gesetzt.");
                        endTime = hour+":"+minute;
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
                break;
            }
            case R.id.floating_button:{
                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                builder.setTitle("Klasse/Team hinzufügen");
                EditText tn = new EditText(getContext());
                EditText tn2 = new EditText(getContext());
                tn.setHint("Klasse");
                tn2.setHint("Lehrer Kürzel");
                final LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(tn);
                ll.addView(tn2);

                builder.setView(ll);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        EditText et1 = (EditText) ll.getChildAt(0);
                        EditText et2 = (EditText) ll.getChildAt(1);
                        String a = et1.getText().toString();
                        String b = et2.getText().toString();
                        TeamModel tm = new TeamModel(a,b);
                        adapter.teamModelList.add(tm);
                        adapter.notifyDataSetChanged();
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

        }
    }

    public class SendTournamentData extends AsyncTask<String, String, String> {

        URL url;

        @Override
        protected void onPreExecute() {
            try {
                url = new URL(serverUrl + "setupTournament.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                List<TeamModel> teams = adapter.teamModelList;
                JSONObject jsof = new JSONObject();
                JSONObject jso = new JSONObject();
                JSONArray jsa = new JSONArray();
                for (int i = 0; i < teams.size(); i++) {
                    jso.put("teamName",teams.get(i).getTeamName());
                    jso.put("teamManager",teams.get(i).getTeamManager());
                    jsa.put(jso);
                }
                jsof.put("teams",jsa);
                System.out.println(jsof.toString());
                String json_string;
                String textparam = "startTime="+startTime+"&endTime="+endTime+"&matchTime="+matchTime+"&teams="+jsof.toString();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(2000);
                httpURLConnection.getOutputStream().write(textparam.getBytes("UTF-8"));

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((json_string = br.readLine()) != null) {
                    stringBuilder.append(json_string);
                }
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String finalJson = stringBuilder.toString().trim();
                System.out.println("finalJson:" + finalJson);

               /* InputStream is = getResources().openRawResource(R.raw.teams);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while(line != null)
                {
                    sb.append(line);
                    line = br.readLine();
                }
                String finalJson = sb.toString();*/
                JSONObject parentObject = new JSONObject(finalJson);

                if(parentObject.names().get(0).equals("success")){
                    return parentObject.get("success").toString();
                }
                else if (parentObject.names().get(0).equals("error")) {
                return parentObject.get("error").toString();
                }
                return null;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                Toast.makeText(getActivity(), "FEHLER: Turnier wurde nicht erstellt.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    }

class ListAdapterTournament extends ArrayAdapter {

        public List<TeamModel> teamModelList;
        private int resource;
        private LayoutInflater inflater;
        public ListAdapterTournament(Context context, int resource, ArrayList<TeamModel> objects){
            super(context,resource,objects);
            teamModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        @NonNull
        public View getView(final int position, View convertView, @NonNull ViewGroup parent){
            if(convertView == null){
                convertView = inflater.inflate(resource, null);
            }
            TextView tvTeamName = convertView.findViewById(R.id.mt_tv_team_name);
            TextView tvTeamManager = convertView.findViewById(R.id.mt_tv_team_manager);

            tvTeamName.setText(teamModelList.get(position).getTeamName());
            tvTeamManager.setText(teamModelList.get(position).getTeamManager());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                    builder.setTitle("Team entfernen");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            teamModelList.remove(position);
                            notifyDataSetChanged();
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

            return convertView;
        }
}