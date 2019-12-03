package id.kuliah.prameksapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.activity_registrasi.*
import org.json.JSONObject

class Registrasi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        bt_daftar.setOnClickListener{
            create()
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun create(){
        val loading = ProgressDialog(this)
        loading.setMessage("Menambahkan data...")
        loading.show()

        val ktp_r = et_ktp.getText().toString()
        val nama_r = et_nama.getText().toString()
        val notelp_r = et_notelp.getText().toString()
        val email_r = et_email.getText().toString()
        val username_r = et_username.getText().toString()
        val password_r = et_password.getText().toString()

        println(ktp_r+" "+nama_r+" "+notelp_r+" "+email_r+" "+username_r+" "+password_r)

        AndroidNetworking.post(ApiKoneksi.CREATE3)
            .addBodyParameter("ktp",ktp_r)
            .addBodyParameter("username",username_r)
            .addBodyParameter("password",password_r)
            .addBodyParameter("nama",nama_r)
            .addBodyParameter("notelp",notelp_r)
            .addBodyParameter("email",email_r)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext,response?.getString("message"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("message")?.contains("successfully")!!){}
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
