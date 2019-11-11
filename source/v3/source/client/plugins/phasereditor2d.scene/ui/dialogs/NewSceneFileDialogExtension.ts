namespace phasereditor2d.scene.ui.dialogs {

    export class NewSceneFileDialogExtension extends phasereditor2d.ide.ui.dialogs.NewFileContentExtension {

        constructor() {
            super({
                id: "phasereditor2d.scene.ui.wizards.NewSceneFileWizardExtension",
                wizardName: "Scene File",
                icon: ScenePlugin.getInstance().getIcon(ICON_GROUP),
                fileExtension: "scene",
                initialFileName: "Scene",
                fileContent: `{
                    "sceneType": "Scene",
                    "displayList": []
                }`
            });
        }

        getInitialFileLocation() {
            return super.findInitialFileLocationBasedOnContentType(scene.core.CONTENT_TYPE_SCENE);
        }
    }
}