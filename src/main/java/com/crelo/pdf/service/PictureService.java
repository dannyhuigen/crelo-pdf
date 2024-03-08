package com.crelo.pdf.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Service responsible for generating all the pictures
 * Basically all the bars with a percentage
 */
@Service
public class PictureService {

    @Value("${pdfLoc}")
    String pdfloc;

    public Optional<String> generateLoadingBarImage(int widthPX, int heightPx, int percentage) {
        int width = widthPX;
        int height = heightPx;
        int borderRadius = heightPx; // Border radius for both outer image and inner rectangle
        String filePath = pdfloc + "test.png";

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Draw the rounded outer rectangle
        g2d.setColor(Color.decode("#f0ebff")); // Background color
        RoundRectangle2D roundedOuterRectangle = new RoundRectangle2D.Float(0, 0, width, height, borderRadius, borderRadius);
        g2d.fill(roundedOuterRectangle);

        // Draw the rounded inner rectangle
        int rectWidth = (widthPX / 100) * percentage;
        int rectHeight = height;
        g2d.setColor(Color.decode("#6135df")); // Inner rectangle color

        // Create a rounded rectangle with rounded right corners
        RoundRectangle2D roundedInnerRectangle = new RoundRectangle2D.Float(0, 0, rectWidth, rectHeight, borderRadius, borderRadius);
        g2d.fill(roundedInnerRectangle);

        g2d.dispose();

        // Save the image as PNG
        File output = new File(filePath);
        try {
            ImageIO.write(image, "png", output);
            System.out.println("Image created successfully.");
            return Optional.of(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
