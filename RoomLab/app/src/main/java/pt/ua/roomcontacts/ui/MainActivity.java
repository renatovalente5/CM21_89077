package pt.ua.roomcontacts.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import pt.ua.roomcontacts.R;
import pt.ua.roomcontacts.data.Contact;
import pt.ua.roomcontacts.viewmodel.ContactsViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;

    // reference to to my (view)model
    private ContactsViewModel mContactsViewModel;


    @SuppressWarnings("Convert2Lambda")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callTheContactsInputActivity();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ContactsListAdapter adapter = new ContactsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // associate a view model to this activity  and start observing its state
        mContactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        mContactsViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts) {
                // Update the cached copy of the words in the adapter.
                adapter.setContacts(contacts);
            }
        });
    }

    private void callTheContactsInputActivity() {
        Intent intent = new Intent(MainActivity.this, InputNewContactActivity.class);
        startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
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
        if (id == R.id.action_clear_all) {
                mContactsViewModel.clearAll();
        }

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get the response from the second activity
        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            Contact contact = new Contact(
                    Objects.requireNonNull(data.getStringExtra(InputNewContactActivity.EXTRA_TAG_REPLY_DATA_EMAIL)),
                    Objects.requireNonNull(data.getStringExtra(InputNewContactActivity.EXTRA_TAG_REPLY_DATA_NAME))
                    );

            if(  mContactsViewModel.alreadyExists(contact) ) {
                Toast.makeText( this, "Email already exists!", Toast.LENGTH_LONG).show();

            } else {

                // save the new world into the database
                // since the activity has no awareness of the database, it will
                // inform  its supporting viewmodel of the state change

                mContactsViewModel.insert(contact);
            }

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}
