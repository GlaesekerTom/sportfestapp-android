package de.glaeseker_tom.sportfestapp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.glaeseker_tom.sportfestapp.R;
import de.glaeseker_tom.sportfestapp.models.MatchModel;

public class MatchListFragment extends Fragment {

    private String serverUrl;
    private String tableType;
    private ListAdapter adapter;
    private ArrayList<MatchModel> resultlist;
    private OnListFragmentInteractionListener listener;

    public MatchListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_list, container, false);
        //Bei der Erstellung des Fragments werden Argumente übergeben (siehe MainActivity).
        if(getArguments()!=null){
            serverUrl = getArguments().getString("serverUrl");
            tableType = getArguments().getString("tableType");
        }
        ListView lvMatches = v.findViewById(R.id.list_view);
        resultlist = new ArrayList<>();
        adapter = new ListAdapter(getActivity(), R.layout.match_list_item, resultlist,listener);
        lvMatches.setAdapter(adapter);
        new GetJsonData().execute();

        return v;
    }
    private class GetJsonData extends AsyncTask<String, String, ArrayList<MatchModel>> {

        @Override
        protected ArrayList<MatchModel> doInBackground(String... params) {
            try {
                URL url = new URL(serverUrl + "get_json_data.php");
                String json_string;
                String textparam = "sportname="+tableType+"_fixtures";
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(2000);
                httpURLConnection.getOutputStream().write(textparam.getBytes("UTF-8"));

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((json_string = br.readLine())!=null){
                    stringBuilder.append(json_string);
                }
                br.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String finalJson = stringBuilder.toString().trim();
                System.out.println("finalJson:"+finalJson);

               /* Für Testzwecke Todo Entfernen des Tests
                InputStream is = getResources().openRawResource(R.raw.example);
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
                if (parentObject.names().get(0).equals("error")) {
                    System.out.println("------------- JSON ERROR: " + parentObject.get("error").toString());
                    return null;
                }
                for (int i = 0; i < resultlist.size(); i++) {
                    resultlist.remove(i);
                    //adapter.notifyDataSetChanged();
                }
                System.out.println("RESULTLIST:"+resultlist.toString());

                JSONArray parentArray = parentObject.getJSONArray(tableType);

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    System.out.println(finalObject.toString());
                    MatchModel mm = new MatchModel(finalObject.getString("matchId"), finalObject.getString("time"),
                            finalObject.getString("team1"), finalObject.getString("team2"), finalObject.getString("result"),
                            tableType, finalObject.getString("referee"), finalObject.getString("gym"),finalObject.getString("groupChar"));
                    resultlist.add(mm);
                }
                if(resultlist.size() == 0){
                    return null;
                }
                return resultlist;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MatchModel> resultlist) {
            super.onPostExecute(resultlist);
            
            if(resultlist == null){
                Toast.makeText(getActivity(), "FEHLER: Keine Daten gefunden!", Toast.LENGTH_SHORT).show();
                return;
            }
            adapter.notifyDataSetChanged();
        }
    }
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String[] item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
}

    class ListAdapter extends ArrayAdapter{

        private List<MatchModel> matchModelList;
        private int resource;
        private LayoutInflater inflater;
        private MatchListFragment.OnListFragmentInteractionListener listener;
        ListAdapter(Context context, int resource, List<MatchModel> objects,
                           MatchListFragment.OnListFragmentInteractionListener listener){
            super(context,resource,objects);
            matchModelList = objects;
            this.resource = resource;
            this.listener = listener;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        @NonNull
        public View getView(final int position, View convertView, @NonNull ViewGroup parent){
            if(convertView == null){
                convertView = inflater.inflate(resource, null);
            }
            final TextView tv_MatchID = convertView.findViewById(R.id.tv_match_id);
            TextView tv_Time = convertView.findViewById(R.id.tv_time);
            TextView tv_Teams = convertView.findViewById(R.id.tv_teams);
            TextView tv_Gym = convertView.findViewById(R.id.tv_gym);
            TextView tv_Result = convertView.findViewById(R.id.tv_result);
            TextView tv_Referee = convertView.findViewById(R.id.tv_referee);

            tv_MatchID.setText(matchModelList.get(position).getGroup());
            tv_Time.setText(matchModelList.get(position).getTime()+ " Uhr");
            tv_Teams.setText(matchModelList.get(position).getTeam1() +" vs. " + matchModelList.get(position).getTeam2());
            tv_Gym.setText("Halle: " + matchModelList.get(position).getGym());
            tv_Result.setText("Ergebnis: " +matchModelList.get(position).getResult());
            tv_Referee.setText("Schiedsgericht: " +matchModelList.get(position).getReferee());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MatchModel mm = matchModelList.get(position);
                    String[] mmArray = new String[9];
                    mmArray[0] = mm.getMatchID();
                    mmArray[1] = mm.getGroup();
                    mmArray[2] = mm.getTime();
                    mmArray[3] = mm.getTeam1();
                    mmArray[4] = mm.getTeam2();
                    mmArray[5] = mm.getResult();
                    mmArray[6] = mm.getSport();
                    mmArray[7] = mm.getReferee();
                    mmArray[8] = mm.getGym();
                    listener.onListFragmentInteraction(mmArray);
                }
            });

            return convertView;
        }
}
