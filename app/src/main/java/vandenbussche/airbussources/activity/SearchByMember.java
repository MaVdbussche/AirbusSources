package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import vandenbussche.airbussources.R;

public class SearchByMember extends AppCompatActivity {

    private TextView screenTitle;
    private EditText nameField;
    private EditText surnameField;
    private EditText buField;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyperson);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.searchByMember_title);
        ab.show();

        screenTitle = (TextView) findViewById(R.id.searchByPersonTitle);

        nameField = (EditText) findViewById(R.id.searchByPersonNameField);
        surnameField = (EditText) findViewById(R.id.searchByPersonSurnameField);
        buField = (EditText) findViewById(R.id.searchByPersonBUField);

        searchButton = (Button) findViewById(R.id.searchByPersonButton);

        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchByMember.this, DetailsMember.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
