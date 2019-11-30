package id.kuliah.prameksapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import android.content.Intent as Intent1

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "Login"

        bt_go.setOnClickListener{
            CekLogin()
        }
    }

    private fun CekLogin(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        val userr = et_user.getText().toString()
        val passs = et_pass.getText().toString()

        AndroidNetworking.post(ApiKoneksi.READ3)
            .addBodyParameter("username",userr)
            .addBodyParameter("password",passs)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()

                    for(i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)
                        val id_penumpang = jsonObject.getString("id_penumpang").toString()

                        if (jsonArray?.length()-1 == i){
                            tv_id_ambil.setText(id_penumpang)
                            val id_kir = tv_id_ambil.text
                            intent = Intent1(this@Login, CariTiket::class.java)
                            intent.putExtra("id_penumpang",id_kir)
                            startActivity(intent)
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
