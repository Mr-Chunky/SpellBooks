package com.dean.spellbooks

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHandler private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // TODO: CREATE A UTILITY CLASS TO AVOID GOD CLASS

    companion object {

        // Setting up the Singleton with thread-safety
        @Volatile private var instance: DBHandler? = null

        fun getDBHandler(context: Context): DBHandler {
            if (instance != null)
                return instance!!

            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = DBHandler(context)
                    instance!!
                }
            }
        }

        // Setting up database metadata
        private const val DATABASE_VERSION = 8
        private const val DATABASE_NAME = "SpellBooksDatabase"
        private const val TABLE_USERS = "WizardsTable"
        private const val TABLE_BOOKS = "BooksTable"

        // Fields for the users
        private const val KEY_ID = "_user_id"
        private const val KEY_NAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_FAVOURITE_GENRE = "favourite_genre"
        private const val KEY_FAVOURITE_BOOK = "favourite_book"
        private const val KEY_BOOKS_GOAL = "books_goal"
        private const val KEY_PAGES_GOAL = "pages_goal"
        private const val KEY_RECENT_BOOK = "recent_book" // Foreign Key (initially deferred)


        // Fields for the books
        private const val KEY_BOOK_ID = "_book_id"
        private const val KEY_BOOK_TITLE = "book_title"
        private const val KEY_BOOK_AUTHOR = "book_author"
        private const val KEY_BOOK_PAGES = "number_pages"
        private const val KEY_BOOK_GENRE = "book_genre"
        private const val KEY_BOOK_PUBLISHER = "book_publisher"
        private const val KEY_BOOK_YEAR_PUBLISHED = "book_year_published"
        private const val KEY_BOOK_ISBN = "isbn"
        private const val KEY_BOOK_RATING = "book_star_rating"
        private const val KEY_BOOK_READ_STATUS = "book_read_status" // 0 = False (not read yet) | 1 = True (has been read)
        private const val KEY_BOOK_IMAGE = "book_image"
        private const val KEY_BOOK_OWNER = "book_owner" // Foreign Key
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_WIZARDS_TABLE = ("CREATE TABLE $TABLE_USERS($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_NAME TEXT NOT NULL,$KEY_PASSWORD TEXT NOT NULL,$KEY_FAVOURITE_GENRE TEXT NOT NULL,$KEY_FAVOURITE_BOOK TEXT NOT NULL," +
                "$KEY_BOOKS_GOAL INTEGER NOT NULL,$KEY_PAGES_GOAL INTEGER NOT NULL,$KEY_RECENT_BOOK INTEGER)")


        val CREATE_BOOKS_TABLE = ("CREATE TABLE $TABLE_BOOKS($KEY_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_BOOK_TITLE TEXT NOT NULL,$KEY_BOOK_AUTHOR TEXT NOT NULL,$KEY_BOOK_PAGES INTEGER NOT NULL," +
                "$KEY_BOOK_GENRE TEXT NOT NULL,$KEY_BOOK_PUBLISHER TEXT NOT NULL,$KEY_BOOK_YEAR_PUBLISHED INTEGER NOT NULL," +
                "$KEY_BOOK_ISBN TEXT NOT NULL,$KEY_BOOK_RATING REAL,$KEY_BOOK_READ_STATUS INTEGER NOT NULL,$KEY_BOOK_IMAGE BLOB,$KEY_BOOK_OWNER INTEGER NOT NULL," +
                "FOREIGN KEY($KEY_BOOK_OWNER) REFERENCES $TABLE_USERS($KEY_ID))")
        
        p0?.execSQL(CREATE_WIZARDS_TABLE)
        p0?.execSQL(CREATE_BOOKS_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        p0.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(p0)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        db!!.setForeignKeyConstraintsEnabled(true)
    }

    // Add a new book to the books table of the database; currently used in CreateBookFragment.kt
    fun addBook(book: BookModelClass) : Long {
        val database = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_BOOK_ID, book.bookID)
        values.put(KEY_BOOK_TITLE, book.bookTitle)
        values.put(KEY_BOOK_AUTHOR, book.bookAuthor)
        values.put(KEY_BOOK_PAGES, book.bookNumberOfPages)
        values.put(KEY_BOOK_GENRE, book.bookGenre)
        values.put(KEY_BOOK_PUBLISHER, book.bookPublisher)
        values.put(KEY_BOOK_YEAR_PUBLISHED, book.bookYearPublished)
        values.put(KEY_BOOK_ISBN, book.ISBN)
        values.put(KEY_BOOK_RATING, book.bookStarRating)
        values.put(KEY_BOOK_READ_STATUS, book.readStatus)
        values.put(KEY_BOOK_IMAGE, book.bookImage)
        values.put(KEY_BOOK_OWNER, book.bookOwner)

        val response = database.insert(TABLE_BOOKS, null, values)

        database.close()
        return response
    }

    // Add a new user to the users table of the database; currently used in CreateAccountFragment.kt
    fun registerWizard(wiz: UserModelClass): Long {
        val database = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_ID, wiz.userID)
        values.put(KEY_NAME, wiz.username)
        values.put(KEY_PASSWORD, wiz.password)
        values.put(KEY_FAVOURITE_GENRE, wiz.favouriteGenre)
        values.put(KEY_FAVOURITE_BOOK, wiz.favouriteBook)
        values.put(KEY_BOOKS_GOAL, wiz.booksReadingGoal)
        values.put(KEY_PAGES_GOAL, wiz.pagesReadingGoal)
        values.put(KEY_RECENT_BOOK, wiz.recentBook)

        val response = database.insert(TABLE_USERS, null, values)

        database.close()
        return response
    }

    // Fetches an ArrayList containing books that are attributed to a particular userID;
    // currently used in BookListFragment.kt to display a user's added books in the RecyclerView
    fun viewUserBooks(userID: Int): ArrayList<BookModelClass> {
        val bookList: ArrayList<BookModelClass> = ArrayList()

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery("SELECT * FROM $TABLE_BOOKS WHERE $KEY_BOOK_OWNER = ?", arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Error: Unable to find books with the matching user ID.")
        }

        var _id: Int
        var bookTitle: String
        var bookAuthor: String
        var bookNumberOfPages: Int
        var bookGenre: String
        var bookPublisher: String
        var bookYearPublished: Int
        var ISBN: String
        var bookStarRating: Float
        var bookReadStatus: Int
        var bookImage: ByteArray?
        var bookOwner: Int

        if (cursor!!.moveToFirst()) {
            do {
                _id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_ID))
                bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_TITLE))
                bookAuthor = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_AUTHOR))
                bookNumberOfPages = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_PAGES))
                bookGenre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_GENRE))
                bookPublisher = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_PUBLISHER))
                bookYearPublished = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_YEAR_PUBLISHED))
                ISBN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_ISBN))
                bookStarRating = cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_BOOK_RATING))
                bookReadStatus = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_READ_STATUS))
                bookImage = cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_BOOK_IMAGE))
                bookOwner = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_OWNER))

                val book = BookModelClass(_id, bookTitle, bookAuthor, bookNumberOfPages, bookGenre,
                    bookPublisher, bookYearPublished, ISBN, bookStarRating, bookReadStatus, bookImage, bookOwner)

                bookList.add(book)
            } while (cursor.moveToNext())
        }

        cursor.close()
        database.close()

        return bookList
    }

    // Returns a specific book based on that book's bookID
    fun getBook(bookID: Int): BookModelClass? {
        var book: BookModelClass? = null

        val select = "SELECT * FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            database.execSQL(select)
            return null
        }

        var _bookID: Int
        var bookTitle: String
        var bookAuthor: String
        var bookNumberOfPages: Int
        var bookGenre: String
        var bookPublisher: String
        var bookYearPublished: Int
        var bookISBN: String
        var bookStarRating: Float
        var bookReadStatus: Int
        var bookImage: ByteArray?
        var bookOwner: Int

        if (cursor.moveToFirst()) {
            do {
                _bookID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_ID))
                bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_TITLE))
                bookAuthor = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_AUTHOR))
                bookNumberOfPages = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_PAGES))
                bookGenre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_GENRE))
                bookPublisher = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_PUBLISHER))
                bookYearPublished = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_YEAR_PUBLISHED))
                bookISBN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_ISBN))
                bookStarRating = cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_BOOK_RATING))
                bookReadStatus = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_READ_STATUS))
                bookImage = cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_BOOK_IMAGE))
                bookOwner = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_OWNER))

                book = BookModelClass(_bookID, bookTitle, bookAuthor, bookNumberOfPages, bookGenre,
                    bookPublisher, bookYearPublished, bookISBN, bookStarRating, bookReadStatus, bookImage, bookOwner)

            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return book
    }

    // Returns a specific user based on that user's userID
    fun getWizard(userID: Int): UserModelClass? {
        var wiz: UserModelClass? = null

        val select = "SELECT * FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            database.execSQL(select)
            return null
        }

        var _id: Int
        var username: String
        var password: String
        var favouriteGenre: String
        var favouriteBook: String
        var readingBooksGoal: Int
        var readingPagesGoal: Int
        var recentBook: Int

        if (cursor.moveToFirst()) {
            do {
                _id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD))
                favouriteGenre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FAVOURITE_GENRE))
                favouriteBook = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FAVOURITE_BOOK))
                readingBooksGoal = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOKS_GOAL))
                readingPagesGoal = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PAGES_GOAL))
                recentBook = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_RECENT_BOOK))

                wiz = UserModelClass(_id, username,
                    password, favouriteGenre, favouriteBook,
                    readingBooksGoal, readingPagesGoal, recentBook)
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return wiz
    }

    // Tells database to update a particular book's information for real-time "refreshing" of views
    // inside a Fragment (for example in EditBookFragment.kt)
    fun updateBookInformation(book: BookModelClass): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_BOOK_ID, book.bookID)
        values.put(KEY_BOOK_TITLE, book.bookTitle)
        values.put(KEY_BOOK_AUTHOR, book.bookAuthor)
        values.put(KEY_BOOK_PAGES, book.bookNumberOfPages)
        values.put(KEY_BOOK_GENRE, book.bookGenre)
        values.put(KEY_BOOK_PUBLISHER, book.bookPublisher)
        values.put(KEY_BOOK_YEAR_PUBLISHED, book.bookYearPublished)
        values.put(KEY_BOOK_ISBN, book.ISBN)
        values.put(KEY_BOOK_RATING, book.bookStarRating)
        values.put(KEY_BOOK_READ_STATUS, book.readStatus)
        values.put(KEY_BOOK_IMAGE, book.bookImage)
        values.put(KEY_BOOK_OWNER, book.bookOwner)

        val response = database.update(TABLE_BOOKS, values, "$KEY_BOOK_ID = ${book.bookID}",
        null)

        database.close()
        return response
    }

    // Tells database to update a particular user's information for real-time "refreshing" of views
    // inside a fragment (for example in EditAccountFragment.kt)
    fun updateWizardInformation(wiz: UserModelClass): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, wiz.username)
        values.put(KEY_PASSWORD, wiz.password)
        values.put(KEY_FAVOURITE_GENRE, wiz.favouriteGenre)
        values.put(KEY_FAVOURITE_BOOK, wiz.favouriteBook)
        values.put(KEY_BOOKS_GOAL, wiz.booksReadingGoal)
        values.put(KEY_PAGES_GOAL, wiz.pagesReadingGoal)
        values.put(KEY_RECENT_BOOK, wiz.recentBook)

        val response = database.update(TABLE_USERS, values, "$KEY_ID = ${wiz.userID}",
        null)

        database.close()
        return response
    }

    // Not yet implemented, but can be used to delete a book entry from the database
    fun removeBook(book: BookModelClass): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_BOOK_ID, book.bookID)

        val response = database.delete(TABLE_BOOKS, "$KEY_BOOK_ID = ${book.bookID}",
        null)

        database.close()
        return response
    }

    // Not yet implemented, but can be used to delete a user entry from the database
    // IMPORTANT: Do not delete a user before you delete *ALL* of their associated books, or the app
    // will crash and break
    fun removeWizard(wiz: UserModelClass): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_ID, wiz.userID)

        val response = database.delete(TABLE_USERS, "$KEY_ID = ${wiz.userID}",
            null)

        database.close()
        return response
    }

    // Makes sure that a username and password match with what is stored inside the database;
    // returns true if found in database and false if not found in database
    fun checkUsernameAndPassword(username: String, password: String): Boolean {
        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.query(TABLE_USERS, arrayOf(KEY_ID), "$KEY_NAME = ? AND $KEY_PASSWORD = ?",
            arrayOf(username, password), null, null, null)
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Error: Cannot parse the username and password provided.")
        }

        val cursorCount = cursor!!.count
        cursor.close()
        database.close()

        if (cursorCount > 0)
            return true

        return false
    }

    // Used after checkUsernameAndPassword() to hold a reference to that user's ID.  Currently
    // implemented in a way that passes this userID to the MainActivity.kt for a static reference
    // to the currently logged-in user.  This should be improved in the future for security's sake.
    fun getUserID(username: String, password: String): Int {
        var userID: Int = -1

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery("SELECT $KEY_ID FROM $TABLE_USERS WHERE $KEY_NAME = ? AND " +
                    "$KEY_PASSWORD = ?", arrayOf(username, password))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Error: User does not exist.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                userID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return userID
    }

    // Returns the username of a particular user, by passing the MainActivity's userID reference
    fun getUsername(userID: Int): String {
        var username: String = ""

        val select = "SELECT $KEY_NAME FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Unable to get username for wizard.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return username
    }

    // Returns the password of a particular user, by passing the MainActivity's userID reference
    fun getUserPassword(userID: Int): String {
        var password: String = ""

        val select = "SELECT $KEY_PASSWORD FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Unable to get password for wizard.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return password
    }

    // Returns the favourite genre of a particular user, by passing the MainActivity's userID
    // reference
    fun getUserFavGenre(userID: Int): String {
        var favGenre: String = ""

        val select = "SELECT $KEY_FAVOURITE_GENRE FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Unable to get favourite genre for wizard.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                favGenre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FAVOURITE_GENRE))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return favGenre
    }

    // Returns the favourite book of a particular user, by passing the MainActivity's userID
    // reference
    fun getUserFavBook(userID: Int): String {
        var favBook: String = ""

        val select = "SELECT $KEY_FAVOURITE_BOOK FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Unable to get favourite book for wizard.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                favBook = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FAVOURITE_BOOK))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return favBook
    }

    // Returns the book goal of a particular user, by passing the MainActivity's userID reference
    fun getUserBookGoal(userID: Int): Int {
        var bookGoal: Int = 0

        val select = "SELECT $KEY_BOOKS_GOAL FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Unable to get book goal for wizard.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookGoal = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOKS_GOAL))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookGoal
    }

    // Returns the page goal of a particular user, by passing the MainActivity's userID reference
    fun getUserPageGoal(userID: Int): Int {
        var pageGoal: Int = 0

        val select = "SELECT $KEY_PAGES_GOAL FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Unable to get page goal for wizard.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                pageGoal = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PAGES_GOAL))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return pageGoal
    }

    // Returns the recently read book of a particular user, by passing the MainActivity's userID
    // reference
    fun getUserRecentBook(userID: Int): Int {
        var recentBook: Int? = null

        val select = "SELECT $KEY_RECENT_BOOK FROM $TABLE_USERS WHERE $KEY_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite","Unable to get recent book for wizard.")
        }

        if (cursor!!.moveToFirst()) {
            do {
                recentBook = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_RECENT_BOOK))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return recentBook!!
    }

    // Returns the title of a particular book, by passing the MainActivity's bookID reference
    fun getBookTitle(bookID: Int): String {
        var bookTitle: String = ""

        val select = "SELECT $KEY_BOOK_TITLE FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book title given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_TITLE))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookTitle
    }

    // Returns the author of a particular book, by passing the MainActivity's bookID reference
    fun getBookAuthor(bookID: Int): String {
        var bookAuthor: String = ""

        val select = "SELECT $KEY_BOOK_AUTHOR FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book author given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookAuthor = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_AUTHOR))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookAuthor
    }

    // Returns the total pages of a particular book, by passing the MainActivity's bookID reference
    fun getBookTotalPages(bookID: Int): Int {
        var bookTotalPages: Int = 0

        val select = "SELECT $KEY_BOOK_PAGES FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book total pages given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookTotalPages = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_PAGES))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookTotalPages
    }

    // Returns the genre of a particular book, by passing the MainActivity's bookID reference
    fun getBookGenre(bookID: Int): String {
        var bookGenre: String = ""

        val select = "SELECT $KEY_BOOK_GENRE FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book genre given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookGenre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_GENRE))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookGenre
    }

    // Returns the publisher of a particular book, by passing the MainActivity's bookID reference
    fun getBookPublisher(bookID: Int): String {
        var bookPublisher: String = ""

        val select = "SELECT $KEY_BOOK_PUBLISHER FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book publisher given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookPublisher = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_PUBLISHER))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookPublisher
    }

    // Returns the publishing year of a particular book, by passing the MainActivity's bookID
    // reference
    fun getBookYearPublished(bookID: Int): Int {
        var bookYearPublished: Int = 0

        val select = "SELECT $KEY_BOOK_YEAR_PUBLISHED FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book year published given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookYearPublished = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_YEAR_PUBLISHED))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookYearPublished
    }

    // Returns the ISBN code of a particular book, by passing the MainActivity's bookID reference
    fun getBookISBN(bookID: Int): String {
        var bookISBN: String = ""

        val select = "SELECT $KEY_BOOK_ISBN FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book ISBN given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookISBN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_ISBN))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookISBN
    }

    // Returns the rating of a particular book, by passing the MainActivity's bookID reference
    fun getBookStarRating(bookID: Int): Float {
        var bookStarRating: Float = 0F

        val select = "SELECT $KEY_BOOK_RATING FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book rating given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookStarRating = cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_BOOK_RATING))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookStarRating
    }

    // Returns the image ByteArray of a particular book, by passing the MainActivity's bookID
    // reference; should be converted to a Bitmap thereafter to use in displaying images to
    // ImageViews and ImageButtons
    fun getBookImage(bookID: Int): ByteArray? {
        var bookImage: ByteArray? = null

        val select = "SELECT $KEY_BOOK_IMAGE FROM $TABLE_BOOKS WHERE $KEY_BOOK_ID = ?"

        val database = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf(bookID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get book image given the book ID")
        }

        if (cursor!!.moveToFirst()) {
            do {
                bookImage = cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_BOOK_IMAGE))
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return bookImage
    }

    // Returns an ArrayList of all books with a read status of 1 that belong to a particular user
    fun getUserCompletedBooks(userID: Int): ArrayList<BookModelClass> {
        val completedBooks: ArrayList<BookModelClass> = ArrayList()

        val select = "SELECT * FROM $TABLE_BOOKS WHERE $KEY_BOOK_READ_STATUS = ? AND $KEY_BOOK_OWNER = ?"

        val database = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = database.rawQuery(select, arrayOf("1", userID.toString()))
        } catch (exception: SQLiteException) {
            Log.e("sqlite", "Unable to get completed books for the specified user")
        }

        var _bookID: Int
        var bookTitle: String
        var bookAuthor: String
        var bookNumberOfPages: Int
        var bookGenre: String
        var bookPublisher: String
        var bookYearPublished: Int
        var bookISBN: String
        var bookStarRating: Float
        var bookReadStatus: Int
        var bookImage: ByteArray?

        var bookOwner: Int

        if (cursor!!.moveToFirst()) {
            do {
                _bookID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_ID))
                bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_TITLE))
                bookAuthor = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_AUTHOR))
                bookNumberOfPages = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_PAGES))
                bookGenre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_GENRE))
                bookPublisher = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_PUBLISHER))
                bookYearPublished = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_YEAR_PUBLISHED))
                bookISBN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOK_ISBN))
                bookStarRating = cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_BOOK_RATING))
                bookReadStatus = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_READ_STATUS))
                bookImage = cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_BOOK_IMAGE))
                bookOwner = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_OWNER))

                val book = BookModelClass(_bookID, bookTitle, bookAuthor, bookNumberOfPages, bookGenre,
                    bookPublisher, bookYearPublished, bookISBN, bookStarRating, bookReadStatus, bookImage, bookOwner)

                completedBooks.add(book)
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return completedBooks
    }

    // Receives completedBooks ArrayList from the getUserCompletedBooks() function and calculates
    // which genre is read most from a list of pre-defined genres that have been plugged into
    // spinners throughout the app
    fun calculateMostReadGenre(completedBooks: ArrayList<BookModelClass>): String {
        var fantasyCount: Int = 0
        var horrorCount: Int = 0
        var childrenCount: Int = 0
        var nonfictionCount: Int = 0
        var scienceFictionCount: Int = 0
        var romanceCount: Int = 0
        var selfHelpCount: Int = 0
        var religiousCount: Int = 0

        for (book in completedBooks) {
            when (book.bookGenre) {
                "Fantasy" -> fantasyCount++
                "Horror" -> horrorCount++
                "Children" -> childrenCount++
                "Non-fiction" -> nonfictionCount++
                "Science Fiction" -> scienceFictionCount++
                "Romance" -> romanceCount++
                "Self-Help" -> selfHelpCount++
                "Religious" -> religiousCount++
            }
        }
        if (fantasyCount > horrorCount && fantasyCount > childrenCount && fantasyCount > nonfictionCount &&
                fantasyCount > scienceFictionCount && fantasyCount > romanceCount && fantasyCount > selfHelpCount &&
                fantasyCount > religiousCount)
            return "Fantasy"
        else if (horrorCount > fantasyCount && horrorCount > childrenCount && horrorCount > nonfictionCount &&
                horrorCount > scienceFictionCount && horrorCount > romanceCount && horrorCount > selfHelpCount &&
                horrorCount > religiousCount)
            return "Horror"
        else if (childrenCount > fantasyCount && childrenCount > horrorCount && childrenCount > nonfictionCount &&
                childrenCount > scienceFictionCount && childrenCount > romanceCount && childrenCount > selfHelpCount &&
                childrenCount > religiousCount)
            return "Children"
        else if (nonfictionCount > fantasyCount && nonfictionCount > horrorCount && nonfictionCount > childrenCount &&
                nonfictionCount > scienceFictionCount && nonfictionCount > romanceCount && nonfictionCount > selfHelpCount &&
                nonfictionCount > religiousCount)
            return "Non-fiction"
        else if (scienceFictionCount > fantasyCount && scienceFictionCount > horrorCount && scienceFictionCount > childrenCount &&
                scienceFictionCount > nonfictionCount && scienceFictionCount > romanceCount && scienceFictionCount > selfHelpCount &&
                scienceFictionCount > religiousCount)
            return "Science Fiction"
        else if (romanceCount > fantasyCount && romanceCount > horrorCount && romanceCount > childrenCount && romanceCount > nonfictionCount &&
                romanceCount > scienceFictionCount && romanceCount > selfHelpCount && romanceCount > religiousCount)
            return "Romance"
        else if (selfHelpCount > fantasyCount && selfHelpCount > horrorCount && selfHelpCount > childrenCount && selfHelpCount > nonfictionCount &&
                selfHelpCount > scienceFictionCount && selfHelpCount > romanceCount && selfHelpCount > religiousCount)
            return "Self-Help"
        else if (religiousCount > fantasyCount && religiousCount > horrorCount && religiousCount > childrenCount && religiousCount > nonfictionCount &&
                religiousCount > scienceFictionCount && religiousCount > romanceCount && religiousCount > selfHelpCount)
            return "Religious"

        return "No genre is read more than another"
    }

    // Receives completedBooks ArrayList from the getUserCompletedBooks() function to calculate
    // how many pages the user has successfully read based on the books they've completed
    fun calculatePagesProgress(completedBooks: ArrayList<BookModelClass>): Int {
        var pagesProgress: Int = 0

        if (completedBooks.isNotEmpty()) {
            for (book in completedBooks)
                pagesProgress += book.bookNumberOfPages
        }
        return pagesProgress
    }
}