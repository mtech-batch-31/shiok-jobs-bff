package com.mtech.sj.bff.exception

import org.springframework.http.HttpStatus

class ServiceNotFoundException(serviceName: String) :
    AppException("Service not found: $serviceName", HttpStatus.NOT_FOUND)
