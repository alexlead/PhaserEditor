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
package phasereditor.assetpack.ui.properties;

import java.util.function.Supplier;

import phasereditor.assetpack.core.AnimationsAssetModel.AnimationAssetElementModel;
import phasereditor.assetpack.core.animations.AnimationModel;
import phasereditor.inspect.core.InspectCore;
import phasereditor.ui.properties.PGridInfoProperty;
import phasereditor.ui.properties.PGridModel;

/**
 * @author arian
 *
 */
public class AnimationAssetElementPGridModel extends PGridModel {
	private AnimationAssetElementModel _animationElement;
	private String _id;

	public AnimationAssetElementPGridModel(AnimationAssetElementModel animationElement) {
		super();
		_animationElement = animationElement;

		_id = animationElement.getAsset().getId() + "." + animationElement.getKey();

		addSection("AnimationConfig",

				prop("key", getAnimation()::getKey),

				prop("duration", getAnimation()::getDuration),

				prop("frameRate", getAnimation()::getFrameRate),

				prop("delay", getAnimation()::getDelay),

				prop("repeatDelay", getAnimation()::getRepeatDelay),

				prop("yoyo", getAnimation()::isYoyo),

				prop("showOnStart", getAnimation()::isShowOnStart),

				prop("hideOnComplete", getAnimation()::isHideOnComplete),

				prop("skipMissedFrames", getAnimation()::isSkipMissedFrames)

		);
	}

	private PGridInfoProperty prop(String field, Supplier<Object> getter) {
		return new PGridInfoProperty(_id, field, help(field), getter);
	}

	private static String help(String field) {
		return InspectCore.getPhaserHelp().getMemberHelp("Phaser.Animations.Animation." + field);
	}

	public AnimationModel getAnimation() {
		return _animationElement.getAnimation();
	}

	public AnimationAssetElementModel getAnimationElement() {
		return _animationElement;
	}
}