package models

data class User (val uid: String, val name: String) {
    constructor(): this("", "")
}

data class Message (val fromUid: String, val toUid: String, val content: String, val sendAt: Long) {
    constructor(): this("", "", "", System.currentTimeMillis())
}