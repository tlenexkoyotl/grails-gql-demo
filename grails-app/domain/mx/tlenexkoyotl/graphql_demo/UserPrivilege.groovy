package mx.tlenexkoyotl.graphql_demo

import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.codehaus.groovy.util.HashCodeHelper
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@ToString(cache = true, includeNames = true, includePackage = false)
class UserPrivilege implements Serializable {

    private static final long serialVersionUID = 1

    User user
    Privilege privilege

    @Override
    boolean equals(other) {
        if (other instanceof UserPrivilege) {
            other.userId == user?.id && other.privilegeId == privilege?.id
        }
    }

    @Override
    int hashCode() {
        int hashCode = HashCodeHelper.initHash()
        if (user) {
            hashCode = HashCodeHelper.updateHash(hashCode, user.id)
        }
        if (privilege) {
            hashCode = HashCodeHelper.updateHash(hashCode, privilege.id)
        }
        hashCode
    }

    static UserPrivilege get(long userId, long privilegeId) {
        criteriaFor(userId, privilegeId).get()
    }

    static boolean exists(long userId, long privilegeId) {
        criteriaFor(userId, privilegeId).count()
    }

    private static DetachedCriteria criteriaFor(long userId, long privilegeId) {
        UserPrivilege.where {
            user == User.load(userId) &&
                    privilege == Privilege.load(privilegeId)
        }
    }

    static UserPrivilege create(User user, Privilege privilege, boolean flush = false) {
        def instance = new UserPrivilege(user: user, privilege: privilege)

        instance.save(flush: flush)

        instance
    }

    static User createAll(User user, List<Privilege> privileges, boolean flush = false) {
        List<UserPrivilege> userPrivileges = []

        for (privilege in privileges)
            userPrivileges.add create(user, privilege, flush)

        user
    }

    static User createAll(Long userId, List<Long> privilegeIds, boolean flush = false) {
        User user = User.findById userId
        List<Privilege> privileges = Privilege.findAllByIdInList(privilegeIds as List<Long>)
        /*String message = ''
        boolean isPrivilegesNull = !privileges || privileges?.size() < 1

        if (isPrivilegesNull)
            message += 'Wrong privilege ids. '

        if (!user)
            message += 'Wrong user id.'

        if (!message.empty)
            throw new Exception(message)*/

        createAll user, privileges, flush
    }

    static boolean remove(User u, Privilege r) {
        if (u != null && r != null) {
            UserPrivilege.where { user == u && privilege == r }.deleteAll()
        }
    }

    static int removeAll(User u) {
        u == null ? 0 : UserPrivilege.where { user == u }.deleteAll() as int
    }

    static int removeAll(Privilege r) {
        r == null ? 0 : UserPrivilege.where { privilege == r }.deleteAll() as int
    }

    static constraints = {
        user nullable: false
        privilege nullable: false, validator: { Privilege r, UserPrivilege ur ->
            if (ur.user?.id) {
                if (UserPrivilege.exists(ur.user.id, r.id)) {
                    return ['userRole.exists']
                }
            }
        }
    }

    static mapping = {
        id composite: ['user', 'privilege']
        version false
    }

    static graphql = GraphQLMapping.build {
        operations.update.enabled false

        property('user') {
            order 1
            dataFetcher { UserPrivilege userPrivilege ->
                userPrivilege.user ?: [:]
            }
        }

        property('privilege') {
            order 2
            dataFetcher { UserPrivilege userPrivilege ->
                userPrivilege.privilege ?: [:]
            }
        }

        mutation('userPrivilegesCreate', User) {
            argument('userId', Long)
            argument('privilegeIds', [Long])

            dataFetcher { env ->
                env.context.userPrivilegeService.createAll env.arguments.userId as Long,
                        env.arguments.privilegeIds as List<Long>,
                        true
            }
        }

        mutation('appendNumbers', String) {
            argument('argument1', Long) {
                defaultValue '0'
                nullable true
                description 'A numeric value to be appended first'
            }

            argument('argument2', Integer) {
                description 'A numeric value to be appended second'
            }

            dataFetcher { env ->
                "${env.arguments.argument1}${env.arguments.argument2}"
            }
        }

        mutation('greet', String) {
            argument('greeting', String) {
                defaultValue 'Hello, $name!'
                nullable true
                description 'A greeting'
            }

            argument('name', String) {
                description 'The name to be greeted'
            }

            argument('regex', String) {
                defaultValue '$name'
                nullable true
                description 'A regex to be replaced by a name'
            }

            dataFetcher { env ->
                String greeting = env.arguments.greeting as String
                String name = env.arguments.name as String
                String regex = env.arguments.regex as String

                greeting.replace regex, name
            }
        }
    }
}
