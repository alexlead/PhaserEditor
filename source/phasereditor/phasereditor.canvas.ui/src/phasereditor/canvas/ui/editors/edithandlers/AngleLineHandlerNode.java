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
package phasereditor.canvas.ui.editors.edithandlers;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.transform.Rotate;
import phasereditor.canvas.ui.shapes.IObjectNode;

/**
 * @author arian
 *
 */
public class AngleLineHandlerNode extends PathHandlerNode {

	private boolean _start;
	private Point2D _center;

	public AngleLineHandlerNode(IObjectNode object, boolean start) {
		super(object);

		_start = start;

		setStroke(Color.WHITE);
		setOpacity(0.8);
	}

	@Override
	protected void createElements() {
		double N = 150;

		getElements().setAll(new MoveTo(0, 0), new LineTo(N, 0));

	}

	@Override
	public void updateHandler() {
		_center = computeObjectCenter_global();

		double startAngle;
		double endAngle;

		double parentAngle = _model.getParent().getGlobalAngle();

		if (isLocalCoords()) {
			startAngle = parentAngle;
			endAngle = parentAngle + _model.getAngle();
		} else {
			startAngle = 0;
			endAngle = parentAngle + _model.getAngle();
		}

		getTransforms().setAll(new Rotate(_start ? startAngle : endAngle, 0, 0));

		relocate(_center.getX(), _center.getY());
	}

}
