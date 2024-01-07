package org.openjsr;

import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.PerspectiveCamera;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.MeshNormalComputer;
import org.openjsr.render.Model;
import org.openjsr.render.Rasterizer;
import org.openjsr.render.Scene;
import org.openjsr.render.edge.EdgeRenderStrategy;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.lighting.LightingModel;
import org.openjsr.util.FaceSorter;

import java.util.List;

public class SceneRenderer {
    private static final MeshNormalComputer NORMAL_COMPUTER = new MeshNormalComputer();

    private static final Rasterizer RASTERIZER = Rasterizer.getInstance();

    public void drawScene(
            Scene scene,
            PerspectiveCamera camera,
            LightingModel lightingModel,
            EdgeRenderStrategy edgeRenderStrategy,
            Framebuffer framebuffer
    ) {
        framebuffer.clear();
        List<Model> models = scene.getModels();
        for (Model model : models) {
            if (model.isValid()) {
                drawModel(model, camera, lightingModel, edgeRenderStrategy, framebuffer);
            } else {
                System.err.printf("Модель [%s] находится в неверном состоянии.%n", model);
            }
        }
    }

    private void drawModel(
            Model model,
            PerspectiveCamera camera,
            LightingModel lightingModel,
            EdgeRenderStrategy edgeRenderStrategy,
            Framebuffer framebuffer
    ) {
        prepareModelForRender(model, camera, framebuffer);

        for (Face triangle : model.getMesh().triangles) {
            drawModelTriangle(model, triangle, lightingModel, edgeRenderStrategy, framebuffer);
        }
    }

    private void drawModelTriangle(
            Model model,
            Face triangle,
            LightingModel lightingModel,
            EdgeRenderStrategy edgeRenderStrategy,
            Framebuffer framebuffer
    ) {
        Vector4f[] projectedVertices = model.getProjectedVertices();
        Face sortedTriangle = FaceSorter.sortFace(triangle, projectedVertices);
        List<Integer> sortedVertexIndices = sortedTriangle.getVertexIndices();
        List<Integer> sortedTextureIndices = sortedTriangle.getTextureVertexIndices();
        List<Vector2f> textureVertices = model.getMesh().textureVertices;

        Vector4f v1 = projectedVertices[sortedVertexIndices.get(0)];
        Vector4f v2 = projectedVertices[sortedVertexIndices.get(1)];
        Vector4f v3 = projectedVertices[sortedVertexIndices.get(2)];
        Vector4f[] triangleVertices = new Vector4f[]{v1, v2, v3};

        Vector2f t1 = textureVertices.get(sortedTextureIndices.get(0)).cpy();
        Vector2f t2 = textureVertices.get(sortedTextureIndices.get(1)).cpy();
        Vector2f t3 = textureVertices.get(sortedTextureIndices.get(2)).cpy();
        Vector2f[] triangleTextureVertices = new Vector2f[]{t1, t2, t3};

        if (shouldTriangleBeCulled(triangleVertices)) return;

        RASTERIZER.fillTriangle(
                triangleVertices,
                triangleTextureVertices,
                model.getShader(),
                lightingModel,
                framebuffer
        );
        if (edgeRenderStrategy != null) {
            edgeRenderStrategy.drawTriangleEdges(triangleVertices, framebuffer);
        }
    }

    private boolean shouldTriangleBeCulled(Vector4f[] vertices) {
        // TODO!
        return false;
    }

    private void prepareModelForRender(
            Model model,
            PerspectiveCamera camera,
            Framebuffer framebuffer
    ) {
        validateModelTriangles(model);
        validateModelCaches(model);
        updateModelWorldVertices(model);
        projectModelVertices(model, camera, framebuffer);
        recomputeModelNormalsIfNeeded(model);
    }

    private void validateModelCaches(Model model) {
        int vertexCount = model.getMesh().vertices.size();

        if (model.getWorldVertices() == null
                || model.getWorldVertices().length != vertexCount
        ) {
            model.setWorldVertices(new Vector4f[vertexCount]);
        }

        if (model.getProjectedVertices() == null
                || model.getProjectedVertices().length != vertexCount
        ) {
            model.setProjectedVertices(new Vector4f[vertexCount]);
        }
    }

    private void updateModelWorldVertices(Model model) {
        Vector4f[] worldVertices = model.getWorldVertices();
        Matrix4f combinedTransform = model.getTransform().combinedMatrix;

        int i = 0;
        for (Vector3f vertex : model.getMesh().vertices) {
            worldVertices[i] = combinedTransform.mul(new Vector4f(vertex));
            i++;
        }
    }

    private void projectModelVertices(
            Model model,
            PerspectiveCamera camera,
            Framebuffer framebuffer
    ) {
        Vector4f[] worldVertices = model.getWorldVertices();
        Vector4f[] projectedVertices = model.getProjectedVertices();

        int i = 0;
        for (Vector4f ignored : model.getWorldVertices()) {
            projectedVertices[i] = camera.project(
                    worldVertices[i].cpy(),
                    framebuffer.getWidth(),
                    framebuffer.getHeight()
            );
            i++;
        }
    }

    private void validateModelTriangles(Model model) {
        for (Face triangle : model.getMesh().triangles) {
            if (triangle.getVertexIndices().size() != 3) {
                // "The spy has already breached our defenses..."
                throw new IllegalStateException("Треугольник модели не является треугольником.");
            }
        }
    }

    private void recomputeModelNormalsIfNeeded(Model model) {
        // TODO!
//        if (model.getMesh().vertices.size() != model.getMesh().normals.size()) {
//            model.getMesh().normals = NORMAL_COMPUTER.computeNormals(model.getMesh());
//        }
    }
}
