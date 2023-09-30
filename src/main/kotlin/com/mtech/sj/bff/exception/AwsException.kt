package com.mtech.sj.bff.exception

import org.springframework.http.HttpStatus

class AwsException(message: String, httpStatusCode: HttpStatus) : AppException(message, httpStatusCode)
