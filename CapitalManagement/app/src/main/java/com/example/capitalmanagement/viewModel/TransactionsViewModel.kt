package com.example.capitalmanagement.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capitalmanagement.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TransactionsViewModel: ViewModel() {
    var transactions = MutableLiveData<ArrayList<Transaction>>()
    val newTransactions = arrayListOf<Transaction>()

    fun fetchTransactions() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/transactions")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    Log.d("Transactions", it.toString())
                    val transaction = it.getValue(Transaction::class.java)
                    Log.d("Transactions", "${transaction?.category} : ${transaction?.amount}")
                    if (transaction != null) {
//                        transactions.add(0, transaction)
                        add(transaction)
                    }
                }
//                transactionListAdapter.update(transactions)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun add(transaction: Transaction) {
        newTransactions.add(0, transaction)
        transactions.value = newTransactions
    }
}