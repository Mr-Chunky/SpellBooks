package com.dean.spellbooks

class BookModelClass(val bookID: Int?, var bookTitle: String, var bookAuthor: String, var bookNumberOfPages: Int,
                          var bookGenre: String, var bookPublisher: String, var bookYearPublished: Int, var ISBN: String,
                          var bookStarRating: Float, var readStatus: Int, var bookImage: ByteArray?, var bookOwner: Int)

