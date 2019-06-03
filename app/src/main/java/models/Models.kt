package models

data class User (val uid: String, val name: String) {
    constructor(): this("", "")
}

data class Message (val fromUid: String, val toUid: String, val content: String, val sendAt: Long) {
    constructor(): this("", "", "", System.currentTimeMillis())
}

data class Card (val name: String, val image: String = "https://images.pexels.com/photos/371633/pexels-photo-371633.jpeg") {
    constructor(): this("SUDO LE CHIEN")
}