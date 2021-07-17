package com.example.workshop

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var fragmentContainerView: FragmentContainerView
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // load the main fragment view to the container
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MainFragment>(R.id.fragment_container_view)
            }
            // get workshops from the link
            getWorkshops()
        }
        setContentView(R.layout.activity_main)

        // get all views
        progressBar = findViewById(R.id.progressBar)
        fragmentContainerView = findViewById(R.id.fragment_container_view)
        errorTextView = findViewById(R.id.errorTextView)
    }

    private fun getWorkshops() {
        // get the request queue
        val queue = Volley.newRequestQueue(this)
        val url = "https://bigvu-interviews-assets.s3.amazonaws.com/workshops.json"

        // set the request
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // create empty list
                val workshops: MutableList<Workshop> = mutableListOf()
                // convert the response to JSON object
                val jsonArray = JSONArray(response)
                // add the data from the JSON to the list
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    workshops.add(Workshop(item))
                }
                // update the list on the view model
                viewModel.setWorkshops(workshops)
                // hide progress bar
                progressBar!!.visibility = View.GONE
                // show fragment container.
                fragmentContainerView!!.visibility = View.VISIBLE
            },
            {
                // hide progress bar
                progressBar!!.visibility = View.GONE
                // show error text view
                errorTextView.text = "Error! please check your network connection"
                errorTextView!!.visibility = View.VISIBLE

            })
        // add the request to the request queue
        queue.add(stringRequest)

    }
}