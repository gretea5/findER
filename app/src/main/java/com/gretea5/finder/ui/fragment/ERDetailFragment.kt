package com.gretea5.finder.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gretea5.finder.databinding.FragmentErDetailBinding

class ERDetailFragment : Fragment() {
    private var _binding : FragmentErDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErDetailBinding.inflate(inflater)
        return binding.root
    }
}