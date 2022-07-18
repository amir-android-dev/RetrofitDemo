package com.amir.retrofitdemo

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//add a companion object to this class
/*why companion object?
In Kotlin, companion object initializes when the class is loaded for the first time.
So if we write codes to create a retrofit instance inside this companion object , we will be able to easily get it using the class name.
 */
class RetrofitInstance {


    companion object {
        val BASE_URL = "https://jsonplaceholder.typicode.com/"

        val interceptor = HttpLoggingInterceptor().apply {
            /*This Body level, logs request and response lines and their respective headers and bodies of the network operation.
             There are other levels. If you choose Basic, It will log request and response lines only.
             If you choose header, It will log request and response lines and their respective headers
             */
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                /*
                We will add them inside this OkHttp client builder block. We don’t need to manually add timeouts, if it is not required.
                 */
                /*
                 Connect timeout is the time period in which our app should establish a connection with the server.
                           Since we just set it as 30 seconds our app’s http client which is the retrofit instance will try to connect to the server for 30 seconds.
                           After 30 seconds it will stop trying.
                            By default this is just 10 seconds. So I think for the most of the cases 30 seconds will be more than enough.
                            */
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)//Read timeout has defined as maximum time gap between arrivals of two data packets  when waiting for the server's response
                .writeTimeout(25,TimeUnit.SECONDS)//maximum time gap between two data packets  when sending them to the server

        }.build()


        //crating a function inside of this companion object which returns a Retrofit instance
        fun getRetrofitInstance(): Retrofit {

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())) //convertorFactory will use GSON to convert json to kotlin
                .build()

        }

    }

}