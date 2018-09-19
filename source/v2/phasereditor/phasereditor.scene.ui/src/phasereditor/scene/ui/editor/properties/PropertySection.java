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
package phasereditor.scene.ui.editor.properties;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * @author arian
 *
 */
public abstract class PropertySection {
	private Object[] _models;
	private String _name;

	public PropertySection(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public Object[] getModels() {
		return _models;
	}

	public void setModels(Object[] models) {
		_models = models;
	}

	protected static String flatValues(Stream<?> values) {
		var set = new HashSet<>();
		values.forEach(v -> set.add(v));

		if (set.size() == 1) {
			var value = set.toArray()[0];

			if (value == null) {
				return "";
			}

			return value.toString();
		}

		return "";
	}

	@SuppressWarnings("static-method")
	protected void listen(Text text, Consumer<String> listener) {

		class TextListener implements FocusListener, KeyListener {
			private String _initial;

			@Override
			public void focusLost(FocusEvent e) {
				fireChanged();
			}

			private void fireChanged() {
				var value = text.getText();

				if (!value.equals(_initial)) {
					listener.accept(value);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				_initial = text.getText();
				e.display.asyncExec(text::selectAll);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == SWT.LF || e.character == SWT.CR || e.character == 13) {
					fireChanged();
				}
			}
		}

		var textListener = new TextListener();
		text.addFocusListener(textListener);
		text.addKeyListener(textListener);
	}

	@SuppressWarnings({ "boxing" })
	protected void listenFloat(Text text, Consumer<Float> listener) {
		listen(text, str -> {
			try {
				float value = Float.parseFloat(str);

				listener.accept(value);
			} catch (NumberFormatException e) {
				// noting
			}
		});
	}

	public abstract boolean canEdit(Object obj);

	public abstract Control createContent(Composite parent);

}