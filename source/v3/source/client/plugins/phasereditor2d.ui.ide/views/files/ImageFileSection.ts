namespace phasereditor2d.ui.ide.views.files {

    import controls = colibri.ui.controls;
    import ide = colibri.ui.ide;
    import core = colibri.core;

    export class ImageFileSection extends controls.properties.PropertySection<core.io.FilePath> {

        constructor(page: controls.properties.PropertyPage) {
            super(page, "files.ImagePreviewSection", "Image", true);
        }

        protected createForm(parent: HTMLDivElement) {

            parent.classList.add("ImagePreviewFormArea", "PreviewBackground");

            const imgControl = new controls.ImageControl(ide.IMG_SECTION_PADDING);

            this.getPage().addEventListener(controls.EVENT_CONTROL_LAYOUT, (e: CustomEvent) => {
                imgControl.resizeTo();
            })

            parent.appendChild(imgControl.getElement());
            setTimeout(() => imgControl.resizeTo(), 1);

            this.addUpdater(() => {
                const file = this.getSelection()[0];
                const img = ide.Workbench.getWorkbench().getFileImage(file);
                imgControl.setImage(img);
                setTimeout(() => imgControl.resizeTo(), 1);
            });
        }

        canEdit(obj: any): boolean {

            if (obj instanceof core.io.FilePath) {

                const ct = ide.Workbench.getWorkbench().getContentTypeRegistry().getCachedContentType(obj);

                return ct === ide.CONTENT_TYPE_IMAGE;
            }

            return false;
        }

        canEditNumber(n: number): boolean {
            return n == 1;
        }

    }
}