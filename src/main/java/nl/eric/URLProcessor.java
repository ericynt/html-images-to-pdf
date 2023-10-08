package nl.eric;

import nl.eric.utils.SeleniumUtil;
import nl.eric.utils.ThreadUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URLProcessor {

    public static String getFilename (String urlString) {

        SeleniumUtil su = new SeleniumUtil(urlString);

        // Get the filename and format it to use it for the filename
        String filename = Arrays.stream(su.getTitle().replaceAll("[\\\\/:*?\"<>|]", "").replace(" ", "_").split("_"))
                .map(s -> {

                    s = s.toLowerCase();
                    if (s.length() > 0) {

                        return (s.charAt(0) + "").toUpperCase() + (s.length() > 1 ? s.substring(1) : "");
                    } else {

                        return s;
                    }
                }).collect(Collectors.joining("_"));

        su.closeWebdriver();

        return filename;
    }

    public static URL[] getImageURLs (String urlString) throws IOException, InterruptedException {

        List<URL> urls = new ArrayList<>();
        boolean nextPage = true;
        int imageURLCount = 0;
        do {

            SeleniumUtil su1 = new SeleniumUtil(urlString);

            // Get list of full page image links
            List<WebElement> elements = su1.getElements(By.cssSelector("a[href*=photo]"));
            imageURLCount += elements.size();
            for (WebElement e : elements) {

                ThreadUtil.wait(1000);

                // Get full page link
                String href = e.getAttribute("href");

                SeleniumUtil su2 = new SeleniumUtil(href);
                // Get full page image URL
                String imageURLString = su2.getElement(By.xpath("//*[@id=\"slideshow\"]/center/div[1]/span/img"))
                        .getAttribute("src");
                su2.closeWebdriver();

                // Save full page image URL
                urls.add(new URL(imageURLString));
            }

            if (su1.elementExists(By.xpath("//*[@id=\"gallery\"]/font[1]/span/a[contains(text(), \"next\")]"))) {

                urlString = su1.getElement(By.xpath("//*[@id=\"gallery\"]/font[1]/span/a[contains(text(), \"next\")]"))
                        .getAttribute("href");
            } else {

                nextPage = false;
            }
            su1.closeWebdriver();

        } while (nextPage);

        if (urls.size() == imageURLCount) {

            return urls.toArray(URL[]::new);
        } else {

            System.out.println("Retrieved " + imageURLCount + " image URL's, but found " + urls.size()
                    + " in the gallery, returning empty array");

            return new URL[0];
        }

    }
}
