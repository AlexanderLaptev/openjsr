package org.openjsr.render.framebuffer;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import org.openjsr.core.Color;
import org.openjsr.render.DepthBuffer;

public class CanvasFramebuffer implements Framebuffer {
    private final Canvas canvas;

    private final PixelWriter pixelWriter;
    private final DepthBuffer depthBuffer;
    public CanvasFramebuffer(Canvas canvas) {
        this.canvas = canvas;
        this.pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        this.depthBuffer = new DepthBuffer((int) canvas.getWidth(), (int) canvas.getHeight());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public int getWidth() {
        return (int) canvas.getWidth();
    }

    @Override
    public int getHeight() {
        return (int) canvas.getHeight();
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        pixelWriter.setArgb(x, y, color.toArgb());
    }

    @Override
    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Устанавливает цвет пикселя по данному 32-битному целому в формате ARGB.
     */
    public void setPixel(int x, int y, int argb) {
        pixelWriter.setArgb(x, y, argb);
    }

    @Override
    public DepthBuffer getDepthBuffer() {
        return depthBuffer;
    }
}
