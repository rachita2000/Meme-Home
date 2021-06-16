package com.example.mememail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mememail.R;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    private String currentImageUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadMeme();
    }

    private void loadMeme()
    {
        ProgressBar progressBar= findViewById(R.id.pregressBar);
        progressBar.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        String url ="https://meme-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url,null,
                response -> {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);

                    try {
                        currentImageUrl = response.getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Glide.with(MainActivity.this).load(currentImageUrl).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageView);
                }, error -> Toast.makeText(MainActivity.this , "Something went wrong", Toast.LENGTH_LONG).show());

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void nextMeme(View view) {
        loadMeme();
    }
    public void sendMeme(View view) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! Check out this cool meme I got from reddit "+currentImageUrl);
        Intent chooser= Intent.createChooser(intent, "Share meme using...");
        startActivity(chooser);

    }
}