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
    public void removeVertex(TriangulatedMesh mesh, int idVertex) {
        if (mesh.vertices.size() <= idVertex || idVertex < 0) {
            throw new MeshEditorException("Такой вершины не существует");
        }
        mesh.vertices.remove(idVertex);
        List<Face> removeFace = new ArrayList<>();
        for (Face face : mesh.faces) {
            if (face.getVertexIndices().contains(idVertex) && face.getVertexIndices().size() > 3) {
                int vertexIdInThePolygon = face.getVertexIndices().indexOf(idVertex);
                if (!face.getNormalIndices().isEmpty()) face.getNormalIndices().remove(vertexIdInThePolygon);
                if (!face.getTextureVertexIndices().isEmpty())
                    face.getTextureVertexIndices().remove(vertexIdInThePolygon);
                face.getVertexIndices().remove(vertexIdInThePolygon);
            }
            if (face.getVertexIndices().size() <= 3 && face.getVertexIndices().contains(idVertex))
                removeFace.add(face);
            else for (int indexVertex = 0; indexVertex < face.getVertexIndices().size(); indexVertex++) {
                if (face.getVertexIndices().get(indexVertex) > idVertex) {
                    face.getVertexIndices().set(indexVertex, face.getVertexIndices().get(indexVertex) - 1);
                }
            }
        }
        mesh.faces.removeAll(removeFace);
        mesh.setPolygons(mesh.faces, new SimpleTriangulator());
    }

    public void removeFace(TriangulatedMesh mesh, int idFace) {
        if (mesh.faces.size() <= idFace || idFace < 0) {
            throw new MeshEditorException("Такого полигона не существует");
        }
        mesh.faces.remove(idFace);
        mesh.setPolygons(mesh.faces, new SimpleTriangulator());
    }
}
