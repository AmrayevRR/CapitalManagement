package com.example.capitalmanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.adapter.TransactionListAdapter
import com.example.capitalmanagement.model.Transaction
import com.example.capitalmanagement.viewModel.TransactionsViewModel
import com.example.capitalmanagement.viewModel.MainViewModelFactory


/**
 * A simple [Fragment] subclass.
 * Use the [TransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionsFragment : Fragment() {

    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var transactionListAdapter: TransactionListAdapter

    private lateinit var  viewModel: TransactionsViewModel

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

        bindViewModel()
        initTransactionsRecyclerView()

        return view
    }

    private fun bindViewModel() {
        initViewModel()
        observeData()
        if (viewModel.newTransactions.isEmpty()) {
            viewModel.fetchTransactions()
        }
    }

    private fun initViewModel() {
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(TransactionsViewModel::class.java)
    }

    private fun observeData() {
        viewModel.transactions.observe(requireActivity(), Observer {
            transactionListAdapter.update(it)
        })
    }

    private fun initTransactionsRecyclerView() {
        var transactions = ArrayList<Transaction>()
        transactions.add(Transaction("Loading", 0, "", "", ""))

        transactionListAdapter = TransactionListAdapter(transactions, requireContext())
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        transactionsRecyclerView.adapter = transactionListAdapter
        transactionsRecyclerView.layoutManager = layoutManager
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