package com.jaiveeer.userdeatils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaiveeer.userdeatils.config.URLs;
import com.jaiveeer.userdeatils.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="jaitest" ;
    EditText editTextUser,editTextEmail,editTextPassword,editTextDesigantion;
    Button buttonRegsiter;
    RadioGroup radioGroupGender;
    ProgressBar progressBar;
    TextView editImage,textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"hi jaiveer");


        progressBar = findViewById(R.id.progressBar);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
            return;
        }
        buttonRegsiter = findViewById(R.id.buttonRegister);
        editTextUser = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPaswword);
        radioGroupGender = findViewById(R.id.radioGender);
        editTextDesigantion = findViewById(R.id.editTextDesignation);
        editImage = findViewById(R.id.imageViewedit);

        textViewLogin = findViewById(R.id.textViewLogin);



        buttonRegsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }




    private void registerUser() {
        final String name = editTextUser.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String designation = editTextDesigantion.getText().toString().trim();
        final String image = editImage.getText().toString().trim();

        final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        //first we will do the validations

        if (TextUtils.isEmpty(name)) {
            editTextUser.setError("Please enter username");
            editTextUser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (obj.getBoolean("status")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message")+"jai", Toast.LENGTH_SHORT).show();


                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                 User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getString("password"),
                                        userJson.getString("gender"),
                                        userJson.getString("designation"),
                                        userJson.getString("image")
                                );
                                Log.d(TAG, String.valueOf(obj));
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);


                                finish();
                                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                                i.putExtra("namejai","Barhi");
                                startActivity(i);




                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uname",name);
                params.put("uemail", email);
                params.put("upassword", password);
                params.put("upassword", password);
                params.put("ugender", gender);
                params.put("udesignation", designation);
                params.put("uimage", image);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }







}
