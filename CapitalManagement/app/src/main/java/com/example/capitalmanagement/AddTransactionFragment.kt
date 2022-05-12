package com.example.capitalmanagement

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.capitalmanagement.adapter.AccountListAdapter
import com.example.capitalmanagement.adapter.CategoryListAdapter
import com.example.capitalmanagement.databinding.FragmentAddTransactionBinding
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.Category
import com.example.capitalmanagement.model.Transaction
import com.example.capitalmanagement.viewModel.AddTransactionViewModel
import com.example.capitalmanagement.viewModel.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddTransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTransactionFragment : Fragment(), View.OnClickListener {

    private var transactionType = "Expense"
    private var selectedAccountId: String? = null

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var  viewModel: AddTransactionViewModel

    private val args: AddTransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        initUI()
        initToggleGroup()
        initTimePicker()
        initDatePicker()
        initCategoryChooseButton()
        initAccountChooseButton()
        bindViewModel()

        return binding.root
    }

    private fun bindViewModel() {
        initViewModel()
        observeAccountsData()
        if (viewModel.newAccounts.isEmpty()) {
            viewModel.fetchAccounts()
        }
    }

    private fun initViewModel() {
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AddTransactionViewModel::class.java)
        viewModel.context = requireContext()
    }

    private fun observeAccountsData() {
        viewModel.accounts.observe(requireActivity(), androidx.lifecycle.Observer {
            val adapter = binding.accountAutoCompleteTextView.adapter as AccountListAdapter
            adapter.update(it)
            val autoCompleteTextView = binding.accountAutoCompleteTextView
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                var selectedAccount = parent.getItemAtPosition(position) as Account
                viewModel.selectedAccountId = viewModel.accountsId[position]
//                selectedAccountId = viewModel.accountsId[position]
                autoCompleteTextView.setText(selectedAccount.title)
            }
        })
    }

    private fun initUI() {
        binding.expenseButton.setOnClickListener(this)
        binding.incomeButton.setOnClickListener(this)
        binding.dateButton.setOnClickListener(this)
        binding.timeButton.setOnClickListener(this)
        binding.chooseAccountButton.setOnClickListener(this)
        binding.chooseCategoryButton.setOnClickListener(this)
        binding.addButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)
        binding.button0.setOnClickListener(this)
        binding.button1.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
        binding.button3.setOnClickListener(this)
        binding.button4.setOnClickListener(this)
        binding.button5.setOnClickListener(this)
        binding.button6.setOnClickListener(this)
        binding.button7.setOnClickListener(this)
        binding.button8.setOnClickListener(this)
        binding.button9.setOnClickListener(this)
    }

    private fun initToggleGroup() {
        binding.materialButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val amountTextView = binding.amountTextView
                when(checkedId) {
                    R.id.expense_button -> {
                        amountTextView.text = "-0"
                        amountTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.system_red))
                        transactionType = "Expense"
                    }
                    R.id.income_button -> {
                        amountTextView.text = "0"
                        amountTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.system_green))
                        transactionType = "Income"
                    }
                }
//                binding.chooseCategoryImageView.setImageResource(R.drawable.question)
                initCategoryChooseButton()
            }
        }
    }

    private fun initTimePicker() {
        var timeFormat = SimpleDateFormat("HH:mm")
        val now = Calendar.getInstance()
        binding.timeButton.text = timeFormat.format(now.time)
    }

    private fun initDatePicker() {
        var dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val now = Calendar.getInstance()
        binding.dateButton.text = dateFormat.format(now.time)
    }

    private fun initCategoryChooseButton() {
        val categories = ArrayList<Category>()
        if (transactionType == "Expense") {
            categories.add(Category( "Food & Drinks"))
            categories.add(Category( "Shopping"))
            categories.add(Category( "Housing"))
            categories.add(Category( "Transportation"))
            categories.add(Category( "Life & Entertainment"))
            categories.add(Category( "Education"))
            categories.add(Category( "Taxes"))
        }
        else {
            categories.add(Category( "Salary"))
            categories.add(Category( "Present"))
            categories.add(Category( "Rental"))
            categories.add(Category( "Sale"))
            categories.add(Category( "Refund"))
            categories.add(Category( "Scholarship"))
        }
        val arrayAdapter = CategoryListAdapter(requireContext(), R.layout.dropdown_item, categories)
        val autoCompleteTextView = binding.categoryAutoCompleteTextView
        autoCompleteTextView.setAdapter(arrayAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            var selectedCategory = parent.getItemAtPosition(position) as Category
            autoCompleteTextView.setText(selectedCategory.title)
        }
    }

    private fun initAccountChooseButton() {
        val accounts = ArrayList<Account>()
        val accountsId = ArrayList<String>()

        val arrayAdapter = AccountListAdapter(requireContext(), R.layout.dropdown_item, accounts)
        val autoCompleteTextView = binding.accountAutoCompleteTextView
        autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9 -> {
                val amountTextView = binding.amountTextView
                val button: Button = v as Button
                if (amountTextView.text == "0") {
                    amountTextView.text = button.text
                }
                else if (amountTextView.text == "-0") {
                    amountTextView.text = "-${button.text}"
                }
                else {
                    amountTextView.text = amountTextView.text.toString() + button.text
                }
            }
            R.id.add_button -> {
                val transaction = Transaction(
                    binding.categoryAutoCompleteTextView.text.toString(),
                    binding.amountTextView.text.toString().toInt(),
                    binding.descriptionEditText.text.toString(),
                    "${binding.dateButton.text} ${binding.timeButton.text}",
                    viewModel.selectedAccountId!!
                )
                viewModel.addTransaction(transaction)
            }
            R.id.delete_button -> {
                val amountTextView = binding.amountTextView
                if (amountTextView.text != "0" && amountTextView.text != "-0") {
                    if (amountTextView.text.length == 2 && amountTextView.text.first() == '-') {
                        amountTextView.text = "-0"
                    }
                    else if (amountTextView.text.length == 1) {
                        amountTextView.text = "0"
                    }
                    else {
                        amountTextView.text = amountTextView.text.dropLast(1)
                    }
                }
            }
            R.id.date_button -> {
                var dateFormat = SimpleDateFormat("yyyy/MM/dd")
                val now = Calendar.getInstance()
                val datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    binding.dateButton.text = dateFormat.format(selectedDate.time)
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }
            R.id.time_button -> {
                var timeFormat = SimpleDateFormat("HH:mm")

                val now = Calendar.getInstance()
                try {
                    val date = timeFormat.parse(binding.timeButton.text.toString())
                    now.time = date
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val timePicker = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)
                    binding.timeButton.text = timeFormat.format(selectedTime.time)
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
                timePicker.show()
            }
            R.id.choose_category_button -> {
//                val action = AddTransactionFragmentDirections.navigateToCategoryListFragment(transactionType)
//                Navigation.findNavController(binding.root).navigate(action)
            }
            R.id.choose_account_button -> {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putString("amount", binding.amountTextView.text.toString())
                    apply()
                }

//                val action = AddTransactionFragmentDirections.navigateToAccountListFragment()
//                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }



    companion object {

        fun newInstance(): AddTransactionFragment {
            return AddTransactionFragment()
        }
    }
}