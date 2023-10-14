package com.mtech.sj.bff.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED

class UnauthorizedException(message: String) : AppException(message, UNAUTHORIZED)
