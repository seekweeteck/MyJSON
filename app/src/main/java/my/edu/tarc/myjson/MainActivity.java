package my.edu.tarc.myjson;

import android.content.Context;
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
    String fileUrl ="http://www.ezinfosolution.com/get_all_ca.php";
    List<CustomerAccount> caList;
    //ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCustomer = (ListView)findViewById(R.id.listView);
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
            readCustAccount();
        }

        return super.onOptionsItemSelected(item);
    }

    private void readCustAccount() {
        try{
            // Check availability of network connection.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            Boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if (isConnected) {
                new downLoadCustAcct().execute(fileUrl);
                loadCustAccount();
            }else{
                Toast.makeText(getApplication(), "Network is NOT available",
                        Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplication(),
                    "Error read cust acct:"+ e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void loadCustAccount() {
        final CustomerAccountAdapter adapter = new CustomerAccountAdapter(this, caList);
        listViewCustomer.setAdapter(adapter);
    }

    private class downLoadCustAcct extends AsyncTask<String, Void, List<CustomerAccount>>{
        // Show Progress bar before downloading Music

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
           /* pDialog = new ProgressDialog(getApplication());
            pDialog.setTitle("Getting data");
            pDialog.setMessage("Please wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        @Override
        protected List<CustomerAccount> doInBackground(String... params) {
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
        protected void onPostExecute(List<CustomerAccount> customerAccounts) {
            super.onPostExecute(customerAccounts);
            //pDialog.dismiss();
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

        private List<CustomerAccount> readMessagesArray(JsonReader reader) throws IOException {
            List<CustomerAccount> accounts = new ArrayList<CustomerAccount>();

            reader.beginArray();
            while (reader.hasNext()) {
                accounts.add(readMessage(reader));
            }
            reader.endArray();
            return accounts;
        }

        private CustomerAccount readMessage(JsonReader reader) throws IOException {
            CustomerAccount cust = new CustomerAccount();
            //List geo = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("ca_id")) {
                    cust.setCa_id(reader.nextString());
                } else if (name.equals("ca_name")) {
                    cust.setCa_name(reader.nextString());
                } else {
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
        // Dismiss the progress bar when application is closed
       /* if (pDialog != null) {
            pDialog.dismiss();
        }*/
    }
}
