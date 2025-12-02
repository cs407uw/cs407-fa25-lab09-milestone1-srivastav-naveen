package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L
    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()
    private val ACC_SCALE = 50f

    /**
     * Called by the UI when the game field's size is known.
     */
    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            // Initialize the ball instance
            ball = Ball(
                backgroundWidth = fieldWidth,
                backgroundHeight = fieldHeight,
                ballSize = ballSizePx
            )

            ball?.let {
                _ballPosition.value = Offset(it.posX, it.posY)
            }
        }
    }

    /**
     * Called by the SensorEventListener in the UI.
     */
    fun onSensorDataChanged(event: SensorEvent) {
        // Ensure ball is initialized
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            if (lastTimestamp != 0L) {
                val nanoToSec = 1.0f / 1_000_000_000.0f
                val deltaTime = (event.timestamp - lastTimestamp) * nanoToSec

                val ACC_SCALE = 50f

                val xAcceleration = -event.values[0] * ACC_SCALE
                val yAcceleration =  event.values[1] * ACC_SCALE

                currentBall.updatePositionAndVelocity(
                    xAcc = xAcceleration,
                    yAcc = yAcceleration,
                    dT = deltaTime
                )

                _ballPosition.update { Offset(currentBall.posX, currentBall.posY) }
            }
            lastTimestamp = event.timestamp
        }
    }

    fun reset() {

        ball?.reset()
        ball?.let {
            _ballPosition.value = Offset(it.posX, it.posY)
        }
        lastTimestamp = 0L
    }
}
