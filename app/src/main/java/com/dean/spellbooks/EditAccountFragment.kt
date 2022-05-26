package com.dean.spellbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.solver.GoalRow
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditAccountFragment : Fragment(), View.OnClickListener {

    private var navigationController: NavController? = null
    private var dbHandler: DBHandler? = null

    private lateinit var btnEditAccountCancel: Button
    private lateinit var btnEditAccountSave: Button
    // All of the below UI elements will be programmed in pairs
    private lateinit var etEditAccountValue: EditText; private lateinit var llEditAccountButtonBar: LinearLayout
    private lateinit var btnEditAccountUsername: Button; private lateinit var tvEditAccountUsername: TextView
    private lateinit var btnEditAccountPassword: Button; private lateinit var tvEditAccountPassword: TextView
    private lateinit var btnEditAccountFavGenre: Button; private lateinit var tvEditAccountFavGenre: TextView
    private lateinit var btnEditAccountFavBook: Button; private lateinit var tvEditAccountFavBook: TextView
    private lateinit var btnEditAccountBookGoal: Button; private lateinit var tvEditAccountBookGoal: TextView
    private lateinit var btnEditAccountPageGoal: Button; private lateinit var tvEditAccountPageGoal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_account, container, false)
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
        etEditAccountValue = view.findViewById(R.id.etEditAccountValue); llEditAccountButtonBar = view.findViewById(R.id.llEditAccountButtonBar)
        btnEditAccountUsername = view.findViewById(R.id.btnEditAccountUsername); tvEditAccountUsername = view.findViewById(R.id.tvEditAccountUsername)
        btnEditAccountPassword = view.findViewById(R.id.btnEditAccountPassword); tvEditAccountPassword = view.findViewById(R.id.tvEditAccountPassword)
        btnEditAccountFavGenre = view.findViewById(R.id.btnEditAccountFavGenre); tvEditAccountFavGenre = view.findViewById(R.id.tvEditAccountFavGenre)
        btnEditAccountFavBook = view.findViewById(R.id.btnEditAccountFavBook); tvEditAccountFavBook = view.findViewById(R.id.tvEditAccountFavBook)
        btnEditAccountBookGoal = view.findViewById(R.id.btnEditAccountBookGoal); tvEditAccountBookGoal = view.findViewById(R.id.tvEditAccountBookGoal)
        btnEditAccountPageGoal = view.findViewById(R.id.btnEditAccountPageGoal); tvEditAccountPageGoal = view.findViewById(R.id.tvEditAccountPageGoal)
    }

    private fun setUpTextViewTexts() {
        tvEditAccountUsername.text = dbHandler!!.getUsername(MainActivity.userID!!)
        tvEditAccountPassword.text = dbHandler!!.getUserPassword(MainActivity.userID!!)
        tvEditAccountFavGenre.text = dbHandler!!.getUserFavGenre(MainActivity.userID!!)
        tvEditAccountFavBook.text = dbHandler!!.getUserFavBook(MainActivity.userID!!)
        tvEditAccountBookGoal.text = dbHandler!!.getUserBookGoal(MainActivity.userID!!).toString()
        tvEditAccountPageGoal.text = dbHandler!!.getUserPageGoal(MainActivity.userID!!).toString()
    }

    private fun setUpButtonClickListeners() {
        btnEditAccountUsername.setOnClickListener(this)
        btnEditAccountPassword.setOnClickListener(this)
        btnEditAccountFavGenre.setOnClickListener(this)
        btnEditAccountFavBook.setOnClickListener(this)
        btnEditAccountBookGoal.setOnClickListener(this)
        btnEditAccountPageGoal.setOnClickListener(this)
    }

    private fun setOptionalElementsVisibility(visibility: Boolean) {
        if (visibility) {
            etEditAccountValue.visibility = View.VISIBLE; llEditAccountButtonBar.visibility = View.VISIBLE
        }
        else if (!visibility) {
            etEditAccountValue.visibility = View.GONE; llEditAccountButtonBar.visibility = View.GONE
        }
    }

    private fun clearEditField() {
        etEditAccountValue.text.clear()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnEditAccountUsername -> {

                btnEditAccountUsername.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditAccountCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditAccountUsername.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditAccountSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Username field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newUsername: String = etEditAccountValue.text.toString().trim()

                        if (newUsername.isNotEmpty()) {
                            wiz.username = newUsername
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(false)
                            tvEditAccountUsername.text = dbHandler!!.getUsername(MainActivity.userID!!)
                            btnEditAccountUsername.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated username", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            btnEditAccountPassword -> {
                btnEditAccountPassword.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditAccountCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditAccountPassword.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditAccountSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Password field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newPassword: String = etEditAccountValue.text.toString()

                        if (newPassword.isNotEmpty()) {
                            wiz.password = newPassword
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(false)
                            tvEditAccountPassword.text = dbHandler!!.getUserPassword(MainActivity.userID!!)
                            btnEditAccountPassword.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated password", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            btnEditAccountFavGenre -> {
                btnEditAccountFavGenre.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditAccountCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditAccountFavGenre.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditAccountSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Favourite Genre field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newFavGenre: String = etEditAccountValue.text.toString()

                        if (newFavGenre.isNotEmpty()) {
                            wiz.favouriteGenre = newFavGenre
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(false)
                            tvEditAccountFavGenre.text = dbHandler!!.getUserFavGenre(MainActivity.userID!!)
                            btnEditAccountFavGenre.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated favourite genre", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            btnEditAccountFavBook -> {
                btnEditAccountFavBook.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditAccountCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditAccountFavBook.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditAccountSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty())
                        Toast.makeText(requireContext(), "Favourite Book field cannot be blank", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newFavBook: String = etEditAccountValue.text.toString()

                        if (newFavBook.isNotEmpty()) {
                            wiz.favouriteBook = newFavBook
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(false)
                            tvEditAccountFavBook.text = dbHandler!!.getUserFavBook(MainActivity.userID!!)
                            btnEditAccountFavBook.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated favourite book", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            btnEditAccountBookGoal -> {
                btnEditAccountBookGoal.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditAccountCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditAccountBookGoal.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditAccountSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty() || !isNumeric(etEditAccountValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Book Goal field cannot be blank and must have a numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newBookGoal: Int = etEditAccountValue.text.toString().toInt()

                        if (newBookGoal.toString().isNotEmpty()) {
                            wiz.booksReadingGoal = newBookGoal
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(false)
                            tvEditAccountBookGoal.text = dbHandler!!.getUserBookGoal(MainActivity.userID!!).toString()
                            btnEditAccountBookGoal.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated yearly book goal", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            btnEditAccountPageGoal -> {
                btnEditAccountPageGoal.visibility = View.GONE
                setOptionalElementsVisibility(true)

                btnEditAccountCancel.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditAccountPageGoal.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                btnEditAccountSave.setOnClickListener(View.OnClickListener {
                    if (checkEmpty() || !isNumeric(etEditAccountValue.text.toString().trim()))
                        Toast.makeText(requireContext(), "Pages Goal field cannot be blank and must have a numeric value", Toast.LENGTH_SHORT).show()
                    else {
                        val wiz: UserModelClass = dbHandler!!.getWizard(MainActivity.userID!!)!!
                        val newPageGoal: Int = etEditAccountValue.text.toString().toInt()

                        if (newPageGoal.toString().isNotEmpty()) {
                            wiz.pagesReadingGoal = newPageGoal
                            dbHandler!!.updateWizardInformation(wiz)
                            clearEditField()
                            setOptionalElementsVisibility(false)
                            tvEditAccountPageGoal.text = dbHandler!!.getUserPageGoal(MainActivity.userID!!).toString()
                            btnEditAccountPageGoal.visibility = View.VISIBLE

                            Toast.makeText(requireContext(), "Successfully updated yearly page goal", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    private fun checkEmpty(): Boolean {
        if (etEditAccountValue.text.toString().trim() == "") {
            return true
        }
        return false
    }

    private fun isNumeric(string: String): Boolean {
        return string.all { char -> char.isDigit() }
    }
}