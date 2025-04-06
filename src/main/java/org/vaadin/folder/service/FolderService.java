package org.vaadin.folder.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.folder.domain.Folder;
import org.vaadin.folder.domain.FolderRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class FolderService {

    private final FolderRepository folderRepository;

    FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<Folder> listFolderHierarchy(Collection<Long> expandedFolderIds, Integer offset, Integer limit) {
        return folderRepository.findAllFoldersHierarchy(expandedFolderIds, offset, limit).toList();
    }

    public int countFolderHierarchy(Collection<Long> expandedFolderIds) {
        return folderRepository.findAllFoldersHierarchy(expandedFolderIds, null, null).getSize();
    }
}
