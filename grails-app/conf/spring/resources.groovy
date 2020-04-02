package spring

import mx.tlenexkoyotl.graphql_demo.GraphQLContextBuilder
import mx.tlenexkoyotl.graphql_demo.UserPasswordEncoderListener

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    graphQLContextBuilder(GraphQLContextBuilder)
}
