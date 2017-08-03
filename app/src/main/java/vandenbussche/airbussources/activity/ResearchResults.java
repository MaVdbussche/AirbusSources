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
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Namable;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;


public class ResearchResults extends AppCompatActivity {

    ListView listView;

    final Intent inputIntent = getIntent();
    final String resultsType = inputIntent.getStringExtra("Research Type");
    ArrayList<CharSequence> resultsIdentifiers = inputIntent.getExtras().getCharSequenceArrayList("resultsList");

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

        if(resultsIdentifiers.size() > 0)
        {
            sendToAdapter(resultsIdentifiers);
        }
        else
        {
            Toast nothingFound = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.researchResults_noResultsFound), Toast.LENGTH_SHORT);
            nothingFound.show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (resultsType.equals("Product")){
                    Intent intent = new Intent(ResearchResults.this, DetailsProduct.class);
                    intent.putExtra("Identifier", resultsIdentifiers.get(position).toString());
                    startActivity(intent);

                } else if (resultsType.equals("Member")){
                    Intent intent = new Intent(ResearchResults.this, DetailsMember.class);
                    intent.putExtra("Identifier", resultsIdentifiers.get(position).toString());
                    startActivity(intent);

                } else if (resultsType.equals("Supplier")){
                    Intent intent = new Intent(ResearchResults.this, DetailsSupplier.class);
                    intent.putExtra("Identifier", resultsIdentifiers.get(position).toString());
                    startActivity(intent);

                } else {
                    System.out.println("Research Type in intent to ResearchResults.java is wrong ! No assumptions are made about what will happen next !");
                }
            }
        });
    }


    private void sendToAdapter(List<CharSequence> elementsIdentifiers){

        ArrayList<Namable> results = new ArrayList<>();

        if(resultsType.equals("Member")){
            for ( CharSequence member : elementsIdentifiers){
                results.add(new Member(ResearchResults.this, member.toString()));
            }
        } else if(resultsType.equals("Supplier")){
            for ( CharSequence supplier : elementsIdentifiers){
                results.add(new Supplier(ResearchResults.this, supplier.toString()));
            }
        } else if (resultsType.equals("Product")){
            for ( CharSequence product : elementsIdentifiers){
                results.add(new Product(ResearchResults.this, product.toString()));
            }
        } else {
            System.out.println("Research Type received by sendToAdapter() is wrong ! No assumptions are made about what will happen next !");
        }

        RowAdapter adapter = new RowAdapter(ResearchResults.this, results);
        listView.setAdapter(adapter);
    }
}
