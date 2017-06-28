package vandenbussche.airbussources.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import vandenbussche.airbussources.R;

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
            //Si probleme avec les images, on laisse affiche l'image de base de l'imageView
        }
    }
}
