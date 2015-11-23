package my.edu.tarc.myjson;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    ListView listViewCustomer;
    List<Course> caList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCustomer = (ListView) findViewById(R.id.listView);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();
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
            if(caList.size()> 0){
                loadCourse();
            }else{
                Toast.makeText(getApplicationContext(), "No record.", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.action_insert) {
            Intent intent = new Intent(this, InsertActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readCourse() {
        try {
            // Check availability of network connection.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            Boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if (isConnected) {
                //new downloadCourse().execute(getResources().getString(R.string.get_course_url));
               downloadCourse(this, getResources().getString(R.string.get_course_url));
            } else {
                Toast.makeText(getApplication(), "Network is NOT available",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplication(),
                    "Error reading record:" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void downloadCourse(Context context, String url) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        if (!pDialog.isShowing())
            pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for(int i=0; i < response.length();i++){
                                JSONObject courseResponse = (JSONObject) response.get(i);
                                String code = courseResponse.getString("code");
                                String title = courseResponse.getString("title");
                                String credit = courseResponse.getString("credit");
                                Course course = new Course();
                                course.setCode(code);
                                course.setTitle(title);
                                course.setCredit(credit);
                                caList.add(course);
                            }
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error:" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void loadCourse() {
        final CourseAdapter adapter = new CourseAdapter(this, caList);
        listViewCustomer.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Count :" + caList.size(), Toast.LENGTH_LONG).show();
    }
}
