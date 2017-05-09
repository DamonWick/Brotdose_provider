package xyz.signupdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etContact;
    private EditText etYrate;
    private EditText etMrate;
    private EditText etRate;
    private EditText etTitle;
    private EditText etName;
    private TextView tvDetail;
    private Button btncon;
    private TextView tvMenu;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.skip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.skip_details:
                startActivity(new Intent(this, MapsActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvMenu = (TextView) findViewById(R.id.tvMenu);
        tvMenu.setOnClickListener(this);
        btncon = (Button) findViewById(R.id.btncon);
        btncon.setOnClickListener(this);
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        etName = (EditText) findViewById(R.id.etName);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etRate = (EditText) findViewById(R.id.etRate);
        etMrate = (EditText) findViewById(R.id.etMrate);
        etYrate = (EditText) findViewById(R.id.etYrate);
        etContact = (EditText) findViewById(R.id.etContact);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");


    }


    @Override
    public void onClick(View v) {
        final String name = etName.getText().toString().trim();
        final String title = etTitle.getText().toString().trim();
        final String rate = etRate.getText().toString().trim();
        String monthlyrate = etMrate.getText().toString().trim();
        String yearlyrate = etYrate.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        if (etName.getText().toString().trim().length() <= 0) {
            Toast.makeText(DetailActivity.this, "Enter the name", Toast.LENGTH_SHORT).show();
            return;
        } else if (etTitle.getText().toString().trim().length() <= 0) {
            Toast.makeText(DetailActivity.this, "Enter the title", Toast.LENGTH_SHORT).show();
            return;
        } else if (etRate.getText().toString().trim().length() <= 0) {
            Toast.makeText(DetailActivity.this, "Enter Your rate", Toast.LENGTH_SHORT).show();
            return;
        } else if (etMrate.getText().toString().trim().length() <= 0) {
            Toast.makeText(DetailActivity.this, "Enter the monthly rate", Toast.LENGTH_SHORT).show();
            return;
        } else if (etYrate.getText().toString().trim().length() <= 0) {
            Toast.makeText(DetailActivity.this, "Enter the yearly rate", Toast.LENGTH_SHORT).show();
            return;
        } else if (etContact.getText().toString().trim().length() <= 0) {
            Toast.makeText(DetailActivity.this, "Enter the contact", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("etName", name);
        dataMap.put("title", title);
        dataMap.put("rate", rate);
        dataMap.put("monthly rate", monthlyrate);
        dataMap.put("yearly rate", yearlyrate);
        dataMap.put("contact", contact);
        FirebaseDatabase.getInstance().getReference("tiffin_provider_profile")
                .child(uid)
                .setValue(dataMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(DetailActivity.this, "success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        switch (v.getId()) {
            case R.id.btncon:
                Intent cont = new Intent(DetailActivity.this, MapsActivity.class);
                startActivity(cont);
                break;
        }

    }
}
