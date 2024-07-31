package com.ds.engine.utils;

import com.ds.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class DisplayIconLoader {
    private static final Logger log = LoggerFactory.getLogger(DisplayIconLoader.class);

    public static ByteBuffer @Nullable [] loadIcon(String filepath)
    {
        log.info("Starting loading icon with path {}...", filepath);

        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader().getResource(filepath)));
        } catch (IOException e) {
            ErrorHandler.doError(e);
        }

        if(image == null)
            return null;

        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = loadIconInstance(image, 128);
        buffers[1] = loadIconInstance(image, 32);
        buffers[2] = loadIconInstance(image, 16);

        return buffers;
    }

    private static @NotNull ByteBuffer loadIconInstance(@NotNull BufferedImage image, int dimension)
    {
        log.info("Loading icon instance for dimension {}x{}", dimension, dimension);

        BufferedImage scaledIcon = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaledIcon.createGraphics();
        double ratio = getRatio(image, scaledIcon);
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;

        graphics.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2), (int) ((scaledIcon.getHeight() - height) / 2),
                (int) width, (int) height, null);
        graphics.dispose();

        byte[] imageBuffer = new byte[dimension * dimension * 4];
        int counter = 0;

        for(int i = 0; i < dimension; i++)
        {
            for(int j = 0; j < dimension; j++)
            {
                int colorSpace = scaledIcon.getRGB(j, i);
                imageBuffer[counter] = (byte) ((colorSpace << 8) >> 24);
                imageBuffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
                imageBuffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
                imageBuffer[counter + 3] = (byte) (colorSpace >> 24);
                counter += 4;
            }
        }

        return ByteBuffer.wrap(imageBuffer);
    }

    private static double getRatio(@NotNull BufferedImage image, @NotNull BufferedImage scaledIcon) {
        log.info("Calculating icon ratio...");

        double ratio = (double) (scaledIcon.getWidth()) / image.getWidth();
        double r2 = (double) (scaledIcon.getHeight()) / image.getHeight();

        if(r2 < ratio) {
            ratio = r2;
        }

        log.info("Calculated ratio: {}", ratio);

        return ratio;
    }
}
