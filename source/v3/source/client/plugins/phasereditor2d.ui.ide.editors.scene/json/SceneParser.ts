
namespace phasereditor2d.ui.ide.editors.scene.json {

    export class SceneParser {

        private _scene: GameScene;

        constructor(scene: GameScene) {
            this._scene = scene;
        }

        static isValidSceneDataFormat(data: SceneData) {
            return "displayList" in data && Array.isArray(data.displayList);
        }

        createScene(data: SceneData) {

            this._scene.setSceneType(data.sceneType);

            for (const objData of data.displayList) {
                this.createObject(objData);
            }
        }

        async createSceneCache_async(data: SceneData) {

            for (const objData of data.displayList) {
                await this.updateSceneCacheWithObjectData_async(objData);
            }

        }

        private async updateSceneCacheWithObjectData_async(objData: any) {
            const type = objData.type;
            switch (type) {

                case "Image": {

                    const key = objData[TextureComponent.textureKey];

                    const item = pack.core.PackFinder.findAssetPackItem(key);

                    if (item) {
                        await this.addToCache_async(item);
                    }

                    break;
                }

                case "Container":

                    for (const childData of objData.list) {
                        await this.updateSceneCacheWithObjectData_async(childData);
                    }

                    break;
            }
        }

        async addToCache_async(data: pack.core.AssetPackItem | pack.core.AssetPackImageFrame) {

            let imageFrameContainerPackItem: pack.core.AssetPackItem = null;

            if (data instanceof pack.core.AssetPackItem) {
                if (data.getType() === pack.core.IMAGE_TYPE) {
                    imageFrameContainerPackItem = data;
                } else if (pack.core.AssetPackUtils.isImageFrameContainer(data)) {
                    imageFrameContainerPackItem = data;
                }
            } else if (data instanceof pack.core.AssetPackImageFrame) {
                imageFrameContainerPackItem = data.getPackItem();
            }

            if (imageFrameContainerPackItem !== null) {

                const parser = pack.core.AssetPackUtils.getImageFrameParser(imageFrameContainerPackItem);

                await parser.preload();

                parser.addToPhaserCache(this._scene.game);

            }
        }

        createObject(data: any) {
            const type = data.type;

            let sprite: Phaser.GameObjects.GameObject = null;

            switch (type) {
                case "Image":
                    sprite = this._scene.add.image(0, 0, "");
                    break;
                case "Container":
                    sprite = this._scene.add.container(0, 0, []);
                    break;
            }

            if (sprite) {

                sprite.setEditorScene(this._scene);

                sprite.readJSON(data);

                SceneParser.initSprite(sprite);

            }

            return sprite;
        }

        static initSprite(sprite: Phaser.GameObjects.GameObject) {

            sprite.setDataEnabled();

            if (sprite instanceof Phaser.GameObjects.Image) {
                sprite.setInteractive();
            }

        }

        static setNewId(sprite: Phaser.GameObjects.GameObject) {
            sprite.setEditorId(Phaser.Utils.String.UUID());
        }

    }

}