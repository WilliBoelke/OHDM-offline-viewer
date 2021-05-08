package de.htwBerlin.ois

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer

/**
 * Observer implementation that owns its lifecycle and achieves a one-time only observation
 * by marking it as destroyed once the onChange handler is executed.
 *
 * source : https://gist.github.com/alediaferia/3b5c6042f4c9694274e6e04427123604#file-onetimeobserver-kt
 * @param handler the handler to execute on change.
 */
class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)
    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
            handler(t)
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}