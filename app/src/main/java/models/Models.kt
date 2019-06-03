package models

import android.net.Uri

data class SignUpUser(val uid: String, val name: String) {
    constructor(): this("", "")
}
data class User (val uid: String, val name: String, var petName: String, var petDescription: String, var petBirth: String, var petType: String, var petGender: String, var profilePicture: String) {
    constructor(): this("", "", "", "", "", "", "", "")
}

data class Message (val fromUid: String, val toUid: String, val content: String, val sendAt: Long) {
    constructor(): this("", "", "", System.currentTimeMillis())
}

data class Card (val name: String, val image: String = "https://images.pexels.com/photos/371633/pexels-photo-371633.jpeg") {
    constructor(): this("SUDO LE CHIEN")
}

data class CreateProfileInfo (val petName: String, val petDescription: String, val petBirth: String, val petType: String, val petGender: String, val profilePicture: String) {
    constructor(): this("", "", "", "", "", "")
}