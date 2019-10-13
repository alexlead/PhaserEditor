namespace phasereditor2d.files.ui.viewers {

    import controls = colibri.ui.controls;
    import ide = colibri.ui.ide;
    import io = colibri.core.io;
    import viewers = colibri.ui.controls.viewers;

    export class FileCellRendererProvider implements viewers.ICellRendererProvider {

        getCellRenderer(file: io.FilePath): viewers.ICellRenderer {

            const contentType = ide.Workbench.getWorkbench().getContentTypeRegistry().getCachedContentType(file);

            const extensions = ide.Workbench
                .getWorkbench()
                .getExtensionRegistry()
                .getExtensions<ContentTypeCellRendererExtension>(ContentTypeCellRendererExtension.POINT);

            if (extensions.length > 0) {

                const provider = extensions[0].getRendererProvider(contentType);

                if (provider !== null) {
                    return provider.getCellRenderer(file);
                }
            }

            return new FileCellRenderer();
        }

        preload(file: io.FilePath): Promise<controls.PreloadResult> {
            return ide.Workbench.getWorkbench().getContentTypeRegistry().preload(file);
        }
    }
}