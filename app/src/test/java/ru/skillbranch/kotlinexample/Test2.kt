package ru.skillbranch.kotlinexample

import org.junit.After
import org.junit.Assert
import org.junit.Test
import ru.skillbranch.kotlinexample.extensions.dropLastUntil

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Test2 {

    @After
    fun after() {
        val holder = UserHolder
        holder.clearMap()
    }

    @Test
    fun import_users_from_csv() {
        val holder = UserHolder
        val listCsvUsers =
            arrayListOf(
                " John Doe ;John_Doe@unknown.com;[B@7591083d:c6adb4becdc64e92857e1e2a0fd6af84;;",
                " John Doe1 ;;[B@7591083d:c6adb4becdc64e92857e1e2a0fd6af84;+7 (917) 971-11-11;",
                " John Doe1 ;John_Doe1@unknown.com;[B@7591083d:c6adb4becdc64e92857e1e2a0fd6af84;;"
            )

        holder.importUsers(listCsvUsers)

        val expectedInfo = """
            firstName: John
            lastName: Doe
            login: john_doe@unknown.com
            fullName: John Doe
            initials: J D
            email: John_Doe@unknown.com
            phone: null
            meta: {src=csv}
        """.trimIndent()

        val successResult = holder.loginUser("john_doe@unknown.com", "testPass")

        Assert.assertEquals(expectedInfo, successResult)
    }

    @Test
    fun drop_Last_Until() {

        Assert.assertEquals(
            listOf(1),
            listOf(1, 2, 3).dropLastUntil { it == 2 }
        )

        Assert.assertEquals(
            listOf("House", "Nymeros", "Martell"),
            "House Nymeros Martell of Sunspear".split(" ").dropLastUntil { it == "of" }
        )


        Assert.assertEquals(
            listOf("a", "b", "c", "d", "e", "f"),
            "a b c d e f g h".split(" ").dropLastUntil { it == "g" }
        )
    }

}
