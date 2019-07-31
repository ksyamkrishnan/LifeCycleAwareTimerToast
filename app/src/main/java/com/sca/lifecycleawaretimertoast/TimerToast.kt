package com.sca.lifecycleawaretimertoast

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.os.CountDownTimer
import android.widget.Toast

class TimerToast(app: Application, lifecycleowner: LifecycleOwner) : LifecycleObserver {
    private var started = false
    private var lifecycle = lifecycleowner.lifecycle
    init {
        lifecycle.addObserver(this)
    }
    private var counter = object : CountDownTimer(60000, 3000) {
        override fun onTick(millisUntilFinished: Long) {
            // different thread
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                Toast.makeText(app, "$millisUntilFinished", Toast.LENGTH_SHORT).show()
        }

        override fun onFinish() {
            // different thread
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                Toast.makeText(app, "FINISH", Toast.LENGTH_SHORT).show()

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (!started) {
            counter.start()
            started = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop() {
        counter.cancel()
    }
}