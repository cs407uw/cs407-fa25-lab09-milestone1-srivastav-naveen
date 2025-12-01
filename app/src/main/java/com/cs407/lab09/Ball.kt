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
        // TODO: Call reset()
        reset()
    }

    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * (See lab handout for physics equations)
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if(isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }
// previous acceleration values
        val prevAccX = accX
        val prevAccY = accY

// Read new acceleration values from the sensor (this frame)
        val currAccX = xAcc
        val currAccY = yAcc

// Update stored acceleration for use in next frame
        accX = currAccX
        accY = currAccY

// --- Velocity update using trapezoidal integration ---
// v1 = v0 + 0.5 * (a0 + a1) * dt
        velocityX += 0.5f * (prevAccX + currAccX) * dT
        velocityY += 0.5f * (prevAccY + currAccY) * dT

// --- Position update using Simpson’s Rule (from PDF) ---
// dx = v0 * dt + (1/6)(3a0 + a1) dt²
        val dx = velocityX * dT + (1f / 6f) * (3 * prevAccX + currAccX) * dT * dT
        val dy = velocityY * dT + (1f / 6f) * (3 * prevAccY + currAccY) * dT * dT

// Apply the movement
        posX += dx
        posY += dy

        checkBoundaries()
    }

    /**
     * Ensures the ball does not move outside the boundaries.
     * When it collides, velocity and acceleration perpendicular to the
     * boundary should be set to 0.
     */
    fun checkBoundaries() {
        // TODO: implement the checkBoundaries function
        // (Check all 4 walls: left, right, top, bottom)

        // Left boundary
        if (posX < 0f) {
            posX = 0f
            velocityX = 0f
            accX = 0f
        }

        // Right boundary
        if (posX > backgroundWidth - ballSize) {
            posX = backgroundWidth - ballSize
            velocityX = 0f
            accX = 0f
        }

        // Top boundary
        if (posY < 0f) {
            posY = 0f
            velocityY = 0f
            accY = 0f
        }

        // Bottom boundary
        if (posY > backgroundHeight - ballSize) {
            posY = backgroundHeight - ballSize
            velocityY = 0f
            accY = 0f
        }

    }

    /**
     * Resets the ball to the center of the screen with zero
     * velocity and acceleration.
     */
    fun reset() {
        // TODO: implement the reset function
        // (Reset posX, posY, velocityX, velocityY, accX, accY, isFirstUpdate)

        posX = (backgroundWidth - ballSize) / 2f
        posY = (backgroundHeight - ballSize) / 2f

        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f

        isFirstUpdate = true

    }
}