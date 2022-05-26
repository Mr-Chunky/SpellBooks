package com.dean.spellbooks

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation

class CreateAccountFragment : Fragment(), View.OnClickListener {

    private var navigationController: NavController? = null

    private lateinit var etNewUserName: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etNewFavouriteGenre: EditText
    private lateinit var etNewFavouriteBook: EditText
    private lateinit var etNewBookGoal: EditText
    private lateinit var etNewPageGoal: EditText
    private lateinit var btnCancelCreateAccount: Button
    private lateinit var btnSaveCreateAccount: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_account, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)

        initialiseUIElements(view)

        setUpButtonClickListeners()
    }

    private fun setUpButtonClickListeners() {
        btnCancelCreateAccount.setOnClickListener(this)
        btnSaveCreateAccount.setOnClickListener(this)
    }

    private fun initialiseUIElements(view: View) {
        etNewUserName = view.findViewById(R.id.etNewUsername)
        etNewPassword = view.findViewById(R.id.etNewPassword)
        etNewFavouriteGenre = view.findViewById(R.id.etNewFavouriteGenre)
        etNewFavouriteBook = view.findViewById(R.id.etNewFavouriteBook)
        etNewBookGoal = view.findViewById(R.id.etNewBookGoal)
        etNewPageGoal = view.findViewById(R.id.etNewPageGoal)
        btnCancelCreateAccount = view.findViewById(R.id.btnCancelCreateAccount)
        btnSaveCreateAccount = view.findViewById(R.id.btnSaveCreateAccount)
    }

    private fun addNewWizard(view: View) {
        if (checkEmpty(etNewUserName) || checkEmpty(etNewPassword) || checkEmpty(etNewFavouriteGenre) ||
            checkEmpty(etNewFavouriteBook) || checkEmpty(etNewBookGoal) || checkEmpty(etNewPageGoal))
            Toast.makeText(requireContext(), "Fields cannot be blank", Toast.LENGTH_SHORT).show()
        else {
            val username = etNewUserName.text.toString().trim()
            val password = etNewPassword.text.toString().trim()
            val favouriteGenre = etNewFavouriteGenre.text.toString().trim()
            val favouriteBook = etNewFavouriteBook.text.toString().trim()
            val bookGoal = etNewBookGoal.text.toString().trim().toInt()
            val pageGoal = etNewPageGoal.text.toString().trim().toInt()
            val dbHandler: DBHandler = DBHandler(requireContext())

            val status = dbHandler.registerWizard(UserModelClass(null, username, password,
                favouriteGenre, favouriteBook, bookGoal, pageGoal))

            if (status > -1) {
                Toast.makeText(requireContext(), "Successfully added new wizard! Please login to continue.", Toast.LENGTH_SHORT).show()
                etNewUserName.text.clear()
                etNewPassword.text.clear()
                etNewFavouriteGenre.text.clear()
                etNewFavouriteBook.text.clear()
                etNewBookGoal.text.clear()
                etNewPageGoal.text.clear()
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
        if(p0!!.id == R.id.btnCancelCreateAccount) {
            navigationController!!.navigate(R.id.action_createAccountFragment_to_loginFragment)
            Toast.makeText(context, "Information discarded.  Returning to login page.", Toast.LENGTH_SHORT).show()
        } else if (p0.id == R.id.btnSaveCreateAccount) {
            addNewWizard(p0)
            navigationController!!.navigate(R.id.action_createAccountFragment_to_loginFragment)
        }
    }
}