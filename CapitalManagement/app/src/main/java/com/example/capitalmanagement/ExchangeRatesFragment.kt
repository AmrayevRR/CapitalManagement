package com.example.capitalmanagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.adapter.TransactionListAdapter
import com.example.capitalmanagement.adapter.UserAccountListAdapter
import com.example.capitalmanagement.api.ExchangeApiService
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.ExchangePost
import com.example.capitalmanagement.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [ExchangeRatesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExchangeRatesFragment : Fragment() {

    lateinit var accountsRecyclerView: RecyclerView
    lateinit var accounts: ArrayList<Account>

    lateinit var usdRateTextView: TextView
    lateinit var eurRateTextView: TextView
    lateinit var rubRateTextView: TextView
    lateinit var kgsRateTextView: TextView
    lateinit var gbpRateTextView: TextView
    lateinit var cnyRateTextView: TextView

    lateinit var exchangeApiService: ExchangeApiService

    lateinit var exchangePost: ExchangePost

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

        accountsRecyclerView = view.findViewById(R.id.accounts_recycler_view)

        initAccountsRecyclerView()

        fetchExchangePost()

        return view
    }

    private fun initUI(view: View) {
        usdRateTextView = view.findViewById(R.id.usd_rate_text_view)
        eurRateTextView = view.findViewById(R.id.eur_rate_text_view)
        rubRateTextView = view.findViewById(R.id.rub_rate_text_view)
        kgsRateTextView = view.findViewById(R.id.kgs_rate_text_view)
        gbpRateTextView = view.findViewById(R.id.gbp_rate_text_view)
        cnyRateTextView = view.findViewById(R.id.cny_rate_text_view)
    }

    private fun fetchExchangePost() {
        exchangeApiService = MyApplication.instance.getExchangeApiService()!!

        exchangeApiService.getExchangePost().enqueue(object: Callback<ExchangePost> {
            override fun onFailure(call: Call<ExchangePost>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

            override fun onResponse(call: Call<ExchangePost>, response: Response<ExchangePost>) {
                exchangePost = response.body()!!
                Log.d("Response", response.body().toString())
                configureRates()
            }
        })
    }

    private fun configureRates() {
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
        accounts = ArrayList<Account>()
        accounts.add(Account("Cash", "caspi", 20000))
        fetchAccounts()

        val accountListAdapter = UserAccountListAdapter(accounts, requireContext())
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        accountsRecyclerView.adapter = accountListAdapter
        accountsRecyclerView.layoutManager = layoutManager
    }

    private fun fetchAccounts() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/accounts")

        val accounts = ArrayList<Account>()

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    Log.d("Accounts", it.toString())
                    val account = it.getValue(Account::class.java)
                    Log.d("Accounts", "${account?.title} : ${account?.amount}")
                    if (account != null) {
                        accounts.add(account)
                    }
                }
                val accountListAdapter = UserAccountListAdapter(accounts, requireContext())
                accountsRecyclerView.adapter = accountListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
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