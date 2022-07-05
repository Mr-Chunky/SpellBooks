package com.dean.spellbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation

class LoginFragment : Fragment() {

    private var navigationController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)
        val dbHandler: DBHandler = DBHandler.getDBHandler(requireContext())

        val tvCreateAccountLink: TextView = view.findViewById(R.id.tvCreateAccountLink)
        val etLoginUsername: EditText = view.findViewById(R.id.etLoginUsername)
        val etLoginPassword: EditText = view.findViewById(R.id.etLoginPassword)
        val btnLogin : Button = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            if (dbHandler.checkUsernameAndPassword(etLoginUsername.text.toString(), etLoginPassword.text.toString())) {

                // Using MainActivity's companion object to access the userID from anywhere in the app
                MainActivity.userID = dbHandler.getUserID(etLoginUsername.text.toString(), etLoginPassword.text.toString())

                Toast.makeText(requireContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show()
                navigationController!!.navigate(R.id.action_loginFragment_to_accountDetailsFragment)
            }
            else
                Toast.makeText(requireContext(), "That user does not exist.",
                    Toast.LENGTH_SHORT).show()

            etLoginUsername.text.clear()
            etLoginPassword.text.clear()
        }

        tvCreateAccountLink.setOnClickListener {
            navigationController!!.navigate(R.id.action_loginFragment_to_createAccountFragment)

            etLoginUsername.text.clear()
            etLoginPassword.text.clear()
        }
    }
}