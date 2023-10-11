package nl.eric;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import nl.eric.utils.ThreadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Slf4j
public class MyPDFWriter {

    public static void writePDF (URL[] urls, String title)
            throws IOException, DocumentException, InterruptedException {

        Document document = new Document();
        String output = "./" + title + ".pdf";

        try (FileOutputStream fos = new FileOutputStream(output)) {
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.setStrictImageSequence(true);
            writer.open();
            float margin = 0F;
            document.setMargins(margin, margin, margin, margin);
            document.open();

            int addCount = 0;
            for (URL url : urls) {

                // Download full page image and add to PDF document
                Image image = Image.getInstance(url);

                image.scalePercent(calculateScaleRatio(document, image) * 100F);
                document.add(image);

                ThreadUtil.wait(1000);

                addCount++;
            }

            document.close();
            writer.close();

            if (addCount != urls.length) {

                document.close();
                writer.close();

                log.info("Received " + urls.length + " URL's, but wrote " + addCount
                        + ", deleting file " + output);

                new File(output).delete();
            } else {

                log.info("Done creating PDF: " + output);
            }
        }
    }

    private static float calculateScaleRatio (Document doc, Image image) {

        float scaleRatio;
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();
        if (imageWidth > 0 && imageHeight > 0) {

            // Firstly get the scale ratio required to fit the image width
            Rectangle pageSize = doc.getPageSize();
            float pageWidth = pageSize.getWidth() - doc.leftMargin() - doc.rightMargin();
            scaleRatio = pageWidth / imageWidth;

            // Get scale ratio required to fit image height - if smaller, use this instead
            float pageHeight = pageSize.getHeight() - doc.topMargin() - doc.bottomMargin();
            float heightScaleRatio = pageHeight / imageHeight;
            if (heightScaleRatio < scaleRatio) {
                scaleRatio = heightScaleRatio;
            }
        } else {

            // No scaling if the width or height is zero.
            scaleRatio = 1F;
        }

        return scaleRatio;
    }
}
