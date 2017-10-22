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

public class SearchByProduct extends AppCompatActivity {

    private TextView screenTitle;
    private EditText nameField;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyproduct);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.searchByProduct_title);
            ab.show();
        }

        screenTitle = (TextView) findViewById(R.id.searchByProductTitle);

        nameField = (EditText) findViewById(R.id.searchByProductNameField);

        searchButton = (Button) findViewById(R.id.searchByProductButton);

        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Research research = new Research(SearchByProduct.this);
                        ArrayList<String> researchWords = new ArrayList<>();
                        researchWords.addAll(Arrays.asList(nameField.getText().toString().split(" ")));
                        ArrayList<CharSequence> researchResult = new ArrayList<CharSequence>(research.getProductsNames(SearchByProduct.this, researchWords));

                        Intent intent = new Intent(SearchByProduct.this, ResearchResults.class);
                        intent.putCharSequenceArrayListExtra("resultsList", researchResult);
                        intent.putExtra("Research Type", "Product");
                        startActivity(intent);
                    }
                }
        );
    }
}
