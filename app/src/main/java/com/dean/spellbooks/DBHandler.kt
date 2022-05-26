package com.dean.spellbooks

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // TODO: ADD A NEW FIELD TO USERS (RECENT BOOK) AND BOOKS (IS READ)

    companion object {
        private const val DATABASE_VERSION = 5
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
        private const val KEY_BOOK_OWNER = "book_owner" // Foreign Key
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_WIZARDS_TABLE = ("CREATE TABLE $TABLE_USERS($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_NAME TEXT NOT NULL,$KEY_PASSWORD TEXT NOT NULL,$KEY_FAVOURITE_GENRE TEXT NOT NULL,$KEY_FAVOURITE_BOOK TEXT NOT NULL," +
                "$KEY_BOOKS_GOAL INTEGER,$KEY_PAGES_GOAL INTEGER)")

        val CREATE_BOOKS_TABLE = ("CREATE TABLE $TABLE_BOOKS($KEY_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_BOOK_TITLE TEXT NOT NULL,$KEY_BOOK_AUTHOR TEXT NOT NULL,$KEY_BOOK_PAGES INTEGER NOT NULL," +
                "$KEY_BOOK_GENRE TEXT NOT NULL,$KEY_BOOK_PUBLISHER TEXT NOT NULL,$KEY_BOOK_YEAR_PUBLISHED INTEGER NOT NULL," +
                "$KEY_BOOK_ISBN TEXT NOT NULL,$KEY_BOOK_RATING REAL,$KEY_BOOK_OWNER INTEGER NOT NULL," +
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
        values.put(KEY_BOOK_OWNER, book.bookOwner)

        val response = database.insert(TABLE_BOOKS, null, values)

        database.close()
        return response
    }

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

        val response = database.insert(TABLE_USERS, null, values)

        database.close()
        return response
    }

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
                bookOwner = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_OWNER))

                val book = BookModelClass(_id, bookTitle, bookAuthor, bookNumberOfPages, bookGenre,
                    bookPublisher, bookYearPublished, ISBN, bookStarRating, bookOwner)
                bookList.add(book)
            } while (cursor.moveToNext())
        }

        cursor.close()
        database.close()

        return bookList
    }

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
                bookOwner = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOK_OWNER))

                book = BookModelClass(_bookID, bookTitle, bookAuthor, bookNumberOfPages, bookGenre,
                    bookPublisher, bookYearPublished, bookISBN, bookStarRating, bookOwner)
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return book
    }

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

        if (cursor.moveToFirst()) {
            do {
                _id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD))
                favouriteGenre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FAVOURITE_GENRE))
                favouriteBook = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FAVOURITE_BOOK))
                readingBooksGoal = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BOOKS_GOAL))
                readingPagesGoal = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PAGES_GOAL))

                wiz = UserModelClass(_id, username,
                    password, favouriteGenre, favouriteBook,
                    readingBooksGoal, readingPagesGoal)
            } while (cursor.moveToNext())
        }
        cursor.close()
        database.close()
        return wiz
    }

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
        values.put(KEY_BOOK_OWNER, book.bookOwner)

        val response = database.update(TABLE_BOOKS, values, "$KEY_BOOK_ID = ${book.bookID}",
        null)

        database.close()
        return response
    }

    fun updateWizardInformation(wiz: UserModelClass): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, wiz.username)
        values.put(KEY_PASSWORD, wiz.password)
        values.put(KEY_FAVOURITE_GENRE, wiz.favouriteGenre)
        values.put(KEY_FAVOURITE_BOOK, wiz.favouriteBook)
        values.put(KEY_BOOKS_GOAL, wiz.booksReadingGoal)
        values.put(KEY_PAGES_GOAL, wiz.pagesReadingGoal)

        val response = database.update(TABLE_USERS, values, "$KEY_ID = ${wiz.userID}",
        null)

        database.close()
        return response
    }

    fun removeBook(book: BookModelClass): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_BOOK_ID, book.bookID)

        val response = database.delete(TABLE_BOOKS, "$KEY_BOOK_ID = ${book.bookID}",
        null)

        database.close()
        return response
    }

    fun removeWizard(wiz: UserModelClass): Int {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_ID, wiz.userID)

        val response = database.delete(TABLE_USERS, "$KEY_ID = ${wiz.userID}",
            null)

        database.close()
        return response
    }

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
}