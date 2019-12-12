package com.jaiveeer.userdeatils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaiveeer.userdeatils.model.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewid,textViewName,textViewMail,textViewDesignation,textViewGender;
    Button logoutButton;
    ImageView imageViewPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewid = findViewById(R.id.textViewIdPro);
        textViewName = findViewById(R.id.textViewNamePro);
        textViewMail = findViewById(R.id.textViewEmailPro);
        textViewGender = findViewById(R.id.textViewGenderPro);
        textViewDesignation = findViewById(R.id.textViewDesignationPro);

        imageViewPro = findViewById(R.id.imageViewPro);

        logoutButton = findViewById(R.id.buttonLogoutPro);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }



        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewid.setText(String.valueOf(user.getId()));
        textViewMail.setText(user.getEmail());
        textViewName.setText(user.getName());
        textViewGender.setText(user.getGender());
        textViewDesignation.setText(user.getDesignation());

        Picasso.with(this).load(user.getImage()).into(imageViewPro);

        //when the user presses logout button
        //calling the logout method
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });




    }
}
