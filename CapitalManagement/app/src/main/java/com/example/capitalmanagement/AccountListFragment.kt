package com.example.capitalmanagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.adapter.AccountListAdapter
import com.example.capitalmanagement.adapter.TransactionListAdapter
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 * Use the [AccountListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountListFragment : Fragment() {

    lateinit var accountRecyclerView: RecyclerView

    lateinit var accounts: ArrayList<Account>
    lateinit var transactionAmount: String
    lateinit var transactionDescription: String
    lateinit var transactionDate: String
    lateinit var transactionTime: String
    lateinit var transactionCategory: String

    val args: AccountListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_list, container, false)

        transactionAmount = args.transactionAmount
        transactionDescription = args.transactionDescription
        transactionDate = args.transactionDate
        transactionTime = args.transactionTime
        transactionCategory = args.transactionCategory

        accountRecyclerView = view.findViewById(R.id.account_recycler_view)

        initAccounts()

        initAccountRecyclerView()

        fetchAccounts()

        return view
    }

    private fun initAccounts() {
        accounts = ArrayList<Account>()
        accounts.add(Account("Loading", "loading", 0))
    }

    private fun initAccountRecyclerView() {
//        val accountListAdapter = AccountListAdapter(accounts,
//            requireContext())
//        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//
//        accountRecyclerView.layoutManager = layoutManager
//        accountRecyclerView.adapter = accountListAdapter
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
                    Log.d("Accounts", "${account?.title} : ${account?.type} : ${account?.amount}")
                    if (account != null) {
                        accounts.add(account)
                    }
                }
//                val accountListAdapter = AccountListAdapter(accounts,
//                    requireContext())
//                accountRecyclerView.adapter = accountListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}