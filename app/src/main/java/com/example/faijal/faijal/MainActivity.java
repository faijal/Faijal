package com.example.faijal.faijal;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.faijal.faijal.JSONParser;


public class MainActivity extends Activity {
    ListView list;
    TextView userId;
    TextView id;
    TextView title;
    TextView body;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://jsonplaceholder.typicode.com/posts/";

    //JSON Node Names
    private static final String TAG_userId = "userId";
    private static final String TAG_id = "id";
    private static final String TAG_title = "title";
    private static final String TAG_body = "body";

    JSONArray android = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oslist = new ArrayList<HashMap<String, String>>();

        Btngetdata = (Button) findViewById(R.id.getdata);
        Btngetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();

            }
        });
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userId = (TextView)findViewById(R.id.userId);
            id = (TextView)findViewById(R.id.id);
            title = (TextView)findViewById(R.id.title);
            body = (TextView)findViewById(R.id.body);
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }
        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String userId = c.getString(TAG_userId);
                    String id = c.getString(TAG_id);
                    String title = c.getString(TAG_title);
                    String body = c.getString(TAG_body);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_userId, userId);
                    map.put(TAG_id, id);
                    map.put(TAG_title, title);
                    map.put(TAG_body, body);

                    oslist.add(map);
                    list=(ListView)findViewById(R.id.list);
                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist,
                            R.layout.list_v,
                            new String[] { TAG_userId,TAG_id, TAG_title,TAG_body }, new int[] {
                            R.id.userId,R.id.id, R.id.title,R.id.body});
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(MainActivity.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}