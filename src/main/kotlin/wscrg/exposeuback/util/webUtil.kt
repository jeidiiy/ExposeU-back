package wscrg.exposeuback.util

import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest

const val APPLICATION_JSON = "application/json"

fun isAjax(request: HttpServletRequest): Boolean {
    return request.getHeader(HttpHeaders.CONTENT_TYPE) == APPLICATION_JSON
}