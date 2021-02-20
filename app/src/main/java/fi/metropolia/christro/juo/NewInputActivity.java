package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class NewInputActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText editWordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_input);
        editWordView = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                int intakeInput = Integer.parseInt(editWordView.getText().toString());
                replyIntent.putExtra(EXTRA_REPLY, intakeInput);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}