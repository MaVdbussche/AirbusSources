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

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Research;

public class SearchBySupplier extends AppCompatActivity {

    private TextView screenTitle;
    private EditText nameField;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbysupplier);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.searchBySupplier_title);
            ab.show();
        }

        screenTitle = (TextView) findViewById(R.id.searchBySupplierTitle);

        nameField = (EditText) findViewById(R.id.searchBySupplierNameField);

        searchButton = (Button) findViewById(R.id.searchBySupplierButton);

        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Research research = new Research(SearchBySupplier.this);
                        ArrayList<String> researchWords = new ArrayList<>();
                        researchWords.addAll(Arrays.asList(nameField.getText().toString().split(" ")));
                        ArrayList<CharSequence> researchResult = new ArrayList<CharSequence>(research.getSuppliersNames(SearchBySupplier.this, researchWords));

                        Intent intent = new Intent(SearchBySupplier.this, ResearchResults.class);
                        intent.putCharSequenceArrayListExtra("resultsList", researchResult);
                        intent.putExtra("Research Type", "Supplier");
                        startActivity(intent);
                    }
                }
        );
    }
}
