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
        
        scattered.update(rec.p, scatterDirection)
        attenuation.update(albedo.x, albedo.y, albedo.z)

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

        scattered.update(rec.p, reflected)
        attenuation.update(albedo.x, albedo.y, albedo.z)

        return scattered.direction.dot(rec.normal) > 0
    }
}

class Dielectric(
    val refractionIndex: Double,
): Material {
    override fun scatter(
        rIn: Ray,
        rec: HitRecord,
        attenuation: Color,
        scattered: Ray,
    ): Boolean {
        attenuation.update(1.0, 1.0, 1.0)

        val ri: Double = if (rec.frontFace) (1.0 / refractionIndex) else refractionIndex
        val unitDirection: Vec3 = unitVector(rIn.direction)
        val refracted = refract(unitDirection, rec.normal, ri)

        scattered.update(rec.p, refracted)

        return true
    }
}

