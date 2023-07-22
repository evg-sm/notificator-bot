package com.notificator.bot.cucumber.helper

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class FixtureLoadHelper(
    private val resourceLoader: ResourceLoader
) {

    fun load(fixturePath: String): InputStream =
        resourceLoader.getResource("classpath:/feature$fixturePath").let {
            if(!it.isFile) {
                throw RuntimeException("$fixturePath isn't file")
            }
            it.file.inputStream()
        }

    fun loadAsString(fixturePath: String): String = load(fixturePath).reader().use { it.readText() }
}
