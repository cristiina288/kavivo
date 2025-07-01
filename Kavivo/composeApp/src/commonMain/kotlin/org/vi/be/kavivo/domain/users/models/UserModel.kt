package org.vi.be.kavivo.domain.users.models

import kotlinx.serialization.Serializable


@Serializable
data class UserModel(
    var id: String = "",
    var email: String,
    var name: String,
    var password: String,
    var groupsIds: List<String>? = emptyList<String>()
) {
    constructor() : this("", "", "", "", emptyList())
}