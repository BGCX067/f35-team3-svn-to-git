package edu.tamu.f35.client.gui.radar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import edu.tamu.f35.client.gui.Cockpit;
import edu.tamu.f35.client.logic.radar.RadarLogic;
import f35.rmi.RadarObject;

public class RadarVisualDisplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1832600583516931548L;

	public static final double MARGIN_SIZE = 10.0;

	public static final Color PRIMARY_COLOR = Color.GREEN;

	public static final Color SECONDARY_COLOR = new Color(104, 104, 150);

	public static final Color ENEMY_COLOR = new Color(255, 0, 51);

	public static final Color ALLY_COLOR = Color.GREEN;

	public static final Stroke PRIMARY_STROKE = new BasicStroke(3.0f);

	public static final Stroke SECONDARY_STROKE = new BasicStroke(1.0f);

	public static final double BASIC_OBJECT_RADIUS = 5.0;

	public static final String GRID_SOLID = "solid";

	public static final String GRID_DASH = "dash";

	public static final String GRID_OFF = "off";

	private String gridType;

	private double range = RadarLogic.DEFAULT_RANGE;

	private List<RadarObject> radarObjects;

	public RadarVisualDisplay() {
		this.setPreferredSize(new Dimension(400, 400));
		this.setBackground(Color.BLACK);
		radarObjects = new ArrayList<RadarObject>();
		gridType = GRID_SOLID;
	}

	protected void clear(Graphics g) {
		super.paintComponent(g);
	}

	public void paintComponent(Graphics g) {
		clear(g);
		Graphics2D g2d = (Graphics2D) g;

		int size = this.getWidth();
		Point2D center = new Point(size / 2, size / 2);

		g2d.setPaint(PRIMARY_COLOR);
		g2d.setStroke(PRIMARY_STROKE);

		// draw External circle
		Shape externalCircle = createCenteredCircle(1.0);
		g2d.draw(externalCircle);

		g2d.setPaint(Cockpit.BORDER_COLOR);
		// g2d.setPaint(Color.GREEN);
		g2d.setStroke(SECONDARY_STROKE);

		// draw vertical axis
		Shape verticalAxis = new Line2D.Double(center.getX(), 0, center.getX(),
				this.getHeight());
		g2d.draw(verticalAxis);

		// draw horizontal axis
		Shape horizontalAxis = new Line2D.Double(0, center.getY(),
				this.getWidth(), center.getY());
		g2d.draw(horizontalAxis);

		float[] dashes = { 5.0f, 10.0f };
		Stroke dashedStroke = new BasicStroke(1.0F, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0F, dashes, 0.F);

		if (!gridType.equals(GRID_OFF)) {
			if (gridType.equals(GRID_DASH))
				g2d.setStroke(dashedStroke);
			// draw inner circle 1
			Shape inner1 = createCenteredCircle(3.0 / 4);
			g2d.draw(inner1);
			// draw inner circle 1
			Shape inner2 = createCenteredCircle(0.5);
			g2d.draw(inner2);
			// draw inner circle 1
			Shape inner3 = createCenteredCircle(0.25);
			g2d.draw(inner3);
			// draw diagonal lines
			double radius = (size - MARGIN_SIZE) / 2;
			double relX = radius * Math.cos(Math.PI / 4);
			double relY = radius * Math.sin(Math.PI / 4);
			double p1x = center.getX() + relX;
			double p1y = center.getY() + relY;
			relX = radius * Math.cos(5 * Math.PI / 4);
			relY = radius * Math.sin(5 * Math.PI / 4);
			double p2x = center.getX() + relX;
			double p2y = center.getY() + relY;
			Shape diag1 = new Line2D.Double(p1x, p1y, p2x, p2y);
			g2d.draw(diag1);
			relX = radius * Math.cos(3 * Math.PI / 4);
			relY = radius * Math.sin(3 * Math.PI / 4);
			p1x = center.getX() + relX;
			p1y = center.getY() + relY;
			relX = radius * Math.cos(7 * Math.PI / 4);
			relY = radius * Math.sin(7 * Math.PI / 4);
			p2x = center.getX() + relX;
			p2y = center.getY() + relY;
			Shape diag2 = new Line2D.Double(p1x, p1y, p2x, p2y);
			g2d.draw(diag2);
		}
		// draw plane
		BufferedImage planeIcon = null;

		try {
			planeIcon = ImageIO.read(new File("images/f35.png"));

			double scaleValue = 0.3;

			int centerX = new Double(center.getX() - planeIcon.getWidth()
					* scaleValue / 2).intValue() + 2;
			int centerY = new Double(center.getY() - planeIcon.getHeight()
					* scaleValue / 2).intValue();

			AffineTransform transform = new AffineTransform();
			transform.translate(centerX, centerY);
			transform.scale(scaleValue, scaleValue);

			g2d.drawImage(planeIcon, transform, null);

		} catch (IOException e) {
			e.printStackTrace();
		}

		g2d.setPaint(ENEMY_COLOR);

		for (RadarObject radarObject : radarObjects) {
			double transformedX = scaleToRadar(radarObject.getRelativeX());
			double transformedY = this.getHeight()
					- scaleToRadar(radarObject.getRelativeY());

			Shape objectAvatar = new Ellipse2D.Double(transformedX
					- BASIC_OBJECT_RADIUS, transformedY - BASIC_OBJECT_RADIUS,
					2 * BASIC_OBJECT_RADIUS, 2 * BASIC_OBJECT_RADIUS);

			if (radarObject.isFriend())
				g2d.setPaint(ALLY_COLOR);
			else
				g2d.setPaint(ENEMY_COLOR);

			g2d.fill(objectAvatar);
		}

	}

	private Shape createCenteredCircle(double scale) {
		int size = this.getWidth();

		Shape circle = new Ellipse2D.Double(size / 2 - (size - MARGIN_SIZE)
				* scale / 2, size / 2 - (size - MARGIN_SIZE) * scale / 2,
				(size - MARGIN_SIZE) * scale, (size - MARGIN_SIZE) * scale);
		return circle;
	}

	private double scaleToRadar(double originalValue) {
		return this.getWidth() / 2 + originalValue * this.getWidth()
				/ (2 * range);
	}

	public void setRadarObjects(List<RadarObject> objectsToDisplay) {
		radarObjects.clear();
		for (RadarObject radarObject : objectsToDisplay) {
			radarObjects.add(radarObject);
		}
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public String getGridType() {
		return gridType;
	}

	public void setGridType(String gridType) {
		if (gridType.equals(GRID_DASH) || gridType.equals(GRID_SOLID)
				|| gridType.equals(GRID_OFF))
			this.gridType = gridType;
	}

}
