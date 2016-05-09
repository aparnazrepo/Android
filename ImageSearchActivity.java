package aparna.outlook.androidtest.searchimageapplication;

import android.app.ListActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
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

    private static final String givenString = "https://en.wikipedia.org/w/api.php?action=query&prop=pageima;ges&" +
            "format=json&piprop=thumbnail&pithumbsize=50&pilimit=50&generator=prefixsearch&gpssearch=";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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

        editTextSearchbox = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ImageAdapter(listOfImageNames, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        editTextSearchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ImageSearchActivity.this.adapter.getFilter().filter(s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextSearchbox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(ImageSearchActivity.this, editTextSearchbox.getText(), Toast.LENGTH_SHORT).show();
                    String query = editTextSearchbox.getText().toString();
                    String url = givenString + query;
                    new DownloadAsyncTask().execute(url);
                    return true;
                } else {
                    return false;
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ImageSearch Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://aparna.outlook.androidtest.searchimageapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ImageSearch Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://aparna.outlook.androidtest.searchimageapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class DownloadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                String jsonContent = downloadContent(params[0]);
                // Create a Reader from String
                Reader stringReader = new StringReader(jsonContent);

                // Pass the string reader to JsonReader constructor
                JsonReader reader = new JsonReader(stringReader);
                reader.setLenient(true);
                readGson(reader);


                // at the end of method return the JSON response
                return  jsonContent.trim();


            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(ImageSearchActivity.this, result, Toast.LENGTH_LONG).show();
            //populate the listOfImageNames to display in the listview

        }
    }

    private void readGson(JsonReader reader) {

        Log.e("TAG", "in readGson"+reader);

        try {
            reader.beginObject();
            Log.e("TAG", "inside try  ");
            while(reader.hasNext()){
                Log.e("TAG", "reader values" +reader);
                String name = reader.nextName();
                if (name.equals("pages")) {
                    String pages = reader.nextString();
                    Log.e("TAG",pages);
                }  else {
                    Log.e("TAG", "skipped value");
                    reader.skipValue();
                   //Log.e("TAG", reader.nextString());
                }
            }
            reader.endObject();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("TAG", "inside catch");
            e.printStackTrace();
        }
    }

    private String downloadContent(String myUrl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); /* milliseconds */
            conn.setConnectTimeout(15000); /* milliseconds */
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is, length);
            Log.d(TAG, " The response body is :" + contentAsString);
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
