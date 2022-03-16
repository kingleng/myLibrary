package com.kingleng.demo

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.options.ProjectOptions
import com.android.build.gradle.options.BooleanOption
import com.kingleng.demo.hooker.TaskHookerManager
import com.kingleng.demo.transform.StripClassAndResTransform
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyResolveDetails
import org.gradle.api.artifacts.ResolutionStrategy
import org.gradle.internal.reflect.Instantiator
import com.kingleng.demo.hooker.PrepareDependenciesHooker
import com.kingleng.demo.hooker.MergeAssetsHooker
import com.kingleng.demo.hooker.MergeManifestsHooker
import com.kingleng.demo.hooker.MergeJniLibsHooker
import com.kingleng.demo.hooker.ProcessResourcesHooker
import com.kingleng.demo.hooker.ProguardHooker
import com.kingleng.demo.hooker.DxTaskHooker
import com.kingleng.demo.utils.FileBinaryCategory
import com.kingleng.demo.utils.Reflect
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry

import javax.inject.Inject

public class KLPlugin extends BasePlugin {

    private def hostDir

    protected boolean isBuildingPlugin = false
    private boolean checked

    /**
     * TaskHooker manager, registers hookers when apply invoked
     */
    private TaskHookerManager taskHookerManager

    private StripClassAndResTransform stripClassAndResTransform

    @Inject
    KLPlugin(Instantiator instantiator, ToolingModelBuilderRegistry registry) {
        super(instantiator, registry)
    }

    @Override
    public void apply(Project project) {
        super.apply(project)

        hostDir = new File(project.rootDir, "host")
        if (!hostDir.exists()) {
            hostDir.mkdirs()
        }

        KLApk.hostDependenceFile = new File(hostDir, "versions.txt")

        project.afterEvaluate {
            if (!isBuildingPlugin) {
                return
            }

            Log.i 'KLPlugin', "stripClassAndResTransform.onProjectAfterEvaluate()"
            stripClassAndResTransform.onProjectAfterEvaluate()
            taskHookerManager = new VATaskHookerManager(project, instantiator)
            taskHookerManager.registerTaskHookers()

            if (android.dataBinding.enabled) {
                project.dependencies.add('annotationProcessor', project.files(jarPath.absolutePath))
            }

            android.applicationVariants.each { ApplicationVariantImpl variant ->

                KLApk.with {
                    VAExtention.VAContext vaContext = getVaContext(variant.name)
                    vaContext.packageName = variant.applicationId
                    vaContext.packagePath = vaContext.packageName.replace('.'.charAt(0), File.separatorChar)
                    vaContext.hostSymbolFile = new File(hostDir, "Host_R.txt")
                }
            }
        }

    }

