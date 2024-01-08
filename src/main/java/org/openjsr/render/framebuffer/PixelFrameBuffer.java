package org.openjsr.render.framebuffer;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import org.openjsr.core.Color;
import org.openjsr.render.DepthBuffer;

import java.nio.IntBuffer;
import java.util.Arrays;

public class PixelFrameBuffer implements Framebuffer {
    private PixelBuffer<IntBuffer> pixelBuffer;
    private IntBuffer intBuffer;
    private DepthBuffer depthBuffer;
    private int[] pixels;

    int width;
    int height;

    public PixelFrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        intBuffer = IntBuffer.allocate(width * height);
        pixelBuffer = new PixelBuffer<>(width, height, intBuffer, PixelFormat.getIntArgbPreInstance());
        depthBuffer = new DepthBuffer(width, height);
        pixels = intBuffer.array();
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
        pixels[y * width + x] = color.toArgb();
    }

    @Override
    public void clear() {
        Arrays.fill(pixels, 0);
        intBuffer.clear();
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
