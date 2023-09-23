package com.mtech.sj.bff.exception

import org.springframework.http.HttpStatusCode

class AwsException(message: String, httpStatusCode: HttpStatusCode) : AppException(message, httpStatusCode) {
}
