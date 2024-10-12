package com.xazhuxj.smartroute.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xazhuxj.smartroute.R
import com.xazhuxj.smartroute.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Bind layout with ViewModel
        binding.viewmodel = homeViewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        view.findViewById<Button>(R.id.button_xy_anypoint)?.setOnClickListener {
            //单点坐标计算
            //用MVVM方式
            //取数据到myViewModel中
            //计算
            binding.viewmodel?.onCalculateSinglePoint()

            val bundle = Bundle()
            bundle.putString("smartroute_curve_data", binding.viewmodel!!.route.toString())

            // 向PointListActivity传入数据pts, pts 是 Point的列表
            bundle.putParcelableArrayList("smartroute_point_list_data", binding.viewmodel!!.ptList)
            findNavController().navigate(R.id.action_nav_home_to_points, bundle, options)
        }

        view.findViewById<Button>(R.id.button_xy_allpoints)?.setOnClickListener {

            binding.viewmodel?.onCalculateAllPoints()

            val bundle = Bundle()
            bundle.putString("smartroute_curve_data", binding.viewmodel!!.route.toString())
            bundle.putParcelableArrayList("smartroute_point_list_data", binding.viewmodel!!.ptList)

            findNavController().navigate(R.id.action_nav_home_to_points, bundle, options)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
