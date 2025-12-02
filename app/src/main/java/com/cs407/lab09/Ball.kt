package com.cs407.lab09

/**
 * Represents a ball that can move. (No Android UI imports!)
 *
 * Constructor parameters:
 * - backgroundWidth: the width of the background, of type Float
 * - backgroundHeight: the height of the background, of type Float
 * - ballSize: the width/height of the ball, of type Float
 */
class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f

    private var accX = 0f
    private var accY = 0f

    private var isFirstUpdate = true

    init {
        // Put ball in the center with zero motion
        reset()
    }

    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * Uses the linear-acceleration model from the handout:
     *
     * v1 = v0 + 1/2 (a0 + a1) * Δt
     * l  = v0 * Δt + 1/6 (3a0 + a1) * (Δt)^2
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if (isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }

        val timeStep = dT

        val prevAccX = accX
        val newAccX = xAcc
        val startVelX = velocityX

        val updatedVelX = startVelX + 0.5f * (prevAccX + newAccX) * timeStep
        val deltaX = startVelX * timeStep +
                (1f / 6f) * (3f * prevAccX + newAccX) * timeStep * timeStep


        val prevAccY = accY
        val newAccY = yAcc
        val startVelY = velocityY

        val updatedVelY = startVelY + 0.5f * (prevAccY + newAccY) * timeStep
        val deltaY = startVelY * timeStep +
                (1f / 6f) * (3f * prevAccY + newAccY) * timeStep * timeStep
        posX += deltaX
        posY += deltaY
        velocityX = updatedVelX
        velocityY = updatedVelY
        accX = newAccX
        accY = newAccY

        checkBoundaries()
    }

    /**
     * Ensures the ball does not move outside the boundaries.
     * When it collides, velocity and acceleration perpendicular to the
     * boundary are set to 0.
     */
    fun checkBoundaries() {
        var collidedX = false
        var collidedY = false

        // Left wall
        if (posX < 0f) {
            posX = 0f
            collidedX = true
        }

        // Right wall
        if (posX + ballSize > backgroundWidth) {
            posX = backgroundWidth - ballSize
            collidedX = true
        }

        // Top wall
        if (posY < 0f) {
            posY = 0f
            collidedY = true
        }

        // Bottom wall
        if (posY + ballSize > backgroundHeight) {
            posY = backgroundHeight - ballSize
            collidedY = true
        }

        // Cancel velocity & acceleration in collided directions
        if (collidedX) {
            velocityX = 0f
            accX = 0f
        }
        if (collidedY) {
            velocityY = 0f
            accY = 0f
        }
    }

    /**
     * Resets the ball to the center of the screen with zero
     * velocity and acceleration.
     */
    fun reset() {
        posX = (backgroundWidth - ballSize) / 2f
        posY = (backgroundHeight - ballSize) / 2f

        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f

        isFirstUpdate = true
    }
}
