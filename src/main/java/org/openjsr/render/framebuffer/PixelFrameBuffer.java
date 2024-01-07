package org.openjsr.render.framebuffer;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import org.openjsr.core.Color;
import org.openjsr.render.DepthBuffer;

import java.nio.IntBuffer;
import java.util.Arrays;

public class PixelFrameBuffer implements Framebuffer{
    PixelBuffer<IntBuffer> pixelBuffer;
    IntBuffer colorBuffer;
    DepthBuffer depthBuffer;

    int width;
    int height;

    public PixelFrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        colorBuffer = IntBuffer.allocate(width * height);
        pixelBuffer = new PixelBuffer<>(width, height, colorBuffer, PixelFormat.getIntArgbPreInstance());
        depthBuffer = new DepthBuffer(width, height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        colorBuffer.array()[(x % width) + (y * width)] = color.toArgb();
    }

    @Override
    public void clear() {
        colorBuffer.put(new int[width * height]);
        colorBuffer.clear();
        depthBuffer.clear();
    }

    @Override
    public DepthBuffer getDepthBuffer() {
        return depthBuffer;
    }

    public PixelBuffer<IntBuffer> getPixelBuffer() {
        return pixelBuffer;
    }

    @Override
    public void update() {
        pixelBuffer.updateBuffer(b -> null);
    }
}
