package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Research;

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
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.searchByMember_title);
            ab.show();
        }

        screenTitle = (TextView) findViewById(R.id.searchByPersonTitle);

        nameField = (EditText) findViewById(R.id.searchByPersonNameField);
        surnameField = (EditText) findViewById(R.id.searchByPersonSurnameField);
        buField = (EditText) findViewById(R.id.searchByPersonBUField);

        searchButton = (Button) findViewById(R.id.searchByPersonButton);

        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Research research = new Research(SearchByMember.this);
                        ArrayList<String> researchNames = new ArrayList<String>();
                        ArrayList<String> researchBUs = new ArrayList<String>();
                        researchNames.addAll(Arrays.asList(nameField.getText().toString().split(" ")));
                        researchNames.addAll(Arrays.asList(surnameField.getText().toString().split(" ")));
                        researchBUs.addAll(Arrays.asList(buField.getText().toString().split(" ")));
                        ArrayList<String> idProfiles = research.getMembersIdProfilesByNames(SearchByMember.this, researchNames);
                        idProfiles.addAll(research.getMembersIDProfilesByBU(SearchByMember.this, researchBUs));
                        ArrayList<CharSequence> researchResult = new ArrayList<CharSequence>(idProfiles);

                        Intent intent = new Intent(SearchByMember.this, ResearchResults.class);
                        intent.putCharSequenceArrayListExtra("resultsList", researchResult);
                        intent.putExtra("Research Type", "Member");
                        startActivity(intent);
                    }
                }
        );
    }
}
