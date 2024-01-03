package org.openjsr.render.framebuffer;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;

public class CanvasFramebuffer implements Framebuffer {
    private final Canvas canvas;
    private final PixelWriter pixelWriter;

    public CanvasFramebuffer(Canvas canvas) {
        this.canvas = canvas;
        this.pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
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
    public void setPixel(int x, int y, int color) {
        pixelWriter.setArgb(x, y, color);
    }
}
