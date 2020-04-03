package mx.tlenexkoyotl.graphql_demo.beans

import groovy.transform.CompileStatic
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.gorm.graphql.entity.GraphQLEntityNamingConvention
import org.grails.gorm.graphql.types.GraphQLPropertyType

@CompileStatic
class GQLEntityNamingConvention extends GraphQLEntityNamingConvention {

    /**
     * @param entity The persistent entity
     * @return The name to use. Ex: "person"
     */
    String getGet(PersistentEntity entity) {
        "get${entity.javaClass.simpleName}"
    }

    /**
     * @param entity The persistent entity
     * @return The name to use. Ex: "personList"
     */
    String getList(PersistentEntity entity) {
        "list${entity.javaClass.simpleName}"
    }

    /**
     * @param entity The persistent entity
     * @return The name to use. Ex: "personCount"
     */
    String getCount(PersistentEntity entity) {
        "count${entity.javaClass.simpleName}"
    }

    /**
     * @param entity The persistent entity
     * @return The name to use. Ex: "personCreate"
     */
    String getCreate(PersistentEntity entity) {
        "create${entity.javaClass.simpleName}"
    }

    /**
     * @param entity The persistent entity
     * @return The name to use. Ex: "personUpdate"
     */
    String getUpdate(PersistentEntity entity) {
        "update${entity.javaClass.simpleName}"
    }

    /**
     * @param entity The persistent entity
     * @return The name to use. Ex: "personDelete"
     */
    String getDelete(PersistentEntity entity) {
        "delete${entity.javaClass.simpleName}"
    }

    private static String normalizeType(GraphQLPropertyType type) {
        type.name().split('_').collect { String name ->
            name.toLowerCase().capitalize()
        }.join('').replace('Output', '')
    }

    /**
     * @param entity The persistent entity
     * @param type The property returnType
     * @return The name to use. Ex: "Person", "PersonCreate", "PersonUpdate", "PersonCreateNested"
     */
    String getType(PersistentEntity entity, GraphQLPropertyType type) {
        getType(entity.javaClass.simpleName, type)
    }

    /**
     * @param typeName The custom type name
     * @param type The property returnType
     * @return The name to use. Ex: "Person", "PersonCreate", "PersonUpdate", "PersonCreateNested"
     */
    String getType(String typeName, GraphQLPropertyType type) {
        typeName + normalizeType(type)
    }

    /**
     * @param entity The persistent entity
     * @return The name to use. Ex: "PersonPagedResult"
     */
    String getPagination(PersistentEntity entity) {
        entity.javaClass.simpleName + 'PagedResult'
    }
}
