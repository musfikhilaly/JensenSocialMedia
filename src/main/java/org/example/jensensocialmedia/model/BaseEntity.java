package org.example.jensensocialmedia.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    protected Instant createdAt;
    
    @CreationTimestamp
    @Column(nullable = false)
    protected Instant updatedAt;
}
