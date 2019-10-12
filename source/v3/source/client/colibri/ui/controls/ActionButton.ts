/// <reference path="../controls/Control.ts" />

namespace colibri.ui.controls {

    export class ActionButton extends Control {
        private _action: Action;

        constructor(action: Action) {
            super("button");

            this._action = action;

            this.getElement().classList.add("actionButton");
        }

        getAction() {
            return this._action;
        }
    }
}