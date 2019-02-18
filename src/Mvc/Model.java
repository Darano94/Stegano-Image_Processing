package Mvc;

import Steganography.SteganoProcessing;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Model {
    private BufferedImage image;
    private BufferedImage modified_image;
    private int width, height;
    private SteganoProcessing stego;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) throws IOException {
        if (image != null) {
            this.image = image;
            setWidth(image.getWidth());
            setHeight(image.getHeight());
        } else {
            throw new IOException();
        }
    }

    void hideMessage(byte[] msg) {
        if (msg != null) {
            stego = new SteganoProcessing();
            this.modified_image = stego.encode(getImage(), msg);
        }
    }

    byte[] extractMessage(BufferedImage img) {
        stego = new SteganoProcessing();
        return stego.decode(img);
    }

    public BufferedImage getModified_image() {
        return modified_image;
    }

    public void setModified_image(BufferedImage modified_image) {
        this.modified_image = modified_image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
