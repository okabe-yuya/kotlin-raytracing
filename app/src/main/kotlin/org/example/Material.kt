package org.example

import org.example.Rtweekend.*
import org.example.HitRecord

interface Material {
    fun scatter(rIn: Ray, rec: HitRecord, attenuation: Color, scattered: Ray): Boolean
}

data class Lambertian(val albedo: Color): Material {
    override fun scatter(
        rIn: Ray,
        rec: HitRecord,
        attenuation: Color,
        scattered: Ray,
    ): Boolean {
        var scatterDirection = rec.normal + randomUnitVector()
        // Catch degenerate scatter direction
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.normal
        }
        
        scattered.origin = rec.p
        scattered.direction = scatterDirection

        attenuation.e0 = albedo.x
        attenuation.e1 = albedo.y
        attenuation.e2 = albedo.z

        return true
    }
}

data class Metal(
    val albedo: Color,
    var fuzz: Double,
): Material {

    init {
        fuzz = if (fuzz < 1.0) fuzz else 1.0
    }

    override fun scatter(
        rIn: Ray,
        rec: HitRecord,
        attenuation: Color,
        scattered: Ray
    ): Boolean {
        var reflected = reflect(rIn.direction, rec.normal)
        reflected = unitVector(reflected) + (fuzz * randomUnitVector())
        scattered.origin = rec.p
        scattered.direction = reflected

        attenuation.e0 = albedo.x
        attenuation.e1 = albedo.y
        attenuation.e2 = albedo.z

        return scattered.direction.dot(rec.normal) > 0
    }
}

