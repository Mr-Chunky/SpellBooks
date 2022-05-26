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
    private lateinit var etPagesProgressEdit: EditText
    private lateinit var ibConfirmPagesProgressEdit: ImageButton
    private lateinit var ibCancelPagesProgressEdit: ImageButton
    private lateinit var btnEditPagesProgress: Button
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
        setOptionalElementsVisibility(false)
        setUpButtonClickListeners()

        tvBooksProgressIndicator.text = MainActivity.checkCount.toString()
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
        etPagesProgressEdit = view.findViewById(R.id.etPagesProgressEdit)
        ibConfirmPagesProgressEdit = view.findViewById(R.id.ibConfirmPagesProgressEdit)
        ibCancelPagesProgressEdit = view.findViewById(R.id.ibCancelPagesProgressEdit)
        btnEditPagesProgress = view.findViewById(R.id.btnEditPagesProgress)
        btnLogOut = view.findViewById(R.id.btnLogOut)
    }

    private fun setOptionalElementsVisibility(visibility: Boolean) {
        if (visibility) {
            ibConfirmPagesProgressEdit.visibility = View.VISIBLE
            ibCancelPagesProgressEdit.visibility = View.VISIBLE
            etPagesProgressEdit.visibility = View.VISIBLE
        }
        else if (!visibility) {
            ibConfirmPagesProgressEdit.visibility = View.GONE
            ibCancelPagesProgressEdit.visibility = View.GONE
            etPagesProgressEdit.visibility = View.GONE
        }
    }

    private fun setUpButtonClickListeners() {
        btnEditPagesProgress.setOnClickListener(this)
        btnLogOut.setOnClickListener(this)
    }

    private fun clearEditField() {
        etPagesProgressEdit.text.clear()
    }

    override fun onClick(p0: View?) {
        when(p0) {
            btnEditPagesProgress -> {
                btnEditPagesProgress.visibility = View.GONE
                setOptionalElementsVisibility(true)

                ibCancelPagesProgressEdit.setOnClickListener(View.OnClickListener {
                    clearEditField()
                    setOptionalElementsVisibility(false)
                    btnEditPagesProgress.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show()
                })

                ibConfirmPagesProgressEdit.setOnClickListener(View.OnClickListener {
                    val pagesProgress = etPagesProgressEdit.text.toString().toInt()

                    if (pagesProgress.toString().isNotEmpty()) {

                        clearEditField()
                        setOptionalElementsVisibility(false)
                        tvPagesProgressIndicator.text = pagesProgress.toString()
                        btnEditPagesProgress.visibility = View.VISIBLE
                        val booksGoal: Int = dbHandler!!.getUserBookGoal(MainActivity.userID!!)
                        val booksFinished: Int = tvBooksProgressIndicator.text.toString().toInt()
                        tvBooksGoalIndicator.text = (booksGoal - booksFinished).toString()

                        Toast.makeText(requireContext(), "Successfully updated current pages progress", Toast.LENGTH_SHORT).show()
                    }
                })

            }
            btnLogOut -> {
                MainActivity.userID = null
                MainActivity.bookID = null
                MainActivity.checkCount = 0
                navigationController!!.navigate(R.id.action_accountDetailsFragment_to_loginFragment)
                Toast.makeText(requireContext(), "Successfully logged out of the app.  Goodbye", Toast.LENGTH_SHORT).show()
            }
        }
    }
}