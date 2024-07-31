package org.example

import org.example.Rtweekend.*
import org.example.Hittable

class Camera(
    val aspectRatio: Double = 1.0, // Ratio of image with over height
    val imageWidth: Int = 100, // Rendered image in pixel count
    val samplesPerPixel: Int = 10, // Count of random samples for each pixel
) {
    private var imageHeight: Int // Rendered image height
    private var center: Point3 = Point3(0.0, 0.0, 0.0) // Camera center
    private var pixel00Loc: Point3 // Location of pixel 0, 0
    private var pixelDeltaU: Vec3 // Offset to pixel to the right
    private var pixelDeltaV: Vec3 // Offset to pixel below
    private val pixelSamplesScale = 1.0 / samplesPerPixel // Color scale factor for a sum of pixel samples

    init {
        imageHeight = (imageWidth / aspectRatio).toInt()
        imageHeight = if (imageHeight < 1) 1 else imageHeight

        // Determine viewport dimensions
        val focalLength = 1.0
        val viewportHeight = 2.0
        val viewportWidth = viewportHeight * (imageWidth.toDouble() / imageHeight)

        // Calculate the vectors across the horizontal and down the vertical viewport edges.         
        val viewportU = Vec3(viewportWidth, 0.0, 0.0)
        val viewportV = Vec3(0.0, -viewportHeight, 0.0)

        // Calculate the horizontal and vertical delta vectors from pixel to pixel
        pixelDeltaU = viewportU / imageWidth.toDouble()
        pixelDeltaV = viewportV / imageHeight.toDouble()

        // Calculate the location of the upper left pixel
        val viewportUpperLeft = center - Vec3(0.0, 0.0, focalLength) - viewportU / 2.0 - viewportV / 2.0
        pixel00Loc = viewportUpperLeft + 0.5 * (pixelDeltaU + pixelDeltaV)
    }

    fun render(world: Hittable): Unit {
        print("P3\n${imageWidth} ${imageHeight}\n255\n") 

        for  (j in 0..(imageHeight - 1)) {
            System.err.println("\rScanlines remaining: ${imageHeight - j} ")
            System.err.flush()

            for  (i in 0..(imageWidth - 1)) {
                val pixelColor = Color(0.0, 0.0, 0.0)
                for (sample in 0..(samplesPerPixel - 1)) {
                    val r: Ray = getRay(i, j)
                    pixelColor += rayColor(r, world)
                }
                writeColor(pixelSamplesScale * pixelColor)
            }
        }

        System.err.println("\rDone.            \n")
    }

    fun getRay(i: Int, j: Int): Ray {
        val offset = sampleSquare()
        val pixelSample = pixel00Loc + ((i + offset.x) * pixelDeltaU) + ((j + offset.y) * pixelDeltaV)
        val rayOrigin = center
        val rayDirection = pixelSample - rayOrigin

        return Ray(rayOrigin, rayDirection)
    }

    fun sampleSquare(): Vec3 {
        return Vec3(randomDouble() - 0.5, randomDouble() - 0.5, 0.0)
    }

    fun rayColor(r: Ray, world: Hittable): Color {
        val rec: HitRecord = HitRecord.default()
        if (world.hit(r, Interval(0.0, infinity), rec)) {
            val direction = randomOnHemisphere(rec.normal)
            return 0.5 * rayColor(Ray(rec.p, direction), world)
        }

        val unitDirection: Vec3 = unitVector(r.direction)
        val a = 0.5 * (unitDirection.y + 1.0)

        return (1.0 - a) * Color(1.0, 1.0, 1.0) + a * Color(0.5, 0.7, 1.0)
    }
}

