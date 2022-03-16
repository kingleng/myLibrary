package com.kingleng.demo

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.ide.ArtifactDependencyGraph
import com.android.build.gradle.internal.publishing.AndroidArtifacts
import com.android.build.gradle.internal.pipeline.TransformTask
import com.android.build.gradle.internal.transforms.ProGuardTransform
import com.android.build.gradle.tasks.ProcessAndroidResources
import com.google.common.collect.ImmutableMap
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import com.kingleng.demo.utils.FileUtil
import com.kingleng.demo.utils.Reflect

public class KLHostPlugin implements Plugin<Project> {

    public static final String TAG = "KLHostPlugin"
    Project project
    File vaHostDir

    @Override
    public void apply(Project project) {

        this.project = project
        project.ext.set(Constants.GRADLE_3_1_0, false)

        try {
            Class.forName('com.android.builder.core.VariantConfiguration')
        } catch (Throwable e) {
            // com.android.tools.build:gradle:3.1.0
            project.ext.set(Constants.GRADLE_3_1_0, true)
        }

        //The target project must be a android application module
        if (!project.plugins.hasPlugin('com.android.application')) {
            Log.e(TAG, "application required!")
            return
        }

        vaHostDir = new File(project.getBuildDir(), "KLHost")

        project.afterEvaluate {

            project.android.applicationVariants.each { ApplicationVariantImpl variant ->

                generateDependencies(variant)
                backupHostR(variant)
                backupProguardMapping(variant)
            }
        }


    }

    /**
     * Generate ${project.buildDir}/VAHost/versions.txt
     */
    def generateDependencies(ApplicationVariantImpl applicationVariant) {

        applicationVariant.javaCompile.doLast {

            FileUtil.saveFile(vaHostDir, "allVersions", {
                List<String> deps = new ArrayList<String>()
                project.configurations.each {
                    String configName = it.name

                    if (!it.canBeResolved) {
                        deps.add("${configName} -> NOT READY")
                        return
                    }

                    try {
                        it.resolvedConfiguration.resolvedArtifacts.each {
                            deps.add("${configName} -> id: ${it.moduleVersion.id}, type: ${it.type}, ext: ${it.extension}")
                        }

                    } catch (Exception e) {
                        deps.add("${configName} -> ${e}")
                    }
                }
                Collections.sort(deps)
                return deps
            })

            FileUtil.saveFile(vaHostDir, "versions", {
                List<String> deps = new ArrayList<String>()
                Log.i TAG, "Used compileClasspath: ${applicationVariant.name}"
                Set<ArtifactDependencyGraph.HashableResolvedArtifactResult> compileArtifacts
                if (project.extensions.extraProperties.get(Constants.GRADLE_3_1_0)) {
                    ImmutableMap<String, String> buildMapping = Reflect.on('com.android.build.gradle.internal.ide.ModelBuilder')
                            .call('computeBuildMapping', project.gradle)
                            .get()
                    compileArtifacts = ArtifactDependencyGraph.getAllArtifacts(
                            applicationVariant.variantData.scope, AndroidArtifacts.ConsumedConfigType.COMPILE_CLASSPATH, null, buildMapping)
                } else {
                    compileArtifacts = ArtifactDependencyGraph.getAllArtifacts(
                            applicationVariant.variantData.scope, AndroidArtifacts.ConsumedConfigType.COMPILE_CLASSPATH, null)
                }

                compileArtifacts.each { ArtifactDependencyGraph.HashableResolvedArtifactResult artifact ->
                    ComponentIdentifier id = artifact.id.componentIdentifier
                    if (id instanceof ProjectComponentIdentifier) {
                        deps.add("${id.projectPath.replace(':', '')}:${ArtifactDependencyGraph.getVariant(artifact)}:unspecified ${artifact.file.length()}")

                    } else if (id instanceof ModuleComponentIdentifier) {
                        deps.add("${id.group}:${id.module}:${id.version} ${artifact.file.length()}")

                    } else {
                        deps.add("${artifact.id.displayName.replace(':', '')}:unspecified:unspecified ${artifact.file.length()}")
                    }
                }

                Collections.sort(deps)
                return deps
            })
        }

    }

    /**
     * Save R symbol file
     */
    def backupHostR(ApplicationVariant applicationVariant) {

        final ProcessAndroidResources aaptTask = this.project.tasks["process${applicationVariant.name.capitalize()}Resources"]

        aaptTask.doLast {
            project.copy {
                from aaptTask.textSymbolOutputFile
                into vaHostDir
                rename { "Host_R.txt" }
            }
        }
    }

    /**
     * Save proguard mapping
     */
    def backupProguardMapping(ApplicationVariant applicationVariant) {

        if (applicationVariant.buildType.minifyEnabled) {
            TransformTask proguardTask = project.tasks["transformClassesAndResourcesWithProguardFor${applicationVariant.name.capitalize()}"]

            ProGuardTransform proguardTransform = proguardTask.transform
            File mappingFile = proguardTransform.mappingFile

            proguardTask.doLast {
                project.copy {
                    from mappingFile
                    into vaHostDir
                }
            }
        }

    }

}

