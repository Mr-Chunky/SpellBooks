package com.dean.spellbooks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class BookListFragment : Fragment() {

    var navigationController: NavController? = null

    companion object {
        lateinit var dbHandler: DBHandler
        lateinit var recyclerView: RecyclerView
        lateinit var adapter: BookAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationController = Navigation.findNavController(view)
        dbHandler = DBHandler.getDBHandler(requireContext())
        recyclerView = view.findViewById(R.id.rvBookList)
        val ivAddBook: ImageView = view.findViewById(R.id.ivAddBook)

        val bottomNav: BottomNavigationView = view.findViewById(R.id.bnvBookListNavigation)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.user_settings_icon -> {
                    navigationController!!.navigate(R.id.action_bookListFragment_to_editAccountFragment)
                }
                R.id.stats_icon -> {
                    navigationController!!.navigate(R.id.action_bookListFragment_to_accountDetailsFragment)
                }
            }

            return@setOnItemSelectedListener true
        }

        ivAddBook.setOnClickListener(View.OnClickListener {
            navigationController!!.navigate(R.id.action_bookListFragment_to_createBookFragment)
        })

        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        val bookList = dbHandler.viewUserBooks(MainActivity.userID!!)
        adapter = BookAdapter(requireContext(), bookList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) as RecyclerView.LayoutManager
        recyclerView.adapter = adapter

        adapter.setOnItemSelectedListener(object: BookAdapter.onItemSelectedListener {
            override fun onItemSelected(position: Int) {
                val book = bookList[position]
                MainActivity.bookID = book.bookID

                navigationController!!.navigate(R.id.action_bookListFragment_to_editBookFragment)
            }
        })
    }
}