package com.malcolmcrum.typescriptapigenerator.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.malcolmcrum.typescriptapigenerator.demo.api.UsersApi;
import com.malcolmcrum.typescriptapigenerator.demo.services.UsersService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// A simple app that demonstrates an API described in an interface with both
// a backend implementation and a generated TypeScript client
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main( String[] args ) {
        Javalin app = Javalin.create(cfg -> {
            // The HTML and compiled JS are copied into the /resources/dist folder when you run build_frontend.sh
            cfg.addStaticFiles("frontend/src", Location.EXTERNAL);
        });
        exposeService(app, UsersApi.class, new UsersService());
        app.start();
    }

    private static <T> void exposeService(Javalin app, Class<T> api, T implementation) {
        String apiName = api.getSimpleName();
        for (Method method : api.getMethods()) {
            // handle calls to, for example, POST /api/UsersApi/getUser
            String path = "/api/" + apiName + "/" + method.getName();
            app.post(path, (ctx) -> {
                JsonNode tree = objectMapper.readTree(ctx.body());
                List<Object> args = new ArrayList<>();
                for (Parameter param : method.getParameters()) {
                    JsonNode node = tree.get(param.getName());
                    var arg = objectMapper.treeToValue(node, param.getType());
                    args.add(arg);
                }
                try {
                    var result = method.invoke(implementation, args.toArray());
                    var json = objectMapper.writeValueAsBytes(result);
                    ctx.result(json);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke " + apiName + "/" + method, e);
                }
            });
            log.info("Exposed API endpoint " + path);
        }
    }
}
