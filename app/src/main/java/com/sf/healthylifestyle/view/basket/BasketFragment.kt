package com.sf.healthylifestyle.view.basket

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sf.healthylifestyle.databinding.FragmentBasketBinding
import com.sf.healthylifestyle.domain.models.Dish
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

class BasketFragment : Fragment() {

    private val basketAdapter = BasketAdapter()

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private lateinit var basketViewModel: BasketViewModel

    @Inject
    lateinit var basketViewModelFactory: BasketViewModel.Factory

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        println("onViewCreated BasketFragment")

        basketViewModel =
            ViewModelProvider(this, basketViewModelFactory)[BasketViewModel::class.java]

        binding.rvBasket.adapter = basketAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            basketViewModel.dishes.collect {

                println("dishes collect $it")
                initRV(it)
            }
        }

        val textToastBasket = "Ваш заказ оформлен!"


        binding.btnArrange.setOnClickListener {
            basketAdapter.setData(listOf())
            basketViewModel.delBasket()
            Toast.makeText(activity, textToastBasket, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initRV(data: List<Dish>) {

        basketAdapter.setData(data)

    }
}