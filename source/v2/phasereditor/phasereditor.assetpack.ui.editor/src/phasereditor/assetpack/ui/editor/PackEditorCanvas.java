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
package phasereditor.assetpack.ui.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import phasereditor.animation.ui.AnimationsCellRender;
import phasereditor.assetpack.core.AnimationsAssetModel;
import phasereditor.assetpack.core.AssetModel;
import phasereditor.assetpack.core.AssetPackModel;
import phasereditor.assetpack.core.AssetSectionModel;
import phasereditor.assetpack.core.AssetType;
import phasereditor.assetpack.core.AtlasAssetModel;
import phasereditor.assetpack.core.AudioAssetModel;
import phasereditor.assetpack.core.AudioSpriteAssetModel;
import phasereditor.assetpack.core.ImageAssetModel;
import phasereditor.assetpack.core.MultiAtlasAssetModel;
import phasereditor.assetpack.core.SpritesheetAssetModel;
import phasereditor.assetpack.ui.AssetLabelProvider;
import phasereditor.assetpack.ui.AudioSpriteAssetCellRenderer;
import phasereditor.assetpack.ui.preview.AtlasAssetFramesProvider;
import phasereditor.assetpack.ui.preview.MultiAtlasAssetFrameProvider;
import phasereditor.audio.core.AudioCore;
import phasereditor.audio.ui.AudioCellRenderer;
import phasereditor.ui.BaseImageCanvas;
import phasereditor.ui.EditorSharedImages;
import phasereditor.ui.FrameCellRenderer;
import phasereditor.ui.FrameData;
import phasereditor.ui.FrameGridCellRenderer;
import phasereditor.ui.ICanvasCellRenderer;
import phasereditor.ui.IEditorSharedImages;
import phasereditor.ui.IconCellRenderer;
import phasereditor.ui.SwtRM;

/**
 * @author arian
 *
 */
public class PackEditorCanvas extends BaseImageCanvas implements PaintListener, MouseWheelListener, MouseListener {

	private static final int MIN_ROW_HEIGHT = 48;
	private AssetPackModel _model;
	private int _imageSize;
	private int _origin;
	private Set<Object> _collapsed;
	private Map<Rectangle, Object> _collapseIconBoundsMap;
	private Font _boldFont;

	public PackEditorCanvas(Composite parent, int style) {
		super(parent, style);

		addPaintListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);

