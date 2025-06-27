package org.vaadin.folder.domain;

public interface FolderHierarchyItem {
    Long getId();
    String getName();
    Long getParentId();
    int getDepth();
    boolean isHasChildren();
}
