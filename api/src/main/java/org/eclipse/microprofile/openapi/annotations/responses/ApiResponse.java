/**
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.microprofile.openapi.annotations.responses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.links.Link;
import org.eclipse.microprofile.openapi.annotations.media.Content;

/**
 * Describes a single response from an API Operation, including design-time, static links to operations based on the response.
 **/
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(ApiResponses.class)
public @interface ApiResponse {
    /**
     * A short description of the response. This is a REQUIRED property.
     * 
     * @return description of the response.
     **/
    String description() default "";

    /**
     * The HTTP response code, or 'default', for the supplied response. May only have 1 default entry.
     * 
     * @return HHTTP response code for this response instance or default
     **/
    String responseCode() default "default";

    /**
     * An array of response headers. Allows additional information to be included with response.
     * <p>
     * RFC7230 states header names are case insensitive. If a response header is defined with the name "Content-Type", it SHALL be ignored.
     * 
     * @return array of headers for this reponse instance
     **/
    Header[] headers() default {};

    /**
     * An array of operation links that can be followed from the response.
     * 
     * @return array of operation links for this response instance
     **/
    Link[] links() default {};

    /**
     * An array containing descriptions of potential response payloads, for different media types.
     * 
     * @return content of this response instance
     **/
    Content[] content() default {};

}