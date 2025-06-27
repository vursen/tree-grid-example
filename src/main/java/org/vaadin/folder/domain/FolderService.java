package org.vaadin.folder.domain;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class FolderService {
    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Stream<FolderHierarchyItem> fetchFolderHierarchy(Collection<Long> expandedFolderIds, int offset, int limit) {
        return folderRepository.fetchFolderHierarchy(expandedFolderIds, offset, limit).stream();
    }

    public int countFolderHierarchy(Collection<Long> expandedFolderIds) {
        return folderRepository.fetchFolderHierarchy(expandedFolderIds, null, null).getSize();
    }
}
