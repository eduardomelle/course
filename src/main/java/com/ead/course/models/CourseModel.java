package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;

    @Column(nullable = false, length = 255)
    private String name;

    private String description;

    private String imageUrl;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdateDate;

    private CourseStatus courseStatus;

    private CourseLevel courseLevel;

    private UUID userInstructor;

}
