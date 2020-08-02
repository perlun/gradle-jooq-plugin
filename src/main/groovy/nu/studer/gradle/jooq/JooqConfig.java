package nu.studer.gradle.jooq;

import groovy.lang.Closure;
import nu.studer.gradle.jooq.jaxb.JaxbConfigurationBridge;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.Internal;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Target;

import javax.inject.Inject;

import static java.lang.String.format;
import static nu.studer.gradle.jooq.util.Objects.applyClosureToDelegate;

public class JooqConfig {

    final String name;

    private final Configuration jooqConfiguration;
    private final DirectoryProperty outputDir;

    @Inject
    public JooqConfig(String name, ObjectFactory objects, ProjectLayout layout) {
        this.name = name;

        this.jooqConfiguration = new Configuration().withGenerator(new Generator().withTarget(new Target().withDirectory(null)));
        this.outputDir = objects.directoryProperty().convention(layout.getBuildDirectory().dir("generated-src/jooq/" + name));
    }

    @Internal
    public Configuration getJooqConfiguration() {
        return jooqConfiguration;
    }

    @Internal
    public DirectoryProperty getOutputDir() {
        return outputDir;
    }

    @SuppressWarnings("unused")
    public void generationTool(Closure<?> closure) {
        // apply the given closure to the configuration bridge, i.e. its contained JAXB Configuration object
        JaxbConfigurationBridge delegate = new JaxbConfigurationBridge(jooqConfiguration, format("jooq.%s.generationTool", name));
        applyClosureToDelegate(closure, delegate);
    }

    @Override
    public String toString() {
        return "JooqConfig{" +
            "name='" + name + '\'' +
            ", jooqConfiguration=" + jooqConfiguration +
            ", outputDir=" + outputDir +
            '}';
    }

}

