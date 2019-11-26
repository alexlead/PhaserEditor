namespace phasereditor2d.code.ui.editors {

    import controls = colibri.ui.controls;
    import io = colibri.core.io;

    export class MonacoEditorFactory extends colibri.ui.ide.EditorFactory {

        private _language: string;
        private _contentType: string;

        constructor(language: string, contentType: string) {
            super("phasereditor2d.core.ui.editors.MonacoEditorFactory#" + language);

            this._language = language;
            this._contentType = contentType;
        }

        acceptInput(input: any): boolean {

            if (input instanceof io.FilePath) {

                const contentType = colibri.ui.ide.Workbench.getWorkbench().getContentTypeRegistry().getCachedContentType(input);

                return this._contentType === contentType;
            }

            return false;
        }

        createEditor(): colibri.ui.ide.EditorPart {
            return new MonacoEditor(this._language);
        }

    }

    export class MonacoEditor extends colibri.ui.ide.FileEditor {

        private static _factory: colibri.ui.ide.EditorFactory;

        private _monacoEditor: monaco.editor.IStandaloneCodeEditor;
        private _language: string;

        constructor(language: string) {

            super("phasereditor2d.core.ui.editors.JavaScriptEditor");

            this.addClass("MonacoEditor");

            this._language = language;
        }

        protected createPart(): void {

            const container = document.createElement("div");
            container.classList.add("MonacoEditorContainer");

            this.getElement().appendChild(container);

            this._monacoEditor = monaco.editor.create(container, {
                language: this._language,
                fontSize: 16,
                scrollBeyondLastLine: false,
            });

            this._monacoEditor.onDidChangeModelContent(e => {
                this.setDirty(true);
            });
        }

        async doSave() {

            const content = this._monacoEditor.getValue();

            try {

                await colibri.ui.ide.FileUtils.setFileString_async(this.getInput(), content);

                this.setDirty(false);

            } catch (e) {

                console.error(e);
            }
        }

        setInput(file: io.FilePath) {

            super.setInput(file);

            this.updateContent();
        }

        private async updateContent() {

            const file = this.getInput();

            if (!file) {
                return;
            }

            const content = await colibri.ui.ide.FileUtils.preloadAndGetFileString(file);

            this._monacoEditor.setValue(content);

            this.setDirty(false);
        }

        layout() {

            super.layout();

            if (this._monacoEditor) {

                this._monacoEditor.layout();
            }
        }

        protected onEditorInputContentChanged() {

        }
    }
}