package mx.tlenexkoyotl.graphql_demo

import grails.plugin.springsecurity.SpringSecurityService
import org.grails.gorm.graphql.plugin.DefaultGraphQLContextBuilder
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired

class GraphQLContextBuilder extends DefaultGraphQLContextBuilder {

    @Autowired
    SpringSecurityService springSecurityService

    @Autowired
    UserPrivilegeService userPrivilegeService

    @Override
    Map buildContext(GrailsWebRequest request) {
        org.springframework.security.core.userdetails.User user =
                springSecurityService.getPrincipal() as org.springframework.security.core.userdetails.User

        println "${user?.username}: \n${user?.authorities}\n"

        Map context = super.buildContext(request)
        context.springSecurityService = springSecurityService
        context.userPrivilegeService = userPrivilegeService

        context
    }
}
