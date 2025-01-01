package io.lyricalsoul

import io.lyricalsoul.rpc.DesktopPresenceClient

class JVMPlatform : Platform {
    override val name: String =
        "Java ${System.getProperty("java.version")}; Kotlin ${KotlinVersion.CURRENT}; running on ${System.getProperty("os.name")}"
    override val rpcClient = DesktopPresenceClient()
    override val isDesktop: Boolean = true
}

actual fun getPlatform(): Platform = JVMPlatform()