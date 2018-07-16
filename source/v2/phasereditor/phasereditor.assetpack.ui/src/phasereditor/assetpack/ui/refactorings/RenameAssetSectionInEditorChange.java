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
package phasereditor.assetpack.ui.refactorings;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.swt.widgets.Display;

import phasereditor.assetpack.core.AssetSectionModel;
import phasereditor.assetpack.ui.editors.AssetPackEditor2;

/**
 * @author arian
 *
 */
public class RenameAssetSectionInEditorChange extends BaseRenameAssetInEditorChange {

	private AssetSectionModel _section;

	public RenameAssetSectionInEditorChange(AssetSectionModel section, String initialName, String newName,
			AssetPackEditor2 editor) {
		super(section, initialName, newName, editor);
		_section = section;
	}

	@Override
	public String getName() {
		return "Rename asset section '" + _initialName + "'.";
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		Display.getDefault().syncExec(() -> {

			AssetSectionModel section = _editor.getModel().findSection(_initialName);
			if (section != null) {
				section.setKey(_newName, true);
				_editor.refresh();
				_editor.updateAssetEditor();
			}

		});
		return new RenameAssetSectionInEditorChange(_section, _newName, _initialName, _editor);

	}

}
