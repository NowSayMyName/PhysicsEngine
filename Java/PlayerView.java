import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.TreeMap;
import java.awt.Color;

public class PlayerView extends View {
	private BufferedImage imageBuffer;
	private Graphics gImageBuffer;
	// private BufferedImage imageExtendedBuffer;
	// private Graphics gImageExtendedBuffer;

	public PlayerView(Point viewPoint, int maxChunkLevel, double alphaMax, int width, int height) {
		super(viewPoint, maxChunkLevel, alphaMax, width, height);

		// imageExtendedBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// gImageExtendedBuffer = imageExtendedBuffer.getGraphics();
		imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		gImageBuffer = imageBuffer.getGraphics();
	}

	@Override
	public void drawFunction(Triangle tri, int xMin, int xMax, int yMin, int yMax) {
		float[] colorModifiers = tri.getColorModifiers();
		if (colorModifiers[0] != -1) {
			// BufferedImage texture = tri.getTexture();
			// TexturePoint[] texturePoints = tri.getTexturePoints();

			// double areaTri = edgeFunction(vertices[1][0], vertices[1][1], vertices[2][0], vertices[2][1], vertices[0][0], vertices[0][1]);
			// double areaTextureTri = edgeFunction(texturePoints[1].getX(), texturePoints[1].getY(), texturePoints[2].getX(), texturePoints[2].getY(), texturePoints[0].getX(), texturePoints[0].getY());
			// double areaRatio = areaTextureTri/(2.0*areaTri);
	
			int redTexture   = tri.getDefaultColor().getRed();
			int greenTexture = tri.getDefaultColor().getGreen();
			int blueTexture  = tri.getDefaultColor().getBlue();
	
			int rgb;

			boolean firstPixelFound;
			for (int y = yMin; y < yMax; y++) {
				for (int i = 0; i < 3; i++) {
					coordYSteps[i] = coordDiffs[i][0] * (y - vertices[i][1]);
				}
				firstPixelFound = false;
				for (int x = xMin; x < xMax; x++) {
					if (depthBuffer.getRGB(x, y) == -16777216) {
						computeBarycentricCoords(x);
						if (barycentricCoord[0] != -1) {
							depthBuffer.setRGB(x, y, -1);
							firstPixelFound = true;

							// rgb = (int)((1.0 - colorModifiers[0]) * (float)redTexture) << 16 | (int)((1.0 - colorModifiers[1]) * (float)greenTexture) << 8 | (int)((1.0 - colorModifiers[2]) * (float)blueTexture);
							rgb = (int)(colorModifiers[0] * (float)redTexture) << 16 | (int)(colorModifiers[1] * (float)greenTexture) << 8 | (int)(colorModifiers[2] * (float)blueTexture);
							imageBuffer.setRGB(x, y, rgb);
						} else if (firstPixelFound) {
							break;
						}
					}
				}
			}
		} else {
			boolean firstPixelFound;
			for (int y = yMin; y < yMax; y++) {
				for (int i = 0; i < 3; i++) {
					coordYSteps[i] = coordDiffs[i][0] * (y - vertices[i][1]);
				}
				firstPixelFound = false;
				for (int x = xMin; x < xMax; x++) {
					if (depthBuffer.getRGB(x, y) == -16777216) {
						computeBarycentricCoords(x);
						if (barycentricCoord[0] != -1) {
							depthBuffer.setRGB(x, y, -1);
							firstPixelFound = true;

						} else if (firstPixelFound) {
							break;
						}
					}
				}
			}
		}
	}

	/*
	public void downSampleImage() {
		int totalRed;
		int totalGreen;
		int totalBlue;
		int rgb;
		int resolutionSquare = resolution * resolution;
		for (int x = 0; x < width / resolution; x++) {
			for (int y = 0; y < height / resolution; y++) {
				totalRed = 0;
				totalGreen = 0;
				totalBlue = 0;
				for (int i = 0; i < resolution; i++) {
					for (int j = 0; j < resolution; j++) {
						rgb = imageExtendedBuffer.getRGB(x * resolution + i, y * resolution + j);
						totalRed   = (rgb >> 16) & 0x000000FF;
						totalBlue  = (rgb >> 8 ) & 0x000000FF;
						totalGreen = (rgb      ) & 0x000000FF;
					}
				}
				totalRed   /= resolutionSquare;
				totalBlue  /= resolutionSquare;
				totalGreen /= resolutionSquare;

				rgb = totalRed << 16 | totalGreen << 8 | totalBlue;
				imageBuffer.setRGB(x, y, rgb);
			}
		}
	}
	*/

	@Override
	public void computeView(TreeMap<Point, Surface> chunks, int originChunkLevel, int debugChunkLevel) {
		gImageBuffer.setColor(Color.BLACK);
		gImageBuffer.fillRect(0, 0, width, height);
		super.computeView(chunks, originChunkLevel, debugChunkLevel);
		// downSampleImage();
	}

	//---GETTERS

	public BufferedImage getImageBuffer() {
		return imageBuffer;
	}
}