    @Override
    protected void beforeCreateAndroidTasks(boolean isBuildingPlugin) {

        this.isBuildingPlugin = isBuildingPlugin
        if (!isBuildingPlugin) {
            Log.i 'KLPlugin', "Skipped all KLApk's configurations! isBuildingPlugin = " + isBuildingPlugin
            return
        }

        checkConfig()

        stripClassAndResTransform = new StripClassAndResTransform(project)
        android.registerTransform(stripClassAndResTransform)

        android.defaultConfig.buildConfigField("int", "PACKAGE_ID", "0x" + Integer.toHexString(KLApk.packageId))

        HashSet<String> replacedSet = [] as HashSet
        project.rootProject.subprojects { Project p ->
            p.configurations.all { Configuration configuration ->
                configuration.resolutionStrategy { ResolutionStrategy resolutionStrategy ->
                    resolutionStrategy.eachDependency { DependencyResolveDetails details ->
                        if (!isBuildingPlugin) {
                            return
                        }

                        checkConfig()

                        def hostDependency = KLApk.hostDependencies.get("${details.requested.group}:${details.requested.name}")
                        if (hostDependency != null) {
                            if ("${details.requested.version}" != "${hostDependency['version']}") {
                                String key = "${p.name}:${details.requested}"
                                if (!replacedSet.contains(key)) {
                                    replacedSet.add(key)
                                    if (KLApk.forceUseHostDependences) {
                                        Log.i 'Dependencies', "ATTENTION: Replaced module [${details.requested}] in project(:${p.name})'s configuration to host version: [${hostDependency['version']}]!"
                                    } else {
                                        KLApk.addWarning "WARNING: [${details.requested}] in project(:${p.name})'s configuration will be occupied by Host App! Please change it to host version: [${hostDependency['group']}:${hostDependency['name']}:${hostDependency['version']}]."
                                        KLApk.setFlag('tip.forceUseHostDependences', true)
                                    }
                                }

                                if (KLApk.forceUseHostDependences) {
                                    details.useVersion(hostDependency['version'])
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    File getJarPath() {
        URL url = this.class.getResource("")
        int index = url.path.indexOf('!')
        if (index < 0) {
            index = url.path.length()
        }
        return project.file(url.path.substring(0, index))
    }

    /**
     * Check the plugin apk related config infos
     */
    private void checkConfig() {
        Log.i 'KLPlugin', "checkConfig = " + checked
        if (checked) {
            return
        }
        checked = true

        int packageId = KLApk.packageId
        if (packageId == 0) {
            def err = new StringBuilder('you should set the packageId in build.gradle,\n ')
            err.append('please declare it in application project build.gradle:\n')
            err.append('    KLApk {\n')
            err.append('        packageId = 0xXX \n')
            err.append('    }\n')
            err.append('apply for the value of packageId.\n')
            throw new InvalidUserDataException(err.toString())
        }
        if (packageId >= 0x7f || packageId <= 0x01) {
            throw new IllegalArgumentException('the packageId must be in [0x02, 0x7E].')
        }

        String targetHost = KLApk.targetHost
        if (!targetHost) {
            def err = new StringBuilder('\nyou should specify the targetHost in build.gradle, e.g.: \n')
            err.append('    KLApk {\n')
            err.append('        //when target Host in local machine, value is host application directory\n')
            err.append('        targetHost = ../xxxProject/app \n')
            err.append('    }\n')
            throw new InvalidUserDataException(err.toString())
        }

        File hostLocalDir = new File(targetHost)
        if (!hostLocalDir.exists()) {
            def err = "The directory of host application doesn't exist! Dir: ${hostLocalDir.canonicalPath}"
            throw new InvalidUserDataException(err)
        }

        File hostR = new File(hostLocalDir, "build/KLHost/Host_R.txt")
        if (hostR.exists()) {
            def dst = new File(hostDir, "Host_R.txt")
            use(FileBinaryCategory) {
                dst << hostR
            }
        } else {
            def err = new StringBuilder("Can't find ${hostR.canonicalPath}, please check up your host application\n")
            err.append("  need apply com.kingleng.demo.host in build.gradle of host application\n ")
            throw new InvalidUserDataException(err.toString())
        }

        File hostVersions = new File(hostLocalDir, "build/KLHost/versions.txt")
        if (hostVersions.exists()) {
            def dst = new File(hostDir, "versions.txt")
            use(FileBinaryCategory) {
                dst << hostVersions
            }
        } else {
            def err = new StringBuilder("Can't find ${hostVersions.canonicalPath}, please check up your host application\n")
            err.append("  need apply com.kingleng.demo.host in build.gradle of host application \n")
            throw new InvalidUserDataException(err.toString())
        }

        File hostMapping = new File(hostLocalDir, "build/KLHost/mapping.txt")
        if (hostMapping.exists()) {
            def dst = new File(hostDir, "mapping.txt")
            use(FileBinaryCategory) {
                dst << hostMapping
            }
        }


        AppPlugin appPlugin = project.plugins.findPlugin(AppPlugin)
        ProjectOptions projectOptions = Reflect.on(appPlugin).field('projectOptions').get()
        if (projectOptions.get(BooleanOption.ENABLE_DEX_ARCHIVE)) {
            throw new InvalidUserDataException("Can't using incremental dexing mode, please add 'android.useDexArchive=false' in gradle.properties of :${project.name}.")
        }
//        project.ext.set('android.useDexArchive', false)

    }

    static class VATaskHookerManager extends TaskHookerManager {

        VATaskHookerManager(Project project, Instantiator instantiator) {
            super(project, instantiator)
        }

        @Override
        void registerTaskHookers() {
            android.applicationVariants.all { ApplicationVariantImpl appVariant ->
                if (!appVariant.buildType.name.equalsIgnoreCase("release")) {
                    return
                }

                registerTaskHooker(instantiator.newInstance(PrepareDependenciesHooker, project, appVariant))
                registerTaskHooker(instantiator.newInstance(MergeAssetsHooker, project, appVariant))
                registerTaskHooker(instantiator.newInstance(MergeManifestsHooker, project, appVariant))
                registerTaskHooker(instantiator.newInstance(MergeJniLibsHooker, project, appVariant))
//                registerTaskHooker(instantiator.newInstance(ShrinkResourcesHooker, project, appVariant))
                registerTaskHooker(instantiator.newInstance(ProcessResourcesHooker, project, appVariant))
                registerTaskHooker(instantiator.newInstance(ProguardHooker, project, appVariant))
                registerTaskHooker(instantiator.newInstance(DxTaskHooker, project, appVariant))
            }
        }
    }

}

