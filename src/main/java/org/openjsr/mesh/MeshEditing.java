package org.openjsr.mesh;

import org.openjsr.mesh.triangulation.SimpleTriangulator;
import org.openjsr.mesh.triangulation.TriangulatedMesh;

public class MeshEditing {
    public void removeVertex(TriangulatedMesh model, int idVertex) {

    }

    public void removeFace(TriangulatedMesh mesh, int idFace) {
        mesh.faces.remove(idFace);
        mesh.setPolygons(mesh.faces, new SimpleTriangulator());
    }
}
