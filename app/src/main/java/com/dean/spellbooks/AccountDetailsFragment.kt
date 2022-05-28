package com.dean.spellbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class AccountDetailsFragment : Fragment(), View.OnClickListener {

    private var navigationController: NavController? = null
    private var dbHandler: DBHandler? = null

    private lateinit var pbBooksProgress: ProgressBar
    private lateinit var pbPagesProgress: ProgressBar
    private lateinit var tvBooksProgressIndicator: TextView
    private lateinit var tvPagesProgressIndicator: TextView
    private lateinit var tvMostReadGenreIndicator: TextView
    private lateinit var tvRecentBookIndicator: TextView
    private lateinit var btnLogOut: Button

    private lateinit var completedBooks: ArrayList<BookModelClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)
        dbHandler = DBHandler(requireContext())
        completedBooks = dbHandler!!.getUserCompletedBooks(MainActivity.userID!!)


        initialiseUIElements(view)

        setUpButtonClickListener()

        setUpStats()

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
        pbBooksProgress = view.findViewById(R.id.pbBooksProgress)
        pbPagesProgress = view.findViewById(R.id.pbPagesProgress)
        tvBooksProgressIndicator = view.findViewById(R.id.tvBooksProgressIndicator)
        tvPagesProgressIndicator = view.findViewById(R.id.tvPagesProgressIndicator)
        tvMostReadGenreIndicator = view.findViewById(R.id.tvMostReadGenreIndicator)
        tvRecentBookIndicator = view.findViewById(R.id.tvRecentBookIndicator)
        btnLogOut = view.findViewById(R.id.btnLogOut)
    }

    private fun setUpStats() {
        val completedBooksCount: Int = completedBooks.size
        val mostReadGenre: String = dbHandler!!.calculateMostReadGenre(completedBooks)
        val pagesProgress: Int = dbHandler!!.calculatePagesProgress(completedBooks)
        val recentBook: Int? = dbHandler!!.getUserRecentBook(MainActivity.userID!!)

        // Setting up the most read book genre indicator
        tvMostReadGenreIndicator.text = mostReadGenre

        // Setting up the most recent book read indicator
        if (recentBook != null)
            tvRecentBookIndicator.text = dbHandler!!.getBookTitle(recentBook)
        else
            tvRecentBookIndicator.text = R.string.no_books_read.toString()

        // Setting up the books progress bar
        pbBooksProgress.max = dbHandler!!.getUserBookGoal(MainActivity.userID!!)
        pbBooksProgress.progress = completedBooksCount
        tvBooksProgressIndicator.text = "${pbBooksProgress.progress}/${pbBooksProgress.max}" // TODO: FIND A BEST-PRACTICE WAY TO IMPLEMENT THIS

        // Setting up the pages progress bar
        pbPagesProgress.max = dbHandler!!.getUserPageGoal(MainActivity.userID!!)
        pbPagesProgress.progress = pagesProgress
        tvPagesProgressIndicator.text = "${pbPagesProgress.progress}/${pbPagesProgress.max}" // TODO: FIND A BEST-PRACTICE WAY TO IMPLEMENT THIS
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