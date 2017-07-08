package vandenbussche.airbussources.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.exception.InvalidFieldException;
import vandenbussche.airbussources.exception.InvalidPasswordException;

public class StartActivity extends AppCompatActivity {

    private ImageView image;

    private Button signUpButton;
    private Button logInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        image = (ImageView) findViewById(R.id.airbusLogo);

        signUpButton = (Button) findViewById(R.id.buttonSignUp);
        logInButton = (Button) findViewById(R.id.buttonConnect);

        try {
            image.setImageBitmap(BitmapFactory.decodeStream(getApplicationContext().getAssets().open("airbus-logo-large.png")));
        } catch (IOException e) {
            //Do nothing if the asset doesnt load properly (default image will be displayed)
        }

        logInButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(Member.connectedMember != null){
                            Intent intent = new Intent(StartActivity.this, MainMenu.class);
                            startActivity(intent);
                        }
                        Intent intent = new Intent(StartActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }
                }
        );
        signUpButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        Intent intent = new Intent(StartActivity.this, SignUpActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


}
