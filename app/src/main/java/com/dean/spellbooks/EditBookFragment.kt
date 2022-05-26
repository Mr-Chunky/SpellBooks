package com.dean.spellbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class EditBookFragment : Fragment(), View.OnClickListener {
    var navigationController: NavController? = null
    var dbHandler: DBHandler? = null

    // These UI elements will be programmed in pairs
    private lateinit var etEditBookNewValue: EditText; private lateinit var llEditBookButtonBar: LinearLayout
    private lateinit var tvEditBookTitle: TextView; private lateinit var btnEditBookTitle: Button
    private lateinit var tvEditBookAuthor: TextView; private lateinit var btnEditBookAuthor: Button
    private lateinit var tvEditBookTotalPages: TextView; private lateinit var btnEditBookTotalPages: Button
    private lateinit var tvEditBookGenre: TextView; private lateinit var btnEditBookGenre: Button
    private lateinit var tvEditBookPublisher: TextView; private lateinit var btnEditBookPublisher: Button
    private lateinit var tvEditBookYearPublished: TextView; private lateinit var btnEditBookYearPublished: Button
    private lateinit var tvEditBookISBN: TextView; private lateinit var btnEditBookISBN: Button
    private lateinit var tvEditBookStarRating: TextView; private lateinit var btnEditBookStarRating: Button
    private lateinit var btnEditBookCancel: Button; private lateinit var btnEditBookSave: Button
    private lateinit var btnEditBookReturn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_edit_book, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)
        dbHandler = DBHandler(requireContext())

        initialiseUIElements(view)

        setOptionalElementsVisibility(false)

        setUpTextViewTexts()

        setUpButtonClickListeners()


    }

    private fun initialiseUIElements(view: View) {
        etEditBookNewValue = view.findViewById(R.id.etEditBookNewValue); llEditBookButtonBar = view.findViewById(R.id.llEditBookButtonBar)
        tvEditBookTitle = view.findViewById(R.id.tvEditBookTitle); btnEditBookTitle = view.findViewById(R.id.btnEditBookTitle)
        tvEditBookAuthor = view.findViewById(R.id.tvEditBookAuthor); btnEditBookAuthor = view.findViewById(R.id.btnEditBookAuthor)
        tvEditBookTotalPages = view.findViewById(R.id.tvEditBookTotalPages); btnEditBookTotalPages = view.findViewById(R.id.btnEditBookTotalPages)
        tvEditBookGenre = view.findViewById(R.id.tvEditBookGenre); btnEditBookGenre = view.findViewById(R.id.btnEditBookGenre)
        tvEditBookPublisher = view.findViewById(R.id.tvEditBookPublisher); btnEditBookPublisher = view.findViewById(R.id.btnEditBookPublisher)
        tvEditBookYearPublished = view.findViewById(R.id.tvEditBookYearPublished); btnEditBookYearPublished = view.findViewById(R.id.btnEditBookYearPublished)
        tvEditBookISBN = view.findViewById(R.id.tvEditBookISBN); btnEditBookISBN = view.findViewById(R.id.btnEditBookISBN)
        tvEditBookStarRating = view.findViewById(R.id.tvEditBookStarRating); btnEditBookStarRating = view.findViewById(R.id.btnEditBookStarRating)
        btnEditBookCancel = view.findViewById(R.id.btnEditBookCancel); btnEditBookSave = view.findViewById(R.id.btnEditBookSave)
        btnEditBookReturn = view.findViewById(R.id.btnEditBookReturn)
    }

    private fun setOptionalElementsVisibility(visibility: Boolean) {
        if (visibility) {
            etEditBookNewValue.visibility = View.VISIBLE
            llEditBookButtonBar.visibility = View.VISIBLE
            btnEditBookCancel.visibility = View.VISIBLE
            btnEditBookSave.visibility = View.VISIBLE
        }
        else if (!visibility) {
            etEditBookNewValue.visibility = View.GONE
            llEditBookButtonBar.visibility = View.GONE
            btnEditBookCancel.visibility = View.GONE
            btnEditBookSave.visibility = View.GONE
        }
    }

    private fun clearEditField() {
        etEditBookNewValue.text.clear()
    }

    private fun setUpButtonClickListeners() {
        btnEditBookTitle.setOnClickListener(this)
        btnEditBookAuthor.setOnClickListener(this)
        btnEditBookTotalPages.setOnClickListener(this)
        btnEditBookGenre.setOnClickListener(this)
        btnEditBookPublisher.setOnClickListener(this)
        btnEditBookYearPublished.setOnClickListener(this)
        btnEditBookISBN.setOnClickListener(this)
        btnEditBookStarRating.setOnClickListener(this)
        btnEditBookReturn.setOnClickListener(this)
    }

    private fun setUpTextViewTexts() {
        tvEditBookTitle.text = dbHandler!!.getBookTitle(MainActivity.bookID!!)
        tvEditBookAuthor.text = dbHandler!!.getBookAuthor(MainActivity.bookID!!)
        tvEditBookTotalPages.text = dbHandler!!.getBookTotalPages(MainActivity.bookID!!).toString()
        tvEditBookGenre.text = dbHandler!!.getBookGenre(MainActivity.bookID!!)
        tvEditBookPublisher.text = dbHandler!!.getBookPublisher(MainActivity.bookID!!)
        tvEditBookYearPublished.text = dbHandler!!.getBookYearPublished(MainActivity.bookID!!).toString()
        tvEditBookISBN.text = dbHandler!!.getBookISBN(MainActivity.bookID!!)
        tvEditBookStarRating.text = dbHandler!!.getBookStarRating(MainActivity.bookID!!).toString()
    }

    override fun onClick(p0: View?) {
        when(p0) {
            btnEditBookTitle -> {
                btnEditBookTitle.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookTitle.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Book Title field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookTitle: String = etEditBookNewValue.text.toString().trim()

                        book.bookTitle = newBookTitle
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookTitle.text = dbHandler!!.getBookTitle(MainActivity.bookID!!)
                        btnEditBookTitle.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Title", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnEditBookAuthor -> {
                btnEditBookAuthor.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookAuthor.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Book Author field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookAuthor: String = etEditBookNewValue.text.toString().trim()

                        book.bookAuthor = newBookAuthor
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookAuthor.text = dbHandler!!.getBookAuthor(MainActivity.bookID!!)
                        btnEditBookAuthor.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Author", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnEditBookTotalPages -> {
                btnEditBookTotalPages.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookTotalPages.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty() || !isNumeric(etEditBookNewValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Pages field cannot be blank and must have numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookTotalPages: Int = etEditBookNewValue.text.toString().trim().toInt()

                        book.bookNumberOfPages = newBookTotalPages
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookTotalPages.text = dbHandler!!.getBookTotalPages(MainActivity.bookID!!).toString()
                        btnEditBookTotalPages.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Total Pages", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnEditBookGenre -> {
                btnEditBookGenre.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookGenre.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Book Genre field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookGenre: String = etEditBookNewValue.text.toString().trim()

                        book.bookGenre = newBookGenre
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookGenre.text = dbHandler!!.getBookGenre(MainActivity.bookID!!)
                        btnEditBookGenre.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Genre", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnEditBookPublisher -> {
                btnEditBookPublisher.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookPublisher.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Book Publisher field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookPublisher: String = etEditBookNewValue.text.toString().trim()

                        book.bookPublisher = newBookPublisher
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookPublisher.text = dbHandler!!.getBookPublisher(MainActivity.bookID!!)
                        btnEditBookPublisher.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Publisher", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnEditBookYearPublished -> {
                btnEditBookYearPublished.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookYearPublished.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty() || !isNumeric(etEditBookNewValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Year Published field cannot be blank and must have numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookYearPublished: Int = etEditBookNewValue.text.toString().trim().toInt()

                        book.bookYearPublished = newBookYearPublished
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookYearPublished.text = dbHandler!!.getBookYearPublished(MainActivity.bookID!!).toString()
                        btnEditBookYearPublished.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Year Published", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnEditBookISBN -> {
                btnEditBookISBN.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookISBN.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Book ISBN field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookISBN: String = etEditBookNewValue.text.toString().trim()

                        book.ISBN = newBookISBN
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookISBN.text = dbHandler!!.getBookISBN(MainActivity.bookID!!)
                        btnEditBookISBN.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book ISBN Code", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnEditBookStarRating -> {
                btnEditBookStarRating.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditBookCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditBookStarRating.visibility = View.GONE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditBookSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty() || !isNumeric(etEditBookNewValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Rating field cannot be blank and must have numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookStarRating: Float = etEditBookNewValue.text.toString().trim().toFloat()

                        book.bookStarRating = newBookStarRating
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvEditBookStarRating.text = dbHandler!!.getBookStarRating(MainActivity.bookID!!).toString()
                        btnEditBookStarRating.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Rating", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            btnEditBookReturn -> {
                navigationController!!.navigate(R.id.action_editBookFragment_to_bookListFragment)
            }
        }
    }

    private fun isNumeric(string: String): Boolean {
        return string.all { char -> char.isDigit() }
    }

    private fun checkEmpty(): Boolean {
        if (etEditBookNewValue.text.toString().trim() == "") {
            return true
        }
        return false
    }
}