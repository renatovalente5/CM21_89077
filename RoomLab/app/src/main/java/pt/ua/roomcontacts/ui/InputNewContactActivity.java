package pt.ua.roomcontacts.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ua.roomcontacts.R;
import pt.ua.roomcontacts.data.Contact;

public class InputNewContactActivity extends AppCompatActivity {

    public static final String EXTRA_TAG_REPLY_DATA_NAME = "pt.ua.roomcontacts.REPLY_NAME" ;
    public static final String EXTRA_TAG_REPLY_DATA_EMAIL = "pt.ua.roomcontacts.REPLY_EMAIL" ;

    private EditText viewName, viewEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_word);

        // old-style way of getting references to views
        // wouls be better to use ViewBindings
        viewName = findViewById(R.id.edName);
        viewEmail = findViewById(R.id.edEmail);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(viewEmail.getText()) || TextUtils.isEmpty(viewName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {

                    // send the information back to the calling activity using a supporting Contact object
                    replyIntent.putExtra(EXTRA_TAG_REPLY_DATA_NAME, viewName.getText().toString());
                    replyIntent.putExtra(EXTRA_TAG_REPLY_DATA_EMAIL, viewEmail.getText().toString());
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

    }
}
