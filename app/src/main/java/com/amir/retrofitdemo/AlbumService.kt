package com.amir.retrofitdemo

import retrofit2.Response
import retrofit2.http.*

interface AlbumService {

    //abstract function to get albums data
    //return retrofit response object of type albums
    /*why suspend: because we will use coroutines with retrofit in this project
    if we dont coroutines we don't need suspend modifier*/
    /*how do we decide the return type. Retrofit always give the result as a Retrofit response object
    we decide the type by looking at that json(the name of the class which represents the entire json object
    * */
    /*for this example we send a get request(it is the simplest form of http request)
    inside of get we pass the URL*/
    /*
    https://jsonplaceholder.typicode.com/albums
    https://jsonplaceholder.typicode.com => base URL
    /albums => end point URL */
    /*
    we will add the base url when we are creating the retrofit instance class
    for this function we we will only add the url end point
     */
    @GET("/albums")
    suspend fun getAlbums(): Response<Albums>

    //to add the query parameter we use the query annotation
    //then we need to provide the name of the field  as the value
    //then define the parameter  =>  @Query("userId")userId:Int
    @GET("/albums")
    suspend fun getSortedAlbums(@Query("userId") userId: Int): Response<Albums>

    //As you can see here, This URL with path parameter gives an AlbumItem object.
    //Therefore the return type should be a Retrofit Response object of type AlbumItem.
    @GET("/albums/{id}")
    suspend fun getAlbum(@Path(value = "id") albumId: Int): Response<AlbumsItem>

    //This albumsItem will send as the body of the POST request. So, we should annotate this with the body annotation.
    @POST("/albums")
    suspend fun uploadAlbum(@Body album: AlbumsItem): Response<AlbumsItem>


}