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
import id.kuliah.prameksapp.Adapter.LvAdapterMyTrips
import id.kuliah.prameksapp.Adapter.LvAdapterTampilTiket
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_tampil_tiket.*
import org.json.JSONObject

class MyTrips : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trips)
    }

    override fun onResume(){
        super.onResume()
        TampilSepur()
    }

    private fun TampilSepur(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        val bundle = intent.extras
        val asl = bundle?.get("asal").toString()
        val tuj = bundle?.get("tuju").toString()

        AndroidNetworking.post(ApiKoneksi.READ)
            .addBodyParameter("asal",asl)
            .addBodyParameter("tujuan",tuj)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()

                    val list_a = arrayListOf<String>()
                    val list_b = arrayListOf<String>()
                    val list_c = arrayListOf<String>()
                    val list_d = arrayListOf<String>()
                    val list_e = arrayListOf<String>()

                    for(i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)
                        list_a.add((jsonObject.getString("namakrt")).toString())
                        list_b.add((jsonObject.getString("asal")).toString())
                        list_c.add((jsonObject.getString("tujuan")).toString())
                        list_d.add((jsonObject.getString("jam_brk")).toString())
                        list_e.add((jsonObject.getString("id")).toString())

                        if (jsonArray?.length()-1 == i) {
                            loading.dismiss()

                            //mengirim data" ke adapter LvAdapterTampilTiket untuk menampilkan data ke listview
                            val adapter = LvAdapterMyTrips(this@MyTrips, list_e, list_a, list_b, list_c, list_d)
                            lvnya.adapter = adapter
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
