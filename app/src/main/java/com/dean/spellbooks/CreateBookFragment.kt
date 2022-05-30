package com.dean.spellbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation

class CreateBookFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var navigationController: NavController? = null

    private lateinit var etCreateBookTitle: EditText
    private lateinit var etCreateBookAuthor: EditText
    private lateinit var etCreateBookPages: EditText
    private lateinit var spinCreateBookGenre: Spinner
    private lateinit var etCreateBookPublisher: EditText
    private lateinit var etCreateBookYearPublished: EditText
    private lateinit var etCreateBookISBN: EditText
    private lateinit var etCreateBookStarRating: EditText
    private lateinit var btnCancelCreateBook: Button
    private lateinit var btnSaveCreateBook: Button
    private lateinit var genres: Array<out String>
    private lateinit var spinnerText: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_create_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)

        initialiseUIElements(view)

        setUpButtonClickListeners()

        setUpGenreSpinner()
    }

    private fun initialiseUIElements(view: View) {
        etCreateBookTitle = view.findViewById(R.id.etCreateBookTitle)
        etCreateBookAuthor = view.findViewById(R.id.etCreateBookAuthor)
        etCreateBookPages = view.findViewById(R.id.etCreateBookPages)
        spinCreateBookGenre = view.findViewById(R.id.spinCreateBookGenre)
        etCreateBookPublisher = view.findViewById(R.id.etCreateBookPublisher)
        etCreateBookYearPublished = view.findViewById(R.id.etCreateBookYearPublished)
        etCreateBookISBN = view.findViewById(R.id.etCreateBookISBN)
        etCreateBookStarRating = view.findViewById(R.id.etCreateBookStarRating)
        btnCancelCreateBook = view.findViewById(R.id.btnCancelCreateBook)
        btnSaveCreateBook = view.findViewById(R.id.btnSaveCreateBook)
    }

    private fun setUpButtonClickListeners() {
        btnCancelCreateBook.setOnClickListener(this)
        btnSaveCreateBook.setOnClickListener(this)
    }

    private fun setUpGenreSpinner() {
        genres = resources.getStringArray(R.array.book_genres)
        val spinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinCreateBookGenre.adapter = spinnerAdapter
        spinCreateBookGenre.onItemSelectedListener = this
    }

    private fun addBook(view: View) {
        if (checkEmpty(etCreateBookTitle) || checkEmpty(etCreateBookAuthor) ||
            (checkEmpty(etCreateBookPages) || !isNumeric(etCreateBookPages.text.toString().trim())) ||
            spinnerText.isEmpty() || checkEmpty(etCreateBookPublisher) ||
            (checkEmpty(etCreateBookYearPublished) || !isNumeric(etCreateBookYearPublished.text.toString().trim())) ||
            checkEmpty(etCreateBookISBN) ||
            (checkEmpty(etCreateBookStarRating) || !isNumeric(etCreateBookStarRating.text.toString().trim()))) {
            Toast.makeText(requireContext(), "Fields cannot be blank", Toast.LENGTH_SHORT).show()
        }
        else {
            val bookTitle: String = etCreateBookTitle.text.toString().trim()
            val bookAuthor: String = etCreateBookAuthor.text.toString().trim()
            val bookPages: Int = etCreateBookPages.text.toString().trim().toInt()
            val bookGenre: String = spinnerText
            val bookPublisher: String = etCreateBookPublisher.text.toString().trim()
            val bookYearPublished: Int = etCreateBookYearPublished.text.toString().trim().toInt()
            val ISBN: String = etCreateBookISBN.text.toString().trim()
            val bookStarRating: Float = etCreateBookStarRating.text.toString().trim().toFloat()
            val bookImage: ByteArray    // TODO: IMPLEMENT THIS
            val dbHandler: DBHandler = DBHandler(requireContext())

            val status = dbHandler.addBook(BookModelClass(null, bookTitle, bookAuthor, bookPages,
                bookGenre, bookPublisher, bookYearPublished, ISBN, bookStarRating, 0, null, MainActivity.userID!!))

            if (status > -1) {
                Toast.makeText(requireContext(), "Successfully added book to list", Toast.LENGTH_SHORT).show()
                etCreateBookTitle.text.clear()
                etCreateBookAuthor.text.clear()
                etCreateBookPages.text.clear()
                etCreateBookPublisher.text.clear()
                etCreateBookYearPublished.text.clear()
                etCreateBookISBN.text.clear()
                etCreateBookStarRating.text.clear()
            }
        }
    }

    private fun checkEmpty(editText: EditText): Boolean {
        if (editText.text.toString().trim() == "") {
            return true
        }
        return false
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnCancelCreateBook -> {
                navigationController!!.navigate(R.id.action_createBookFragment_to_bookListFragment)
                Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
            }
            btnSaveCreateBook -> {
                addBook(p0)

                navigationController!!.navigate(R.id.action_createBookFragment_to_bookListFragment)
            }
        }
    }

    private fun isNumeric(string: String): Boolean {
        return string.all { char -> char.isDigit() }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        spinnerText = genres[p2].toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerText = ""
    }
}