package com.example.capitalmanagement.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.capitalmanagement.R
import com.example.capitalmanagement.model.Transaction

class TransactionListAdapter(val transactions: ArrayList<Transaction>, val context: Context): RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)

        return TransactionViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions.get(position)

        holder.categoryTextView.text = transaction.category
        holder.amountTextView.text = transaction.amount.toString()
        holder.descriptionTextView.text = transaction.description
        holder.dateTextView.text = transaction.date

        if (transaction.amount < 0) {
//            holder.amountTextView.setTextColor(ContextCompat.getColor(holder, R.color.system_red))
            holder.amountTextView.setTextColor(Color.parseColor("#e84e3d"))
        }
        else {
            holder.amountTextView.setTextColor(Color.parseColor("#69c366"))
        }

        when(transaction.category) {
            "Education", "Scholarship" -> {
                holder.imageView.setImageResource(R.drawable.education)
            }
            "Salary" -> {
                holder.imageView.setImageResource(R.drawable.salary)
            }
            "Present" -> {
                holder.imageView.setImageResource(R.drawable.present)
            }
            "Rental", "Housing" -> {
                holder.imageView.setImageResource(R.drawable.housing)
            }
            "Sale" -> {
                holder.imageView.setImageResource(R.drawable.sale)
            }
            "Refund" -> {
                holder.imageView.setImageResource(R.drawable.refund)
            }
            "Food & Drinks" -> {
                holder.imageView.setImageResource(R.drawable.food)
            }
            "Shopping" -> {
                holder.imageView.setImageResource(R.drawable.shopping)
            }
            "Transportation" -> {
                holder.imageView.setImageResource(R.drawable.transport)
            }
            "Life & Entertainment" -> {
                holder.imageView.setImageResource(R.drawable.microphone)
            }
            "Taxes" -> {
                holder.imageView.setImageResource(R.drawable.taxes)
            }
            "Fine" -> {
                holder.imageView.setImageResource(R.drawable.fine)
            }
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class TransactionViewHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView) {
        var categoryTextView: TextView = itemView.findViewById(R.id.category_text_view)
        var amountTextView: TextView = itemView.findViewById(R.id.amount_text_view)
        var descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)
        var dateTextView: TextView = itemView.findViewById(R.id.date_text_view)
        var imageView: ImageView = itemView.findViewById(R.id.image_view)

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, "${categoryTextView.text}: ${amountTextView.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}