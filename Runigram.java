import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {


    public static void main(String[] args) {
        
       
        Color[][] tinypic = read("tinypic.ppm");
        print(tinypic);
        Color[][] image;

        image = flippedHorizontally(tinypic);
        System.out.println();
        print(image);
        
    }

    
    public static Color[][] read(String fileName) {
        In in = new In(fileName);
        in.readString();
        int numCols = in.readInt();
        int numRows = in.readInt();
        in.readInt();
        Color[][] image = new Color[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int r = in.readInt();
                int g = in.readInt();
                int b = in.readInt();
                image[i][j] = new Color(r, g, b);
            }
        }
        return image;
    }

    // Prints the RGB values of a given color.
    private static void print(Color c) {
        System.out.print("(");
        System.out.printf("%3s,", c.getRed());   // Prints the red component
        System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
    }

 
    private static void print(Color[][] image) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                print(image[i][j]);
            }
            System.out.println(); // New line after each row
        }
    }
    
    /**
     * Returns an image which is the horizontally flipped version of the given image. 
     */
    public static Color[][] flippedHorizontally(Color[][] image) {
        int rows = image.length;
        int cols = image[0].length;
        Color[][] newImage = new Color[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = image[i][cols - 1 - j];
            }
        }
        return newImage;
    }
    
    public static Color[][] flippedVertically(Color[][] image){
        int rows = image.length;
        int cols = image[0].length;
        Color[][] newImage = new Color[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = image[rows - 1 - i][j];
            }
        }
        return newImage;
    }
    
    private static Color luminance(Color pixel) {
        int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();
        int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        return new Color(lum, lum, lum);
    }
    
    /**
     * Returns an image which is the grayscaled version of the given image.
     */
    public static Color[][] grayScaled(Color[][] image) {
        int rows = image.length;
        int cols = image[0].length;
        Color[][] newImage = new Color[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = luminance(image[i][j]);
            }
        }
        return newImage;
    }   
    
    public static Color[][] scaled(Color[][] image, int width, int height) {
        int h0 = image.length;
        int w0 = image[0].length;
        Color[][] newImage = new Color[height][width];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int sourceRow = i * h0 / height;
                int sourceCol = j * w0 / width;
                newImage[i][j] = image[sourceRow][sourceCol];
            }
        }
        return newImage;
    }
    
    public static Color blend(Color c1, Color c2, double alpha) {
        double r = alpha * c1.getRed() + (1 - alpha) * c2.getRed();
        double g = alpha * c1.getGreen() + (1 - alpha) * c2.getGreen();
        double b = alpha * c1.getBlue() + (1 - alpha) * c2.getBlue();
        return new Color((int) r, (int) g, (int) b);
    }
    
    public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
        int rows = image1.length;
        int cols = image1[0].length;
        Color[][] newImage = new Color[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
            }
        }
        return newImage;
    }
    public static void morph(Color[][] source, Color[][] target, int n) {
        if (source.length != target.length || source[0].length != target[0].length) {
            target = scaled(target, source[0].length, source.length);
        }
        
        for (int i = 0; i <= n; i++) {
            double alpha = (double) (n - i) / n;
            Color[][] blended = blend(source, target, alpha);
            display(blended);
            StdDraw.pause(500); 
        }
    }
    
    public static void setCanvas(Color[][] image) {
        StdDraw.setTitle("Runigram 2023");
        int height = image.length;
        int width = image[0].length;
        StdDraw.setCanvasSize(height, width);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.enableDoubleBuffering();
    }

    public static void display(Color[][] image) {
        int height = image.length;
        int width = image[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Sets the pen color to the pixel color
                StdDraw.setPenColor( image[i][j].getRed(),
                                     image[i][j].getGreen(),
                                     image[i][j].getBlue() );
                StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
            }
        }
        StdDraw.show();
    }
}
