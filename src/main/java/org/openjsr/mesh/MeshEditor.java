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
            throw new MeshEditorException("Указанной вершины не существует.");
        }
        // mesh.vertices.remove(vertexIndex);
        List<Face> facesToRemove = new ArrayList<>();

        for (Face face : mesh.faces) {
            while (face.getVertexIndices().contains(vertexIndex)) {
                int faceVertexIndex = face.getVertexIndices().indexOf(vertexIndex);
                face.getVertexIndices().remove(faceVertexIndex);

                if (face.getNormalIndices().size()>face.getVertexIndices().size()) face.getNormalIndices().remove(faceVertexIndex);
                if (face.getTextureVertexIndices().size()>face.getVertexIndices().size()) face.getTextureVertexIndices().remove(faceVertexIndex);
            }
            if (face.getVertexIndices().size() < 3) facesToRemove.add(face);
        }
        mesh.faces.removeAll(facesToRemove);
        mesh.setPolygons(mesh.faces, SimpleTriangulator.getInstance());
        mesh.normals = MeshNormalComputer.getInstance().computeNormals(mesh);
    }

    public void removeFace(TriangulatedMesh mesh, int idFace) {
        if (mesh.faces.size() <= idFace || idFace < 0) {
            throw new MeshEditorException("Указанной грани не существует.");
        }
        mesh.faces.remove(idFace);
        mesh.setPolygons(mesh.faces, SimpleTriangulator.getInstance());
        mesh.normals = MeshNormalComputer.getInstance().computeNormals(mesh);
    }
}
