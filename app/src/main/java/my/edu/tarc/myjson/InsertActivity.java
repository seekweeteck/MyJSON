package my.edu.tarc.myjson;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    }

    public void makeServiceCall(int method, Course course) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(getResources().getString(R.string.insert_course_url));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }
}
