namespace phasereditor2d.pack.ui.editor {

    import controls = colibri.ui.controls;
    import io = colibri.core.io;
    import ide = colibri.ui.ide;

    export class ImportFileSection extends controls.properties.PropertySection<io.FilePath> {

        constructor(page: controls.properties.PropertyPage) {
            super(page, "phasereditor2d.pack.ui.editor.ImportFileSection", "Import File", false);
        }

        protected createForm(parent: HTMLDivElement) {
            const comp = this.createGridElement(parent, 1);

            this.addUpdater(() => {

                console.log("----");

                while (comp.children.length > 0) {
                    comp.children.item(0).remove();
                }

                const importList: ImportData[] = [];

                for (const importer of importers.Importers.LIST) {

                    const files = this.getSelection().filter(file => importer.acceptFile(file));

                    if (files.length > 0) {
                        importList.push({
                            importer: importer,
                            files: files
                        });
                    }
                }

                for (const importData of importList) {

                    const btn = document.createElement("button");

                    btn.innerText = `Import ${importData.importer.getType()} (${importData.files.length})`;

                    btn.addEventListener("click", e => {

                        const editor = <AssetPackEditor>ide.Workbench.getWorkbench().getActiveEditor();

                        editor.importData(importData);
                    });

                    comp.appendChild(btn);
                }
            });
        }

        canEdit(obj: any, n: number): boolean {
            return obj instanceof io.FilePath && obj.isFile();
        }

        canEditNumber(n: number): boolean {
            return n > 0;
        }
    }
}