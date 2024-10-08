package org.example

import kotlin.math.tan
import org.example.Rtweekend.*
import org.example.Hittable
import org.example.Material

class Camera(
    val aspectRatio: Double = 1.0, // Ratio of image with over height
    val imageWidth: Int = 100, // Rendered image in pixel count
    val samplesPerPixel: Int = 10, // Count of random samples for each pixel
    val maxDepth: Int = 10, // Maximum number of ray bounces into scene
    val vFov: Double = 90.0, // Vertical view angle (field of view)
    val lookFrom: Point3 = Point3(0.0, 0.0, 0.0), // Point camera is looking from
    val lookAt: Point3 = Point3(0.0, 0.0, -1.0), // POint camera is looking at
    val vup: Vec3 = Vec3(0.0, 1.0, 0.0), // Camera-relative "up" direction
    val defocusAngle: Double = 0.0, // Variation angle of rays through each pixel
    val focusDist: Double = 10.0, // Distance from camera lookfrom point to plane of perfect focus
) {
    private var imageHeight: Int // Rendered image height
    private var center: Point3 // Camera center
    private var pixel00Loc: Point3 // Location of pixel 0, 0
    private var pixelDeltaU: Vec3 // Offset to pixel to the right
    private var pixelDeltaV: Vec3 // Offset to pixel below
    private var pixelSamplesScale: Double // Color scale factor for a sum of pixel samples
    private var u: Vec3 // Camera frame basic vectors
    private var v: Vec3 // Camera frame basic vectors
    private var w: Vec3 // Camera frame basic vectors
    private var defocusDiskU: Vec3 // Defocus disk horizontal radius
    private var defocusDiskV: Vec3 // Defocus disk vertical radius

    init {
        imageHeight = (imageWidth / aspectRatio).toInt()
        imageHeight = if (imageHeight < 1) 1 else imageHeight

        center = lookFrom
        pixelSamplesScale = 1.0 / samplesPerPixel

        // Determine viewport dimensions
        val theta = degreesToRadius(vFov)
        val h = tan(theta / 2)
        val viewportHeight = 2 * h * focusDist
        val viewportWidth = viewportHeight * (imageWidth.toDouble() / imageHeight)

        // Calculate the u,v,w unit basic vectors for the camera coordinate frame.
        w = unitVector(lookFrom - lookAt)
        u = unitVector(vup.cross(w))
        v = w.cross(u)

        // Calculate the vectors across the horizontal and down the vertical viewport edges.         
        val viewportU = viewportWidth * u
        val viewportV = viewportHeight * -v

        // Calculate the horizontal and vertical delta vectors from pixel to pixel
        pixelDeltaU = viewportU / imageWidth.toDouble()
        pixelDeltaV = viewportV / imageHeight.toDouble()

        // Calculate the location of the upper left pixel
        val viewportUpperLeft = center - (focusDist * w) - viewportU / 2.0 - viewportV / 2.0
        pixel00Loc = viewportUpperLeft + 0.5 * (pixelDeltaU + pixelDeltaV)

        // Calculate the camera defocus disk basic vectors.
        val defocusRadius = focusDist * tan(degreesToRadius(defocusAngle / 2.0))
        defocusDiskU = u * defocusRadius
        defocusDiskV = v * defocusRadius

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
                    pixelColor += rayColor(r, maxDepth, world)
                }
                writeColor(pixelSamplesScale * pixelColor)
            }
        }

        System.err.println("\rDone.            \n")
    }

    fun getRay(i: Int, j: Int): Ray {
        val offset = sampleSquare()
        val pixelSample = pixel00Loc + ((i + offset.x) * pixelDeltaU) + ((j + offset.y) * pixelDeltaV)
        val rayOrigin = if (defocusAngle <= 0.0) center else defocusDiskSample()
        val rayDirection = pixelSample - rayOrigin

        return Ray(rayOrigin, rayDirection)
    }

    fun defocusDiskSample(): Point3 {
        val p = randomInUnitDisk()
        return center + (p[0] * defocusDiskU) + (p[1] * defocusDiskV)
    }

    fun sampleSquare(): Vec3 {
        return Vec3(randomDouble() - 0.5, randomDouble() - 0.5, 0.0)
    }

    fun rayColor(r: Ray, depth: Int, world: Hittable): Color {
        if (depth <= 0) {
            return Color(0.0, 0.0, 0.0)
        }

        var rec: HitRecord = HitRecord.default()
        if (world.hit(r, Interval(0.001, infinity), rec)) {
            val scattered: Ray = Ray.default()
            val attenuation: Color = Color(0.0, 0.0, 0.0)

            if (rec.mat?.scatter(r, rec, attenuation, scattered) == true) {
                return attenuation * rayColor(scattered, depth - 1, world)
            }
            return Color(0.0, 0.0, 0.0) 
        }

        val unitDirection: Vec3 = unitVector(r.direction)
        val a = 0.5 * (unitDirection.y + 1.0)

        return (1.0 - a) * Color(1.0, 1.0, 1.0) + a * Color(0.5, 0.7, 1.0)
    }
}

