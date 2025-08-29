package com.example.application;

public record FolderHierarchyItem(
    int id,
    String name,
    Integer parentId,
    Integer[] path,
    boolean hasChildren
) {}
