package org.vaadin.folder.ui.view;

import java.util.List;
import java.util.stream.Stream;

import org.vaadin.folder.domain.Folder;
import org.vaadin.folder.service.FolderService;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.FlatHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.router.Route;

@Route("")
public class FolderView extends Main {

    private class FolderDataProvider
            extends AbstractHierarchicalDataProvider<Folder, Object>
            implements FlatHierarchicalDataProvider<Folder, Object> {
        private final FolderService folderService;

        public FolderDataProvider(FolderService folderService) {
            this.folderService = folderService;
        }

        @Override
        public int getChildCount(HierarchicalQuery<Folder, Object> query) {
            List<Long> expandedFolderIds = query.getExpandedItemIds()
                    .stream()
                    .map(id -> (Long) id)
                    .toList();

            return folderService.countFolderHierarchy(expandedFolderIds);
        }

        @Override
        public Stream<Folder> fetchChildren(
                HierarchicalQuery<Folder, Object> query) {
            List<Long> expandedFolderIds = query.getExpandedItemIds()
                    .stream()
                    .map(id -> (Long) id)
                    .toList();

            return folderService.listFolderHierarchy(expandedFolderIds, query.getOffset(), query.getLimit()).stream();
        }

        @Override
        public Object getId(Folder folder) {
            return folder.getId();
        }

        @Override
        public int getDepth(Folder folder) {
            return folder.getDepth();
        }

        @Override
        public boolean hasChildren(Folder folder) {
            return true;
        }

        @Override
        public Folder getParentItem(Folder folder) {
            return null;
        }

        @Override
        public boolean isInMemory() {
            return false;
        }
    }

    public FolderView(FolderService folderService) {
        TreeGrid<Folder> treeGrid = new TreeGrid<>();
        treeGrid.addHierarchyColumn(folder -> folder.getName()).setHeader("Folder");
        treeGrid.setDataProvider(new FolderDataProvider(folderService));
        add(treeGrid);
    }
}
