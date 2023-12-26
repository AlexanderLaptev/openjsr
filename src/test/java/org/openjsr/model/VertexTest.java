package org.openjsr.model;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VertexTest {

    @Test
    void hashCodeTest() {
        Vertex v1 = new Vertex(
                new Vector3f(new float[] {1.0F, 1.0F, 1.0F}),
                new Vector3f(new float[] {1.0F, 0.0F, 0.0F}),
                new Vector2f(new float[] {1.0F, 1.0F})
        );
        Vertex v2 = new Vertex(
                new Vector3f(new float[] {1.0F, 1.0F, 1.0F}),
                new Vector3f(new float[] {1.0F, 0.0F, 0.0F}),
                new Vector2f(new float[] {1.0F, 1.0F})
        );

        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    void equalsTest() {
        Vertex v1 = new Vertex(
                new Vector3f(new float[] {1.0F, 1.0F, 1.0F}),
                new Vector3f(new float[] {1.0F, 0.0F, 0.0F}),
                new Vector2f(new float[] {1.0F, 1.0F})
        );
        Vertex v2 = new Vertex(
                new Vector3f(new float[] {1.0F, 1.0F, 1.0F}),
                new Vector3f(new float[] {1.0F, 0.0F, 0.0F}),
                new Vector2f(new float[] {1.0F, 1.0F})
        );

        assertEquals(v1, v2);

        v1 = v2;

        assertEquals(v1, v2);

        v2 = new Vertex(
                new Vector3f(new float[] {2.0F, 2.0F, 1.0F}),
                new Vector3f(new float[] {1.0F, 0.0F, 0.0F}),
                new Vector2f(new float[] {1.0F, 1.0F})
        );

        assertNotEquals(v1, v2);
    }
}
