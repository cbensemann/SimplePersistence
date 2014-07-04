/*
 * Copyright 2014 Nomad Consulting Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nz.co.nomadconsulting.simplepersistence;

import nz.co.nomadconsulting.simplepersistence.FilterParameters.FilterParameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;


/**
 * Simple extension which scans all classes and package-info for {@link FilterParameters} and {@link FilterParameter}. These are later accessed to 
 * automatically enable hibernate filters. 
 */
@ApplicationScoped
public class HibernateFilterExtension implements Extension {

    private final Map<String, Filter> filterDefinitions = new HashMap<>();

    private final Set<String> processedPackages = new HashSet<>();


    // TODO this is called way too much. I thought it would be on startup only? is it a scoping thing? or cause we inject into Interceptor?
    public <T> void processAnnotatedType(
            @Observes @WithAnnotations({ FilterParameters.class, FilterParameter.class }) final ProcessAnnotatedType<T> event) {
        final AnnotatedType<T> annotatedType = event.getAnnotatedType();
        if (annotatedType.isAnnotationPresent(FilterParameters.class)) {
            final FilterParameters annotation = annotatedType.getAnnotation(FilterParameters.class);
            addFilterParameters(annotation);
        }
        if (annotatedType.isAnnotationPresent(FilterParameter.class)) {
            // TODO deal with single parameters
        }
    }


    private <T> void addFilterParameters(final FilterParameters annotation) {
        final Filter filter = new Filter(annotation.name());
        for (final FilterParameter parameter : annotation.parameters()) {
            filter.addParameter(parameter.key(), parameter.value());
        }
        if (!annotation.enabledExpression().trim().isEmpty()) {
            filter.setEnabledExpression(annotation.enabledExpression());
        }
        filterDefinitions.put(annotation.name(), filter);
    }


    public <T> void processPackageAnnotatedType(@Observes final ProcessAnnotatedType<T> event) {
        final Package pkg = event.getAnnotatedType().getJavaClass().getPackage();

        if (pkg == null) {
            return;
        }

        final String packageName = pkg.getName();
        if (processedPackages.contains(packageName)) {
            return; // already processed a class from this package
        }

        processedPackages.add(packageName);
        if (pkg.isAnnotationPresent(FilterParameters.class)) {
            addFilterParameters(pkg.getAnnotation(FilterParameters.class));
        }

        if (pkg.isAnnotationPresent(FilterParameter.class)) {
            // TODO addFilterParameter(pkg.getAnnotation(FilterParameter.class));
        }
    }


    public Map<String, Filter> getFilterDefinitions() {
        return filterDefinitions;
    }
}
