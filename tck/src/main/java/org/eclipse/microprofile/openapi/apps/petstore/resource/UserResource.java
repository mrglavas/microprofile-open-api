/**
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

package org.eclipse.microprofile.openapi.apps.petstore.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirements;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirementsSet;

import org.eclipse.microprofile.openapi.apps.petstore.data.UserData;
import org.eclipse.microprofile.openapi.apps.petstore.model.User;
import org.eclipse.microprofile.openapi.apps.petstore.exception.ApiException;
import org.eclipse.microprofile.openapi.apps.petstore.exception.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

@Path("/user")
@Schema(name = "/user")
@Produces({"application/json", "application/xml"})
@SecuritySchemes(
    value = {
        @SecurityScheme(
            securitySchemeName = "userApiKey",
            type = SecuritySchemeType.APIKEY,
            description = "authentication needed to create a new user profile for the store",
            apiKeyName = "createOrUpdateUserProfile1",
            in = SecuritySchemeIn.HEADER
        ),
        @SecurityScheme(
            securitySchemeName = "userBasicHttp",
            type = SecuritySchemeType.HTTP,
            description = "authentication needed to create a new user profile for the store",
            apiKeyName = "createOrUpdateUserProfile2",
            scheme = "basic"
        ),
        @SecurityScheme(
            securitySchemeName = "userBearerHttp",
            type = SecuritySchemeType.HTTP,
            description = "authentication needed to create a new user profile for the store",
            apiKeyName = "createOrUpdateUserProfile3",
            scheme = "bearer",
            bearerFormat = "JWT"
        )
    }
)
public class UserResource {
    static UserData userData = new UserData();

    @POST
    @Operation(
        method = "post",
        summary = "Create user",
        description = "This can only be done by the logged in user.",
        responses = {
            @APIResponse(
                description = "successful operation"
            )
        }
    )
    @SecurityRequirements(
        value = {
            @SecurityRequirement(
                name = "userApiKey"
            ),
            @SecurityRequirement(
                name = "userBasicHttp"
            ),
            @SecurityRequirement(
                name = "userBearerHttp"
            )
        }
    )
    public Response createUser(
        @Parameter(
            description = "Created user object",
            schema = @Schema(implementation = User.class),
            required = true
            ) User user) {
                userData.addUser(user);
                return Response.ok().entity("").build();
            }

    @POST
    @Path("/createWithArray")
    @Operation(
        method = "post",
        summary = "Creates list of users with given input array",
        responses = {
            @APIResponse(
                description = "successful operation"
            )
        }
    )
    public Response createUsersWithArrayInput(
        @Parameter(
            description = "List of user object",
            required = true
            ) User[] users) {
                for (User user : users) {
                    userData.addUser(user);
            }
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createWithList")
    @Operation(
        method = "post",
        summary = "Creates list of users with given input array",
        responses = {
            @APIResponse(
                description = "successful operation"
            )
        }
    )
    public Response createUsersWithListInput(
        @Parameter(
            description = "List of user object",
            required = true) java.util.List<User> users) {
                for (User user : users) {
                    userData.addUser(user);
                }
                return Response.ok().entity("").build();
            }

    @PUT
    @Path("/{username}")
    @Operation(
        method = "put",
        summary = "Updated user",
        description = "This can only be done by the logged in user.",
        responses = {
            @APIResponse(
                responseCode = "400",
                description = "Invalid user supplied"
            ),
            @APIResponse(
                responseCode = "404",
                description = "User not found"
            )
        }
    )
    @SecurityRequirementsSet(
        value = {
            @SecurityRequirement(
                name = "userApiKey"
            ),
            @SecurityRequirement(
                name = "userBearerHttp"
            )
        }
    )
    public Response updateUser(
        @Parameter(
            name = "username",
            description = "name that need to be deleted",
            schema = @Schema(type = "String"),
            required = true
        )
        @PathParam("username") String username,
        @Parameter(
            description = "Updated user object",
            required = true) User user) {
                userData.addUser(user);
                return Response.ok().entity("").build();
            }

    @DELETE
    @Path("/{username}")
    @Operation(
        method = "delete",
        summary = "Delete user",
        description = "This can only be done by the logged in user.",
        responses = {
            @APIResponse(
                responseCode = "400",
                description = "Invalid username supplied"
            ),
            @APIResponse(
                responseCode = "404",
                description = "User not found"
            )
        }
    )
    public Response deleteUser(
        @Parameter(
            name = "username",
            description = "The name that needs to be deleted",
            schema = @Schema(type = "String"),
            required = true
        )
        @PathParam("username") String username) {
            if (userData.removeUser(username)) {
                return Response.ok().entity("").build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

    @GET
    @Path("/{username}")
    @Operation(
        method = "get",
        summary = "Get user by user name",
        responses = {
            @APIResponse(
                responseCode = "200",
                description = "successful operation",
                content = @Content(
                    schema = @Schema(implementation = User.class)
                )
            ),
            @APIResponse(
                responseCode = "400",
                description = "Invalid username supplied",
                content = @Content(
                    schema = @Schema(implementation = User.class)
                )
            ),
            @APIResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(
                    schema = @Schema(implementation = User.class)
                )
            )
        }
    )
    public Response getUserByName(
        @Parameter(
            name = "username",
            description = "The name that needs to be fetched. Use user1 for testing.",
            schema = @Schema(type = "String"),
            required = true
        )
        @PathParam("username") String username) throws ApiException {
            User user = userData.findUserByName(username);
            if (null != user) {
                return Response.ok().entity(user).build();
            }
            else {
                throw new NotFoundException(404, "User not found");
            }
        }

    @GET
    @Path("/login")
    @Operation(
        method = "get",
        summary = "Logs user into the system",
        responses = {
            @APIResponse(
                responseCode = "200",
                description = "successful operation",
                    content = @Content(
                        schema = @Schema(implementation = String.class)
                    )
            ),
            @APIResponse(
                responseCode = "400",
                description = "Invalid username/password supplied",
                content = @Content(
                    schema = @Schema(implementation = String.class)
                )
            )
        }
    )
    public Response loginUser(
        @Parameter(
            name = "username",
            description = "The user name for login",
            schema = @Schema(type = "String"),
            required = true
        )
        @QueryParam("username") String username,
        @Parameter(
            name = "password",
            description = "The password for login in clear text",
            schema = @Schema(type = "String"),
            required = true)
        @QueryParam("password") String password) {
            return Response.ok()
            .entity("logged in user session:" + System.currentTimeMillis())
            .build();
        }

    @GET
    @Path("/logout")
    @Operation(
        method = "get",
        summary = "Logs out current logged in user session",
        responses = {
            @APIResponse(
                description = "successful operation"
            )
        }
    )
    public Response logoutUser() {
        return Response.ok().entity("").build();
    }
}