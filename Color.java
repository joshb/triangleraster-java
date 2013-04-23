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

public class Color
{
	public float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;

	public
	Color()
	{
	}

	public
	Color(float r_arg, float g_arg, float b_arg)
	{
		r = r_arg;
		g = g_arg;
		b = b_arg;
	}

	public
	Color(float r_arg, float g_arg, float b_arg, float a_arg)
	{
		r = r_arg;
		g = g_arg;
		b = b_arg;
		a = a_arg;
	}

	public int
	toInt()
	{
		int r_tmp = (int)(r * 255.0f);
		int g_tmp = (int)(g * 255.0f);
		int b_tmp = (int)(b * 255.0f);
		int a_tmp = (int)(a * 255.0f);

		return (a_tmp << 24) | (r_tmp << 16) | (g_tmp << 8) | b_tmp;
	}

	public Color
	add(Color c)
	{
		return new Color(r + c.r, g + c.g, b + c.b, a + c.a);
	}

	public Color
	subtract(Color c)
	{
		return new Color(r - c.r, g - c.g, b - c.b, a - c.a);
	}

	public Color
	scale(float f)
	{
		return new Color(r * f, g * f, b * f, a * f);
	}
}
