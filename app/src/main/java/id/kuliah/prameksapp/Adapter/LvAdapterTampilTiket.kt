package id.kuliah.prameksapp.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import id.kuliah.prameksapp.R
import id.kuliah.prameksapp.TampilTiket

class LvAdapterTampilTiket(private val context: TampilTiket,
                           private val id: ArrayList<String>,
                           private val nama_krt: ArrayList<String>,
                           private val asal: ArrayList<String>,
                           private val tujuan: ArrayList<String>,
                           private val jam_brk: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.lv_tiket, id){
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.lv_tiket, null, true)

        val idnya = rowView.findViewById(R.id.txt_id) as TextView
        val nm_krt = rowView.findViewById(R.id.txt_nm_krt) as TextView
        val asl = rowView.findViewById(R.id.txt_asal) as TextView
        val tuj = rowView.findViewById(R.id.txt_tujuan) as TextView
        val jam_brkt = rowView.findViewById(R.id.txt_jam_brk) as TextView

        idnya.text = id[position]
        nm_krt.text = nama_krt[position]
        asl.text = asal[position]
        tuj.text = tujuan[position]
        jam_brkt.text = jam_brk[position]

        return rowView
    }
}