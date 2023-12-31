package org.openjsr.mesh;

import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.reader.MeshReader;
import org.openjsr.mesh.reader.ObjReader;
import org.openjsr.mesh.triangulation.SimpleTriangulator;
import org.openjsr.mesh.triangulation.TriangulatedMesh;
import org.openjsr.mesh.triangulation.Triangulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MeshEditingTest {

    @Test
    void removeVertexTest() {
        TriangulatedMesh triangulatedMesh = newModel();
        TriangulatedMesh triangulatedMeshOrigin = newModel();
        MeshEditor meshEditor = new MeshEditor();

        meshEditor.removeVertex(triangulatedMesh, 4);

        assertEquals(triangulatedMesh.faces.get(0).getVertexIndices(), triangulatedMeshOrigin.faces.get(0).getVertexIndices());
        triangulatedMeshOrigin.faces.get(1).getVertexIndices().remove(3);
        assertEquals(triangulatedMesh.faces.get(1).getVertexIndices(), triangulatedMeshOrigin.faces.get(1).getVertexIndices());
        triangulatedMeshOrigin.faces.get(3).getVertexIndices().remove(1);
        assertEquals(triangulatedMesh.faces.get(2).getVertexIndices(), triangulatedMeshOrigin.faces.get(3).getVertexIndices());
        assertEquals(triangulatedMesh.faces.get(3).getVertexIndices(), triangulatedMeshOrigin.faces.get(4).getVertexIndices());

        assertEquals(triangulatedMesh.faces.get(0).getTextureVertexIndices(),triangulatedMeshOrigin.faces.get(0).getTextureVertexIndices());
        triangulatedMeshOrigin.faces.get(1).getTextureVertexIndices().remove(3);
        assertEquals(triangulatedMesh.faces.get(1).getTextureVertexIndices(),triangulatedMeshOrigin.faces.get(1).getTextureVertexIndices());
        triangulatedMeshOrigin.faces.get(3).getTextureVertexIndices().remove(1);
        assertEquals(triangulatedMesh.faces.get(2).getTextureVertexIndices(), triangulatedMeshOrigin.faces.get(3).getTextureVertexIndices());
    }

    @Test
    void testFile(){
        File file = new File(Objects.requireNonNull(getClass().getResource("/meshes/NonManifold.obj")).getFile());
        MeshReader reader = new ObjReader();
        Mesh mesh ;
        try {
            mesh = reader.read(file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MeshEditor meshEditor = new MeshEditor();
        Triangulator triangulator = SimpleTriangulator.getInstance();
        List<Face> triangles = triangulator.triangulateFaces(mesh.faces);
        TriangulatedMesh triangulatedMesh = new TriangulatedMesh(mesh, triangles);
        meshEditor.removeVertex(triangulatedMesh, 0);
        meshEditor.removeVertex(triangulatedMesh, 1);

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
        assertNotEquals(triangulatedMesh.triangles.size(), triangulatedMeshOrigin.triangles.size());
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
        mesh.faces.get(2).setVertexIndices(new ArrayList<>(Arrays.asList(0, 3, 4)));
        mesh.faces.get(3).setVertexIndices(new ArrayList<>(Arrays.asList(0, 4, 5, 1)));
        mesh.faces.get(4).setVertexIndices(new ArrayList<>(Arrays.asList(1, 5, 2)));

        mesh.faces.get(0).setTextureVertexIndices(new ArrayList<>(Arrays.asList(1, 3, 2, 5)));
        mesh.faces.get(1).setTextureVertexIndices(new ArrayList<>(Arrays.asList(3, 2, 5, 4)));
        mesh.faces.get(2).setTextureVertexIndices(new ArrayList<>(Arrays.asList(9, 3, 40)));
        mesh.faces.get(3).setTextureVertexIndices(new ArrayList<>(Arrays.asList(3, 4, 15, 1)));
        mesh.faces.get(4).setTextureVertexIndices(new ArrayList<>(Arrays.asList(91, 5, 2)));

        Triangulator triangulator = SimpleTriangulator.getInstance();
        List<Face> triangles = triangulator.triangulateFaces(mesh.faces);
        return new TriangulatedMesh(mesh, triangles);
    }
}
