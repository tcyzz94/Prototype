package com.example.prototype.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.prototype.R

class HomeFragment : Fragment() {
    private lateinit var ivTimeTable: ImageView
    private lateinit var ivAddStudent: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivAddStudent = view.findViewById(R.id.iv_add_std)
        ivTimeTable = view.findViewById(R.id.iv_time_table)

        ivTimeTable.setOnClickListener {
            findNavController().navigate(R.id.timeTableFragment,null)
        }
    }


}