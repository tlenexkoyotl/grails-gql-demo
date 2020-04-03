package mx.tlenexkoyotl.graphql_demo.beans

import grails.plugin.springsecurity.SpringSecurityService
import mx.tlenexkoyotl.graphql_demo.service.UserPrivilegeService
import org.grails.gorm.graphql.plugin.DefaultGraphQLContextBuilder
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User

class GQLContextBuilder extends DefaultGraphQLContextBuilder {

    @Autowired
    SpringSecurityService springSecurityService

    @Autowired
    UserPrivilegeService userPrivilegeService

    @Override
    Map buildContext(GrailsWebRequest request) {
        Map context = super.buildContext(request)
        context.springSecurityService = springSecurityService
        context.userService = userPrivilegeService
        User user = springSecurityService.getPrincipal() as User

        println "${user?.username}: \n${user?.authorities}\n"

        context
    }
}
