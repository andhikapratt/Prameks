package id.kuliah.prameksapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_data_penumpang.*
import kotlinx.android.synthetic.main.activity_data_penumpang.et_email
import kotlinx.android.synthetic.main.activity_data_penumpang.et_ktp
import kotlinx.android.synthetic.main.activity_data_penumpang.et_nama
import kotlinx.android.synthetic.main.activity_data_penumpang.et_notelp
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.et_password
import kotlinx.android.synthetic.main.activity_profile.et_username
import kotlinx.android.synthetic.main.activity_registrasi.*
import org.json.JSONObject

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val actionbar = supportActionBar
        actionbar!!.title = "Akun"
        actionbar.setDisplayHomeAsUpEnabled(true)

        TampilPenumpang()

        bt_update.setOnClickListener(){
            update()
        }
    }

    private fun TampilPenumpang(){

        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        val bundle = intent.extras
        val id_penum = bundle?.get("ktp").toString()

        AndroidNetworking.post(ApiKoneksi.READ6)
            .addBodyParameter("ktp",id_penum)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()

                    val jsonObject = jsonArray?.optJSONObject(0)
                    val ktpp = jsonObject?.getString("ktp").toString()
                    val namaa = jsonObject?.getString("nama").toString()
                    val notelpp = jsonObject?.getString("notelp").toString()
                    val emaill = jsonObject?.getString("email").toString()
                    val username = jsonObject?.getString("username").toString()
                    val password = jsonObject?.getString("password").toString()

                    loading.dismiss()
                    et_ktp.setText(ktpp)
                    et_nama.setText(namaa)
                    et_notelp.setText(notelpp)
                    et_email.setText(emaill)
                    et_username.setText(username)
                    et_password.setText(password)
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun update(){
        val loading = ProgressDialog(this)
        loading.setMessage("Mengupdate data...")
        loading.show()

        val ktp_r = et_ktp.getText().toString()
        val nama_r = et_nama.getText().toString()
        val notelp_r = et_notelp.getText().toString()
        val email_r = et_email.getText().toString()
        val username_r = et_username.getText().toString()
        val password_r = et_password.getText().toString()

        println(ktp_r+" "+nama_r+" "+notelp_r+" "+email_r+" "+username_r+" "+password_r)

        AndroidNetworking.post(ApiKoneksi.UPDATE)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val bundle = intent.extras
        val id_penumpang = bundle?.get("ktp").toString()

        intent = Intent(this, CariTiket::class.java)
        intent.putExtra("ktp",id_penumpang)
        startActivity(intent)
        return true
    }
}
