package com.example.capitalmanagement.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capitalmanagement.MyApplication
import com.example.capitalmanagement.adapter.UserAccountListAdapter
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.ExchangePost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeRatesViewModel: ViewModel() {
    var accounts = MutableLiveData<ArrayList<Account>>()
    var newAccounts = ArrayList<Account>()
    var exchangePost = MutableLiveData<ExchangePost?>()
    var newExchangePost: ExchangePost? = null

    fun fetchAccounts() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/accounts")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    Log.d("Accounts", it.toString())
                    val account = it.getValue(Account::class.java)
                    Log.d("Accounts", "${account?.title} : ${account?.amount}")
                    if (account != null) {
//                        accounts.add(account)
                        add(account)
                    }
                }
//                val accountListAdapter = UserAccountListAdapter(accounts, requireContext())
//                accountsRecyclerView.adapter = accountListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun add(account: Account) {
        newAccounts.add(account)
        accounts.value = newAccounts
    }

    fun fetchExchangePost() {
        val exchangeApiService = MyApplication.instance.getExchangeApiService()!!

        exchangeApiService.getExchangePost().enqueue(object: Callback<ExchangePost> {
            override fun onFailure(call: Call<ExchangePost>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

            override fun onResponse(call: Call<ExchangePost>, response: Response<ExchangePost>) {
//                exchangePost = response.body()!!
                updateExchangePost(response.body()!!)
                Log.d("Response", response.body().toString())
//                configureRates()
            }
        })
    }

    private fun updateExchangePost(exchangePost: ExchangePost) {
        newExchangePost = exchangePost
        this.exchangePost.value = newExchangePost
    }
}