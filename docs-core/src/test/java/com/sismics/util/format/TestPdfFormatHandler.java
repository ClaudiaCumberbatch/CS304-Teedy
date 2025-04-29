package com.sismics.util.format;

import com.sismics.BaseTest;
import com.sismics.docs.core.util.format.PdfFormatHandler;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;


/**
 * Test of {@link PdfFormatHandler}
 *
 * @author bgamard
 */
public class TestPdfFormatHandler extends BaseTest {
    /**
     * Test related to https://github.com/sismics/docs/issues/373.
     */
    @Test
    public void testIssue373() throws Exception {
        PdfFormatHandler formatHandler = new PdfFormatHandler();
        String content = formatHandler.extractContent("deu", Paths.get(getResource("issue373.pdf").toURI()));
        Assert.assertTrue(content.contains("Aufrechterhaltung"));
        Assert.assertTrue(content.contains("Außentemperatur"));
        Assert.assertTrue(content.contains("Grundumsatzmessungen"));
        Assert.assertTrue(content.contains("ermitteln"));
    }
    /**
     * Test thumbnail generation from PDF.
     */
    // @Test
    // public void testGenerateThumbnail() throws Exception {
    //     PdfFormatHandler formatHandler = new PdfFormatHandler();
    //     Path pdfPath = Paths.get(getResource("issue373.pdf").toURI());

    //     BufferedImage thumbnail = formatHandler.generateThumbnail(pdfPath);

    //     Assert.assertNotNull("Thumbnail should not be null", thumbnail);
    //     Assert.assertTrue("Thumbnail width should be greater than 0", thumbnail.getWidth() > 0);
    //     Assert.assertTrue("Thumbnail height should be greater than 0", thumbnail.getHeight() > 0);

    //     // Optional: save the image to a temporary file for visual inspection (for debugging)
    //     File tempImage = File.createTempFile("thumbnail-", ".png");
    //     ImageIO.write(thumbnail, "png", tempImage);
    //     System.out.println("Thumbnail saved to: " + tempImage.getAbsolutePath());
    // }
}
