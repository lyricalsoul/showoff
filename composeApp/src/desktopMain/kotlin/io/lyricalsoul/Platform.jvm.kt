package io.lyricalsoul

import io.lyricalsoul.rpc.DesktopPresenceClient

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val rpcClient = DesktopPresenceClient()
    override val isDesktop: Boolean = true
}

actual fun getPlatform(): Platform = JVMPlatform()