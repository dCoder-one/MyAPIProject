package com.example.myapiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapiproject.databinding.ActivityJapaneseDetailBinding
import javax.security.auth.callback.Callback

class JapaneseListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJapaneseDetailBinding
    private lateinit var adapter: JapaneseAdapter

    companion object {
        val TAG = "JapaneseListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJapaneseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = RetrofitHelper.getInstance()
        val japaneseService = retrofit.create(JapaneseService::class.java)
        val earthquakeCall = japaneseService.getEarthquakeDataPastDay()
        earthquakeCall.enqueue(object : Callback<JapaneseCollection> {
            override fun onResponse(
                call: Call<JapaneseCollection>,
                response: Response<JapaneseCollection>
            ) {
//                if(response.body() != null) {
//                    supportActionBar?.title = "Earthquake App"
//                    var adapter = EarthquakeAdapter
//                }
                // this is where you wil get your data.
                // this is where you will set up your adapter for recyclerview
                // don't forget a null check before trying to use the data
                // response.body() contains the object in the <> after Response

                Log.d(TAG, "onResponse: ${response.body()}")
                val japanese =
                    response.body()?.features?.filter { it.properties.mag >= 1.0 } ?: listOf()
                adapter = JapaneseAdapter(japanese)
                adapter.japanseList =
                    adapter.japaneseList.sortedWith(compareByDescending<Japanese> { it.properties.mag }.thenBy { it.properties.time })
                binding.recyclerViewEarthquakesListEarthquakes.adapter = adapter
                binding.recyclerViewEarthquakesListEarthquakes.layoutManager =
                    LinearLayoutManager(this@JapaneseListActivity)
            }

            override fun onFailure(call: Call<JapaneseCollection>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
}