package org.vaadin.folder.ui.view;

import java.util.List;
import java.util.stream.Stream;

import org.vaadin.folder.domain.Folder;
import org.vaadin.folder.domain.FolderHierarchyItem;
import org.vaadin.folder.domain.FolderRepository;
import org.vaadin.folder.domain.FolderService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.FlatHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.router.Route;

@Route("")
public class FolderView extends Main {

    private class FolderDataProvider
            extends AbstractHierarchicalDataProvider<FolderHierarchyItem, Object>
            implements FlatHierarchicalDataProvider<FolderHierarchyItem, Object> {
        private final FolderService folderService;

        public FolderDataProvider(FolderService folderService) {
            this.folderService = folderService;
        }

        @Override
        public int getChildCount(HierarchicalQuery<FolderHierarchyItem, Object> query) {
            List<Long> expandedItemIds = query.getExpandedItemIds()
                    .stream()
                    .map(id -> (Long) id)
                    .toList();

            return folderService.countFolderHierarchy(expandedItemIds);
        }

        @Override
        public Stream<FolderHierarchyItem> fetchChildren(
                HierarchicalQuery<FolderHierarchyItem, Object> query) {
            // TODO: There should be getExpandedItems() instead
            List<Long> expandedItemIds = query.getExpandedItemIds()
                    .stream()
                    .map(id -> (Long) id)
                    .toList();

            return folderService.fetchFolderHierarchy(expandedItemIds,
                    query.getOffset(), query.getLimit());
        }

        @Override
        public Object getId(FolderHierarchyItem item) {
            return item.getId();
        }

        @Override
        public int getDepth(FolderHierarchyItem item) {
            return item.getDepth();
        }

        @Override
        public boolean hasChildren(FolderHierarchyItem item) {
            return item.isHasChildren();
        }

        @Override
        public boolean isInMemory() {
            return false;
        }
    }

    private FolderHierarchyItem draggedItem = null;

    public FolderView(FolderService folderService, FolderRepository folderRepository) {
        TreeGrid<FolderHierarchyItem> treeGrid = new TreeGrid<>();
        treeGrid.addHierarchyColumn(folder -> folder.getName()).setHeader("Folder");
        treeGrid.setDataProvider(new FolderDataProvider(folderService));
        treeGrid.setRowsDraggable(true);
        treeGrid.setDropMode(GridDropMode.ON_TOP);
        treeGrid.setHeight("800px");

        treeGrid.addDragStartListener(event -> {
            draggedItem = event.getDraggedItems().get(0);
        });

        treeGrid.addDropListener(event -> {
            FolderHierarchyItem targetItem = event.getDropTargetItem().orElse(null);
            if (targetItem == null || targetItem.getId() == draggedItem.getId()) {
                return;
            }

            Folder folder = folderRepository.findById(draggedItem.getId()).get();
            folder.setParentId(targetItem.getId());
            folderRepository.save(folder);

            treeGrid.getDataProvider().refreshAll();
        });

        Button expand1Level = new Button("Expand 1 level", event -> {
            // treeGrid.expandRecursively(folderService(), 1);
        });

        Button expand2Levels = new Button("Expand 2 levels", event -> {
            // treeGrid.expandRecursively(folderService.findAll(), 2);
        });

        Button expand3Levels = new Button("Expand 3 levels", event -> {
            // treeGrid.expandRecursively(folderService.findAll(), 3);
        });

        add(treeGrid, expand1Level, expand2Levels, expand3Levels);
    }
}
