package com.dean.spellbooks

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.EditText
import java.io.ByteArrayOutputStream

class SpellBooksUtils {

    companion object {

        // Checks to see if an EditText's field is empty; Returns true if it is and false otherwise
        fun checkEmpty(editText: EditText): Boolean {
            if (editText.text.toString().trim() == "") {
                return true
            }
            return false
        }

        // Checks to see if a given string can be converted to its numerical equivalent (int, float, etc.)
        fun isNumeric(string: String): Boolean {
            return string.all { char -> char.isDigit() }
        }

        // Takes a Bitmap and converts it into a ByteArray to be stored in the database for retrieval later
        fun convertToByteArray(bitmap: Bitmap): ByteArray? {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
            val pictureByteArray = stream.toByteArray()
            stream.close()
            return pictureByteArray
        }

        // Takes a ByteArray and converts it into a Bitmap to be displayed in an ImageButton or ImageView
        fun convertToBitmap(image: ByteArray): Bitmap? {
            return BitmapFactory.decodeByteArray(image, 0, image.size)
        }
    }
}