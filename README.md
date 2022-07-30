# SpellBooks

My first completed Android mobile application.  The app is built entirely with Kotlin.


The app is built for users who have trouble keeping track of the physical hard-cover books that they own, and thus end up either buying duplicates or forgetting about them entirely.  It is built on top of a SQLite backend, so multiple users can use the same device to access their content in a segregated fashion, as long as they log in with the appropriate credentials.  A few notable features include the ability for a user to set a goal for the amount of books they'd like to read in a single year as well as the total pages they'd like to read by the end of that year.

## Development Environment

* Android Studio (Chipmunk)
* **Target:** API 23

## Demonstration

### Step 1: Create an account and log in.
_Unique usernames are enforced under the hood and the user will not be allowed to create a new account if the username they have chosen already exists._

![login-process](img/login-process.gif)

### Step 2: View account details and edit those details as the user wishes.

![edit-account-process](img/edit-account-process.gif)

### Step 3: Create a new book to add to the user's list of owned books.

![create-book-process](img/create-book-process.gif)

### Step 4: View the book's details by tapping on the book in the dedicated RecyclerView that contains it.  Thereafter, the user may choose to edit any of the book's details, including a custom picture from their device to represent said book.  Finally, log out of the application.

![edit-book-process](img/edit-book-process.gif)
## Instructions

_Side Note: This app is unreleased._

If you would like to view the source code associated with this project, navigate to **app/src/main/java/com/dean/SpellBooks** to view all of the associated fragments, the main activity and the helper classes.
