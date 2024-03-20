package net.deechael.esjzone.network

import java.io.Serializable

data class Authorization(
    val ewsKey: String,
    val ewsToken: String,
) : Serializable