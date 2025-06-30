package org.vi.be.kavivo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform