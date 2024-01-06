package org.openjsr.render;

import cg.vsu.render.math.vector.Vector4f;
import javafx.scene.canvas.GraphicsContext;
import org.openjsr.core.Color;
import org.openjsr.core.PerspectiveCamera;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.render.edge.EdgeRenderer;
import org.openjsr.render.framebuffer.CanvasFramebuffer;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.lighting.LightingModel;
import org.openjsr.util.FaceSorter;
import org.openjsr.util.GeometryUtils;

public class Rasterizer {
    private static final Rasterizer INSTANCE = new Rasterizer();

    private Rasterizer() {
    }

    public static Rasterizer getInstance() {
        return INSTANCE;
    }

    /**
     * Растеризует модель по треугольникам, предварительно расчитав экранные координаты вершин.
     *
     * @param model         растеризумеая модель
     * @param camera        камера,относительно которой идет обзор модели
     * @param lightingModel модель освещения, используемая в отрисовке
     * @param buffer        framebuffer, где происходит растеризация
     */
    public void drawModel(
            Model model,
            PerspectiveCamera camera,
            LightingModel lightingModel,
            Framebuffer buffer,
            EdgeRenderer edgeRenderer
    ) {
        Vector4f[] projectedVertices = project(model, camera, buffer.getWidth(), buffer.getHeight());
        for (Face triangle : model.getMesh().triangles) {
            drawTriangle(projectedVertices, model, triangle, lightingModel, buffer);
        }
        if (edgeRenderer != null) edgeRenderer.renderEdges(model, projectedVertices, buffer);
    }

