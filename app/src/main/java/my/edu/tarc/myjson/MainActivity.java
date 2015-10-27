package my.edu.tarc.myjson;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    ListView listViewCustomer;
    List<Course> caList;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCustomer = (ListView)findViewById(R.id.listView);

        prgDialog = new ProgressDialog(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_syn) {
            readCourse();
            return true;
        }else if(id == R.id.action_insert){
            Intent intent = new Intent(this, InsertActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readCourse() {
        try{
            // Check availability of network connection.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            Boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if (isConnected) {
                new downloadCourse().execute(getResources().getString(R.string.get_course_url));
            }else{
                Toast.makeText(getApplication(), "Network is NOT available",
                        Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplication(),
                    "Error reading record:"+ e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void loadCourse() {
        final CourseAdapter adapter = new CourseAdapter(this, caList);
        listViewCustomer.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Count :" + caList.size(), Toast.LENGTH_LONG).show();
    }

    private class downloadCourse extends AsyncTask<String, Void, List<Course>>{
        // Show Progress bar before downloading Music

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            prgDialog.show();
            prgDialog.setMessage("Downloading file...");
        }

        @Override
        protected List<Course> doInBackground(String... params) {
            HttpURLConnection urlConnection=null;

            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                readJsonStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                assert urlConnection != null;
                urlConnection.disconnect();
            }
            return caList;
        }

        @Override
        protected void onPostExecute(List<Course> customerAccounts) {
            super.onPostExecute(customerAccounts);
            loadCourse();
            prgDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        private void readJsonStream(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                 caList= readMessagesArray(reader);
            } finally{
                reader.close();
            }
        }

        private List<Course> readMessagesArray(JsonReader reader) throws IOException {
            List<Course> accounts = new ArrayList<Course>();

            reader.beginArray();
            while (reader.hasNext()) {
                accounts.add(readMessage(reader));
            }
            reader.endArray();
            return accounts;
        }

        private Course readMessage(JsonReader reader) throws IOException {
            Course cust = new Course();
            //List geo = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("code")) {
                    cust.setCode(reader.nextString());
                } else if (name.equals("title")) {
                    cust.setTitle(reader.nextString());
                }  else if (name.equals("credit")) {
                    cust.setCredit(Integer.parseInt(reader.nextString()));
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return cust;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
