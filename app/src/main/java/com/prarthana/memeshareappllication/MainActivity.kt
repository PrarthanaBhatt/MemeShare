package com.prarthana.memeshareappllication

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = "https://meme-api.herokuapp.com/gimme"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)
        loadMemes()
    }

    private fun loadMemes() {

        //volley library works in background thread and manages cache
        //multiple request can be handle

        //post:: to post/add data to server
        //put:: to update data on server
        //get:: to get data

        var memeShareImg: ImageView = findViewById(R.id.memeShareImg)
        var progress: ProgressBar = findViewById(R.id.progress)


        progress.visibility = View.VISIBLE
        // Instantiate the RequestQueue.....not to be made when we are creating singleton class
        // google also recommends to use singleton class
        //from now volley will have single instance..for complete lifecycle
//        val queue = Volley.newRequestQueue(this)
//        val url = "https://meme-api.herokuapp.com/gimme"



        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentImageUrl, null,
            Response.Listener { response ->
                currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility = View.GONE
                        return false
                    }
                }).into(memeShareImg)
            }, Response.ErrorListener() {

//                    Log.d("error",it.localizedMessage)
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()

            })


// Add the request to the RequestQueue...when we don't create singleton class
//        queue.add(jsonObjectRequest)

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        //Intent: inter process communication in android
        //to pass data, or call some other process
        //implicit and explicit

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey, checkout this cool meme I got from Reddit $currentImageUrl"
        )

        //make chooser
        val chooser = Intent.createChooser(intent, "Share this meme using..")
        startActivity(chooser)

    }

    fun nextMeme(view: View) {
        loadMemes()
    }
}