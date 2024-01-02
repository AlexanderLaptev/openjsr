package org.openjsr.model;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Transform;

import java.util.ArrayList;
import java.util.List;

public class Model {

    public Model() {
    }

    public Model(
            List<Vector3f> vertices,
            List<Vector3f> normals,
            List<Vector2f> textureVertices,
            List<Polygon> polygons,
            Transform transform) {

        this.vertices = vertices;
        this.normals = normals;
        this.textureVertices = textureVertices;
        this.polygons = polygons;
        this.transform = transform;
    }

    public Model(
            List<Vector3f> vertices,
            List<Vector3f> normals,
            List<Vector2f> textureVertices,
            List<Polygon> polygons) {

        this.vertices = vertices;
        this.normals = normals;
        this.textureVertices = textureVertices;
        this.polygons = polygons;
    }

    public List<Vector3f> vertices = new ArrayList<>();

    public List<Vector3f> normals = new ArrayList<>();

    public List<Vector2f> textureVertices = new ArrayList<>();

    public List<Polygon> polygons = new ArrayList<>();

    public Transform transform = new Transform(
            new Vector3f(new float[]{0.0F, 0.0F, 0.0F}),
            new Vector3f(new float[]{0.0F, 0.0F, 0.0F}),
            new Vector3f(new float[]{1.0F, 1.0F, 1.0F})
    );
}