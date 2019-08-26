package com.qwert2603.permesso_coroutines

open class PermessoException : Exception()

class PermissionCancelledException : PermessoException()

@Suppress("UNUSED")
class PermissionDeniedException(val permission: String) : PermessoException()