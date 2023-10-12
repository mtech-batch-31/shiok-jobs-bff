package com.mtech.sj.bff

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class JsonExampleTest {
    @Test
    fun `should read data from json`() {
        val data = """{"name":"John", "age":31, "city":"New York"}"""

        val person = mapper.readValue<Person> (data)

        assertEquals("John", person.name)
        assertEquals(31, person.age)
        assertEquals("New York", person.city)
    }

    private companion object {
        val mapper = jacksonObjectMapper()

        data class Person(val name: String, val age: Int, val city: String)
    }
}
