package com.malcolmcrum.typescriptapigenerator.typescriptgenerator;

import com.malcolmcrum.typescriptapigenerator.demo.api.UsersApi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Generates a TypeScript file for interfacing with the API.
// This is done using string concatenation for simplicity's sake,
// but I suggest using a templating engine if you want to do this yourself.
public class TypeScriptApiGenerator {
    private static final String TYPES = "types";
    private final Class<?> service;

    public static void main(String[] args) throws IOException, URISyntaxException {
        var services = Set.of(
                UsersApi.class
        );

        StringBuilder output = new StringBuilder("import * as " + TYPES + " from './types'" + System.lineSeparator());
        for (Class<?> service : services) {
            output.append(new TypeScriptApiGenerator(service).toTypeScript());
        }

        // todo: is there a better way to write output to the target folder?
        Path root = Paths.get(TypeScriptApiGenerator.class.getResource("/").toURI()).getParent();
        Path folder = root.resolve("ts");
        folder.toFile().mkdirs();
        Path outputFile = folder.resolve("api.ts");
        Files.writeString(outputFile, output.toString());
        System.out.println("Wrote services to " + outputFile);
    }

    public TypeScriptApiGenerator(Class<?> service) {
        this.service = service;
    }

    // Given an API.class, generate a Typescript API client for it
    public String toTypeScript() {
        StringBuilder out = new StringBuilder("export class " + service.getSimpleName() + " {").append(System.lineSeparator());
        List<Method> methods = Arrays.stream(service.getMethods())
                .filter(m -> !m.isSynthetic())
                .toList();
        // For every method, output the fetch request (with parameters and return type) to request it from our backend.
        for (Method method : methods) {
            String methodName = method.getName();
            String returnType = TypeScriptConverter.getTsType(method.getGenericReturnType(), TYPES);
            String parameters = Arrays.stream(method.getParameters())
                            .map(param -> {
                                String tsType = TypeScriptConverter.getTsType(param.getParameterizedType(), TYPES);
                                return param.getName() + ": " + tsType;
                            }).collect(Collectors.joining(", "));
            String path = "/api/" + service.getSimpleName() + "/" + methodName;
            String bodyParameters = Arrays.stream(method.getParameters())
                            .map(param -> param.getName())
                            .collect(Collectors.joining(", "));
            // oh how I wish for string interpolation in Java...
            String typescriptMethod = String.format("""
                        %s(%s): Promise<%s> {
                            return fetch('%s', {
                                method: 'POST',
                                headers: {'Accept':'application/json', 'Content-Type': 'application/json'},
                                body: JSON.stringify({%s}, (k, v) => v === undefined ? null : v)
                            }).then(r => r.json())
                        }
                    """, methodName, parameters, returnType, path, bodyParameters);
            out.append(typescriptMethod);
        }
        out.append(("}")).append(System.lineSeparator());
        return out.toString();
    }
}
