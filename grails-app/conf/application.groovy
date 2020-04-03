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

            password {
                algorithm = 'SHA-256'

                hash {
                    iterations = 1
                }
            }

            userLookup {
                userDomainClassName = 'mx.tlenexkoyotl.graphql_demo.domain.User'
                authorityJoinClassName = 'mx.tlenexkoyotl.graphql_demo.domain.UserPrivilege'
            }

            authority { className = 'mx.tlenexkoyotl.graphql_demo.domain.Privilege' }

            filterChain {
                chainMap = [
                        [
                                pattern: '/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
                        ]
                ]
            }

            securityConfigType = "InterceptUrlMap"

            interceptUrlMap = [
                    [pattern: '/', access: ['permitAll']],
                    [pattern: '/assets/**', access: ['permitAll']],
                    [pattern: '/static/**', access: ['permitAll']],
                    [pattern: '/login/auth', access: ['ROLE_ANONYMOUS']],
                    [pattern: '/graphql/**', access: ['permitAll']],
                    [pattern: '/oauth/access_token', access: ['ROLE_ANONYMOUS']]
            ]
        }
    }
}
