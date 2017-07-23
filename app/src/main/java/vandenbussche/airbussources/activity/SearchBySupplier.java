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

public class SearchBySupplier extends AppCompatActivity {

    private TextView screenTitle;
    private EditText nameField;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbysupplier);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.searchBySupplier_title);
        ab.show();

        screenTitle = (TextView) findViewById(R.id.searchBySupplierTitle);

        nameField = (EditText) findViewById(R.id.searchBySupplierNameField);

        searchButton = (Button) findViewById(R.id.searchBySupplierButton);

        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchBySupplier.this, DetailsSupplier.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
