// The MIT License (MIT)
//
// Copyright (c) 2015, 2018 Arian Fornaris
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
package phasereditor.ui;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

/**
 * @author arian
 *
 */
public class FrameCellRenderer implements ICanvasCellRenderer {
	private File _imageFile;
	private FrameData _fd;

	public FrameCellRenderer(IFile imageFile, FrameData fd) {
		this(imageFile == null ? null : imageFile.getLocation().toFile(), fd);
	}

	public FrameCellRenderer(File imageFile, FrameData fd) {
		_imageFile = imageFile;
		_fd = fd;
	}

	@Override
	public void render(Canvas canvas, GC gc, int x, int y, int width, int height) {
		if (_imageFile != null) {
			var image = ImageProxy.get(_imageFile, _fd);
			if (image != null) {
				image.paintScaledInArea(gc, new Rectangle(x, y, width, height));
			}
		}
	}

}
