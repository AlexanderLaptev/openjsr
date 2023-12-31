package org.openjsr.mesh;

import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.mesh.MeshNormalComputer;
import org.openjsr.mesh.reader.MeshReader;
import org.openjsr.mesh.reader.ObjReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MeshNormalComputerTest {
    @Test
    void normalsVertex() {
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
        mesh.faces.get(0).setVertexIndices(List.of(new Integer[]{0, 1, 2, 3}));
        mesh.faces.get(1).setVertexIndices(List.of(new Integer[]{3, 2, 5, 4}));
        mesh.faces.get(2).setVertexIndices(List.of(new Integer[]{0, 4, 5, 1}));
        mesh.faces.get(3).setVertexIndices(List.of(new Integer[]{1, 5, 2}));
        mesh.faces.get(4).setVertexIndices(List.of(new Integer[]{0, 3, 4}));

        List<Vector3f> resultNormalsVertex = MeshNormalComputer.getInstance().computeNormals(mesh);
        List<Vector3f> expectedResultNormalsVertex = new ArrayList<>(Arrays.asList(
                new Vector3f((float) (1.0f / Math.sqrt(2)), 0.0f, (float) (1.0f / Math.sqrt(2))),
                new Vector3f((float) (1.0f / Math.sqrt(2)), 0.0f, (float) (-1.0f / Math.sqrt(2))),
                new Vector3f(0.0f, (float) (1.0f / Math.sqrt(2)), (float) (-1.0f / Math.sqrt(2))),
                new Vector3f(0.0f, (float) (1.0f / Math.sqrt(2)), (float) (1.0f / Math.sqrt(2))),
                new Vector3f(
                        (float) (-1.0f / Math.sqrt(3)),
                        (float) (-1.0f / Math.sqrt(3)),
                        (float) (1.0f / Math.sqrt(3))
                ),
                new Vector3f(
                        (float) (-1.0f / Math.sqrt(3)),
                        (float) (-1.0f / Math.sqrt(3)),
                        (float) (-1.0f / Math.sqrt(3))
                )
        ));
        for (int i = 0; i < resultNormalsVertex.size(); i++) {
            assertEquals(resultNormalsVertex.get(i), expectedResultNormalsVertex.get(i));
        }
    }

    @Test
    void test2() {
        File file = new File(Objects.requireNonNull(getClass().getResource("/meshes/NonManifold2.obj")).getFile());
        MeshReader reader = new ObjReader();
        Mesh mesh ;
        try {
            mesh = reader.read(file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Vector3f> resultNormalsVertex = MeshNormalComputer.getInstance().computeNormals(mesh);
        assertEquals(resultNormalsVertex.get(3), new Vector3f(0, 0, 0));
    }
}
