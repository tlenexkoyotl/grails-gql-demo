package mx.tlenexkoyotl.graphql_demo.service

import grails.gorm.transactions.Transactional
import mx.tlenexkoyotl.graphql_demo.domain.Privilege
import mx.tlenexkoyotl.graphql_demo.domain.User
import mx.tlenexkoyotl.graphql_demo.domain.UserPrivilege

@Transactional
class UserPrivilegeService extends BaseService {

    User createAll(def env) {
        Long userId = env.arguments.userId as Long
        List<Long> privilegeIds = env.arguments.privilegeIds as List<Long>

        getPrincipal()
        createAll userId, privilegeIds, true
    }

    User createAll(Long userId, List<Long> privilegeIds, boolean flush = false) {
        User user = User.findById userId
        List<Privilege> privileges = Privilege.findAllByIdInList(privilegeIds as List<Long>)

        UserPrivilege.createAll user, privileges, flush
    }

    LinkedHashMap mutateTwoDomainTypeObjects(def env) {
        Long userId = env.arguments.userId as Long
        LinkedHashMap user = env.arguments.user as LinkedHashMap
        Long privilegeId = env.arguments.privilegeId as Long
        LinkedHashMap privilege = env.arguments.privilege as LinkedHashMap

        getPrincipal()
        mutateTwoDomainTypeObjects userId, user, privilegeId, privilege
    }

    LinkedHashMap mutateTwoDomainTypeObjects(
            Long userId,
            LinkedHashMap<String, Object> updatedUser,
            Long privilegeId,
            LinkedHashMap<String, Object> updatedPrivilege
    ) {
        User user = setEntityAttributes(User.findById(userId), updatedUser) as User
        Privilege privilege = setEntityAttributes(
                Privilege.findById(privilegeId),
                updatedPrivilege
        ) as Privilege

        [user: user, privilege: privilege]
    }
}