		_imageSize = 96;
		_collapsed = new HashSet<>();
		_collapseIconBoundsMap = new HashMap<>();
		_boldFont = SwtRM.getBoldFont(getFont());
	}

	private static int ROW_HEIGHT = 30;
	private static int ASSET_SPACING_X = 10;
	private static int ASSET_SPACING_Y = 30;
	private static int MARGIN_X = 30;
	private static int ASSETS_MARGIN_X = 200;

	@Override
	public void paintControl(PaintEvent e) {

		_collapseIconBoundsMap = new HashMap<>();

		var gc = e.gc;

		if (_model == null) {
			return;
		}

		gc.setAlpha(10);
		gc.setBackground(getForeground());
		gc.fillRectangle(0, 0, ASSETS_MARGIN_X - 10, e.height);
		gc.setAlpha(255);

		var font = gc.getFont();

		var x = MARGIN_X;
		var y = 10;

		// paint objects

		for (var section : _model.getSections()) {

			x = MARGIN_X;

			{
				var collapsed = _collapsed.contains(section);

				gc.setFont(_boldFont);
				gc.drawText(section.getKey(), x, y, true);
				renderCollapseIcon(section, gc, collapsed, x, y);

				gc.setFont(font);

				y += ROW_HEIGHT;

				if (collapsed) {
					continue;
				}
			}

			var types = new ArrayList<>(List.of(AssetType.values()));

			types.sort((a, b) -> {
				var a1 = sortValue(section, a);
				var b1 = sortValue(section, b);
				return Long.compare(a1, b1);
			});

			for (var type : types) {
				var group = section.getGroup(type);
				var assets = group.getAssets();

				if (assets.isEmpty()) {
					continue;
				}

				{
					var collapsed = _collapsed.contains(group);

					var title = type.getCapitalName();
					var size = gc.stringExtent(title);

					var y2 = y + ROW_HEIGHT / 2 - size.y / 2 - 3;

					gc.drawText(title, x, y2, true);

					var count = section.getGroup(type).getAssets().size();

					gc.setAlpha(100);
					gc.drawText(" (" + count + ")", x + size.x, y2, true);
					gc.setAlpha(255);

					renderCollapseIcon(group, gc, collapsed, x, y2);

					// y += ROW_HEIGHT;

					if (collapsed) {
						y += ROW_HEIGHT;
						continue;
					}
				}

				{
					int assetX = ASSETS_MARGIN_X;
					int assetY = y;
					int bottom = y;

					var last = assets.isEmpty() ? null : assets.get(assets.size() - 1);
					for (var asset : assets) {

						Rectangle bounds;

						if (isFullRowAsset(asset)) {
							bounds = new Rectangle(assetX, assetY, e.width - assetX - 10, _imageSize);
						} else {
							bounds = new Rectangle(assetX, assetY, _imageSize, _imageSize);
						}

						bottom = Math.max(bottom, bounds.y + bounds.height);

						// gc.setAlpha(20);
						// gc.setBackground(Colors.color(0, 0, 0));
						// gc.fillRectangle(bounds);
						// gc.setAlpha(255);

						var renderer = getAssetRenderer(asset);

						if (renderer != null) {
							renderer.render(this, gc, bounds.x, bounds.y, bounds.width, bounds.height);
						}

						gc.setAlpha(30);
						gc.setForeground(getForeground());
						if (isFullRowAsset(asset)) {
							gc.drawRectangle(bounds);
						} else {
							gc.drawRectangle(bounds);
						}
						gc.setAlpha(255);

						var key = asset.getKey();

						var key2 = key;

						for (int i = key.length(); i > 0; i--) {
							key2 = key.substring(0, i);
							var size = gc.textExtent(key2);
							if (size.x < bounds.width) {
								break;
							}
						}

						if (key2.length() < key.length()) {
							key2 = key2.substring(0, key2.length() - 2) + "..";
						}

						gc.drawText(key2, assetX, assetY + _imageSize + 5, true);

						assetX += bounds.width + ASSET_SPACING_X;

						if (asset != last) {
							if (assetX + _imageSize > e.width - 5) {
								assetX = ASSETS_MARGIN_X;
								assetY += _imageSize + ASSET_SPACING_Y;
							}
						}
					} // end of assets loop

					y = bottom + ASSET_SPACING_Y;
				} // end of not collapsed types
				y += 10;
			}
		}

	}

	private static int sortValue(AssetSectionModel section, AssetType type) {
		var assets = section.getGroup(type).getAssets();
		var v = AssetType.values().length - type.ordinal();
		if (assets.size() > 0) {
			v += 1000;
		}
		return -v;
	}

	private static boolean isFullRowAsset(AssetModel asset) {
		return asset instanceof AnimationsAssetModel || asset instanceof AudioAssetModel;
	}

	private void renderCollapseIcon(Object obj, GC gc, boolean collapsed, int x, int y) {
		var path = collapsed ? IEditorSharedImages.IMG_BULLET_EXPAND : IEditorSharedImages.IMG_BULLET_COLLAPSE;
		var icon = EditorSharedImages.getImage(path);
		gc.drawImage(icon, x - 20, y);
		_collapseIconBoundsMap.put(new Rectangle(0, y - 5, ASSETS_MARGIN_X, 16 + 5), obj);
	}

	private ICanvasCellRenderer getAssetRenderer(AssetModel asset) {
		if (asset instanceof ImageAssetModel) {
			var asset2 = (ImageAssetModel) asset;
			return new FrameCellRenderer(asset2.getUrlFile(), asset2.getFrame().getFrameData());
		} else if (asset instanceof SpritesheetAssetModel) {
			var asset2 = (SpritesheetAssetModel) asset;
			var file = asset2.getUrlFile();
			var image = loadImage(file);
			return new FrameCellRenderer(file, FrameData.fromImage(image));
		} else if (asset instanceof AtlasAssetModel) {
			var asset2 = (AtlasAssetModel) asset;
			return new FrameGridCellRenderer(new AtlasAssetFramesProvider(asset2));
		} else if (asset instanceof MultiAtlasAssetModel) {
			var asset2 = (MultiAtlasAssetModel) asset;
			return new FrameGridCellRenderer(new MultiAtlasAssetFrameProvider(asset2));
		} else if (asset instanceof AnimationsAssetModel) {
			var asset2 = (AnimationsAssetModel) asset;
			return new AnimationsCellRender(asset2.getAnimationsModel(), 5);
		} else if (asset.getClass() == AudioAssetModel.class) {
			var asset2 = (AudioAssetModel) asset;
			for (var url : asset2.getUrls()) {
				var audioFile = asset2.getFileFromUrl(url);
				if (audioFile != null) {
					return new AudioCellRenderer(audioFile, 5);
				}
			}
			return null;
		} else if (asset instanceof AudioSpriteAssetModel) {
			return new AudioSpriteAssetCellRenderer((AudioSpriteAssetModel) asset, 5);
		}

		else {
			return new IconCellRenderer(AssetLabelProvider.GLOBAL_64.getImage(asset));
		}
	}

	public AssetPackModel getModel() {
		return _model;
	}

	public void setModel(AssetPackModel model) {
		_model = model;
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		if ((e.stateMask & SWT.SHIFT) == 0) {
			return;
		}

		var f = e.count < 0 ? 0.8 : 1.2;

		_imageSize = (int) (_imageSize * f);

		if (_imageSize < MIN_ROW_HEIGHT) {
			_imageSize = MIN_ROW_HEIGHT;
		}

		redraw();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseUp(MouseEvent e) {
		var redraw = false;
		for (var entry : _collapseIconBoundsMap.entrySet()) {
			if (entry.getKey().contains(e.x, e.y)) {
				var obj = entry.getValue();
				if (_collapsed.contains(obj)) {
					_collapsed.remove(obj);
				} else {
					_collapsed.add(obj);
				}
				redraw = true;
			}
		}

		if (redraw) {
			redraw();
		}
	}

}
