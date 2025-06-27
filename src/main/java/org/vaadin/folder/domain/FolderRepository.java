package org.vaadin.folder.domain;

import java.util.Collection;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;

public interface FolderRepository extends CrudRepository<Folder, Long> {
    @NativeQuery("""
            WITH RECURSIVE folders_cte(id, name, parent_id, path, depth) AS (
                SELECT
                    folders.id,
                    folders.name,
                    folders.parent_id,
                    ARRAY[folders.id],
                    0
                    FROM folders
                    WHERE folders.parent_id IS NULL
                UNION ALL
                SELECT
                    folders.id,
                    folders.name,
                    folders.parent_id,
                    array_append(path, folders.id),
                    depth + 1
                    FROM folders, folders_cte
                    WHERE folders.parent_id = folders_cte.id AND folders.parent_id IN (?1)
            )

            SELECT
                *,
                EXISTS (SELECT * FROM folders children WHERE children.parent_id = folders_cte.id) AS has_children
                FROM folders_cte
                ORDER BY path OFFSET ?2 LIMIT ?3
            """)
    Slice<FolderHierarchyItem> fetchFolderHierarchy(Collection<Long> expandedFolderIds, Integer offset, Integer limit);
}
