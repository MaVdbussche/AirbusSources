package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import vandenbussche.airbussources.R;

public class SignUpActivity2 extends AppCompatActivity {

    private Button toMainMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        toMainMenu = (Button) findViewById(R.id.buttonToMainMenu);

        toMainMenu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignUpActivity2.this, MainMenu.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
