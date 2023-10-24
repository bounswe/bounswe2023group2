package com.example.disasterresponseplatform.ui.activity.need

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentAddNeedBinding
import com.example.disasterresponseplatform.databinding.FragmentHomePageBinding

class AddNeedFragment : Fragment() {

    private lateinit var binding: FragmentAddNeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddNeedBinding.inflate(inflater, container,false)

        // Get the array of need categories from resources
        val needCategory = resources.getStringArray(R.array.need_categories)
        val needFoodType = resources.getStringArray(R.array.need_food_types)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val needCategoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, needCategory)
        val needFoodTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, needFoodType)

        // Specify the layout to use when the list of choices appears
        needCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        needFoodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        val spNeedCategory = binding.spNeedCategory
        spNeedCategory.adapter = needCategoryAdapter

        val spNeedFoodType = binding.spNeedFoodTypes
        spNeedFoodType.adapter = needFoodTypeAdapter

        return binding.root
    }
}
