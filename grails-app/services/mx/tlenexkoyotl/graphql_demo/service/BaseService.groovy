package mx.tlenexkoyotl.graphql_demo.service

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.core.userdetails.User

@Transactional
class BaseService {

    SpringSecurityService springSecurityService

    User getPrincipal() {
        User user = springSecurityService.getPrincipal() as User

        println "${user?.username}: \n${user?.authorities}\n"

        return user
    }

    static def setEntityAttributes(def entity, LinkedHashMap<String, Object> attributes) {
        if (entity != null)
            attributes.each { String attribute, value -> entity[attribute] = value }

        entity
    }
}
