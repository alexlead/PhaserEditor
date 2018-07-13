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
package phasereditor.assetpack.ui.preview;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import phasereditor.assetpack.core.IAssetFrameModel;
import phasereditor.ui.FrameGridCanvas.IFrameProvider;

/**
 * @author arian
 *
 */
public class AtlasSingleFrameProvider implements IFrameProvider {

	private IAssetFrameModel _frame;
	private Image _image;

	public AtlasSingleFrameProvider(IAssetFrameModel frame) {
		_frame = frame;
		IFile file = frame.getImageFile();
		if (file != null) {
			_image = new Image(Display.getDefault(), file.getLocation().toFile().getAbsolutePath());
		}
	}

	@Override
	public int getFrameCount() {
		return 1;
	}

	@Override
	public Rectangle getFrameRectangle(int index) {
		return _frame.getFrameData().src;
	}

	@Override
	public Image getFrameImage(int index) {
		return _image;
	}

	@Override
	public String getFrameTooltip(int index) {
		var rect = getFrameRectangle(index);
		return rect.width + "x" + rect.height;
	}

}
