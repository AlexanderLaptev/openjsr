package org.openjsr.render;

import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Color;
import org.openjsr.core.PerspectiveCamera;
import org.openjsr.mesh.Face;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.lighting.LightingModel;
import org.openjsr.util.GeometryUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Rasterizer {
    private static final Rasterizer INSTANCE = new Rasterizer();

    /**
     * Компаратор, сравнивающий трехмерные вектора по Y и X, используется в {@link Rasterizer#sortTriangle(Vector3f[], Face)}
     */
    private static final Comparator<Vector3f> COMPARATOR = (a, b) -> {
        int cmp = Float.compare(a.y, b.y);
        if (cmp != 0) {
            return cmp;
        } else return Float.compare(a.x, b.x);
    };

    private Rasterizer() {
    }

    public static Rasterizer getInstance() {
        return INSTANCE;
    }

    /**
     * Растеризует модель по треугольникам, предварительно расчитав экранные координаты вершин.
     * @param model растеризумеая модель
     * @param camera камера,относительно которой идет обзор модели
     * @param lightingModel модель освещения, используемая в отрисовке
     * @param buffer framebuffer, где происходит растеризация
     * @param depthBuffer z-buffer, где происходит проверка глубины
     */
    public void drawModel(
            Model model,
            PerspectiveCamera camera,
            LightingModel lightingModel,
            Framebuffer buffer,
            DepthBuffer depthBuffer
    ) {
        Vector3f[] projectedVertices = camera.project(model, buffer.getWidth(), buffer.getHeight());
        for (Face triangle : model.getMesh().triangles) {
            drawTriangle(projectedVertices, model, triangle, lightingModel, buffer, depthBuffer);
        }
    }

    /**
     * Растеризует треугольник на экране алгоритмом scan line.
     * Сначала сортирует вершины, нормали и текстурные координаты в порядке возрастания Y, X, затем отрисовывает верхнюю часть и нижнюю.
     * @param projectedVertices массив
     * @param model растеризумеая модель
     * @param triangle растеризуемый треугольник
     * @param lightingModel модель освещения
     * @param buffer framebuffer, куда ставится пиксель
     * @param depthBuffer z-buffer, где проверяется глубина
     */
    public void drawTriangle(
            Vector3f[] projectedVertices,
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Framebuffer buffer,
            DepthBuffer depthBuffer
    ) {
        Face sorted = sortTriangle(projectedVertices, triangle);

        final int x1 = (int) projectedVertices[triangle.getVertexIndices().get(0)].x;
        final int x2 = (int) projectedVertices[triangle.getVertexIndices().get(1)].x;
        final int x3 = (int) projectedVertices[triangle.getVertexIndices().get(2)].x;

        final int y1 = (int) projectedVertices[triangle.getVertexIndices().get(0)].y;
        final int y2 = (int) projectedVertices[triangle.getVertexIndices().get(1)].y;
        final int y3 = (int) projectedVertices[triangle.getVertexIndices().get(2)].y;

        final float z1 = projectedVertices[triangle.getVertexIndices().get(0)].z;
        final float z2 = projectedVertices[triangle.getVertexIndices().get(1)].z;
        final float z3 = projectedVertices[triangle.getVertexIndices().get(2)].z;

        drawTopTriangle(
                model,
                sorted,
                lightingModel,
                buffer,
                depthBuffer,
                x1, y1, z1,
                x2, y2, z2,
                x3, y3, z3
        );

        drawBottomTriangle(
                model,
                sorted,
                lightingModel,
                buffer,
                depthBuffer,
                x1, y1, z1,
                x2, y2, z2,
                x3, y3, z3
        );
    }

    /**
     * Сортирует индексы вершин, нормалей и текстурных вершин в треугольнике сначала по Y вершины, а потом по X. ({@link Rasterizer#COMPARATOR})
     * Чтобы все отсортировалось одновременно, мы сначала берем координаты на экране из projectedVertices, создаем трехмерные вектора, куда в z кладем индексы их индексов в списке у треугольника.
     * Тогда
     *
     * @param projectedVertices
     * @param triangle
     * @return
     */
    public Face sortTriangle(Vector3f[] projectedVertices, Face triangle) {
        List<Vector3f> vertices = new ArrayList<>();

        vertices.add(new Vector3f(
                projectedVertices[triangle.getVertexIndices().get(0)].x,
                projectedVertices[triangle.getVertexIndices().get(0)].y,
                0));
        vertices.add(new Vector3f(
                projectedVertices[triangle.getVertexIndices().get(1)].x,
                projectedVertices[triangle.getVertexIndices().get(1)].y,
                1));
        vertices.add(new Vector3f(
                projectedVertices[triangle.getVertexIndices().get(2)].x,
                projectedVertices[triangle.getVertexIndices().get(2)].y,
                2));

        vertices.sort(COMPARATOR);

        Face sortedTriangle = new Face();
        List<Integer> vertexIndices = new ArrayList<>();
        List<Integer> textureVertexIndices = new ArrayList<>();
        List<Integer> normalIndices = new ArrayList<>();

        for (int ind = 0; ind < 3; ind++) {
            vertexIndices.add(triangle.getVertexIndices().get((int) vertices.get(ind).z));
            textureVertexIndices.add(triangle.getTextureVertexIndices().get((int) vertices.get(ind).z));
            normalIndices.add(triangle.getNormalIndices().get((int) vertices.get(ind).z));
        }

        sortedTriangle.setVertexIndices(vertexIndices);
        sortedTriangle.setTextureVertexIndices(vertexIndices);
        sortedTriangle.setNormalIndices(vertexIndices);
        return sortedTriangle;
    }

    /**
     * Отрисовывает верхнюю часть треугольника
     * @param model модель, откуда берется треугольник и шейдер
     * @param triangle триангулированный полигон
     * @param lightingModel модель освещения
     * @param buffer framebuffer, куда ставится пиксель
     * @param depthBuffer z-buffer, где проверяется глубина
     * @param x1 координата вершины треугольника на экране
     * @param y1 координата вершины треугольника на экране
     * @param z1 глубина вершины треугольника
     * @param x2 координата вершины треугольника на экране
     * @param y2 координата вершины треугольника на экране
     * @param z2 глубина вершины треугольника
     * @param x3 координата вершины треугольника на экране
     * @param y3 координата вершины треугольника на экране
     * @param z3 глубина вершины треугольника
     */
    private void drawTopTriangle(
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Framebuffer buffer,
            DepthBuffer depthBuffer,
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            int x3, int y3, float z3
    ) {
        final int x2x1 = x2 - x1;
        final int x3x1 = x3 - x1;
        final int y2y1 = y2 - y1;
        final int y3y1 = y3 - y1;

        for (int y = y1; y < y2; y++) {
            // No need to check if the divisors are null, because the loop will not execute if y1 == y2.
            int l = x2x1 * (y - y1) / y2y1 + x1; // Edge 1-2.
            int r = x3x1 * (y - y1) / y3y1 + x1; // Edge 1-3.
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
                        x3, y3);

                final float z = GeometryUtils.interpolate(barycentric, new float[]{z1, z2, z3});

                drawPixel(
                        model,
                        triangle,
                        lightingModel,
                        buffer,
                        depthBuffer,
                        barycentric,
                        x, y, z);
            }
        }
    }

    /**
     * Отрисовывает нижнюю часть треугольника
     * @param model модель, откуда берется треугольник и шейдер
     * @param triangle триангулированный полигон
     * @param lightingModel модель освещения
     * @param buffer framebuffer, куда ставится пиксель
     * @param depthBuffer z-buffer, где проверяется глубина
     * @param x1 координата вершины треугольника на экране
     * @param y1 координата вершины треугольника на экране
     * @param z1 глубина вершины треугольника
     * @param x2 координата вершины треугольника на экране
     * @param y2 координата вершины треугольника на экране
     * @param z2 глубина вершины треугольника
     * @param x3 координата вершины треугольника на экране
     * @param y3 координата вершины треугольника на экране
     * @param z3 глубина вершины треугольника
     */
    private void drawBottomTriangle(
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Framebuffer buffer,
            DepthBuffer depthBuffer,
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            int x3, int y3, float z3
    ) {
        final int x3x2 = x3 - x2;
        final int x3x1 = x3 - x1;
        final int y3y2 = y3 - y2;
        final int y3y1 = y3 - y1;

        // Draw the separating line and bottom triangle.
        if (y3y2 == 0 || y3y1 == 0) return; // Stop now if the bottom triangle is degenerate (avoids div by zero).
        for (int y = y2; y <= y3; y++) {
            int l = x3x2 * (y - y2) / y3y2 + x2; // Edge 2-3.
            int r = x3x1 * (y - y1) / y3y1 + x1; // Edge 1-3.
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
                        x3, y3);

                final float z = GeometryUtils.interpolate(barycentric, new float[]{z1, z2, z3});

                drawPixel(
                        model,
                        triangle,
                        lightingModel,
                        buffer,
                        depthBuffer,
                        barycentric,
                        x, y, z);
            }
        }
    }

    /**
     * Ставит пиксель получаемого цвета, если место не занято в zbuffer.
     * @param model модель, откуда берется шейдер
     * @param triangle триангулированный полигон
     * @param lightingModel модель освещения
     * @param buffer framebuffer, куда ставится пиксель
     * @param depthBuffer z-buffer, где проверяется глубина
     * @param barycentric массив барицентрических координат
     * @param x координата пикселя
     * @param y координата пикселя
     * @param z глубина пикселя
     */
    private void drawPixel(
            Model model,
            Face triangle,
            LightingModel lightingModel,
            Framebuffer buffer,
            DepthBuffer depthBuffer,
            float[] barycentric,
            int x, int y, float z
    ) {

        if (depthBuffer.isVisible(x, y, z)) {
            depthBuffer.setZ(x, y, z);
        }

        Color color = model.getShader().getBaseColor(triangle, model, barycentric);
        color = lightingModel.applyLighting(color, triangle, model, barycentric);

        buffer.setPixel(x, y, color);
    }
}
