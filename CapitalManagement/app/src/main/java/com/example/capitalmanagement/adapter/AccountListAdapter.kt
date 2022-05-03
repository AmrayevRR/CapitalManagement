package com.example.capitalmanagement.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.AccountListFragmentDirections
import com.example.capitalmanagement.CategoryListFragmentDirections
import com.example.capitalmanagement.R
import com.example.capitalmanagement.SecondActivity
import com.example.capitalmanagement.model.Account
import com.example.capitalmanagement.model.Category

class AccountListAdapter(
    private val mContext: Context,
    private val mLayoutResourceId: Int,
    accounts: List<Account>
) :
    ArrayAdapter<Account>(mContext, mLayoutResourceId, accounts) {
    private val account: MutableList<Account> = ArrayList(accounts)
    private var allCategories: List<Account> = ArrayList(accounts)

    override fun getCount(): Int {
        return account.size
    }
    override fun getItem(position: Int): Account {
        return account[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = (mContext as Activity).layoutInflater
            convertView = inflater.inflate(mLayoutResourceId, parent, false)
        }
        try {
            val account: Account = getItem(position)
            val categoryAutoCompleteView = convertView!!.findViewById<View>(R.id.dropDownItem) as TextView
            categoryAutoCompleteView.text = account.title
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }


}