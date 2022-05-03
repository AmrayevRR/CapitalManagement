package com.example.capitalmanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.adapter.CategoryListAdapter
import com.example.capitalmanagement.model.Category


/**
 * A simple [Fragment] subclass.
 * Use the [CategoryListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryListFragment : Fragment() {

    val args: CategoryListFragmentArgs by navArgs()
    lateinit var categoryType: String

    lateinit var categoryTypeTextView: TextView
    lateinit var categoryRecyclerView: RecyclerView

    lateinit var expenseCategories: ArrayList<Category>
    lateinit var incomeCategories: ArrayList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category_list, container, false)

        categoryType = args.categoryType

        categoryTypeTextView = view.findViewById(R.id.category_type_text_view)
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view)

        categoryTypeTextView.text = "$categoryType category"

//        initExpenseCategories()
//        initIncomeCategories()

//        var categoryListAdapter: CategoryListAdapter
//        if (categoryType == "Expense") {
//            categoryListAdapter = CategoryListAdapter(expenseCategories, requireContext())
//        }
//        else {
//            categoryListAdapter = CategoryListAdapter(incomeCategories, requireContext())
//        }
//        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//
//        categoryRecyclerView.layoutManager = layoutManager
//        categoryRecyclerView.adapter = categoryListAdapter

        return view
    }

//    private fun initExpenseCategories() {
//        expenseCategories = ArrayList<Category>()
//        expenseCategories.add(Category("Food & Drinks"))
//        expenseCategories.add(Category("Shopping"))
//        expenseCategories.add(Category("Housing"))
//        expenseCategories.add(Category("Transportation"))
//        expenseCategories.add(Category("Life & Entertainment"))
//        expenseCategories.add(Category("Education"))
//        expenseCategories.add(Category("Taxes"))
//        expenseCategories.add(Category("Fine"))
//    }
//
//    private fun initIncomeCategories() {
//        incomeCategories = ArrayList<Category>()
//        incomeCategories.add(Category("Salary"))
//        incomeCategories.add(Category("Present"))
//        incomeCategories.add(Category("Rental"))
//        incomeCategories.add(Category("Sale"))
//        incomeCategories.add(Category("Refund"))
//        incomeCategories.add(Category("Scholarship"))
//    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryListFragment().apply {

            }
    }
}