    /**
     * Растеризует треугольник на экране алгоритмом scan line.
     * Сначала сортирует вершины, нормали и текстурные координаты в порядке возрастания Y, X, затем отрисовывает верхнюю часть и нижнюю.
     *
     * @param projectedVertices массив
     * @param model             растеризумеая модель
     * @param triangle          растеризуемый треугольник
     * @param lightingModel     модель освещения
     * @param buffer            framebuffer, куда ставится пиксель
     */
    public void drawTriangle(
            Vector4f[] projectedVertices,
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Framebuffer buffer
    ) {
        Face sorted = FaceSorter.sortFace(model.getMesh(), triangle, projectedVertices);

        // координаты вершин треугольника
        final int x1 = (int) (projectedVertices[sorted.getVertexIndices().get(0)].x);
        final int x2 = (int) (projectedVertices[sorted.getVertexIndices().get(1)].x);
        final int x3 = (int) (projectedVertices[sorted.getVertexIndices().get(2)].x);

        final int y1 = (int) (projectedVertices[sorted.getVertexIndices().get(0)].y);
        final int y2 = (int) (projectedVertices[sorted.getVertexIndices().get(1)].y);
        final int y3 = (int) (projectedVertices[sorted.getVertexIndices().get(2)].y);

        final float z1 = projectedVertices[sorted.getVertexIndices().get(0)].z;
        final float z2 = projectedVertices[sorted.getVertexIndices().get(1)].z;
        final float z3 = projectedVertices[sorted.getVertexIndices().get(2)].z;

        try {
            drawTopTriangle(
                    model,
                    sorted,
                    lightingModel,
                    projectedVertices,
                    buffer,
                    x1, y1, z1,
                    x2, y2, z2,
                    x3, y3, z3
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            drawBottomTriangle(
                    model,
                    sorted,
                    lightingModel,
                    projectedVertices,
                    buffer,
                    x1, y1, z1,
                    x2, y2, z2,
                    x3, y3, z3
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Отрисовывает верхнюю часть треугольника
     *
     * @param model             модель, откуда берется треугольник и шейдер
     * @param triangle          триангулированный полигон
     * @param lightingModel     модель освещения
     * @param projectedVertices
     * @param buffer            framebuffer, куда ставится пиксель
     * @param x1                координата вершины треугольника на экране
     * @param y1                координата вершины треугольника на экране
     * @param z1                глубина вершины треугольника
     * @param x2                координата вершины треугольника на экране
     * @param y2                координата вершины треугольника на экране
     * @param z2                глубина вершины треугольника
     * @param x3                координата вершины треугольника на экране
     * @param y3                координата вершины треугольника на экране
     * @param z3                глубина вершины треугольника
     */
    private void drawTopTriangle(
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Vector4f[] projectedVertices, Framebuffer buffer,
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            int x3, int y3, float z3
    ) {
        final int x2x1 = x2 - x1;
        final int x3x1 = x3 - x1;
        final int y2y1 = y2 - y1;
        final int y3y1 = y3 - y1;

        for (int y = y1; y < y2; y++) {
            // Не нужно проверять, что делители нулевые, цикл просто не запустится в таком случае.
            int l = x2x1 * (y - y1) / y2y1 + x1; // Ребро 1-2
            int r = x3x1 * (y - y1) / y3y1 + x1; // Ребро 1-3.
            if (l > r) { // Swap.
                int tmp = l;
                l = r;
                r = tmp;
            }
            for (int x = l; x <= r; x++) {

                float[] barycentric = GeometryUtils.getBarycentricCoords(
                        x, y,
                        x1, y1,
                        x2, y2,
                        x3, y3
                );

                final float z = GeometryUtils.interpolate(barycentric, new float[]{z1, z2, z3});

                drawPixel(
                        model,
                        triangle,
                        lightingModel,
                        buffer,
                        projectedVertices,
                        barycentric,
                        x, y, z
                );
            }
        }
    }

    /**
     * Отрисовывает нижнюю часть треугольника
     *
     * @param model             модель, откуда берется треугольник и шейдер
     * @param triangle          триангулированный полигон
     * @param lightingModel     модель освещения
     * @param projectedVertices
     * @param buffer            framebuffer, куда ставится пиксель
     * @param x1                координата вершины треугольника на экране
     * @param y1                координата вершины треугольника на экране
     * @param z1                глубина вершины треугольника
     * @param x2                координата вершины треугольника на экране
     * @param y2                координата вершины треугольника на экране
     * @param z2                глубина вершины треугольника
     * @param x3                координата вершины треугольника на экране
     * @param y3                координата вершины треугольника на экране
     * @param z3                глубина вершины треугольника
     */
    private void drawBottomTriangle(
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Vector4f[] projectedVertices, Framebuffer buffer,
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            int x3, int y3, float z3
    ) {
        final int x3x2 = x3 - x2;
        final int x3x1 = x3 - x1;
        final int y3y2 = y3 - y2;
        final int y3y1 = y3 - y1;

        // Рисует разделяющую линию и нижний треугольник
        if (y3y2 == 0 || y3y1 == 0) return; // Останавливается, если треугольник вырожденный
        for (int y = y2; y <= y3; y++) {
            int l = x3x2 * (y - y2) / y3y2 + x2; // Ребро 2-3
            int r = x3x1 * (y - y1) / y3y1 + x1; // Ребро 1-3.
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            for (int x = l; x <= r; x++) {

                float[] barycentric = GeometryUtils.getBarycentricCoords(
                        x, y,
                        x1, y1,
                        x2, y2,
                        x3, y3
                );

                final float z = GeometryUtils.interpolate(barycentric, new float[]{z1, z2, z3});

                drawPixel(
                        model,
                        triangle,
                        lightingModel,
                        buffer,
                        projectedVertices,
                        barycentric,
                        x, y, z
                );
            }
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2, Framebuffer buffer, Color color) {
        CanvasFramebuffer fb = (CanvasFramebuffer) buffer;
        GraphicsContext gc = fb.getCanvas().getGraphicsContext2D();
        gc.setStroke(javafx.scene.paint.Color.rgb(255, 255, 255, 1.0));
        gc.strokeLine(x1, y1, x2, y2);
//        int dx = Math.abs(x2 - x1);
//        int dy = Math.abs(y2 - y1);
//        int error = 0;
//        int dError = dy + 1;
//
//        int y = y
    }

    /**
     * Ставит пиксель получаемого цвета, если место не занято в zbuffer.
     *
     * @param model         модель, откуда берется шейдер
     * @param triangle      триангулированный полигон
     * @param lightingModel модель освещения
     * @param buffer        framebuffer, куда ставится пиксель
     * @param barycentric   массив барицентрических координат
     * @param x             координата пикселя
     * @param y             координата пикселя
     * @param z             глубина пикселя
     */
    private void drawPixel(
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Framebuffer buffer,
            Vector4f[] projectedVertices,
            float[] barycentric,
            int x, int y, float z
    ) {
        DepthBuffer depthBuffer = buffer.getDepthBuffer();
        if (depthBuffer.isVisible(x, y, z)) {
            depthBuffer.setZ(x, y, z);

            Color color = model.getShader().getBaseColor(triangle, model, projectedVertices, barycentric);
            color = lightingModel.applyLighting(color, triangle, model, barycentric);

            buffer.setPixel(x, y, color);
        }
    }

    /**
     * Получает массив спроецированных координат вершин модели
     *
     * @param model   модель с множеством вершин
     * @param camera камера, с точки зрения которой делается проекция
     * @param width  ширина экрана
     * @param height высота экрана
     * @return массив трехмерных векторов x, y - расположение на экране, z глубина до точки.
     */
    private Vector4f[] project(Model model, PerspectiveCamera camera, int width, int height) {
        Mesh mesh = model.getMesh();
        Vector4f[] projected = new Vector4f[mesh.vertices.size()];
        for (int vertexInd = 0; vertexInd < projected.length; vertexInd++) {
            Vector4f worldCoordinate = new Vector4f(mesh.vertices.get(vertexInd), 1);
            model.getTransform().combinedMatrix.mul(worldCoordinate);
            camera.project(worldCoordinate, width, height);
            projected[vertexInd] = worldCoordinate;
        }
        return projected;
    }
}
