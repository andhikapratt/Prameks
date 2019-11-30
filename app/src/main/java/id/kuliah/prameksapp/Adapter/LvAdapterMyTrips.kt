package id.kuliah.prameksapp.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import id.kuliah.prameksapp.MyTrips
import id.kuliah.prameksapp.R

class LvAdapterMyTrips(private val context: MyTrips,
                       private val nm_krtt: ArrayList<String>,
                       private val kd_booking: ArrayList<String>,
                       private val asal: ArrayList<String>,
                       private val tujuan: ArrayList<String>,
                       private val jam_brk: ArrayList<String>,
                       private val jam_smp: ArrayList<String>,
                       private val harga: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.lv_mytrips, kd_booking){
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.lv_mytrips, null, true)

        val nm_krt = rowView.findViewById(R.id.tv_nm_krt) as TextView
        val kd_book = rowView.findViewById(R.id.tv_kd_booking) as TextView
        val asl = rowView.findViewById(R.id.tv_asal) as TextView
        val tuj = rowView.findViewById(R.id.tv_tujuan) as TextView
        val jam_brkk = rowView.findViewById(R.id.tv_jambrk) as TextView
        val jam_smpp = rowView.findViewById(R.id.tv_jamsmp) as TextView
        val hargaa = rowView.findViewById(R.id.tv_hari) as TextView

        nm_krt.text = nm_krtt[position]
        kd_book.text = kd_booking[position]
        asl.text = asal[position]
        tuj.text = tujuan[position]
        jam_brkk.text = jam_brk[position]
        jam_smpp.text = jam_smp[position]
        hargaa.text = harga[position]

        return rowView
    }
}