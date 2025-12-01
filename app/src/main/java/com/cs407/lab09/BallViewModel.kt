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
    private val sensitivity = 8f
    private var lastTimestamp: Long = 0L

    // Baseline gravity values captured once
    private var baseX = 0f
    private var baseY = 0f
    private var hasBaseline = false

    // Expose the ball's position as a StateFlow
    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()

    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            ball = Ball(fieldWidth, fieldHeight, ballSizePx)
            ball!!.reset()
            _ballPosition.value = Offset(ball!!.posX, ball!!.posY)
        }
    }

    fun onSensorDataChanged(event: SensorEvent) {
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {

            if (!hasBaseline) {
                // capture initial gravity vector
                baseX = event.values[0]
                baseY = event.values[1]
                hasBaseline = true
            }

            if (lastTimestamp != 0L) {
                val NS2S = 1.0f / 1_000_000_000.0f
                val dT = (event.timestamp - lastTimestamp) * NS2S

                // remove constant gravity by subtracting baseline
                val xAcc = -(event.values[0] - baseX) * sensitivity
                val yAcc = -(event.values[1] - baseY) * sensitivity


                currentBall.updatePositionAndVelocity(
                    xAcc = xAcc,
                    yAcc = yAcc,
                    dT = dT
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

        // reset baseline so next reading captures fresh rest orientation
        hasBaseline = false
    }
}
