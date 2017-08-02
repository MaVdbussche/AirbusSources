package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Namable;


public class ResearchResults extends AppCompatActivity {

    ListView listView;
    ArrayList resultsIdentifiers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_results);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.researchResults_screenTitle);
        ab.show();

        listView= (ListView) findViewById(R.id.listView);


        final Intent inputIntent = getIntent();
        resultsIdentifiers = inputIntent.getExtras().getCharSequenceArrayList("resultsList");
        if(results!=null)
        {
            dispResults();
        }
        else
        {
            Toast nothingFound = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.researchResults_noResultsFound), Toast.LENGTH_SHORT);
            nothingFound.show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (inputIntent.getExtras().get("Research Type").equals("Product")){
                    Intent intent = new Intent(ResearchResults.this, DetailsProduct.class);
                    intent.putExtra("Identifier", results.get(position).getIdentifier());
                    startActivity(intent);
                } else if (inputIntent.getExtras().get("Research Type").equals("Member")){
                    Intent intent = new Intent(ResearchResults.this, DetailsMember.class);
                    intent.putExtra("Identifier", results.get(position).getIdentifier());
                    startActivity(intent);

                } else if (inputIntent.getExtras().get("Research Type").equals("Supplier")){
                    Intent intent = new Intent(ResearchResults.this, DetailsSupplier.class);
                    intent.putExtra("Identifier", results.get(position).getIdentifier());
                    startActivity(intent);

                } else{
                    System.out.println("Research Type in intent to ResearchResults.java is wrong ! No assumptions are made about what will happen next !");
                }
            }
        });
    }

    //Envoie a l'adapter la liste de recette a afficher
    private void dispResults()
    {
        RowAdapter adapter = new RowAdapter(ResearchResults.this, (List) results);
        listView.setAdapter(adapter);
    }
}
