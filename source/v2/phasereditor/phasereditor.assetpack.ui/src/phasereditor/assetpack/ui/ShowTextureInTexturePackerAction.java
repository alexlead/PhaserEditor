// The MIT License (MIT)
//
// Copyright (c) 2015, 2019 Arian Fornaris
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

import static phasereditor.ui.IEditorSharedImages.IMG_IMAGES_GO;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;

import phasereditor.assetpack.core.IAssetFrameModel;
import phasereditor.ui.EditorSharedImages;

/**
 * @author arian
 *
 */
public abstract class ShowTextureInTexturePackerAction extends Action {
	public ShowTextureInTexturePackerAction() {
		super("Show this texture in the Texture Packer.", EditorSharedImages.getImageDescriptor(IMG_IMAGES_GO));
	}

	@Override
	public void runWithEvent(Event event) {
		var frame = getTexture();

		var editor = ShowAtlasKeyInTexturePackerAction.openAtlasKeyInTexturePackerEditor(frame.getAsset());

		if (editor != null) {
			editor.revealFrame(frame.getKey());
		}

	}

	protected abstract IAssetFrameModel getTexture();
}
