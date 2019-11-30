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
        val penumm = bundle?.get("id_penumpang").toString()

        AndroidNetworking.post(ApiKoneksi.READ5)
            .addBodyParameter("id_akun",penumm)
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
                    val list_f = arrayListOf<String>()
                    val list_g = arrayListOf<String>()

                    for(i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)

                        list_a.add((jsonObject.getString("namakrt")).toString())
                        list_b.add((jsonObject.getString("kode_bayar")).toString())
                        list_c.add((jsonObject.getString("asal")).toString())
                        list_d.add((jsonObject.getString("tujuan")).toString())
                        list_e.add((jsonObject.getString("jam_brk")).toString())
                        list_f.add((jsonObject.getString("jam_smp")).toString())
                        list_g.add((jsonObject.getString("harga")).toString())

                        if (jsonArray?.length()-1 == i) {
                            loading.dismiss()
                            val adapter = LvAdapterMyTrips(this@MyTrips, list_a, list_b, list_c, list_d, list_e, list_f, list_g)
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
