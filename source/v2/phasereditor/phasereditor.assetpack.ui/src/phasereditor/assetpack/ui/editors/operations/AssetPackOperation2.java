// The MIT License (MIT)
//
// Copyright (c) 2015, 2016 Arian Fornaris
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
package phasereditor.assetpack.ui.editors.operations;

import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;

import phasereditor.assetpack.ui.editors.AssetPackEditor2;

/**
 * @author arian
 *
 */
public abstract class AssetPackOperation2 extends AbstractOperation {

	public AssetPackOperation2(String label) {
		super(label);
		addContext(AssetPackEditor2.UNDO_CONTEXT);
	}
	
	public static AssetPackEditor2 getEditor(IAdaptable info) {
		return info.getAdapter(AssetPackEditor2.class);
	}

}
