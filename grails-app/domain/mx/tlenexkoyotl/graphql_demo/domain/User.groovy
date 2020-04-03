package mx.tlenexkoyotl.graphql_demo.domain

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.fetcher.impl.ClosureDataFetchingEnvironment

@GrailsCompileStatic
@EqualsAndHashCode(includes = 'username')
@ToString(includes = 'username', includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Privilege> getAuthorities() {
        (UserPrivilege.findAllByUser(this) as List<UserPrivilege>)*.privilege as Set<Privilege>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
        table 'user_model'
        password column: '`password`'
    }

    static graphql = GraphQLMapping.build {
        property 'id', order: 1, input: false
        property 'username', order: 2
        property 'password', order: 3, output: false
        property 'enabled', order: 4
        property 'accountExpired', order: 5
        property 'accountLocked', order: 6
        property 'passwordExpired', order: 7

        add('privileges', [Privilege]) {
            order 8
            input false
            dataFetcher { User user, ClosureDataFetchingEnvironment env ->
                user.getAuthorities()
            }
        }
    }
}
