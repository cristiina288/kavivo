package org.vi.be.kavivo

import androidx.compose.ui.window.ComposeUIViewController
import org.vi.be.kavivo.ui.App

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) { App() }