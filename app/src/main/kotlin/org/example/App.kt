/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example

import org.example.Camera
import org.example.Rtweekend.*
import org.example.Hittable
import org.example.HittableList
import org.example.Sphere


fun main() {
    val world: HittableList = HittableList()

    val materialGround = Lambertian(Color(0.8, 0.8, 0.0))
    val materialCenter = Lambertian(Color(0.1, 0.2, 0.5))
    val materialLeft = Dielectric(1.50)
    val materialRight = Metal(Color(0.8, 0.6, 0.2), 1.0)

    world.add(Sphere(Point3(0.0, -100.5, -1.0), 100.0, materialGround))
    world.add(Sphere(Point3(0.0, 0.0, -1.2), 0.5, materialCenter))
    world.add(Sphere(Point3(-1.0, 0.0, -1.0), 0.5, materialLeft))
    world.add(Sphere(Point3(1.0, 0.0, -1.0), 0.5, materialRight))

    val cam = Camera(
        aspectRatio = 16.0 / 9.0,
        imageWidth = 400,
        samplesPerPixel = 100,
    )
    cam.render(world)
}

