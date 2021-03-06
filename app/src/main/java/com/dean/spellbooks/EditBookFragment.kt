package com.dean.spellbooks

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dean.spellbooks.SpellBooksUtils.Companion.checkEmpty
import com.dean.spellbooks.SpellBooksUtils.Companion.convertToByteArray
import com.dean.spellbooks.SpellBooksUtils.Companion.isNumeric

class EditBookFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private var navigationController: NavController? = null
    private var pictureByteArray: ByteArray? = null
    private var dbHandler: DBHandler? = null

    private lateinit var genres: Array<out String>
    private lateinit var spinnerText: String
    private lateinit var ibEditBookImage: ImageButton
    // These UI elements will be programmed in pairs
    private lateinit var spinEditBookGenre: Spinner;        private lateinit var llEditBookSpinnerHolder: LinearLayout
    private lateinit var etEditBookNewValue: EditText;      private lateinit var llEditBookButtonBar: LinearLayout
    private lateinit var tvEditBookTitle: TextView;         private lateinit var btnEditBookTitle: Button
    private lateinit var tvEditBookAuthor: TextView;        private lateinit var btnEditBookAuthor: Button
    private lateinit var tvEditBookTotalPages: TextView;    private lateinit var btnEditBookTotalPages: Button
    private lateinit var tvEditBookGenre: TextView;         private lateinit var btnEditBookGenre: Button
    private lateinit var tvEditBookPublisher: TextView;     private lateinit var btnEditBookPublisher: Button

    private lateinit var tvEditBookYearPublished: TextView; private lateinit var btnEditBookYearPublished: Button
    private lateinit var tvEditBookISBN: TextView;          private lateinit var btnEditBookISBN: Button
    private lateinit var tvEditBookStarRating: TextView;    private lateinit var btnEditBookStarRating: Button
    private lateinit var btnEditBookCancel: Button;         private lateinit var btnEditBookSave: Button
    private lateinit var btnEditBookReturn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)
        dbHandler = DBHandler.getDBHandler(requireContext())

        initialiseUIElements(view)

        setUpImageButton()

        setOptionalElementsVisibility(0)

        setUpTextViewTexts()

        setUpGenreSpinner()

        setUpButtonClickListeners()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainActivity.REQUEST_CAMERA_USAGE && resultCode == Activity.RESULT_OK) {
            val pictureBmp = data!!.extras!!.get("data") as Bitmap
            ibEditBookImage.setImageBitmap(pictureBmp)

            pictureByteArray = convertToByteArray(pictureBmp)

            val book = dbHandler!!.getBook(MainActivity.bookID!!)
            book!!.bookImage = pictureByteArray
            dbHandler!!.updateBookInformation(book)
        }
    }

    private fun initialiseUIElements(view: View) {
        etEditBookNewValue = view.findViewById(R.id.etEditBookNewValue);            llEditBookButtonBar = view.findViewById(R.id.llEditBookButtonBar)
        tvEditBookTitle = view.findViewById(R.id.tvEditBookTitle);                  btnEditBookTitle = view.findViewById(R.id.btnEditBookTitle)
        tvEditBookAuthor = view.findViewById(R.id.tvEditBookAuthor);                btnEditBookAuthor = view.findViewById(R.id.btnEditBookAuthor)
        tvEditBookTotalPages = view.findViewById(R.id.tvEditBookTotalPages);        btnEditBookTotalPages = view.findViewById(R.id.btnEditBookTotalPages)
        tvEditBookGenre = view.findViewById(R.id.tvEditBookGenre);                  btnEditBookGenre = view.findViewById(R.id.btnEditBookGenre)
        tvEditBookPublisher = view.findViewById(R.id.tvEditBookPublisher);          btnEditBookPublisher = view.findViewById(R.id.btnEditBookPublisher)
        tvEditBookYearPublished = view.findViewById(R.id.tvEditBookYearPublished);  btnEditBookYearPublished = view.findViewById(R.id.btnEditBookYearPublished)
        tvEditBookISBN = view.findViewById(R.id.tvEditBookISBN);                    btnEditBookISBN = view.findViewById(R.id.btnEditBookISBN)
        tvEditBookStarRating = view.findViewById(R.id.tvEditBookStarRating);        btnEditBookStarRating = view.findViewById(R.id.btnEditBookStarRating)
        btnEditBookCancel = view.findViewById(R.id.btnEditBookCancel);              btnEditBookSave = view.findViewById(R.id.btnEditBookSave)
        spinEditBookGenre = view.findViewById(R.id.spinEditBookGenre);              llEditBookSpinnerHolder = view.findViewById(R.id.llEditBookSpinnerHolder)
        btnEditBookReturn = view.findViewById(R.id.btnEditBookReturn);              ibEditBookImage = view.findViewById(R.id.ibEditBookImage)
    }

    private fun setOptionalElementsVisibility(visibility: Int) {
        when (visibility) {
            0 -> {
                llEditBookSpinnerHolder.visibility = View.GONE
                etEditBookNewValue.visibility = View.GONE
                llEditBookButtonBar.visibility = View.GONE
                btnEditBookCancel.visibility = View.GONE
                btnEditBookSave.visibility = View.GONE
                btnEditBookReturn.visibility = View.VISIBLE

            }
            1 -> {
                llEditBookSpinnerHolder.visibility = View.GONE
                etEditBookNewValue.visibility = View.VISIBLE
                llEditBookButtonBar.visibility = View.VISIBLE
                btnEditBookCancel.visibility = View.VISIBLE
                btnEditBookSave.visibility = View.VISIBLE
                btnEditBookReturn.visibility = View.GONE

            }
            2 -> {
                llEditBookSpinnerHolder.visibility = View.VISIBLE
                etEditBookNewValue.visibility = View.GONE
                llEditBookButtonBar.visibility = View.VISIBLE
                btnEditBookCancel.visibility = View.VISIBLE
                btnEditBookSave.visibility = View.VISIBLE
                btnEditBookReturn.visibility = View.GONE

            }
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
        ibEditBookImage.setOnClickListener(this)
    }

    private fun setUpGenreSpinner() {
        genres = resources.getStringArray(R.array.book_genres)
        val spinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinEditBookGenre.adapter = spinnerAdapter
        spinEditBookGenre.onItemSelectedListener = this
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

    private fun setUpImageButton() {
        if (dbHandler!!.getBookImage(MainActivity.bookID!!) != null) {
            val pictureByteArray: ByteArray? = dbHandler!!.getBookImage(MainActivity.bookID!!)
            val pictureBitmap: Bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray!!.size)

            ibEditBookImage.setImageBitmap(pictureBitmap)
        }
    }

    override fun onClick(p0: View?) {
        when(p0) {
            ibEditBookImage -> {
                if(requireContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), MainActivity.REQUEST_CAMERA_PERMISSION)
                else
                    takePicture()
            }
            btnEditBookTitle -> {
                btnEditBookAuthor.visibility = View.VISIBLE
                btnEditBookPublisher.visibility = View.VISIBLE
                btnEditBookStarRating.visibility = View.VISIBLE
                btnEditBookISBN.visibility = View.VISIBLE
                btnEditBookYearPublished.visibility = View.VISIBLE
                btnEditBookGenre.visibility = View.VISIBLE
                btnEditBookTotalPages.visibility = View.VISIBLE
                btnEditBookTitle.visibility = View.GONE
                etEditBookNewValue.hint = "Enter a new title"
                etEditBookNewValue.inputType = InputType.TYPE_CLASS_TEXT
                setOptionalElementsVisibility(1)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookTitle.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (checkEmpty(etEditBookNewValue))
                        Toast.makeText(requireContext(), "Book Title field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookTitle: String = etEditBookNewValue.text.toString().trim()

                        book.bookTitle = newBookTitle
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookTitle.text = dbHandler!!.getBookTitle(MainActivity.bookID!!)
                        btnEditBookTitle.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Title", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnEditBookAuthor -> {
                btnEditBookAuthor.visibility = View.GONE
                btnEditBookPublisher.visibility = View.VISIBLE
                btnEditBookStarRating.visibility = View.VISIBLE
                btnEditBookISBN.visibility = View.VISIBLE
                btnEditBookYearPublished.visibility = View.VISIBLE
                btnEditBookGenre.visibility = View.VISIBLE
                btnEditBookTotalPages.visibility = View.VISIBLE
                btnEditBookTitle.visibility = View.VISIBLE
                etEditBookNewValue.hint = "Enter a new author"
                etEditBookNewValue.inputType = InputType.TYPE_CLASS_TEXT
                setOptionalElementsVisibility(1)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookAuthor.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (checkEmpty(etEditBookNewValue))
                        Toast.makeText(requireContext(), "Book Author field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookAuthor: String = etEditBookNewValue.text.toString().trim()

                        book.bookAuthor = newBookAuthor
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookAuthor.text = dbHandler!!.getBookAuthor(MainActivity.bookID!!)
                        btnEditBookAuthor.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Author", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnEditBookTotalPages -> {
                btnEditBookAuthor.visibility = View.VISIBLE
                btnEditBookPublisher.visibility = View.VISIBLE
                btnEditBookStarRating.visibility = View.VISIBLE
                btnEditBookISBN.visibility = View.VISIBLE
                btnEditBookYearPublished.visibility = View.VISIBLE
                btnEditBookGenre.visibility = View.VISIBLE
                btnEditBookTotalPages.visibility = View.GONE
                btnEditBookTitle.visibility = View.VISIBLE
                etEditBookNewValue.hint = "Enter new total pages"
                etEditBookNewValue.inputType = InputType.TYPE_CLASS_NUMBER
                setOptionalElementsVisibility(1)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookTotalPages.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (checkEmpty(etEditBookNewValue) || !isNumeric(etEditBookNewValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Pages field cannot be blank and must have numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookTotalPages: Int = etEditBookNewValue.text.toString().trim().toInt()

                        book.bookNumberOfPages = newBookTotalPages
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookTotalPages.text = dbHandler!!.getBookTotalPages(MainActivity.bookID!!).toString()
                        btnEditBookTotalPages.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Total Pages", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnEditBookGenre -> {
                btnEditBookAuthor.visibility = View.VISIBLE
                btnEditBookPublisher.visibility = View.VISIBLE
                btnEditBookStarRating.visibility = View.VISIBLE
                btnEditBookISBN.visibility = View.VISIBLE
                btnEditBookYearPublished.visibility = View.VISIBLE
                btnEditBookGenre.visibility = View.GONE
                btnEditBookTotalPages.visibility = View.VISIBLE
                btnEditBookTitle.visibility = View.VISIBLE
                setOptionalElementsVisibility(2)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookGenre.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (spinnerText.isEmpty())
                        Toast.makeText(requireContext(), "Book Genre field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookGenre: String = spinnerText

                        book.bookGenre = newBookGenre
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookGenre.text = dbHandler!!.getBookGenre(MainActivity.bookID!!)
                        btnEditBookGenre.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Genre", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnEditBookPublisher -> {
                btnEditBookAuthor.visibility = View.VISIBLE
                btnEditBookPublisher.visibility = View.GONE
                btnEditBookStarRating.visibility = View.VISIBLE
                btnEditBookISBN.visibility = View.VISIBLE
                btnEditBookYearPublished.visibility = View.VISIBLE
                btnEditBookGenre.visibility = View.VISIBLE
                btnEditBookTotalPages.visibility = View.VISIBLE
                btnEditBookTitle.visibility = View.VISIBLE
                etEditBookNewValue.hint = "Enter a new publisher"
                etEditBookNewValue.inputType = InputType.TYPE_CLASS_TEXT
                setOptionalElementsVisibility(1)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookPublisher.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (checkEmpty(etEditBookNewValue))
                        Toast.makeText(requireContext(), "Book Publisher field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookPublisher: String = etEditBookNewValue.text.toString().trim()

                        book.bookPublisher = newBookPublisher
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookPublisher.text = dbHandler!!.getBookPublisher(MainActivity.bookID!!)
                        btnEditBookPublisher.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Publisher", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnEditBookYearPublished -> {
                btnEditBookAuthor.visibility = View.VISIBLE
                btnEditBookPublisher.visibility = View.VISIBLE
                btnEditBookStarRating.visibility = View.VISIBLE
                btnEditBookISBN.visibility = View.VISIBLE
                btnEditBookYearPublished.visibility = View.GONE
                btnEditBookGenre.visibility = View.VISIBLE
                btnEditBookTotalPages.visibility = View.VISIBLE
                btnEditBookTitle.visibility = View.VISIBLE
                etEditBookNewValue.hint = "Enter a new publishing year"
                etEditBookNewValue.inputType = InputType.TYPE_CLASS_NUMBER
                setOptionalElementsVisibility(1)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookYearPublished.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (checkEmpty(etEditBookNewValue) || !isNumeric(etEditBookNewValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Year Published field cannot be blank and must have numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookYearPublished: Int = etEditBookNewValue.text.toString().trim().toInt()

                        book.bookYearPublished = newBookYearPublished
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookYearPublished.text = dbHandler!!.getBookYearPublished(MainActivity.bookID!!).toString()
                        btnEditBookYearPublished.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Year Published", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnEditBookISBN -> {
                btnEditBookAuthor.visibility = View.VISIBLE
                btnEditBookPublisher.visibility = View.VISIBLE
                btnEditBookStarRating.visibility = View.VISIBLE
                btnEditBookISBN.visibility = View.GONE
                btnEditBookYearPublished.visibility = View.VISIBLE
                btnEditBookGenre.visibility = View.VISIBLE
                btnEditBookTotalPages.visibility = View.VISIBLE
                btnEditBookTitle.visibility = View.VISIBLE
                etEditBookNewValue.hint = "Enter a new ISBN code"
                etEditBookNewValue.inputType = InputType.TYPE_CLASS_TEXT
                setOptionalElementsVisibility(1)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookISBN.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (checkEmpty(etEditBookNewValue))
                        Toast.makeText(requireContext(), "Book ISBN field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookISBN: String = etEditBookNewValue.text.toString().trim()

                        book.ISBN = newBookISBN
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookISBN.text = dbHandler!!.getBookISBN(MainActivity.bookID!!)
                        btnEditBookISBN.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book ISBN Code", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnEditBookStarRating -> {
                btnEditBookAuthor.visibility = View.VISIBLE
                btnEditBookPublisher.visibility = View.VISIBLE
                btnEditBookStarRating.visibility = View.GONE
                btnEditBookISBN.visibility = View.VISIBLE
                btnEditBookYearPublished.visibility = View.VISIBLE
                btnEditBookGenre.visibility = View.VISIBLE
                btnEditBookTotalPages.visibility = View.VISIBLE
                btnEditBookTitle.visibility = View.VISIBLE
                etEditBookNewValue.hint = "Enter a new star rating"
                etEditBookNewValue.inputType = InputType.TYPE_CLASS_NUMBER
                setOptionalElementsVisibility(1)

                btnEditBookCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditBookStarRating.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditBookSave.setOnClickListener {
                    if (checkEmpty(etEditBookNewValue) || !isNumeric(etEditBookNewValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Rating field cannot be blank and must have numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val book: BookModelClass = dbHandler!!.getBook(MainActivity.bookID!!)!!
                        val newBookStarRating: Float = etEditBookNewValue.text.toString().trim().toFloat()

                        book.bookStarRating = newBookStarRating
                        dbHandler!!.updateBookInformation(book)
                        clearEditField()
                        setOptionalElementsVisibility(0)
                        tvEditBookStarRating.text = dbHandler!!.getBookStarRating(MainActivity.bookID!!).toString()
                        btnEditBookStarRating.visibility = View.VISIBLE

                        Toast.makeText(requireContext(), "Successfully updated Book Rating", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            btnEditBookReturn -> {
                navigationController!!.navigate(R.id.action_editBookFragment_to_bookListFragment)
            }
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, MainActivity.REQUEST_CAMERA_USAGE)
        } catch (e: Exception) {
            Log.e("camera", e.stackTraceToString())
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        spinnerText = genres[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerText = ""
    }
}