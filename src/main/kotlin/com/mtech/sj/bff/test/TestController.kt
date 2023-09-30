package com.mtech.sj.bff.test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/test")
class TestController {

    @GetMapping
    fun test() = hashMapOf("greeting" to "Hello World")
}
