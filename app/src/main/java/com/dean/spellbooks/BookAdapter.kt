package com.dean.spellbooks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(context: Context, val booklist: ArrayList<BookModelClass>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private lateinit var selectListener: onItemSelectedListener

    interface onItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    fun setOnItemSelectedListener(selectListener: onItemSelectedListener) {
        this.selectListener = selectListener
    }

    class ViewHolder(itemView: View, selectListener: onItemSelectedListener) : RecyclerView.ViewHolder(itemView) {
        val tvListBookTitle: TextView = itemView.findViewById(R.id.tvListBookTitle)
        val tvListBookAuthor: TextView = itemView.findViewById(R.id.tvListBookAuthor)
        val cbBookRead: CheckBox = itemView.findViewById(R.id.cbBookRead)

        init {
            itemView.setOnClickListener {
                selectListener.onItemSelected(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_book_items,parent,false)
        return ViewHolder(view, selectListener)
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        val book: BookModelClass = booklist[position]
        holder.tvListBookTitle.text = book.bookTitle
        holder.tvListBookAuthor.text = book.bookAuthor

        if (PreferenceManager.getDefaultSharedPreferences(holder.cbBookRead.context).getBoolean("cbBookRead", true)) {
            holder.cbBookRead.isChecked = true
            MainActivity.checkCount?.plus(1)
        }
        else if(PreferenceManager.getDefaultSharedPreferences(holder.cbBookRead.context).getBoolean("cbBookRead", false)) {
            holder.cbBookRead.isChecked = false
            if (MainActivity.checkCount!! > 0) {
                MainActivity.checkCount?.minus(1)
            }
        }



        holder.cbBookRead.setOnClickListener {

            if (holder.cbBookRead.isChecked) {
                MainActivity.checkCount?.plus(1)
                PreferenceManager.getDefaultSharedPreferences(holder.cbBookRead.context).edit()
                    .putBoolean("cbBookRead", true).apply()
            }
            else if (!holder.cbBookRead.isChecked) {
                PreferenceManager.getDefaultSharedPreferences(holder.cbBookRead.context).edit()
                    .putBoolean("cbBookRead", false).apply()
            }
        }
    }

    override fun getItemCount(): Int {
        return booklist.size
    }
}