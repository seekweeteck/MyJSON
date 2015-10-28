package my.edu.tarc.myjson;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class InsertActivity extends ActionBarActivity {
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        buttonSave = (Button) findViewById(R.id.buttonSave);
    }

    public void saveRecord(View v) {
        EditText editTextCode, editTextTitle, editTextCredit;

        editTextCode = (EditText) findViewById(R.id.editTextCode);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextCredit = (EditText) findViewById(R.id.editTextCredit);

        Course course = new Course();

        course.setCode(editTextCode.getText().toString());
        course.setTitle(editTextTitle.getText().toString());
        course.setCredit(editTextCredit.getText().toString());
        //format data to string
        String data = getPostDataString(course);
        //send data to server
        makeServiceCall(data);
    }

    public void makeServiceCall(String data) {
        URL url;
        String response = "";

        try {
            url = new URL(getResources().getString(R.string.insert_course_url));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader((new InputStreamReader((conn.getInputStream()))));
                while((line = br.readLine())!=null){
                    response+=line;
                }
            }else{
                response="";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPostDataString(Course course) {
        StringBuilder result = new StringBuilder();

        try {
            result.append(URLEncoder.encode("code", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(course.getCode(), "UTF-8"));
            result.append("&");
            result.append(URLEncoder.encode("title", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(course.getTitle(), "UTF-8"));
            result.append("&");
            result.append(URLEncoder.encode("credit", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(course.getCredit(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
