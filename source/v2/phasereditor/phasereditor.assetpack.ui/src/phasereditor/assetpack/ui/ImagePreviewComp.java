// The MIT License (MIT)
//
// Copyright (c) 2015 Arian Fornaris
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
package phasereditor.assetpack.ui;

import java.io.File;
import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import phasereditor.ui.ImageCanvas_Zoom_1_1_Action;
import phasereditor.ui.ImageCanvas_Zoom_FitWindow_Action;
import phasereditor.ui.ImageProxyCanvas;

public class ImagePreviewComp extends Composite {
	private Label _resolutionLabel;
	private ImageProxyCanvas _canvas;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ImagePreviewComp(Composite parent, int style) {
		super(parent, style);

		setLayout(new GridLayout(1, false));

		_canvas = new ImageProxyCanvas(this, SWT.NONE);
		_canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		_resolutionLabel = new Label(this, SWT.CENTER);
		GridData ld = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		_resolutionLabel.setLayoutData(ld);

	}

	public void destroyResolutionLabel() {
		_resolutionLabel.dispose();
		_resolutionLabel = null;
	}

	public void setImageFile(IFile file) {
		setImageFile(file == null ? null : file.getLocation().toFile().getAbsolutePath());
	}

	public void setImageFile(String filepath) {
		if (filepath == null) {

			_canvas.clear();

			if (_resolutionLabel != null) {
				_resolutionLabel.setText("<No image>");
			}
		} else {
			_canvas.setImageInfo(new File(filepath), null);

			if (_resolutionLabel != null) {
				String name = new File(filepath).getName();
				if (name.length() > 20) {
					name = name.substring(0, 20) + "...";
				}

				String text = name + " (" + _canvas.getResolution() + ")";

				_resolutionLabel.setText(text);
			}
		}
	}

	public void loadImage(Path path, String label) {
		if (path == null) {

			_canvas.clear();

			if (_resolutionLabel != null) {
				_resolutionLabel.setText("<No image>");
			}
		} else {
			_canvas.setImageInfo(path.toFile().getAbsoluteFile(), null);
			if (_resolutionLabel != null) {
				_resolutionLabel.setText(label + " (" + _canvas.getResolution() + ")");
			}
		}

		_canvas.redraw();
	}

	public void createToolBar(IToolBarManager toolbar) {
		toolbar.add(new ImageCanvas_Zoom_1_1_Action(_canvas));
		toolbar.add(new ImageCanvas_Zoom_FitWindow_Action(_canvas));
	}
}
