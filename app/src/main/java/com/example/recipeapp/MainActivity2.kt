package com.example.recipeapp

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity2 : AppCompatActivity() {
    private var  recipinfo: List<RecipeInfo.Datum>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val responseText = findViewById<View>(R.id.tvView) as TextView

        getRecipes(onResult = {
            recipinfo = it
            Log.e("Data", recipinfo.toString())

            var stringToBePritined:String? = "";
            for(recipe in recipinfo!!) stringToBePritined = stringToBePritined +recipe.title + "\n"+recipe.author + "\n"+recipe.ingredients + "\n"+recipe.instructions+ "\n\n"
            responseText.text= stringToBePritined
        } );
    }


    private fun getRecipes(onResult: (List<RecipeInfo.Datum>?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val progressDialog = ProgressDialog(this@MainActivity2)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        if (apiInterface != null) {
            apiInterface.getRecipies()?.enqueue(object : Callback<List<RecipeInfo.Datum>> {
                override fun onResponse(
                    call: Call<List<RecipeInfo.Datum>>,
                    response: Response<List<RecipeInfo.Datum>>
                ) {
                    onResult(response.body())
                    progressDialog.dismiss()

                }

                override fun onFailure(call: Call<List<RecipeInfo.Datum>>, t: Throwable) {
                    onResult(null)
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }}}



