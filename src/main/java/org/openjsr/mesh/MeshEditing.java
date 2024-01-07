package org.openjsr.mesh;

import org.openjsr.mesh.triangulation.SimpleTriangulator;
import org.openjsr.mesh.triangulation.TriangulatedMesh;

public class MeshEditing {
    public void removeVertex(TriangulatedMesh mesh, int idVertex) {
        mesh.vertices.remove(idVertex);
        for (Face face : mesh.faces) {
            if (face.getVertexIndices().contains(idVertex)) {
                int vertexIdIdThePolygon = face.getVertexIndices().indexOf(idVertex);
                if (face.getNormalIndices() != null) face.getNormalIndices().remove(vertexIdIdThePolygon);
                if (face.getTextureVertexIndices() != null) face.getTextureVertexIndices().remove(vertexIdIdThePolygon);
                face.getVertexIndices().remove(vertexIdIdThePolygon);

                for (int indexVertex = 0; indexVertex < face.getVertexIndices().size(); indexVertex++) {
                    if (face.getVertexIndices().get(indexVertex) > idVertex)
                        face.getVertexIndices().add(indexVertex, face.getVertexIndices().get(indexVertex) - 1);
                }
            }
        }
    }

    public void removeFace(TriangulatedMesh mesh, int idFace) {
        mesh.faces.remove(idFace);
        mesh.setPolygons(mesh.faces, new SimpleTriangulator());
    }
}
