
// -- user code here --

/* --- start generated code --- */

// Generated by Phaser Editor 1.5.0 (Phaser v2.6.2)


/**
 * Preloader.
 */
function Preloader() {
	
	Phaser.State.call(this);
	
}

/** @type Phaser.State */
var Preloader_proto = Object.create(Phaser.State.prototype);
Preloader.prototype = Preloader_proto;
Preloader.prototype.constructor = Preloader;

Preloader.prototype.init = function () {
	
	this.scale.scaleMode = Phaser.ScaleManager.SHOW_ALL;
	this.scale.pageAlignHorizontally = true;
	this.scale.pageAlignVertically = true;
	
};

Preloader.prototype.preload = function () {
	
	this.load.pack('preloader', 'assets/pack.json');
	
	
	var _logo1 = this.add.sprite(275, 149, 'logo');
	_logo1.tint = 0x808000;
	
	var _logo = this.add.sprite(275, 149, 'logo');
	
	
	
	this.load.setPreloadSprite(_logo, 0);
	
};

Preloader.prototype.create = function () {
	
	this.nextState();
	
	
};

/* --- end generated code --- */

Preloader.prototype.nextState = function() {
	this.game.state.start("Menu");
};
