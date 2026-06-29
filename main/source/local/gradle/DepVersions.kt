package local.gradle

import java.util.Scanner

class DepVersions {
    companion object {
        private const val PATH = "/libs.versions.toml"
        private val MAP: MutableMap<String, String> = mutableMapOf()

        private fun String.unwrapLiteral(): String {
            if (this[0] == '"' && this[length - 1] == '"') return this.substring(1, length - 1)
            throw Exception("$this is not a string literal")
        }

        init {
            javaClass.getResourceAsStream(PATH)!!.let(::Scanner).use {
                while (it.hasNextLine()) {
                    val line = it.nextLine().trim()
                    if (line == "[libraries]" || line.isEmpty()) continue

                    val entry = line.substringBefore('=')
                    lateinit var group: String
                    lateinit var name: String
                    lateinit var version: String

                    val def = line.substringAfter('=')
                    for (pair in def.trim('{', '}', ' ').split(',')) {
                        val (key, value) = pair.split("=")
                        when (key.trim()) {
                            "group" -> group = value.trim().unwrapLiteral()
                            "name" -> name = value.trim().unwrapLiteral()
                            "version" -> version = value.trim().unwrapLiteral()
                        }
                    }

                    MAP[entry.trim()] = "$group:$name:$version"
                }
            }
        }

        operator fun get(key: String): String {
            return MAP[key] ?: throw Exception("$key was not defined in $PATH")
        }
    }
}
