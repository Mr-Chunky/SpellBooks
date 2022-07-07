package com.dean.spellbooks

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class DatabaseTests {

    private lateinit var dbHandler: DBHandler

    // Pre-test setup
    @Before
    fun setUpDBHandler() {
        dbHandler = DBHandler.getDBHandler(RuntimeEnvironment.application)
        dbHandler.clearDatabase()
    }

    // Post-test cleanup
    @After
    fun clearDatabase() {
        dbHandler.resetDatabase()
    }

    // Testing to see if a new user can be successfully added to the database
    @Test
    fun addNewWizard() {
        // Creating two new users
        val firstUser = UserModelClass(null, "First", "first",
            "Religious", "The Flying Spaghetti Monster", 3,
            307, null)
        val secondUser = UserModelClass(null, "Second", "second",
            "Children", "Goldilocks and the Three Bears", 4,
            411, null)

        // Adding those two users to database
        dbHandler.registerWizard(firstUser)
        dbHandler.registerWizard(secondUser)

        // Confirming that they are indeed found in the database
        assertEquals(dbHandler.checkUsernameAndPassword(firstUser.username, firstUser.password), true)
        assertEquals(dbHandler.checkUsernameAndPassword(secondUser.username, secondUser.password), true)
    }

    // Testing to see if a new book can be successfully added to the database and attributed to
    // a specific user
    @Test
    fun addNewBook() {
        // Creating a new user
        val firstUser = UserModelClass(null, "First", "first",
            "Religious", "The Flying Spaghetti Monster", 3,
            307, null)

        // Adding user to database
        dbHandler.registerWizard(firstUser)

        // Getting the sample user's information for foreign key purposes
        val bookOwner = dbHandler.getUserID(firstUser.username, firstUser.password)

        // Creating two new books
        val firstBook = BookModelClass(null, "First Book", "Random Author",
            115, "Fantasy", "Random Publisher",
            1887, "RND-2981374", 3f, 0, null,
            bookOwner)
        val secondBook = BookModelClass(null, "Second Book", "Random Author",
            107, "Religious", "Random Publisher",
            1995, "RND-295432", 2f, 0, null,
            bookOwner)

        // Adding these two books to the database
        dbHandler.addBook(firstBook)
        dbHandler.addBook(secondBook)


        // Confirming that they are indeed found in the database, and that they belong to the user
        // specified above
        assertEquals(dbHandler.getUserBookCount(bookOwner), 2)
    }
}
