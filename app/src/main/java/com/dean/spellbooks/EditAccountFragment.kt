package com.dean.spellbooks

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.dean.spellbooks.SpellBooksUtils.Companion.checkEmpty
import com.dean.spellbooks.SpellBooksUtils.Companion.isNumeric

class EditAccountFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var navigationController: NavController? = null
    private var dbHandler: DBHandler? = null

    private lateinit var btnEditAccountCancel: Button
    private lateinit var btnEditAccountSave: Button
    private lateinit var genres: Array<out String>
    private lateinit var spinnerText: String
    // All of the below UI elements will be programmed in pairs
    private lateinit var spinEditAccountGenre: Spinner;     private lateinit var llEditAccountSpinnerHolder: LinearLayout
    private lateinit var etEditAccountValue: EditText;      private lateinit var llEditAccountButtonBar: LinearLayout
    private lateinit var btnEditAccountUsername: Button;    private lateinit var tvEditAccountUsername: TextView
    private lateinit var btnEditAccountPassword: Button;    private lateinit var tvEditAccountPassword: TextView
    private lateinit var btnEditAccountFavGenre: Button;    private lateinit var tvEditAccountFavGenre: TextView
    private lateinit var btnEditAccountFavBook: Button;     private lateinit var tvEditAccountFavBook: TextView
    private lateinit var btnEditAccountBookGoal: Button;    private lateinit var tvEditAccountBookGoal: TextView
    private lateinit var btnEditAccountPageGoal: Button;    private lateinit var tvEditAccountPageGoal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)
        dbHandler = DBHandler.getDBHandler(requireContext())

        initialiseUIElements(view)

        setOptionalElementsVisibility(0)

        setUpTextViewTexts()

        setUpGenreSpinner()

        setUpButtonClickListeners()

        val bottomNav: BottomNavigationView? = view.findViewById(R.id.bnvEditAccountNavigation)
        bottomNav!!.setupWithNavController(navigationController!!)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.stats_icon -> {
                    navigationController!!.navigate(R.id.action_editAccountFragment_to_accountDetailsFragment)
                }
                R.id.book_list_icon -> {
                    navigationController!!.navigate(R.id.action_editAccountFragment_to_bookListFragment)
                }
            }

            return@setOnItemSelectedListener true
        }
    }

    private fun initialiseUIElements(view: View) {
        btnEditAccountCancel = view.findViewById(R.id.btnEditAccountCancel)
        btnEditAccountSave = view.findViewById(R.id.btnEditAccountSave)
        spinEditAccountGenre = view.findViewById(R.id.spinEditAccountGenre);        llEditAccountSpinnerHolder = view.findViewById(R.id.llEditAccountSpinnerHolder)
        etEditAccountValue = view.findViewById(R.id.etEditAccountValue);            llEditAccountButtonBar = view.findViewById(R.id.llEditAccountButtonBar)
        btnEditAccountUsername = view.findViewById(R.id.btnEditAccountUsername);    tvEditAccountUsername = view.findViewById(R.id.tvEditAccountUsername)
        btnEditAccountPassword = view.findViewById(R.id.btnEditAccountPassword);    tvEditAccountPassword = view.findViewById(R.id.tvEditAccountPassword)
        btnEditAccountFavGenre = view.findViewById(R.id.btnEditAccountFavGenre);    tvEditAccountFavGenre = view.findViewById(R.id.tvEditAccountFavGenre)
        btnEditAccountFavBook = view.findViewById(R.id.btnEditAccountFavBook);      tvEditAccountFavBook = view.findViewById(R.id.tvEditAccountFavBook)
        btnEditAccountBookGoal = view.findViewById(R.id.btnEditAccountBookGoal);    tvEditAccountBookGoal = view.findViewById(R.id.tvEditAccountBookGoal)
        btnEditAccountPageGoal = view.findViewById(R.id.btnEditAccountPageGoal);    tvEditAccountPageGoal = view.findViewById(R.id.tvEditAccountPageGoal)

    }

    private fun setUpTextViewTexts() {
        tvEditAccountUsername.text = dbHandler!!.getUsername(MainActivity.userID!!)
        tvEditAccountPassword.text = dbHandler!!.getUserPassword(MainActivity.userID!!)
        tvEditAccountFavGenre.text = dbHandler!!.getUserFavGenre(MainActivity.userID!!)
        tvEditAccountFavBook.text = dbHandler!!.getUserFavBook(MainActivity.userID!!)
        tvEditAccountBookGoal.text = dbHandler!!.getUserBookGoal(MainActivity.userID!!).toString()
        tvEditAccountPageGoal.text = dbHandler!!.getUserPageGoal(MainActivity.userID!!).toString()
    }

    private fun setUpGenreSpinner() {
        genres = resources.getStringArray(R.array.book_genres)
        val spinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinEditAccountGenre.adapter = spinnerAdapter
        spinEditAccountGenre.onItemSelectedListener = this
    }

    private fun setUpButtonClickListeners() {
        btnEditAccountUsername.setOnClickListener(this)
        btnEditAccountPassword.setOnClickListener(this)
        btnEditAccountFavGenre.setOnClickListener(this)
        btnEditAccountFavBook.setOnClickListener(this)
        btnEditAccountBookGoal.setOnClickListener(this)
        btnEditAccountPageGoal.setOnClickListener(this)
    }

    private fun setOptionalElementsVisibility(visibility: Int) {
        when (visibility) {
            0 -> {
                etEditAccountValue.visibility = View.GONE
                llEditAccountButtonBar.visibility = View.GONE
                llEditAccountSpinnerHolder.visibility = View.GONE
            }
            1 -> {
                etEditAccountValue.visibility = View.VISIBLE
                llEditAccountButtonBar.visibility = View.VISIBLE
                llEditAccountSpinnerHolder.visibility = View.GONE
            }
            2 -> {
                etEditAccountValue.visibility = View.GONE
                llEditAccountButtonBar.visibility = View.VISIBLE
                llEditAccountSpinnerHolder.visibility = View.VISIBLE
            }
        }
    }

    private fun clearEditField() {
        etEditAccountValue.text.clear()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnEditAccountUsername -> {
                btnEditAccountUsername.visibility = View.GONE
                btnEditAccountPassword.visibility = View.VISIBLE
                btnEditAccountFavGenre.visibility = View.VISIBLE
                btnEditAccountFavBook.visibility = View.VISIBLE
                btnEditAccountBookGoal.visibility = View.VISIBLE
                btnEditAccountPageGoal.visibility = View.VISIBLE
                etEditAccountValue.hint = "Enter a new username"
                etEditAccountValue.inputType = InputType.TYPE_CLASS_TEXT
                setOptionalElementsVisibility(1)

                btnEditAccountCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditAccountUsername.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditAccountSave.setOnClickListener {
                    if (checkEmpty(etEditAccountValue))
                        Toast.makeText(requireContext(), "Username field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newUsername: String = etEditAccountValue.text.toString().trim()

                        if (newUsername.isNotEmpty()) {
                            wiz.username = newUsername
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(0)
                            tvEditAccountUsername.text = dbHandler!!.getUsername(MainActivity.userID!!)
                            btnEditAccountUsername.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated username", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            btnEditAccountPassword -> {
                btnEditAccountUsername.visibility = View.VISIBLE
                btnEditAccountPassword.visibility = View.GONE
                btnEditAccountFavGenre.visibility = View.VISIBLE
                btnEditAccountFavBook.visibility = View.VISIBLE
                btnEditAccountBookGoal.visibility = View.VISIBLE
                btnEditAccountPageGoal.visibility = View.VISIBLE
                etEditAccountValue.hint = "Enter a new password"
                etEditAccountValue.inputType = InputType.TYPE_CLASS_TEXT
                setOptionalElementsVisibility(1)

                btnEditAccountCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditAccountPassword.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditAccountSave.setOnClickListener {
                    if (checkEmpty(etEditAccountValue))
                        Toast.makeText(requireContext(), "Password field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newPassword: String = etEditAccountValue.text.toString()

                        if (newPassword.isNotEmpty()) {
                            wiz.password = newPassword
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(0)
                            tvEditAccountPassword.text = dbHandler!!.getUserPassword(MainActivity.userID!!)
                            btnEditAccountPassword.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            btnEditAccountFavGenre -> {
                btnEditAccountUsername.visibility = View.VISIBLE
                btnEditAccountPassword.visibility = View.VISIBLE
                btnEditAccountFavGenre.visibility = View.GONE
                btnEditAccountFavBook.visibility = View.VISIBLE
                btnEditAccountBookGoal.visibility = View.VISIBLE
                btnEditAccountPageGoal.visibility = View.VISIBLE
                setOptionalElementsVisibility(2)

                btnEditAccountCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditAccountFavGenre.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditAccountSave.setOnClickListener {
                    if (spinnerText.isEmpty())
                        Toast.makeText(requireContext(), "Favourite Genre field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newFavGenre: String = spinnerText

                        if (newFavGenre.isNotEmpty()) {
                            wiz.favouriteGenre = newFavGenre
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(0)
                            tvEditAccountFavGenre.text = dbHandler!!.getUserFavGenre(MainActivity.userID!!)
                            btnEditAccountFavGenre.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated favourite genre", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            btnEditAccountFavBook -> {
                btnEditAccountUsername.visibility = View.VISIBLE
                btnEditAccountPassword.visibility = View.VISIBLE
                btnEditAccountFavGenre.visibility = View.VISIBLE
                btnEditAccountFavBook.visibility = View.GONE
                btnEditAccountBookGoal.visibility = View.VISIBLE
                btnEditAccountPageGoal.visibility = View.VISIBLE
                etEditAccountValue.hint = "Enter a new favourite book"
                etEditAccountValue.inputType = InputType.TYPE_CLASS_TEXT
                setOptionalElementsVisibility(1)

                btnEditAccountCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditAccountFavBook.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditAccountSave.setOnClickListener {
                    if (checkEmpty(etEditAccountValue))
                        Toast.makeText(requireContext(), "Favourite Book field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newFavBook: String = etEditAccountValue.text.toString()

                        if (newFavBook.isNotEmpty()) {
                            wiz.favouriteBook = newFavBook
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(0)
                            tvEditAccountFavBook.text = dbHandler!!.getUserFavBook(MainActivity.userID!!)
                            btnEditAccountFavBook.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated favourite book", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            btnEditAccountBookGoal -> {
                btnEditAccountUsername.visibility = View.VISIBLE
                btnEditAccountPassword.visibility = View.VISIBLE
                btnEditAccountFavGenre.visibility = View.VISIBLE
                btnEditAccountFavBook.visibility = View.VISIBLE
                btnEditAccountBookGoal.visibility = View.GONE
                btnEditAccountPageGoal.visibility = View.VISIBLE
                etEditAccountValue.hint = "Enter a new book goal"
                etEditAccountValue.inputType = InputType.TYPE_CLASS_NUMBER
                setOptionalElementsVisibility(1)

                btnEditAccountCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditAccountBookGoal.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditAccountSave.setOnClickListener {
                    if (checkEmpty(etEditAccountValue) || !isNumeric(etEditAccountValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Goal field cannot be blank and must have a numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newBookGoal: Int = etEditAccountValue.text.toString().toInt()

                        if (newBookGoal.toString().isNotEmpty()) {
                            wiz.booksReadingGoal = newBookGoal
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(0)
                            tvEditAccountBookGoal.text = dbHandler!!.getUserBookGoal(MainActivity.userID!!).toString()
                            btnEditAccountBookGoal.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated yearly book goal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            btnEditAccountPageGoal -> {
                btnEditAccountUsername.visibility = View.VISIBLE
                btnEditAccountPassword.visibility = View.VISIBLE
                btnEditAccountFavGenre.visibility = View.VISIBLE
                btnEditAccountFavBook.visibility = View.VISIBLE
                btnEditAccountBookGoal.visibility = View.VISIBLE
                btnEditAccountPageGoal.visibility = View.GONE
                etEditAccountValue.hint = "Enter a new page goal"
                etEditAccountValue.inputType = InputType.TYPE_CLASS_NUMBER
                setOptionalElementsVisibility(1)

                btnEditAccountCancel.setOnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(0)
                    btnEditAccountPageGoal.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                }

                btnEditAccountSave.setOnClickListener {
                    if (checkEmpty(etEditAccountValue) || !isNumeric(etEditAccountValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Pages Goal field cannot be blank and must have a numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newPageGoal: Int = etEditAccountValue.text.toString().toInt()

                        if (newPageGoal.toString().isNotEmpty()) {
                            wiz.pagesReadingGoal = newPageGoal
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(0)
                            tvEditAccountPageGoal.text = dbHandler!!.getUserPageGoal(MainActivity.userID!!).toString()
                            btnEditAccountPageGoal.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated yearly page goal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }



    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        spinnerText = genres[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        spinnerText = ""
    }
}