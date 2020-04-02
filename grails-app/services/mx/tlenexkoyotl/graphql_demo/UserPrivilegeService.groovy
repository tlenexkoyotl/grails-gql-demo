package mx.tlenexkoyotl.graphql_demo


import grails.gorm.transactions.Transactional

@Transactional
class UserPrivilegeService {

    User createAll(Long userId, List<Long> privilegeIds, boolean flush = false) {
        User user = User.findById userId
        List<Privilege> privileges = Privilege.findAllByIdInList(privilegeIds as List<Long>)

        UserPrivilege.createAll user, privileges, flush
    }
}
