package ImageProcessing;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.nio.IntBuffer;

/*
 *
 *   not used yet, imported from other project of mine
 *
 */
public class ImageProcessing {
    public static int[] getPixels(Image img, int x, int y, int w, int h) {
        int[] pixels = new int[w * h];
        PixelReader reader = img.getPixelReader();

        PixelFormat.Type type = reader.getPixelFormat().getType();

        WritablePixelFormat<IntBuffer> format;

        if (type == PixelFormat.Type.INT_ARGB_PRE) {
            format = PixelFormat.getIntArgbPreInstance();
        } else {
            format = PixelFormat.getIntArgbInstance();
        }

        reader.getPixels(x, y, w, h, format, pixels, 0, w);

        return pixels;
    }


    public static WritableImage boxBlurImage(Image image, int kernelSize) {
        WritableImage modifiedImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();
        PixelWriter writer = modifiedImage.getPixelWriter();

        for (int x = 0; x < width; x++) {           //running through each pixel in the image
            for (int y = 0; y < height; y++) {
                double red = 0;
                double green = 0;
                double blue = 0;
                double alpha = 0;

                int count = 0;
                for (int i = -kernelSize; i <= kernelSize; i++) {           //lay our Grid on top of each pixel which is defined by kernelsize
                    for (int j = -kernelSize; j <= kernelSize; j++) {
                        if (x + i < 0 || x + i >= width     //checking for edges
                                || y + j < 0 || y + j >= height) {
                            continue;
                        }
                        Color color = reader.getColor(x + i, y + j);        //adding up each colors amount from pixels neighbours
                        red += color.getRed();
                        green += color.getGreen();
                        blue += color.getBlue();
                        alpha += color.getOpacity();
                        count++;                            //adding up count for averaging
                    }
                }
                Color blurColor = Color.color(red / count,      //set target pixel to its average neighbours color value
                        green / count,
                        blue / count,
                        alpha / count);
                writer.setColor(x, y, blurColor);
            }
        }
        return modifiedImage;
    }

}
