/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.rest.web

import grails.artefact.Artefact
import grails.persistence.Entity
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

import org.codehaus.groovy.grails.plugins.web.mimes.MimeTypesFactoryBean

import spock.lang.Issue
import spock.lang.Specification

@TestFor(GadgetController)
@Mock(Gadget)
class RespondMethodWithEmptyCollectionSpec extends Specification{

    void setup() {
        def ga = grailsApplication
        ga.config.grails.mime.types =
            [ html: ['text/html','application/xhtml+xml'],
            xml: ['text/xml', 'application/xml'],
            text: 'text/plain',
            js: 'text/javascript',
            rss: 'application/rss+xml',
            atom: 'application/atom+xml',
            css: 'text/css',
            csv: 'text/csv',
            all: '*/*',
            json: ['application/json','text/json'],
            form: 'application/x-www-form-urlencoded',
            multipartForm: 'multipart/form-data'
        ]

        defineBeans {
            mimeTypes(MimeTypesFactoryBean) {
                grailsApplication = ga
            }
        }
    }

    @Issue('GRAILS-10683')
    void "Test responding to a REST call with an empty collection"() {

        when:"The respond method is used to render an empty collection in XML"
        response.format = 'xml'
        controller.index()

        then:"The response status should be 204"
        response.status == 204
        !model
        response.contentType == null
    }
    
    @Issue('GRAILS-10683')
    void "Test responding to an HTML call with an empty collection"() {
        when:"The respond method is used to render an empty collection in HTML"
        response.format = 'html'
        controller.index()

        then:"The response status should be 200"
        response.status == 200
    }
}

@Artefact("Controller")
class GadgetController {

    def index() {
        respond Gadget.list()
    }
}
@Entity
class Gadget {
    String title

    static constraints = {
        title blank:false
    }
}

