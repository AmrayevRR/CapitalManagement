package com.example.capitalmanagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.adapter.TransactionListAdapter
import com.example.capitalmanagement.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


/**
 * A simple [Fragment] subclass.
 * Use the [TransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionsFragment : Fragment() {

    lateinit var transactionsRecyclerView: RecyclerView
    lateinit var transactions: ArrayList<Transaction>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transactions, container, false)

        transactionsRecyclerView = view.findViewById(R.id.transactions_recycler_view)

        initTransactionsRecyclerView()

        return view
    }

    private fun initTransactionsRecyclerView() {
        transactions = ArrayList<Transaction>()
        transactions.add(Transaction("Loading", 0, "", "", ""))
        fetchTransactions()

        val transactionListAdapter = TransactionListAdapter(transactions, requireContext())
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        transactionsRecyclerView.adapter = transactionListAdapter
        transactionsRecyclerView.layoutManager = layoutManager
    }

    private fun fetchTransactions() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/transactions")

        val transactions = ArrayList<Transaction>()

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    Log.d("Transactions", it.toString())
                    val transaction = it.getValue(Transaction::class.java)
                    Log.d("Transactions", "${transaction?.category} : ${transaction?.amount}")
                    if (transaction != null) {
                        transactions.add(0, transaction)
//                        transactions.add(transaction)
                    }
                }
                val transactionListAdapter = TransactionListAdapter(transactions, requireContext())
                transactionsRecyclerView.adapter = transactionListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}