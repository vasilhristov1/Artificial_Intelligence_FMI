package hw7;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCreator {
    kMeans kmeans;
    String filename;

    public ImageCreator(kMeans kmeans, String filename) {
        setKmeans(kmeans);
        setFilename(filename);
    }

    public void setKmeans(kMeans kmeans) {
        this.kmeans = kmeans;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public kMeans getKmeans() {
        return kmeans;
    }

    public String getFilename() {
        return filename;
    }

    public void createImg() throws IOException {
        Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GRAY, Color.YELLOW, Color.PINK, Color.ORANGE,  Color.DARK_GRAY, Color.BLACK, Color.MAGENTA};
        Color centroidColor = Color.BLACK;
        int pixel = 1080;
        int div = 1;
        int r = 5;
        int multiple = 100;

        if (this.kmeans.getFilename().contains("unbalance")) {
            r = 5;
            multiple = 1;
            div = 1080;
        }

        BufferedImage bufferedImage = new BufferedImage(pixel, pixel, BufferedImage.TYPE_INT_RGB);
        Color transparent = new Color(0x00FFFFFF, true);

        Graphics2D g = bufferedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(transparent);
        g.setBackground(transparent);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int clusterColor = 0;

        for (Point centroid : this.getKmeans().getClusters().keySet()) {
            g.setColor(colors[clusterColor]);
            clusterColor++;

            for (Point point : this.getKmeans().getClusters().get(centroid)) {
                int x = (int) (point.getX() * multiple) / div;
                int y = (int) (point.getY() * multiple) / div;
                g.fillOval(x - r, y - r, r, r);
            }

            g.setColor(centroidColor);
            int centroidX= (int) (centroid.getX() * multiple) / div;
            int centroidY = (int) (centroid.getY() * multiple) / div;
            g.drawString("X", centroidX, centroidY);

        }

        ImageIO.write(bufferedImage, "png", new File(this.getFilename()));
    }
}
