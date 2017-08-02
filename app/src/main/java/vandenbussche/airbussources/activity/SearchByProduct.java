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

public class SearchByProduct extends AppCompatActivity {

    private TextView screenTitle;
    private EditText nameField;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyproduct);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.searchByProduct_title);
        ab.show();

        screenTitle = (TextView) findViewById(R.id.searchByProductTitle);

        nameField = (EditText) findViewById(R.id.searchByProductNameField);

        searchButton = (Button) findViewById(R.id.searchByProductButton);

        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchByProduct.this, DetailsProduct.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
