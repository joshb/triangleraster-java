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

import java.lang.Math;
import java.awt.EventQueue;
import java.awt.event.*;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class TriangleRaster extends JFrame implements Runnable, ActionListener, KeyListener
{
	private final static int WINDOW_WIDTH = 320;
	private final static int WINDOW_HEIGHT = 240;

	private Rasterizer m_rasterizer = null;
	private double m_rotation = 0.0;
	private long m_lastTicks = 0;

	private Timer m_timer = null;

	public
	TriangleRaster()
	{
		super("Triangle Rasterization Demo");
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		m_rasterizer = new Rasterizer(WINDOW_WIDTH, WINDOW_HEIGHT);
		add(m_rasterizer);
		pack();

		m_lastTicks = new Date().getTime();

		// create timer to rotate and rasterize the triangle
		m_timer = new Timer(1, this);
		m_timer.start();
	}

	public void
	actionPerformed(ActionEvent event)
	{
		double r = m_rotation;

		// clear framebuffer
		m_rasterizer.clear();

		// calculate coordinates for triangle
		float size = 110.0f;
		float x1 = (WINDOW_WIDTH / 2) + (float)Math.cos(r - Math.PI / 6.0) * size;
		float y1 = (WINDOW_HEIGHT / 2) + (float)Math.sin(r - Math.PI / 6.0) * size;
		float x2 = (WINDOW_WIDTH / 2) + (float)Math.cos(r + Math.PI / 2.0) * size;
		float y2 = (WINDOW_HEIGHT / 2) + (float)Math.sin(r + Math.PI / 2.0) * size;
		float x3 = (WINDOW_WIDTH / 2) + (float)Math.cos(r + Math.PI + Math.PI / 6.0) * size;
		float y3 = (WINDOW_HEIGHT / 2) + (float)Math.sin(r + Math.PI + Math.PI / 6.0) * size;

		// colors for each point of the triangle
		Color color1 = new Color(1.0f, 0.0f, 0.0f);
		Color color2 = new Color(0.0f, 1.0f, 0.0f);
		Color color3 = new Color(0.0f, 0.0f, 1.0f);

		// draw triangle
		m_rasterizer.drawTriangle(color1, x1, y1,
		                          color2, x2, y2,
		                          color3, x3, y3);
		m_rasterizer.repaint(0, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		// calculate the number of seconds that
		// have passed since the last update
		long ticks = new Date().getTime();
		long ticksDiff = ticks - m_lastTicks;
		if(ticksDiff == 0)
			return;
		float time = ticksDiff / 1000.0f;
		m_lastTicks = ticks;

		// update rotation
		m_rotation += Math.PI / 2.0f * time;

		// display frames per second
		long fps = 1000 / ticksDiff;
		System.out.print("Frames per second: " + fps + "    \r");
	}

	public void
	keyTyped(KeyEvent event)
	{
	}

	public void
	keyPressed(KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			m_timer.stop();
			System.exit(0);
		}
	}

	public void
	keyReleased(KeyEvent event)
	{
	}

	public void
	run()
	{
		setVisible(true);
		addKeyListener(this);
	}

	public static void
	main(String[] args)
	{
		EventQueue.invokeLater(new TriangleRaster());
	}
}
