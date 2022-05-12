package com.example.capitalmanagement.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.R
import com.example.capitalmanagement.model.Account

class UserAccountListAdapter(var accounts: ArrayList<Account>, val context: Context): RecyclerView.Adapter<UserAccountListAdapter.AccountViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.account_item, parent, false)

        return AccountViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accounts.get(position)

        holder.titleTextView.text = account.title
        holder.amountTextView.text = account.amount.toString()

        when(account.type) {
            "cash" -> {
                holder.imageView.setImageResource(R.drawable.cash)
            }
            "card" -> {
                holder.imageView.setImageResource(R.drawable.card)
            }
        }
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    fun update(data: ArrayList<Account>) {
        accounts = data
        notifyDataSetChanged()
    }

    class AccountViewHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.account_title_text_view)
        var amountTextView: TextView = itemView.findViewById(R.id.account_amount_text_view)
        var imageView: ImageView = itemView.findViewById(R.id.image_view)

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, "${titleTextView.text}: ${amountTextView.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}