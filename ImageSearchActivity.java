package aparna.outlook.androidtest.searchimageapplication;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ImageSearchActivity extends AppCompatActivity {

    EditText editTextSearchbox;
    ListView listView;
    ImageAdapter adapter;
    ArrayList<String> listOfImageNames; //image names received to http response
    private static final String TAG = ImageSearchActivity.class.getSimpleName();

    private static final String  givenString = "https://en.wikipedia.org/w/api.php?action=query&prop=pageima;ges&" +
            "format=json&piprop=thumbnail&pithumbsize=50&pilimit=50&generator=prefixsearch&gpssearch=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //Temp
        listOfImageNames = new ArrayList<String>();
        listOfImageNames.add("One");
        listOfImageNames.add("Two");
        listOfImageNames.add("Three");


        /*this.setListAdapter(new ArrayAdapter(
                this, R.layout.image_list));*/
        editTextSearchbox=(EditText)findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.listView);
        adapter = new ImageAdapter(listOfImageNames,this);
        listView.setAdapter(adapter);


        editTextSearchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ImageSearchActivity.this.adapter.getFilter().filter(s);
                //executeAsyntask
                /*String query = editTextSearchbox.getText().toString();
                String url = givenString+query;
                makeAsyncRequest(url);*/
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //executeAsyncTask
               /* String query = editTextSearchbox.getText().toString();
                String url = givenString + query;
                makeAsyncRequest(url);
*/
            }
        });

        editTextSearchbox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
               if ((event.getAction() == KeyEvent.ACTION_DOWN) &&(keyCode == KeyEvent.KEYCODE_ENTER)) {
               // Perform action on key press
                    Toast.makeText(ImageSearchActivity.this, editTextSearchbox.getText(), Toast.LENGTH_SHORT).show();
                     String query = editTextSearchbox.getText().toString();
                     String url = givenString + query;
                     new DownloadAsyncTask().execute(url);
                     return true;
                }else{
                return false;}
            }
        });
    }

    public void makeAsyncRequest(String searchUrl){// Prepare your search string to be put in a URL
        new DownloadAsyncTask().execute(searchUrl);
    }

    //private class DownloadAsyncTask extends AsyncTask<String, Void, HashMap<String,String>> {
        //HashMap<String,String> jsonPair = new HashMap<String,String>();
     private class DownloadAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        //protected HashMap<String,String> doInBackground(String... params) {
        protected String doInBackground(String params){
            try {
               JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = jsonObject.names();
                if(jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d(" JSON Array value :", jsonArray.getString(i));
                    }
                }

                /*Iterator<String> jsonKeyIterator = jsonObject.keys();
                while(jsonKeyIterator.hasNext()){
                    String key = jsonKeyIterator.next();
                    try{
                        Object value = jsonObject.get(key);
                        jsonPair.put(key,value.toString());
                        Log.d(" JSON Object Value :", value.toString());
                    }catch(JSONException e) {
                        Log.d(" EXCEPTION :", e.toString());
                    }
                }
                return jsonPair;*/
                return downloadContent(params[0]);

            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(ImageSearchActivity.this, result, Toast.LENGTH_LONG).show();
            //populate the listOfImageNames
        }
    }

    private String downloadContent(String myUrl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is, length);
            Log.d(TAG," The response body is :"+contentAsString);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
