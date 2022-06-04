package com.dean.spellbooks

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class BookAdapter(context: Context, private val bookList: ArrayList<BookModelClass>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private lateinit var selectListener: onItemSelectedListener
    private lateinit var dbHandler: DBHandler

    interface onItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    fun setOnItemSelectedListener(selectListener: onItemSelectedListener) {
        this.selectListener = selectListener
    }

    class ViewHolder(itemView: View, selectListener: onItemSelectedListener) : RecyclerView.ViewHolder(itemView) {
        val tvListBookTitle: TextView = itemView.findViewById(R.id.tvListBookTitle)
        val tvListBookAuthor: TextView = itemView.findViewById(R.id.tvListBookAuthor)
        val ivListBookImage: ImageView = itemView.findViewById(R.id.ivListBookImage)
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
        val book: BookModelClass = bookList[position]
        holder.tvListBookTitle.text = book.bookTitle
        holder.tvListBookAuthor.text = book.bookAuthor

        holder.cbBookRead.isChecked = book.readStatus == 1

        dbHandler = DBHandler(holder.itemView.context)

        val pictureByteArray: ByteArray? = dbHandler.getBookImage(book.bookID!!)
        val pictureBitmap: Bitmap = convertToBitmap(pictureByteArray!!)!!

        if (pictureBitmap != null)
            holder.ivListBookImage.setImageBitmap(pictureBitmap)

        holder.cbBookRead.setOnClickListener {

            if (holder.cbBookRead.isChecked) {
                book.readStatus = 1

                val wiz: UserModelClass = dbHandler.getWizard(book.bookOwner)!!
                wiz.recentBook = book.bookID

                dbHandler.updateWizardInformation(wiz)
                dbHandler.updateBookInformation(book)
            }
            else if (!holder.cbBookRead.isChecked) {
                book.readStatus = 0
                dbHandler.updateBookInformation(book)
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    private fun convertToBitmap(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }
}