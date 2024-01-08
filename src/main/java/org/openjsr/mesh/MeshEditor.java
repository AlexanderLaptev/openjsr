package org.openjsr.mesh;

import org.openjsr.mesh.triangulation.SimpleTriangulator;
import org.openjsr.mesh.triangulation.TriangulatedMesh;

import java.util.ArrayList;
import java.util.List;

public class MeshEditor {
    public static class MeshEditorException extends RuntimeException {
        public MeshEditorException(String message) {
            super(message);
        }
    }

    public void removeVertex(TriangulatedMesh mesh, int vertexIndex) {
        if (mesh.vertices.size() <= vertexIndex || vertexIndex < 0) {
            throw new MeshEditorException("Такой вершины не существует");
        }
        mesh.vertices.remove(vertexIndex);
        List<Face> facesToRemove = new ArrayList<>();

        for (Face face : mesh.faces) {
            if (face.getVertexIndices().contains(vertexIndex)
                    && face.getVertexIndices().size() > 3
            ) {
                int faceVertexIndex = face.getVertexIndices().indexOf(vertexIndex);
                if (!face.getNormalIndices().isEmpty()) {
                    face.getNormalIndices().remove(faceVertexIndex);
                }
                if (!face.getTextureVertexIndices().isEmpty()) {
                    face.getTextureVertexIndices().remove(faceVertexIndex);
                }
                face.getVertexIndices().remove(faceVertexIndex);
            }

            if (face.getVertexIndices().size() <= 3
                    && face.getVertexIndices().contains(vertexIndex)
            ) {
                facesToRemove.add(face);
            } else {
                for (int faceVertexIndex = 0; faceVertexIndex < face.getVertexIndices().size(); faceVertexIndex++) {
                    if (face.getVertexIndices().get(faceVertexIndex) > vertexIndex) {
                        face.getVertexIndices().set(
                                faceVertexIndex,
                                face.getVertexIndices().get(faceVertexIndex) - 1
                        );
                    }
                }
            }
        }

        mesh.faces.removeAll(facesToRemove);
        mesh.setPolygons(mesh.faces, new SimpleTriangulator());
        mesh.normals = MeshNormalComputer.getInstance().computeNormals(mesh);
    }

    public void removeFace(TriangulatedMesh mesh, int idFace) {
        if (mesh.faces.size() <= idFace || idFace < 0) {
            throw new MeshEditorException("Такого полигона не существует");
        }
        mesh.faces.remove(idFace);
        mesh.setPolygons(mesh.faces, new SimpleTriangulator());
        mesh.normals = MeshNormalComputer.getInstance().computeNormals(mesh);
    }
}
