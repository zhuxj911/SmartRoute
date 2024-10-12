package com.xazhuxj.smartroute.ui.points

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xazhuxj.smartroute.R
import com.xazhuxj.smartroute.databinding.FragmentPointsBinding
import com.xazhuxj.smartroute.models.Point

class PointsFragment : Fragment() {

    private var _binding: FragmentPointsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // return inflater.inflate(R.layout.fragment_points, container, false)

        val pointsViewModel = ViewModelProvider(this)[PointsViewModel::class.java]

        _binding = FragmentPointsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textViewCurve.text = arguments?.getString("smartroute_curve_data")

        val ptList =  arguments?.getParcelableArrayList<Point>("smartroute_point_list_data")
        binding.pointsListView.layoutManager = LinearLayoutManager(context)
        binding.pointsListView.adapter = PointAdapter(ptList!!)

        return root
    }
}

class PointAdapter(private val pointList: List<Point>) : RecyclerView.Adapter<PointAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.point_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pointList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(pointList[position]) {
            holder.knoTextView.text = String.format("%s", kNoInfo)
            holder.xTextView.text = String.format("%.3f", x)
            holder.yTextView.text = String.format("%.3f", y)
            holder.noteTextView.text = note
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val knoTextView: TextView = view.findViewById(R.id.point_kno)
        val xTextView: TextView = view.findViewById(R.id.point_x)
        val yTextView: TextView = view.findViewById(R.id.point_y)
        val noteTextView: TextView = view.findViewById(R.id.point_note)
    }
}
