package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(fullName: String, email: String, password: String): User {
        val newUser = User.makeUser(fullName, email = email, password = password)
        require(!map.containsKey(newUser.login)) { "A user with this email already exists" }
        return newUser.also { user -> map[user.login] = user }
    }


    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        val number = rawPhone.replace("[^+\\d]|(?<=.)\\+".toRegex(), "")
        require(number.length == 12) { "Enter a valid phone number starting with a + and containing 11 digits" }
        require(map[number] == null) { "A user with this phone already exists" }
        return User.makeUser(fullName, phone = number)
            .also { user -> map[number] = user }
    }

    fun loginUser(login: String, password: String): String? {
        return map[login.trim()]?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearMap() = map.clear()
}
