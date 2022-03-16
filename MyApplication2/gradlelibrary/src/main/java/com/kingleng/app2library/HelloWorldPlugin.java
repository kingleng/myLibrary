package com.kingleng.app2library;

import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Created by leng on 2020/3/24.
 */
public class HelloWorldPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().create("hello", DefaultTask.class);


    }

}
