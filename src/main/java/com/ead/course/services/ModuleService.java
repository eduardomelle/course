package com.ead.course.services;

import com.ead.course.models.ModuleModel;

public interface ModuleService {

    ModuleModel save(ModuleModel moduleModel);

    void delete(ModuleModel moduleModel);

}
