grails {
    plugin {
        springsecurity {
            rest {
                login {
                    active = true
                    endpointUrl = '/login/auth'
                    failureStatusCode = 401
                    useJsonCredentials = true
                    usernamePropertyName = 'username'
                    passwordPropertyName = 'password'
                }

                token {
                    storage {
                        jwt {
                            secret = 'pleaseChangeThisSecretForANewOne'
                            expiration = 86400
                        }
                    }

                    validation {
                        active = true
                        headerName = 'X-Auth-Token'
                        endpointUrl = '/api/validate'
                    }
                }
            }

            securityConfigType = "InterceptUrlMap"

            filterChain {
                chainMap = [
                        [
                                pattern: '/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
                        ]
                ]
            }

            userLookup {
                userDomainClassName = 'mx.tlenexkoyotl.graphql_demo.User'
                authorityJoinClassName = 'mx.tlenexkoyotl.graphql_demo.UserPrivilege'
            }

            authority { className = 'mx.tlenexkoyotl.graphql_demo.Privilege' }

            interceptUrlMap = [
                    [pattern: '/', access: ['permitAll']],
                    [pattern: '/static/**', access: ['permitAll']],
                    [pattern: '/login/auth', access: ['ROLE_ANONYMOUS']],
                    [pattern: '/graphql/**', access: ['permitAll']],
                    [pattern: '/oauth/access_token', access: ['ROLE_ANONYMOUS']]
            ]
        }
    }
}
