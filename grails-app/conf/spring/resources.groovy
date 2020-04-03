package spring

import mx.tlenexkoyotl.graphql_demo.beans.GQLContextBuilder
import mx.tlenexkoyotl.graphql_demo.beans.GQLEntityNamingConvention
import mx.tlenexkoyotl.graphql_demo.beans.UserPasswordEncoderListener

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    graphQLEntityNamingConvention(GQLEntityNamingConvention)
    graphQLContextBuilder(GQLContextBuilder)
}
