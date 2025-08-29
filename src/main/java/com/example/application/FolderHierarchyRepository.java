package com.example.application;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.application.jooq.tables.Folders;

import java.util.stream.Stream;

@Repository
public class FolderHierarchyRepository {
    @Autowired
    private DSLContext dsl;

    private static String CTE_SQL = """
            WITH RECURSIVE folders_cte(id, name, parent_id, path) AS (
                SELECT
                    folders.id,
                    folders.name,
                    folders.parent_id,
                    ARRAY[folders.id]
                    FROM folders
                    WHERE {0}
                UNION ALL
                SELECT
                    folders.id,
                    folders.name,
                    folders.parent_id,
                    ARRAY_APPEND(folders_cte.path, folders.id)
                    FROM folders, folders_cte
                    WHERE folders.parent_id = folders_cte.id AND folders.parent_id = ANY({1})
            )
            """;

    public int countFlattenedFolderHierarchy(FolderHierarchyItem parent, Integer[] expandedItemIds) {
        String sql = CTE_SQL + """
                SELECT COUNT(*) FROM folders_cte
                """;

        return dsl.resultQuery(sql, getParentIdCondition(parent), DSL.val(expandedItemIds)).fetchOne(0, int.class);
    }

    public Stream<FolderHierarchyItem> fetchFlattenedFolderHierarchy(FolderHierarchyItem parent,
            Integer[] expandedItemIds, int offset, int limit) {
        String sql = CTE_SQL + "," + """
                has_children AS (SELECT DISTINCT parent_id FROM folders WHERE parent_id IS NOT NULL)

                SELECT
                    folders_cte.id,
                    folders_cte.name,
                    folders_cte.parent_id,
                    folders_cte.path,
                    (hc.parent_id IS NOT NULL) AS has_children
                FROM folders_cte
                LEFT JOIN has_children hc ON hc.parent_id = folders_cte.id
                ORDER BY path OFFSET {2} LIMIT {3}
                """;

        return dsl.resultQuery(sql, getParentIdCondition(parent), DSL.val(expandedItemIds), offset, limit)
                .fetchStreamInto(FolderHierarchyItem.class);
    }

    public int changeParent(FolderHierarchyItem newParentItem, FolderHierarchyItem item) {
        return dsl.update(Folders.FOLDERS)
                .set(Folders.FOLDERS.PARENT_ID, newParentItem.id())
                .where(Folders.FOLDERS.ID.eq(item.id()))
                .execute();
    }

    private Condition getParentIdCondition(FolderHierarchyItem parent) {
        return parent != null ? DSL.condition("folders.parent_id = ?", parent.id())
                : DSL.condition("folders.parent_id IS NULL");
    }
}
