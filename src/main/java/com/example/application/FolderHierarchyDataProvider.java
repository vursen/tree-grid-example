package com.example.application;

import java.util.stream.Stream;

import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;

public class FolderHierarchyDataProvider extends AbstractHierarchicalDataProvider<FolderHierarchyItem, Void> {
    private final FolderHierarchyRepository folderRepository;

    public FolderHierarchyDataProvider(FolderHierarchyRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public HierarchyFormat getHierarchyFormat() {
        return HierarchyFormat.FLATTENED;
    }

    @Override
    public int getChildCount(HierarchicalQuery<FolderHierarchyItem, Void> query) {
        return folderRepository.countFlattenedFolderHierarchy(query.getParent(),
                query.getExpandedItemIds().toArray(Integer[]::new));
    }

    @Override
    public Stream<FolderHierarchyItem> fetchChildren(HierarchicalQuery<FolderHierarchyItem, Void> query) {
        return folderRepository.fetchFlattenedFolderHierarchy(query.getParent(),
                query.getExpandedItemIds().toArray(Integer[]::new), query.getOffset(), query.getLimit());
    }

    @Override
    public Object getId(FolderHierarchyItem item) {
        return item.id();
    }

    @Override
    public int getDepth(FolderHierarchyItem item) {
        return item.path().length - 1;
    }

    @Override
    public boolean hasChildren(FolderHierarchyItem item) {
        return item.hasChildren();
    }

    @Override
    public boolean isInMemory() {
        return false;
    }
}
