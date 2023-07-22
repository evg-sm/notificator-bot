package com.notificator.bot.cucumber.step

import com.notificator.bot.cucumber.helper.FixtureLoadHelper
import io.cucumber.java8.LambdaGlue

class ParameterTypes(
    private val fixtureLoadHelper: FixtureLoadHelper
) : LambdaGlue {
    init {
        ParameterType("bodyPath", "(.+.(json|xml))") { value: String ->
            fixtureLoadHelper.loadAsString(value)
        }
    }
}
