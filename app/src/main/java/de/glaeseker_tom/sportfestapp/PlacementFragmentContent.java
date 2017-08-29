package de.glaeseker_tom.sportfestapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlacementFragmentContent extends Fragment {

    private ListView lvMatches;
    private String serverUrl = "http://192.168.20.30:80/sportfest/";
    private String tableType;
    private ListAdapter2 adapter;
    private ArrayList<PlacementModel> resultlist;
    //private OnListFragmentInteractionListener2 listener;
    //private Button btn;

    public PlacementFragmentContent() {
    }

    @SuppressLint("ValidFragment")
    public PlacementFragmentContent(String tableType) {
        this.tableType = tableType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_placement_content, container, false);
        /*btn = v.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked");
            }
        });*/
        lvMatches = v.findViewById(R.id.pf_listview);
        resultlist = new ArrayList<>();
        adapter = new ListAdapter2(getActivity(), R.layout.placement_list_item, resultlist);//,listener);
        lvMatches.setAdapter(adapter);
        new GetJsonData().execute();
        return v;
    }

    public void setServerURL(String pUrl){
        serverUrl = pUrl;
    }

    public void setTableType(String pTableType){
        tableType = pTableType;
    }



    public class GetJsonData extends AsyncTask<String, String, ArrayList<PlacementModel>> {

        URL url;

        @Override
        protected void onPreExecute() {
            try {
                url = new URL(serverUrl + "get_json_data.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<PlacementModel> doInBackground(String... params) {
            try {
                String json_string;
                String textparam = "sportname="+tableType+"_data";
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
                /*
                InputStream is = getResources().openRawResource(R.raw.placement);
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
                    adapter.notifyDataSetChanged();
                }
                System.out.println("RESULTLIST:"+resultlist.toString());

                JSONArray parentArray = parentObject.getJSONArray(tableType);

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    System.out.println(finalObject.toString());
                    PlacementModel mm = new PlacementModel(Integer.parseInt(finalObject.getString("placementId")), finalObject.getString("team"),
                            Integer.parseInt(finalObject.getString("points")), finalObject.getString("goalDifference"));
                    resultlist.add(mm);
                }
                if(resultlist.size() == 0){
                    return null;
                }
                return resultlist;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PlacementModel> resultlist) {
            super.onPostExecute(resultlist);

            if(resultlist == null){
                Toast.makeText(getActivity(), "FEHLER: Keine Daten gefunden!", Toast.LENGTH_SHORT).show();
                return;
            }
            adapter.notifyDataSetChanged();
        }
    }
  /*  public interface OnListFragmentInteractionListener2 {
        void onListFragmentInteraction(String[] item);
    }*/

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener2) {
            listener = (OnListFragmentInteractionListener2) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
*/
}

class ListAdapter2 extends ArrayAdapter {

    public List<PlacementModel> placementModelList;
    private int resource;
    private LayoutInflater inflater;
    //private PlacementFragmentContent.OnListFragmentInteractionListener2 listener;
    public ListAdapter2(Context context, int resource, List<PlacementModel> objects){
                       //PlacementFragmentContent.OnListFragmentInteractionListener2 listener){
        super(context,resource,objects);
        placementModelList = objects;
        this.resource = resource;
        //this.listener = listener;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(resource, null);
        }
        TextView tvPlace = convertView.findViewById(R.id.pl_tv_place);
        TextView tvTeam = convertView.findViewById(R.id.pl_tv_team);
        TextView tvPoints = convertView.findViewById(R.id.pl_tv_points);
        TextView tvGoalDifference = convertView.findViewById(R.id.pl_tv_goal_difference);

        tvPlace.setText(String.valueOf(placementModelList.get(position).getPlacementId()));
        tvTeam.setText(placementModelList.get(position).getTeam());
        tvPoints.setText(String.valueOf(placementModelList.get(position).getPoints()));
        tvGoalDifference.setText(placementModelList.get(position).getGoalDifference());

        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacementModel pm = placementModelList.get(position);
                String[] pmArray = new String[8];
                pmArray[0] = String.valueOf(pm.getPlacementId());
                pmArray[1] = pm.getTeam();
                pmArray[2] = String.valueOf(pm.getPoints());
                pmArray[3] = pm.getGoalDifference();
                listener.onListFragmentInteraction(pmArray);
            }
        });*/
        return convertView;
    }
}
