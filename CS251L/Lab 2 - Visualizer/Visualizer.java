// Anish Adhikari UNM- aaadhikari999@gmail.com
// CS 251 - Lab 2 Visualizer
// This class draws a box, a circle in a circle and a custom drawing

import cs251.lab2.Display;
import java.util.Random;

public class Visualizer {

	static int m_centerHeight;
	static int m_centerWidth;
	static Display m_displayObj;

	public static void main(String[] args) {
		m_displayObj = new Display();

		m_centerHeight = m_displayObj.getHeight() / 2;
		m_centerWidth = m_displayObj.getWidth() / 2;

		drawBox(600);
		drawCircleOnCircle(100, 40);
		drawX();
	}

	/**
	 * Draws a box on screen of size boxPerimeter one coordinate point at a
	 * time.
	 * 
	 * @param boxPerimeter
	 *            Gives the total perimeter of the box.
	 */
	static void drawBox(int boxPerimeter) {
		// This program won't properly draw a square without being a factor of 4
		boxPerimeter += (boxPerimeter % 4);

		// Getting the center of the screen
		int centerHeight = m_displayObj.getHeight() / 2;
		int centerWidth = m_displayObj.getWidth() / 2;

		// Offset based on the total size of the box
		int offset = boxPerimeter / 8;

		for (int i = 0; i < boxPerimeter; ++i) {
			// Top left to top right
			if (i < boxPerimeter / 4) {
				m_displayObj.drawNextPoint(m_centerWidth - offset + i, m_centerHeight - offset);
			}
			// Top right to bottom right
			else if (i < boxPerimeter / 2) {
				m_displayObj.drawNextPoint(m_centerWidth + offset, m_centerHeight - offset + (i - boxPerimeter / 4));
			}
			// Bottom right to bottom left
			else if (i < (boxPerimeter / 2 + boxPerimeter / 4)) {
				m_displayObj.drawNextPoint(m_centerWidth + offset + (boxPerimeter / 2 - i), m_centerHeight + offset);
			}
			// Bottom left to upper left
			else if (i < boxPerimeter) {
				m_displayObj.drawNextPoint(m_centerWidth - offset,
						m_centerHeight + offset + (boxPerimeter / 2 + boxPerimeter / 4) - i);
			}
		}

	}

	/**
	 * Draws an "epicycle" which is a circle in a circle.
	 * 
	 * @param circleRadius
	 *            Sets the radius of how far the first circle will loop.
	 * @param secondCircleRadius
	 *            Sets the smaller circle for a tighter/looser drawing of the
	 *            point.
	 */
	static void drawCircleOnCircle(int circleRadius, int secondCircleRadius) {

		double angleInDeg = 270;
		double angleInRad, firstCircleX, firstCircleY;
		double secondAngleInDeg = 270;
		double secondAngleInRad;
		double secondCircleX, secondCircleY;

		// How many times the circle will go around
		int iterations = 2;

		for (int i = 0; i < 360 * iterations; ++i) {
			angleInRad = (angleInDeg * Math.PI) / 180;
			firstCircleX = m_centerWidth + (int) (circleRadius * Math.cos(angleInRad));
			firstCircleY = m_centerHeight + (int) (circleRadius * Math.sin(angleInRad));
			angleInDeg++;

			// Nesting a circle drawing that is usin the x and y generated from
			// the first circle
			for (int j = 0; j < 8; ++j) {
				secondAngleInRad = (secondAngleInDeg * Math.PI) / 180;
				secondCircleX = firstCircleX + secondCircleRadius * Math.cos(secondAngleInRad);
				secondCircleY = firstCircleY + secondCircleRadius * Math.sin(secondAngleInRad);
				m_displayObj.drawNextPoint((int) secondCircleX, (int) secondCircleY);
				secondAngleInDeg++;
			}
		}

	}

	/**
	 * Draws random size and random location "X"s on the screen.
	 */
	static void drawX() {
		Random rand = new Random();
		int maxHeight = m_centerHeight * 2;
		int maxWidth = m_centerWidth * 2;

		int iterations = 5;
		int randHeight, randWidth, randSize;

		for (int i = 0; i < iterations; ++i) {
			// randHeight and randWidth is the center of the X to draw
			randHeight = rand.nextInt(maxHeight);
			randWidth = rand.nextInt(maxWidth);

			randSize = rand.nextInt(maxHeight / 2);

            // Drawing each segment of the X
			for (int j = randSize; j > 0; --j) {
				m_displayObj.drawNextPoint(randHeight - j, randWidth - j);
			}
			for (int j = 0; j < randSize; ++j) {
				m_displayObj.drawNextPoint(randHeight + j, randWidth + j);
			}
			for (int j = randSize; j > 0; --j) {
				m_displayObj.drawNextPoint(randHeight - j, randWidth + j);
			}
			for (int j = 0; j < randSize; ++j) {
				m_displayObj.drawNextPoint(randHeight + j, randWidth - j);
			}

		}

	}

}