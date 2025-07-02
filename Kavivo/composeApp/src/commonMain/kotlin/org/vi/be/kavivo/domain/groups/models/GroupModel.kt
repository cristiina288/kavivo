package org.vi.be.kavivo.domain.groups.models

import kotlinx.serialization.Serializable

@Serializable
data class GroupModel(
    var id: String = "",
    val title: String,
    val ownerId: String,
) {
    constructor() : this("", "", "")
}