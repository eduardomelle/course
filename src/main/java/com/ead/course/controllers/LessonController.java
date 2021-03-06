package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    private final ModuleService moduleService;
    private final LessonService lessonService;

    public LessonController(ModuleService moduleService, LessonService lessonService) {
        this.moduleService = moduleService;
        this.lessonService = lessonService;
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody @Valid LessonDto lessonDto) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if (!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
        }

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setModule(moduleModelOptional.get());
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId) {
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (!optionalLessonModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }

        lessonService.delete(optionalLessonModel.get());

        return ResponseEntity.ok("Lesson deleted successfully.");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto) {
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (!optionalLessonModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }

        var lessonModel = optionalLessonModel.get();
        BeanUtils.copyProperties(lessonDto, lessonModel);

        return ResponseEntity.ok(lessonService.save(lessonModel));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(
            @PathVariable(value = "moduleId") UUID moduleId,
            SpecificationTemplate.LessonSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId
    ) {
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (!optionalLessonModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }

        return ResponseEntity.ok(optionalLessonModel.get());
    }

}
