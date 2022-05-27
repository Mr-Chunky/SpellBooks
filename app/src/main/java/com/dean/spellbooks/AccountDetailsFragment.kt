package com.dean.spellbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text

class AccountDetailsFragment : Fragment(), View.OnClickListener {

    private var navigationController: NavController? = null
    private var dbHandler: DBHandler? = null

    private lateinit var tvBooksProgressIndicator: TextView
    private lateinit var tvBooksGoalIndicator: TextView
    private lateinit var tvPagesProgressIndicator: TextView
    private lateinit var tvPagesGoalIndicator: TextView
    private lateinit var tvMostReadGenreIndicator: TextView
    private lateinit var tvRecentBookIndicator: TextView
    private lateinit var btnLogOut: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_account_details, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)
         dbHandler = DBHandler(requireContext())

        initialiseUIElements(view)
        setUpButtonClickListener()

        tvBooksProgressIndicator.text = 0.toString()
        tvMostReadGenreIndicator.text = dbHandler!!.getUserFavGenre(MainActivity.userID!!)
        tvRecentBookIndicator.text = dbHandler!!.getUserFavBook(MainActivity.userID!!)

        val booksGoal: Int = dbHandler!!.getUserBookGoal(MainActivity.userID!!)
        val booksFinished: Int = tvBooksProgressIndicator.text.toString().toInt()
        tvBooksGoalIndicator.text = (booksGoal - booksFinished).toString()

        val pagesGoal: Int = dbHandler!!.getUserPageGoal(MainActivity.userID!!)
        val pagesFinished: Int = tvPagesProgressIndicator.text.toString().toInt()
        tvPagesGoalIndicator.text = (pagesGoal - pagesFinished).toString()

        val bottomNav: BottomNavigationView? = view.findViewById(R.id.bnvStatsNavigation)
        bottomNav!!.setupWithNavController(navigationController!!)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.user_settings_icon -> {
                    navigationController!!.navigate(R.id.action_accountDetailsFragment_to_editAccountFragment)
                }
                R.id.book_list_icon -> {
                    navigationController!!.navigate(R.id.action_accountDetailsFragment_to_bookListFragment)
                }
            }

            return@setOnItemSelectedListener true
        }
    }

    private fun initialiseUIElements(view: View) {
        tvBooksProgressIndicator = view.findViewById(R.id.tvBooksProgressIndicator)
        tvBooksGoalIndicator = view.findViewById(R.id.tvBooksGoalIndicator)
        tvPagesProgressIndicator = view.findViewById(R.id.tvPagesProgressIndicator)
        tvPagesGoalIndicator = view.findViewById(R.id.tvPagesGoalIndicator)
        tvMostReadGenreIndicator = view.findViewById(R.id.tvMostReadGenreIndicator)
        tvRecentBookIndicator = view.findViewById(R.id.tvRecentBookIndicator)
        btnLogOut = view.findViewById(R.id.btnLogOut)
    }

    private fun setUpButtonClickListener() {
        btnLogOut.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0) {
            btnLogOut -> {
                MainActivity.userID = null
                MainActivity.bookID = null
                navigationController!!.navigate(R.id.action_accountDetailsFragment_to_loginFragment)
                Toast.makeText(requireContext(), "Successfully logged out of the app.  Goodbye", Toast.LENGTH_SHORT).show()
            }
        }
    }
}