package org.vi.be.kavivo.domain.users.models

import kotlinx.serialization.Serializable
import org.vi.be.kavivo.domain.groups.models.GroupModel


@Serializable
data class UserModel(
    var id: String = "",
    var email: String,
    var name: String,
    var password: String,
    var groupIds: List<String>? = emptyList(),
    var groups: List<GroupModel>? = emptyList(),
    var groupByDefault: String = ""
) {
    constructor() : this("", "", "", "", emptyList(), emptyList(), "")
}