/// <reference path="./Viewer.ts"/>

namespace phasereditor2d.ui.controls.viewers {

    export const TREE_ICON_SIZE = 16;
    export const LABEL_MARGIN = TREE_ICON_SIZE + 0;


    export declare type TreeIconInfo = {
        rect: Rect,
        obj: any
    }

    export class TreeViewer extends Viewer {

        private _treeRenderer: TreeViewerRenderer;
        private _treeIconList: TreeIconInfo[];

        constructor(...classList: string[]) {
            super("TreeViewer", ...classList);

            this.getCanvas().addEventListener("click", e => this.onClick(e));

            this._treeRenderer = new TreeViewerRenderer(this);

            this._treeIconList = [];
        }

        getTreeRenderer() {
            return this._treeRenderer;
        }

        setTreeRenderer(treeRenderer: TreeViewerRenderer): void {
            this._treeRenderer = treeRenderer;
        }

        private onClick(e: MouseEvent) {
            for (let icon of this._treeIconList) {
                if (icon.rect.contains(e.offsetX, e.offsetY)) {
                    this.setExpanded(icon.obj, !this.isExpanded(icon.obj));
                    this.repaint();
                    return;
                }
            }
        }

        visitObjects(visitor: Function) {
            const list = this.getContentProvider().getRoots(this.getInput());
            this.visitObjects2(list, visitor);
        }

        private visitObjects2(objects: any[], visitor: Function) {
            for (var obj of objects) {
                visitor(obj);
                if (this.isExpanded(obj) || this.getFilterText() !== "") {
                    const list = this.getContentProvider().getChildren(obj);
                    this.visitObjects2(list, visitor);
                }
            }
        }

        async preload(): Promise<PreloadResult> {
            const list: Promise<PreloadResult>[] = [];
            try {
                this.visitObjects(obj => {
                    try {
                        const provider = this.getCellRendererProvider();
                        list.push(provider.preload(obj).then(r => {
                            try {
                                const renderer = provider.getCellRenderer(obj);
                                return renderer.preload(obj);
                            } catch (e) {
                                console.log(e);
                            }

                        }));
                    } catch (e) {
                        console.log(e);
                    }
                });
            } catch (e) {
                console.log(e);
            }

            return Controls.resolveAll(list);
        }


        protected paint(): void {
            const result = this._treeRenderer.paint();

            this._contentHeight = result.contentHeight;
            this._paintItems = result.paintItems;
            this._treeIconList = result.treeIconList;
        }

        setFilterText(filter: string) {
            super.setFilterText(filter);

            if (filter !== "") {
                this.expandFilteredParents(this.getContentProvider().getRoots(this.getInput()));
                this.repaint();
            }
        }

        private expandFilteredParents(objects: any[]): void {
            const contentProvider = this.getContentProvider();
            for (const obj of objects) {
                if (this.isFilterIncluded(obj)) {
                    const children = contentProvider.getChildren(obj);
                    if (children.length > 0) {
                        this.setExpanded(obj, true);
                        this.expandFilteredParents(children);
                    }
                }
            }
        }

        protected buildFilterIncludeMap() {
            const roots = this.getContentProvider().getRoots(this.getInput());

            this.buildFilterIncludeMap2(roots);
        }

        private buildFilterIncludeMap2(objects: any[]): boolean {
            let result = false;

            for (const obj of objects) {
                let resultThis = this.matches(obj);

                const children = this.getContentProvider().getChildren(obj);
                const resultChildren = this.buildFilterIncludeMap2(children);

                resultThis = resultThis || resultChildren;

                if (resultThis) {
                    this._filterIncludeSet.add(obj);
                    result = true;
                }
            }

            return result;
        }

        getContentProvider(): ITreeContentProvider {
            return <ITreeContentProvider>super.getContentProvider();
        }

        setContentProvider(contentProvider: ITreeContentProvider): void {
            super.setContentProvider(contentProvider);
        }
    }
}