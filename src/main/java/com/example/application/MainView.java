package com.example.application;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends Div {
    private FolderHierarchyItem draggedItem = null;

    @Autowired
    public MainView(FolderHierarchyRepository folderRepository) {
        TreeGrid<FolderHierarchyItem> treeGrid = new TreeGrid<>();
        treeGrid.addHierarchyColumn(FolderHierarchyItem::name);
        treeGrid.setDataProvider(new FolderHierarchyDataProvider(folderRepository));
        treeGrid.setRowsDraggable(true);
        treeGrid.setDropMode(GridDropMode.ON_TOP);
        treeGrid.setHeight("800px");

        treeGrid.addDragStartListener(event -> {
            draggedItem = event.getDraggedItems().get(0);
        });

        treeGrid.addDropListener(event -> {
            FolderHierarchyItem targetItem = event.getDropTargetItem().orElse(null);
            if (targetItem == null || targetItem.id() == draggedItem.id()) {
                return;
            }

            // Folder folder = folderRepository.findById(draggedItem.getId()).get();
            // folder.setParentId(targetItem.getId());
            // folderRepository.save(folder);

            treeGrid.getDataProvider().refreshAll();
        });

        // Button expand1Level = new Button("Expand 1 level", event -> {
        // // treeGrid.expandRecursively(folderService(), 1);
        // });

        // Button expand2Levels = new Button("Expand 2 levels", event -> {
        // // treeGrid.expandRecursively(folderService.findAll(), 2);
        // });

        // Button expand3Levels = new Button("Expand 3 levels", event -> {
        // // treeGrid.expandRecursively(folderService.findAll(), 3);
        // });

        add(treeGrid);
    }
}
