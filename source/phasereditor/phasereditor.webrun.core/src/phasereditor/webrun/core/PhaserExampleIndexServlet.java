// The MIT License (MIT)
//
// Copyright (c) 2015, 2017 Arian Fornaris
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions: The above copyright notice and this permission
// notice shall be included in all copies or substantial portions of the
// Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.
package phasereditor.webrun.core;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import phasereditor.inspect.core.InspectCore;

/**
 * @author arian
 *
 */
public class PhaserExampleIndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintStream out = new PrintStream(resp.getOutputStream());

		String exampleName = req.getParameter("n");
		String fname = exampleName.replace("%20", " ");

		resp.setContentType("text/html");

		out.println("<!DOCTYPE html>");
		out.println("<html>");

		out.println("<head>");
		out.println("<script src='/phaser-code/build/phaser.js'></script>");
		out.println("<script src='/jslibs/highlight.pack.js'></script>");
		out.println("<link rel='stylesheet' href='jslibs/default.css'>");
		out.println("<title>" + fname + "</title>");
		out.println("</head>");

		out.println("<body>");
		out.println("<div id='phaser-example'>");
		out.println("</div>");

		out.println("<script src='/examples-files/" + exampleName + "'></script>");

		out.println("<pre id='text'><code>");

		Path file = InspectCore.getBundleFile(InspectCore.RESOURCES_EXAMPLES_PLUGIN,
				"phaser-examples-master/examples/" + fname);
		byte[] bytes = Files.readAllBytes(file);

		out.println(new String(bytes));

		out.println("</code></pre>");

		out.println(
				"<script>window.onload = function () { hljs.highlightBlock(document.getElementById('text')); }</script>");

		out.println("</body>");

		out.println("</html>");

	}

}