/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2015 Erich Seifert <dev[at]erichseifert.de>,
 * Michael Seifert <mseifert[at]error-reports.org>
 *
 * This file is part of GRAL.
 *
 * GRAL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GRAL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GRAL.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erichseifert.gral.plots.areas;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static de.erichseifert.gral.TestUtils.assertEmpty;
import static de.erichseifert.gral.TestUtils.assertNotEmpty;
import static de.erichseifert.gral.TestUtils.createTestImage;
import static org.junit.Assert.assertNotNull;

import de.erichseifert.gral.TestUtils;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.LinearRenderer2D;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.util.PointND;
import org.junit.Before;
import org.junit.Test;

public class DefaultAreaRendererTest {
	private PointData data;

	@Before
	public void setUp() {
		Axis axisX = new Axis(-5.0, 5.0);
		Axis axisY = new Axis(-5.0, 5.0);
		AxisRenderer axisRendererX = new LinearRenderer2D();
		AxisRenderer axisRendererY = new LinearRenderer2D();
		data = new PointData(
			Arrays.asList(axisX, axisY),
			Arrays.asList(axisRendererX, axisRendererY),
			null, 0);
	}

	@Test
	public void testArea() {
		// Get area
		AreaRenderer r = new DefaultAreaRenderer2D();
		List<DataPoint> points = Arrays.asList(
			new DataPoint(data, new PointND<Double>(0.0, 0.0)),
			new DataPoint(data, new PointND<Double>(1.0, 1.0))
		);
		Shape shape = r.getAreaShape(points);
		Drawable area = r.getArea(points, shape);
		assertNotNull(area);

		// Draw area
		BufferedImage image = createTestImage();
		DrawingContext context = new DrawingContext((Graphics2D) image.getGraphics());
		area.draw(context);
		assertNotEmpty(image);
	}

	@Test
	public void testNullRenderer() {
		PointData data2 = new PointData(
			data.axes,
			Arrays.asList((AxisRenderer) null, null),
			null, 0);

		// Get area
		AreaRenderer r = new DefaultAreaRenderer2D();
		List<DataPoint> points = Arrays.asList(
			new DataPoint(data2, new PointND<Double>(0.0, 0.0)),
			new DataPoint(data2, new PointND<Double>(1.0, 1.0))
		);
		Shape shape = r.getAreaShape(points);
		Drawable area = r.getArea(points, shape);
		assertNotNull(area);

		// Draw area
		BufferedImage image = createTestImage();
		DrawingContext context = new DrawingContext((Graphics2D) image.getGraphics());
		area.draw(context);
		assertEmpty(image);
	}

	@Test
	public void testEmptyShape() {
		// Get area
		AreaRenderer r = new DefaultAreaRenderer2D();
		List<DataPoint> points = Arrays.asList();
		Drawable area = r.getArea(points, null);
		assertNotNull(area);

		// Draw area
		BufferedImage image = createTestImage();
		DrawingContext context = new DrawingContext((Graphics2D) image.getGraphics());
		area.draw(context);
		assertEmpty(image);
	}

	@Test
	public void testNullPoint() {
		// Get area
		AreaRenderer r = new DefaultAreaRenderer2D();
		List<DataPoint> points = Arrays.asList((DataPoint) null);
		Shape shape = r.getAreaShape(points);
		Drawable area = r.getArea(points, shape);
		assertNotNull(area);

		// Draw area
		BufferedImage image = createTestImage();
		DrawingContext context = new DrawingContext((Graphics2D) image.getGraphics());
		area.draw(context);
		assertEmpty(image);
	}

	@Test
	public void testGap() {
		AreaRenderer r = new DefaultAreaRenderer2D();
		List<DataPoint> points = Arrays.asList(
			new DataPoint(data, new PointND<Double>(0.0, 0.0)),
			new DataPoint(data, new PointND<Double>(1.0, 1.0))
		);

		List<Double> gaps = Arrays.asList(Double.NaN, 0.0, 1.0);
		List<Boolean> roundedValues = Arrays.asList(false, true);

		// Test different gap sizes
		for (Double gap : gaps) {
			r.setGap(gap);

			// Draw non-rounded and non rounded gaps
			for (Boolean rounded : roundedValues) {
				r.setGapRounded(rounded);

				Shape shape = r.getAreaShape(points);
				Drawable area = r.getArea(points, shape);
				assertNotNull(area);

				BufferedImage image = createTestImage();
				DrawingContext context = new DrawingContext((Graphics2D) image.getGraphics());
				area.draw(context);
				assertNotEmpty(image);
			}
		}
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		AreaRenderer original = new DefaultAreaRenderer2D();
		@SuppressWarnings("unused")
		AreaRenderer deserialized = TestUtils.serializeAndDeserialize(original);
    }
}