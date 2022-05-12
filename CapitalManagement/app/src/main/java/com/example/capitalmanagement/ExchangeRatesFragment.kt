package com.example.capitalmanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.adapter.UserAccountListAdapter
import com.example.capitalmanagement.api.ExchangeApiService
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.ExchangePost
import com.example.capitalmanagement.viewModel.ExchangeRatesViewModel
import com.example.capitalmanagement.viewModel.MainViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [ExchangeRatesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExchangeRatesFragment : Fragment() {

    private lateinit var accountsRecyclerView: RecyclerView

    private lateinit var usdRateTextView: TextView
    private lateinit var eurRateTextView: TextView
    private lateinit var rubRateTextView: TextView
    private lateinit var kgsRateTextView: TextView
    private lateinit var gbpRateTextView: TextView
    private lateinit var cnyRateTextView: TextView

    private lateinit var  viewModel: ExchangeRatesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exchange_rates, container, false)

        initUI(view)
        initAccountsRecyclerView()
        bindViewModel()

        return view
    }

    private fun initUI(view: View) {
        accountsRecyclerView = view.findViewById(R.id.accounts_recycler_view)
        usdRateTextView = view.findViewById(R.id.usd_rate_text_view)
        eurRateTextView = view.findViewById(R.id.eur_rate_text_view)
        rubRateTextView = view.findViewById(R.id.rub_rate_text_view)
        kgsRateTextView = view.findViewById(R.id.kgs_rate_text_view)
        gbpRateTextView = view.findViewById(R.id.gbp_rate_text_view)
        cnyRateTextView = view.findViewById(R.id.cny_rate_text_view)
    }

    private fun configureRates(exchangePost: ExchangePost) {
        usdRateTextView.text = roundDouble(exchangePost.rates.KZT / exchangePost.rates.USD).toString()
        eurRateTextView.text = roundDouble(exchangePost.rates.KZT).toString()
        rubRateTextView.text = roundDouble(exchangePost.rates.KZT / exchangePost.rates.RUB).toString()
        kgsRateTextView.text = roundDouble(exchangePost.rates.KZT / exchangePost.rates.KGS).toString()
        gbpRateTextView.text = roundDouble(exchangePost.rates.KZT / exchangePost.rates.GBP).toString()
        cnyRateTextView.text = roundDouble(exchangePost.rates.KZT / exchangePost.rates.CNY).toString()
    }

    private fun roundDouble(double: Double): Double {
        return (double * 10000).toInt() / 10000.0
    }

    private fun initAccountsRecyclerView() {
        var accounts = ArrayList<Account>()
        accounts.add(Account("Cash", "caspi", 20000))

        val accountListAdapter = UserAccountListAdapter(accounts, requireContext())
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        accountsRecyclerView.adapter = accountListAdapter
        accountsRecyclerView.layoutManager = layoutManager
    }

    private fun bindViewModel() {
        initViewModel()
        observeAccountsData()
        observeExchangeData()
        if (viewModel.newAccounts.isEmpty()) {
            viewModel.fetchAccounts()
        }
        viewModel.fetchExchangePost()
    }

    private fun initViewModel() {
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ExchangeRatesViewModel::class.java)
    }

    private fun observeAccountsData() {
        viewModel.accounts.observe(requireActivity(), Observer {
            val adapter = accountsRecyclerView.adapter as UserAccountListAdapter
            adapter.update(it)
        })
    }

    private fun observeExchangeData() {
        viewModel.exchangePost.observe(requireActivity(), Observer {
            configureRates(it!!)
        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExchangeRatesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}