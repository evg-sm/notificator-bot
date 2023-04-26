package com.notificator.bot.adapter.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class NotificationController {

    @GetMapping("/")
    fun home(): String {
        return "notifications"
    }
}