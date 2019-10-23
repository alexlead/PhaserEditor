
namespace colibri.ui.ide {

    export declare type ContentTypeIconExtensionConfig = {
        icon: controls.IImage,
        contentType: string
    }[];

    export class ContentTypeIconExtension extends core.extensions.Extension {

        static POINT_ID = "colibri.ui.ide.ContentTypeIconExtension";

        private _config: ContentTypeIconExtensionConfig;

        static withPluginIcons(plugin: ide.Plugin, config: {
            iconName: string,
            contentType: string
        }[]) {

            return new ContentTypeIconExtension(`${plugin.getId()}.ContentTypeIconExtension`,
                config.map(item => {
                    return {
                        icon: plugin.getIcon(item.iconName),
                        contentType: item.contentType
                    };
                }));
        }

        constructor(id: string, config: ContentTypeIconExtensionConfig) {
            super(id, 10);

            this._config = config;
        }

        getConfig() {
            return this._config;
        }

    }

}