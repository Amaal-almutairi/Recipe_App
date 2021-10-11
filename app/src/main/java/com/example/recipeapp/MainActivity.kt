package com.example.recipeapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val title = findViewById<View>(R.id.idtitle) as EditText
        val author = findViewById<View>(R.id.idname1) as EditText
        val inge = findViewById<View>(R.id.idingredents) as EditText
        val ins = findViewById<View>(R.id.idinctrucction) as EditText
        val savebtn = findViewById<View>(R.id.savebtn) as Button

        savebtn.setOnClickListener {
            var f = RecipeInfo.Datum(title.text.toString(), author.text.toString(),
                inge.text.toString(),    ins.text.toString())

            addReceipe(f, onResult = {
                title.setText("")
                author.setText("")
                inge.setText("")
                ins.setText("")
                Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show();
            })
        }
    }

    fun addReceipe(userData: RecipeInfo.Datum, onResult: (RecipeInfo?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        if (apiInterface != null) {
            apiInterface.addRecipie(userData).enqueue(object : Callback<RecipeInfo> {
                override fun onResponse(
                    call: Call<RecipeInfo>,
                    response: Response<RecipeInfo>
                ) {
                    onResult(response.body())
                    progressDialog.dismiss()
                }

                override fun onFailure(call: Call<RecipeInfo>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()

                }
            })
        }
    }

    fun viewreceipe(view: android.view.View) {
        intent = Intent(applicationContext, MainActivity2::class.java)
        startActivity(intent)
    }
}