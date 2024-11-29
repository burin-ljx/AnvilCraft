package dev.dubhe.anvilcraft.util;

import org.joml.Vector2f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MathUtil {
    public static final float DEGREE_CONVERT = (float)Math.PI / 180F;

    public static Vector2f rotationDegrees(Vector2f v, float deg){
        return rotate(v,deg * DEGREE_CONVERT);
    }

    public static Vector2f rotate(Vector2f v, float d){
        return new Vector2f(
            (float) (v.x * cos(d) - v.y * sin(d)),
            (float) (v.x * sin(d) + v.y * cos(d))
        );
    }
}
