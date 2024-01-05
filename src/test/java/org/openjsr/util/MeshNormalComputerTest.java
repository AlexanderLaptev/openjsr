package org.openjsr.util;

import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.mesh.MeshNormalComputer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeshNormalComputerTest {
    private static final MeshNormalComputer COMPUTER = new MeshNormalComputer();

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

        List<Vector3f> resultNormalsVertex = COMPUTER.normalsVertex(mesh);
        List<Vector3f> expectedResultNormalsVertex = new ArrayList<>(List.of(new Vector3f[]{
                new Vector3f((float) (1.0f / Math.sqrt(2)), 0.0f, (float) (1.0f / Math.sqrt(2))),
                new Vector3f((float) (1.0f / Math.sqrt(2)), 0.0f, (float) (-1.0f / Math.sqrt(2))),
                new Vector3f(0.0f, (float) (1.0f / Math.sqrt(2)), (float) (-1.0f / Math.sqrt(2))),
                new Vector3f(0.0f, (float) (1.0f / Math.sqrt(2)), (float) (1.0f / Math.sqrt(2))),
                new Vector3f((float) (-1.0f / Math.sqrt(3)), (float) (-1.0f / Math.sqrt(3)), (float) (1.0f / Math.sqrt(3))),
                new Vector3f((float) (-1.0f / Math.sqrt(3)), (float) (-1.0f / Math.sqrt(3)), (float) (-1.0f / Math.sqrt(3)))
        }));
        for (int i = 0; i < resultNormalsVertex.size(); i++) {
            assertEquals(resultNormalsVertex.get(i), expectedResultNormalsVertex.get(i));
        }
    }
}
