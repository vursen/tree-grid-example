package org.vaadin.folder.domain;

import org.vaadin.base.domain.AbstractEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "folders")
public class Folder extends AbstractEntity<Long> {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Override
    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }
}
