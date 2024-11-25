package com.shivam_raj.noteapp.screens.noteSecurityScreen

import java.io.Serializable

/**
 * A data class which represent security details(password, fake title, fake description)
 * @param password Password of the note. It can be null if the user haven't set any password.
 * @param fakeTitle Fake title of the note. It can be null if the user haven't set any fake title.
 * @param fakeDescription Fake description of the note. It can be null if the user haven't set any fake description.
 */
data class SecurityData(
    var password: String? = null,
    var fakeTitle: String? = null,
    var fakeDescription: String? = null
) : Serializable