package com.example.capitalmanagement.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capitalmanagement.adapter.AccountListAdapter
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.Category
import com.example.capitalmanagement.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddTransactionViewModel: ViewModel() {
    var accounts = MutableLiveData<ArrayList<Account>>()
    var newAccounts = arrayListOf<Account>()
    var accountsId = arrayListOf<String>()
    var selectedAccountId: String? = null
    lateinit var context: Context

    fun fetchAccounts() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/accounts")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    Log.d("Accounts", it.toString())
                    val account = it.getValue(Account::class.java)
                    Log.d("Accounts", "${account?.title} : ${account?.type} : ${account?.amount}")
                    if (account != null) {
                        addAccount(account)
                        accountsId.add(it.key.toString())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun addAccount(account: Account) {
        newAccounts.add(account)
        accounts.value = newAccounts
    }

    fun addTransaction(transaction: Transaction) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/transactions")
//        val childRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("/transactions/$uid/${ref.push().key}")
        val childRef: DatabaseReference = ref.child("${ref.push().key}")

        childRef.setValue(transaction)
            .addOnSuccessListener {
                Log.d("Add transaction", "Finally we saved the transaction to Firebase Database")
                Toast.makeText(context, "Finally we saved the transaction to Firebase Database", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("Add transaction", "Failed to save transaction to Firebase Database: ${it.message}")
                Toast.makeText(context, "Failed to save transaction to Firebase Database: ${it.message}", Toast.LENGTH_SHORT).show()
            }

        updateAccount(transaction.amount)
    }

    private fun updateAccount(amount: Int) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/accounts/$selectedAccountId")

        var account: Account?

        // Fetch account
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("Account", dataSnapshot.toString())
                account = dataSnapshot.getValue(Account::class.java)
                Log.d("Accounts", "${account?.title} : ${account?.amount}")

                // Increase account's amount
                account!!.amount += amount

                // Update account
                ref.setValue(account)
                    .addOnSuccessListener {
                        Log.d("Add transaction", "Finally we saved the transaction to Firebase Database")
                        Toast.makeText(context, "Finally we saved the transaction to Firebase Database", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.d("Add transaction", "Failed to save transaction to Firebase Database: ${it.message}")
                        Toast.makeText(context, "Failed to save transaction to Firebase Database: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}