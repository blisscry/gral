package openjchart.charts.axes;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import openjchart.AbstractDrawable;
import openjchart.Drawable;

public class LogarithmicRenderer2D extends AbstractAxisRenderer2D {

	@Override
	public Drawable getRendererComponent(final Axis axis, final Orientation orientation) {
		final Drawable component = new AbstractDrawable() {
			@Override
			public void draw(Graphics2D g2d) {
				AffineTransform txOld = g2d.getTransform();
				Color colorOld = g2d.getColor();

				double axisMin = axis.getMin().doubleValue();
				double axisMax = axis.getMax().doubleValue();
				double minTick = getMinTick(axisMin);
				double maxTick = getMaxTick(axisMax);

				g2d.setColor(axisColor);
				if (Orientation.HORIZONTAL.equals(orientation)) {
					// Draw baseline
					g2d.draw(new Line2D.Double(0.0, getHeight()/2.0, getWidth(), getHeight()/2.0));

					// Draw ticks
					Line2D tick = new Line2D.Double(0.0, getHeight()/2.0 - tickLength/2.0, 0.0, getHeight()/2.0 + tickLength/2.0);
					double w = getWidth() - 1;
					for (double i = minTick; i <= maxTick; i += tickSpacing) {
						double translateX = w * getPos(axis, i);
						g2d.translate(translateX, 0.0);
						g2d.draw(tick);
						// Draw numbers
						FontMetrics metrics = g2d.getFontMetrics();
						int textHeight = metrics.getHeight();
						String label = String.valueOf(i);
						float stringOffsetX = -metrics.stringWidth(label)/2f;
						float stringOffsetY = (float) (getHeight()/2.0 + tickLength/2.0 + textHeight);
						g2d.drawString(label, stringOffsetX, stringOffsetY);
						g2d.setTransform(txOld);
					}
				}
				else if (Orientation.VERTICAL.equals(orientation)) {
					// Draw baseline
					g2d.draw(new Line2D.Double(getWidth()/2.0, 0.0, getWidth()/2.0, getHeight()));

					// Draw ticks
					Line2D tick = new Line2D.Double(getWidth()/2.0 - tickLength/2.0, 0, getWidth()/2.0 + tickLength/2.0, 0);
					double h = getHeight() - 1;
					for (double i = minTick; i <= maxTick; i += tickSpacing) {
						double translateY = h*(1.0 - getPos(axis, i));
						g2d.translate(0.0, translateY);
						g2d.draw(tick);
						// Draw numbers
						FontMetrics metrics = g2d.getFontMetrics();
						int textHeight = metrics.getAscent();
						String label = String.valueOf(i);
						float stringOffsetX = (float) (getWidth()/2.0 - tickLength/2.0 - textHeight/4.0 - metrics.stringWidth(label));
						float stringOffsetY = textHeight/4f;
						g2d.drawString(label, stringOffsetX, stringOffsetY);
						g2d.setTransform(txOld);
					}
				}
				g2d.setColor(colorOld);
			}
		};

		return component;
	}

	@Override
	public double getPos(Axis axis, double value) {
		double axisMin = axis.getMin().doubleValue();
		double axisMinLog = (axisMin > 0.0) ? Math.log(axisMin) : 0.0;
		double axisMax = axis.getMax().doubleValue();
		double axisMaxLog = Math.log(axisMax);
		double pos = (Math.log(value) - axisMinLog) / axisMaxLog;

		return pos;
	}
}