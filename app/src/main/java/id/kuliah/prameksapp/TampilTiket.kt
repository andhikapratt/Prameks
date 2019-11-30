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
import id.kuliah.prameksapp.Adapter.LvAdapterTampilTiket
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_tampil_tiket.*
import org.json.JSONObject

class TampilTiket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampil_tiket)

        //Menampilkan action bar dengan back button
        val actionbar = supportActionBar
        actionbar!!.title = "Tiket Kereta"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //memanggil function carisepur()
        carisepur()
    }

    //Aksi untuk back button pada action bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val bundle = intent.extras
        val id_penumpang = bundle?.get("id_penumpang").toString()

        intent = Intent(this, CariTiket::class.java)
        intent.putExtra("id_penumpang",id_penumpang)
        startActivity(intent)
        return true
    }

    override fun onResume(){

        //ketika keluar aplikasi, maka saat membukanya lagi,
        //aplikasi akan meload ulang data dari database
        super.onResume()
        TampilSepur()
    }

    private fun carisepur(){
        lvnya.setOnItemClickListener{adapterView, view, position, id ->

            //ambil id posisi dari listview yang diklik
            val idnya = adapterView.getItemAtPosition(position)
            val a = idnya.toString()
            val bundle = intent.extras
            val asl = bundle?.get("asal").toString()
            val tuj = bundle?.get("tuju").toString()
            val id_penumpang = bundle?.get("id_penumpang").toString()

            //mengirim id posisi lv ke class ReviewTiket
            intent = Intent(this, ReviewTiket::class.java)
            intent.putExtra("asal",asl)
            intent.putExtra("tuju",tuj)
            intent.putExtra("id_kereta",a)
            intent.putExtra("id_penumpang",id_penumpang)
            startActivity(intent)
        }
    }

    private fun TampilSepur(){

        //menampilkan loading
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        //mengambil inputan asal dan tujuan dari class CariTiket
        val bundle = intent.extras
        val asl = bundle?.get("asal").toString()
        val tuj = bundle?.get("tuju").toString()

        //mengkoneksikan ke database
        AndroidNetworking.post(ApiKoneksi.READ)

            //mengirim asal dan tujuan ke read.php untuk menampilkan
            //tiket berdasarkan asal dan tujuan
            .addBodyParameter("asal",asl)
            .addBodyParameter("tujuan",tuj)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    //cek data dari json dengan variable 'result'
                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()

                    //deklarasi list hasil ambil data dri json
                    //untuk menampung data dari database
                    val list_a = arrayListOf<String>()
                    val list_b = arrayListOf<String>()
                    val list_c = arrayListOf<String>()
                    val list_d = arrayListOf<String>()
                    val list_e = arrayListOf<String>()

                    //ambil data dri json
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
                            val adapter = LvAdapterTampilTiket(this@TampilTiket, list_e, list_a, list_b, list_c, list_d)
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
