package com.amir.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import org.w3c.dom.Text
import retrofit2.Response
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    private lateinit var retService: AlbumService
    private lateinit var tv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById<TextView>(R.id.text_view)
        //to fetch the albums from json placeholder we need to use the retrofit instance
        //pass the name of interface inside of it
        retService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        //getRequestWithQueryParameter()
        //  getRequestWithPathParameters()
        uploadAlbum()


    }

    private fun uploadAlbum() {
        //the id will be ignored by the server and give a new id
        val album = AlbumsItem(1, "My Tile Ema", 3)

        val postResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.uploadAlbum(album)
            emit(response)
        }

        postResponse.observe(this, Observer {
            //getting the received albumsItem instance from the response body
            val receivedAlbumsItem: AlbumsItem? = it.body()
            val result: String = " " + "Album id: ${receivedAlbumsItem?.id}" + "\n" +
                    " " + "Album title: ${receivedAlbumsItem?.title}" + "\n" +
                    " " + "User id: ${receivedAlbumsItem?.userId}" + "\n\n\n"
            tv.text = result
        })

    }

    private fun getRequestWithQueryParameter() {
        //now we are using coroutine liveData builder
        //we get the retrofit response as a live data
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            //   val response = retService.getAlbums()
            //all we needs
            val response = retService.getSortedAlbums(3)
            emit(response)
        }
        //to observe loveData
        responseLiveData.observe(this, Observer {
            //response body
            val albumsList: MutableListIterator<AlbumsItem>? = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()) {

                    val albumsItem = albumsList.next()
                    //Log.e("MY TAG", albumsItem.title)
                    val result: String = " " + "Album id: ${albumsItem.id}" + "\n" +
                            " " + "Album title: ${albumsItem.title}" + "\n" +
                            " " + "User id: ${albumsItem.userId}" + "\n\n\n"
                    //this will the new data be inserted on the last one we dont want this
                    //tv.text = result
                    tv.append(result)
                }

            }
        })
    }

    private fun getRequestWithPathParameters() {
        //path parameter example
        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response: Response<AlbumsItem> = retService.getAlbum(3)
            emit(response)
        }
        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this, "$title", Toast.LENGTH_SHORT).show()
        })
    }
}