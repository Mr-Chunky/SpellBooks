#### 1.0.0 (2022-07-07)

##### Chores

* **testing:**  Add unit tests for database operations, add robolectric dependency ([13651e57](https://github.com/Mr-Chunky/SpellBooks/commit/13651e57b3cc33f1809641c45db318ef58ed25d5))
* **fix:**  Enforce unique usernames, add username checking to database ([350617d9](https://github.com/Mr-Chunky/SpellBooks/commit/350617d9015eb8c8ee365d46b11f01f2dbb63420))

##### Refactors

* **maintenance:**  Remove re-used code, add Utility Class, clear unnecessary redundancies ([82ac8b81](https://github.com/Mr-Chunky/SpellBooks/commit/82ac8b8185334fa72a17cc93c3e9c03a58ed2ad5))

------------------------------------------------------------------------
### 1.1.0 (2022-06-13)

##### Chores

* **layout:**
  *  Change EditText hint depending on edit button pressed ([2b248c8c](https://github.com/Mr-Chunky/SpellBooks/commit/2b248c8cab4234b895cba5f5784c98b9df8fd6f3))
  *  Change EditText hint depending on edit button pressed ([a6b9f413](https://github.com/Mr-Chunky/SpellBooks/commit/a6b9f413325364ff17fedbea5f46755e6b99f2cc))
  *  Align fragment_edit_account.xml layout properly ([ccb2c258](https://github.com/Mr-Chunky/SpellBooks/commit/ccb2c25818926a9714a600e9c38306397aab8976))
* **database:**  Document database functions, resolve TODOs ([2dc83c5b](https://github.com/Mr-Chunky/SpellBooks/commit/2dc83c5bc9220829c66b356666120b173991635d))

##### Bug Fixes

* **images:**
  *  Display current book image in EditBookFragment.kt ([d893ee3c](https://github.com/Mr-Chunky/SpellBooks/commit/d893ee3cc1fcdfea8541f5b43300a4e6ac2ae55b))
  *  Fix picture position on list item ([f2cc02c0](https://github.com/Mr-Chunky/SpellBooks/commit/f2cc02c0d16da1e3dc0f14dc16a18e9acbebbe3a))

##### Refactors

* **general:**  Remove unused imports ([cdad8566](https://github.com/Mr-Chunky/SpellBooks/commit/cdad8566fb0646f8354297c3a5e369b03e5bdcfe))
* **database:**  Implement DBHandler.kt as thread-safe Singleton ([2ba0bafb](https://github.com/Mr-Chunky/SpellBooks/commit/2ba0bafb09bda5eb527fa9e6f0cb11d6a0f991e7))
* **formatting:**  Format text for legibility ([08f05755](https://github.com/Mr-Chunky/SpellBooks/commit/08f0575555a42f6c4c6193bbc4ae12b4e8b8a5a5))
* **clean-up:**  Fix dark background and refactor for legibility ([d0df5be2](https://github.com/Mr-Chunky/SpellBooks/commit/d0df5be28e9fea2ad9179a50f23c80465170989d))

##### Code Style Changes

* **layout:**
  *  Revise layout of fragment_edit_book.xml ([8183a8cc](https://github.com/Mr-Chunky/SpellBooks/commit/8183a8cc4e0a7a08abec65b0994091b3b53ac203))
  *  Revise layout of fragment_edit_account.xml ([1b01c43b](https://github.com/Mr-Chunky/SpellBooks/commit/1b01c43b2efe0cd6af20914e37d01d21651d28d8))

------------------------------------------------------------------------

#### 1.0.0 (2022-06-05)

##### Bug Fixes

* **accessibility:**  Add ScrollViews, adjust colours ([9d16a5a7](https://github.com/Mr-Chunky/SpellBooks/commit/9d16a5a794668121b2b1f9ab3bba6685647c26ed))

------------------------------------------------------------------------

#### 1.0.0 (2022-06-04)

##### Documentation Changes

* **update:**
  *  Update changelog ([e9118028](https://github.com/Mr-Chunky/SpellBooks/commit/e911802838fdc85b3ad10164faccddccfbe6065c))
  *  Update README.md ([119d52a2](https://github.com/Mr-Chunky/SpellBooks/commit/119d52a2a54f545ac2f3d39bd08ec515a1db2b5a))
* **additions:**  Added CHANGELOG ([9d005144](https://github.com/Mr-Chunky/SpellBooks/commit/9d005144491ac8555ff1fa63e4635e1db9f08577))

##### New Features

* **images:**
  *  Allow editing of book picture ([a68a9c86](https://github.com/Mr-Chunky/SpellBooks/commit/a68a9c8605106004c7aed420ecd2a3d876f0d531))
  *  Add camera functionality, save pictures ([40689e20](https://github.com/Mr-Chunky/SpellBooks/commit/40689e205f01a52187dd4f9b1bd020839a4f9be1))

##### Bug Fixes

* **images:**  Fix picture position on list item ([f2cc02c0](https://github.com/Mr-Chunky/SpellBooks/commit/f2cc02c0d16da1e3dc0f14dc16a18e9acbebbe3a))

------------------------------------------------------------------------

#### 1.0.0 (2022-05-30)

##### Breaking Changes

* **database:**  Add new field to database, update database version ([0b667719](https://github.com/Mr-Chunky/SpellBooks/commit/0b6677193777024b00477cd4f860da64f313c971))

##### Chores

* **additions:**  Add picture placeholders to books ([5dc5d494](https://github.com/Mr-Chunky/SpellBooks/commit/5dc5d4944998286b17e70e18eceaceefa0a0cfea))

##### Refactors

* **formatting:**  Format text for legibility ([08f05755](https://github.com/Mr-Chunky/SpellBooks/commit/08f0575555a42f6c4c6193bbc4ae12b4e8b8a5a5))

------------------------------------------------------------------------

#### 1.0.0 (2022-05-28)

##### Chores

* **comment:**  Change TODO in database ([d9c7bc51](https://github.com/Mr-Chunky/SpellBooks/commit/d9c7bc51a3adc7213a7dcfad74564005947b0a97))
* **additions:**
  *  Add progress bar, delete and refactor, edit themes and add ImageView placeholder ([cc455041](https://github.com/Mr-Chunky/SpellBooks/commit/cc45504105ebe4542448df2ba410a20b43f9ce16))
  *  Add stats to AccountDetailsFragment.kt and refactor, add spinners to layouts, add two functions to database ([1d0c1151](https://github.com/Mr-Chunky/SpellBooks/commit/1d0c11510311a6fd5a42c7adf21b0f3bd319b1bb))
  *  Add missing info to AccountDetailsFragment.kt, add new function to database, add new saved string array ([411ad496](https://github.com/Mr-Chunky/SpellBooks/commit/411ad496ccb1de70051a7a10352ca853218df1e1))
  *  Add checkbox functionality and refactor AccountDetailsFragment.kt ([149b1106](https://github.com/Mr-Chunky/SpellBooks/commit/149b1106bd5bb5150e229899ef497ad20ac2039b))
  *  Add two new fields to model classes and amend database ([b688982c](https://github.com/Mr-Chunky/SpellBooks/commit/b688982cad21cfbcb14f3bc20187a313a590dafb))
  *  Added Apache License ([ad2fb509](https://github.com/Mr-Chunky/SpellBooks/commit/ad2fb509b13ecad145460cbc8f1d29f33bd6c16d))
  *  Added a README ([13a90c48](https://github.com/Mr-Chunky/SpellBooks/commit/13a90c4867bfca45670b101b73ad8c097b06b421))
  *  Initial commit of Android project ([1b973695](https://github.com/Mr-Chunky/SpellBooks/commit/1b973695f3af5cd35f10b305f05652b66021304f))
  *  Initial commit of Android project ([5e4f7863](https://github.com/Mr-Chunky/SpellBooks/commit/5e4f78633423624468221def01a9dfbd4f80b2dd))
  *  Initial commit of Android project ([60e88ff6](https://github.com/Mr-Chunky/SpellBooks/commit/60e88ff6ba5ad9babe8b7b91a8d3cd003ee55b6c))

##### Documentation Changes

* **update:**  Update README.md ([119d52a2](https://github.com/Mr-Chunky/SpellBooks/commit/119d52a2a54f545ac2f3d39bd08ec515a1db2b5a))

##### Refactors

* **clean-up:**  Fix dark background and refactor for legibility ([d0df5be2](https://github.com/Mr-Chunky/SpellBooks/commit/d0df5be28e9fea2ad9179a50f23c80465170989d))


