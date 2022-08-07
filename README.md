# Java/Typescript Client Generation

This repo demonstrates some simple hand-rolled tooling to bind an interface
to a backend implementation, and generate a Typescript client for the API.

The demonstration is left as limited as possible - e.g. no build step beyond `tsc`

## Usage

First, run `npm install` in `frontend/` to install Typescript.

1. Generate the API with `./build_api.sh` (generates `target/ts/api.ts` and `target/ts/types.ts`)
2. Build the frontend with `./build_frontend.sh` (compiles TypeScript in `frontend/src`)
3. Run the server with `./run_server.sh` (runs Javalin server on localhost:8080)

## Details

* The API is defined in `com.malcolmcrum.typescriptapigenerator.demo.api`, e.g.
  [UsersApi](src/main/java/com/malcolmcrum/typescriptapigenerator/demo/api/UsersApi.java)
* Implementation is in `com.malcolmcrum.typescriptapigenerator.demo.services`, e.g.
  [UsersService](src/main/java/com/malcolmcrum/typescriptapigenerator/demo/services/UsersService.java)
* API endpoints are exposed for each service in
  [com.malcolmcrum.typescriptapigenerator.demo.App.exposeService](src/main/java/com/malcolmcrum/typescriptapigenerator/demo/App.java#L33)
* Classes shared between the frontend and backend are in `com.malcolmcrum.typescriptapigenerator.demo.dtos`, e.g.
  [UserDto](src/main/java/com/malcolmcrum/typescriptapigenerator/demo/dtos/UserDto.java)
* These shared classes are translated into TypeScript with
[typescript-generator](https://github.com/vojtechhabarta/typescript-generator)
* The API client is generated in
  [TypeScriptApiGenerator.java](src/main/java/com/malcolmcrum/typescriptapigenerator/typescriptgenerator/TypeScriptConverter.java)
* An extremely simple user of the client is in `frontend/src`. If you build and
  run using the scripts mentioned above, Javalin will serve files from this folder
  which you can view with your browser to try out the API.

If you'd like to read more on this topic, please
[read the blog post](https://malcolmcrum.com/blog/2022/07/30/hand-rolled-api-generator.html).