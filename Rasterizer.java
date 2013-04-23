/*
 * Copyright (C) 2009-2010 Josh A. Beam
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Rasterizer extends JPanel
{
	private BufferedImage m_image;
	private int m_width, m_height;

	public
	Rasterizer(int width, int height)
	{
		m_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		m_width = width;
		m_height = height;
		setPreferredSize(new Dimension(width, height));
	}

	protected void
	paintComponent(Graphics g)
	{
		g.drawImage(m_image, 0, 0, null);
	}

	public void
	setPixel(int x, int y, Color c)
	{
		if(m_image == null || x < 0 || x >= m_width || y < 0 || y >= m_height)
			return;

		m_image.setRGB(x, y, c.toInt());
	}

	public void
	clear()
	{
		Color c = new Color(0.0f, 0.0f, 0.0f, 0.0f);

		for(int y = 0; y < m_height; ++y) {
			for(int x = 0; x < m_width; ++x)
				setPixel(x, y, c);
		}
	}

	private void
	drawSpan(Span span, int y)
	{
		int xdiff = span.x2 - span.x1;
		if(xdiff == 0)
			return;

		Color colordiff = span.color2.subtract(span.color1);

		float factor = 0.0f;
		float factorStep = 1.0f / (float)xdiff;

		// draw each pixel in the span
		for(int x = span.x1; x < span.x2; ++x) {
			setPixel(x, y, span.color1.add(colordiff.scale(factor)));
			factor += factorStep;
		}
	}

	private void
	drawSpansBetweenEdges(Edge e1, Edge e2)
	{
		// calculate difference between the y coordinates
		// of the first edge and return if 0
		float e1ydiff = (float)(e1.y2 - e1.y1);
		if(e1ydiff == 0.0f)
			return;

		// calculate difference between the y coordinates
		// of the second edge and return if 0
		float e2ydiff = (float)(e2.y2 - e2.y1);
		if(e2ydiff == 0.0f)
			return;

		// calculate differences between the x coordinates
		// and colors of the points of the edges
		float e1xdiff = (float)(e1.x2 - e1.x1);
		float e2xdiff = (float)(e2.x2 - e2.x1);
		Color e1colordiff = e1.color2.subtract(e1.color1);
		Color e2colordiff = e2.color2.subtract(e2.color1);

		// calculate factors to use for interpolation
		// with the edges and the step values to increase
		// them by after drawing each span
		float factor1 = (float)(e2.y1 - e1.y1) / e1ydiff;
		float factorStep1 = 1.0f / e1ydiff;
		float factor2 = 0.0f;
		float factorStep2 = 1.0f / e2ydiff;

		// loop through the lines between the edges and draw spans
		for(int y = e2.y1; y < e2.y2; ++y) {
			// create and draw span
			Span span = new Span(e1.color1.add(e1colordiff.scale(factor1)),
			                     e1.x1 + (int)(e1xdiff * factor1),
			                     e2.color1.add(e2colordiff.scale(factor2)),
			                     e2.x1 + (int)(e2xdiff * factor2));
			drawSpan(span, y);

			// increase factors
			factor1 += factorStep1;
			factor2 += factorStep2;
		}
	}

	public void
	drawTriangle(Color color1, int x1, int y1,
	             Color color2, int x2, int y2,
	             Color color3, int x3, int y3)
	{
		// create edges for the triangle
		Edge[] edges = {
			new Edge(color1, (int)x1, (int)y1, color2, (int)x2, (int)y2),
			new Edge(color2, (int)x2, (int)y2, color3, (int)x3, (int)y3),
			new Edge(color3, (int)x3, (int)y3, color1, (int)x1, (int)y1)
		};

		int maxLength = 0;
		int longEdge = 0;

		// find edge with the greatest length in the y axis
		for(int i = 0; i < 3; ++i) {
			int length = edges[i].y2 - edges[i].y1;
			if(length > maxLength) {
				maxLength = length;
				longEdge = i;
			}
		}

		int shortEdge1 = (longEdge + 1) % 3;
		int shortEdge2 = (longEdge + 2) % 3;

		// draw spans between edges; the long edge can be drawn
		// with the shorter edges to draw the full triangle
		drawSpansBetweenEdges(edges[longEdge], edges[shortEdge1]);
		drawSpansBetweenEdges(edges[longEdge], edges[shortEdge2]);
	}

	public void
	drawTriangle(Color color1, float x1, float y1,
	             Color color2, float x2, float y2,
	             Color color3, float x3, float y3)
	{
		drawTriangle(color1, (int)x1, (int)y1,
		             color2, (int)x2, (int)y2,
		             color3, (int)x3, (int)y3);
	}
}
