package org.openjsr.mesh;

import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.triangulation.SimpleTriangulator;
import org.openjsr.mesh.triangulation.TriangulatedMesh;
import org.openjsr.mesh.triangulation.Triangulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeshEditingTest {

    @Test
    void removeVertexTest1() {
        TriangulatedMesh triangulatedMesh = newModel();
        TriangulatedMesh triangulatedMeshOrigin = newModel();
        MeshEditor meshEditor = new MeshEditor();

        meshEditor.removeVertex(triangulatedMesh,4);

        assertEquals(triangulatedMesh.vertices.size(),triangulatedMeshOrigin.vertices.size()-1);

    }

    @Test
    void removeFaceTest() {
        TriangulatedMesh triangulatedMesh = newModel();
        TriangulatedMesh triangulatedMeshOrigin = newModel();
        MeshEditor meshEditing = new MeshEditor();

        meshEditing.removeFace(triangulatedMesh, 2);

        assertEquals(triangulatedMesh.faces.size(), 4);
        assertNotEquals(triangulatedMesh.faces.get(2).getVertexIndices(), triangulatedMeshOrigin.faces.get(2).getVertexIndices());
        assertNotEquals(triangulatedMesh.faces.size(), triangulatedMeshOrigin.faces.size());
        assertEquals(triangulatedMesh.faces.get(0).getVertexIndices(), triangulatedMeshOrigin.faces.get(0).getVertexIndices());
        assertEquals(triangulatedMesh.faces.get(2).getVertexIndices(), triangulatedMeshOrigin.faces.get(3).getVertexIndices());
        assertNotEquals(triangulatedMesh.triangles.size(),triangulatedMeshOrigin.triangles.size());
    }

    private TriangulatedMesh newModel() {
        Mesh mesh = new Mesh();
        mesh.vertices = new ArrayList<>(List.of(new Vector3f[]{
                new Vector3f(1.0f, 0.0f, 1.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 0.0f)
        }));
        mesh.faces = new ArrayList<>(List.of(new Face[]{
                new Face(),
                new Face(),
                new Face(),
                new Face(),
                new Face()
        }));
        mesh.faces.get(0).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
        mesh.faces.get(1).setVertexIndices(new ArrayList<>(Arrays.asList(3, 2, 5, 4)));
        mesh.faces.get(2).setVertexIndices(new ArrayList<>(Arrays.asList(0, 4, 5, 1)));
        mesh.faces.get(3).setVertexIndices(new ArrayList<>(Arrays.asList(1, 5, 2)));
        mesh.faces.get(4).setVertexIndices(new ArrayList<>(Arrays.asList(0, 3, 4)));

        Triangulator triangulator = new SimpleTriangulator();
        List<Face> triangles = triangulator.triangulateFaces(mesh.faces);
        TriangulatedMesh triangulatedMesh = new TriangulatedMesh(mesh, triangles);

        return triangulatedMesh;
    }
}
