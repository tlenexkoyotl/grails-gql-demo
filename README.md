# grails-gql-demo
A short guide on how to set up a Grails GraphQL API quickly to hit the ground running

# Grails 4.0.2 + PostgreSQL 10 + GraphQL:
```bash 
grails create-app <app-name> --profile=rest-api --features=events,hibernate5,security
```

This command creates a folder containing a gradle project to act as a server (Grails app).

1. Add the following dependencies to server `build.gradle` for use of PostgreSQL

	```gradle
	// PostgreSQL
	compile group: 'org.postgresql', name: 'postgresql', version: '42.2.11'
	```

2. Add the following dependencies to server gradle for use of GraphQL

	```gradle
	// GraphQL
	compile group: 'org.grails', name: 'gorm-graphql', version: '1.0.2'
	compile group: 'org.grails.plugins', name: 'gorm-graphql', version: '1.0.2'
	```

3. Now you can just start defining gorm entities as usual, gorm-grahpql will expose basic operations as graphql queries and mutations. You can always of course set up your very own custom queries and and mutations..

4. To set up a PostgreSQL datasource, configure application.yml as follows:

	```yml
	hibernate:
	  cache:
	    queries: false
	    use_second_level_cache: false
	    use_query_cache: false	
	dataSource:
	  pooled: true
	  jmxExport: true
	  driverClassName: org.postgresql.Driver
	  username: <username>
	  password: <password>
	  dbCreate: <dbCreate>
	
	environments:
	  development:
	    dataSource:
	      dbCreate: <dev_db_create_strategy>
	      url: jdbc:postgresql://<host>:<port>/<database>
	  test:
	    dataSource:
	      dbCreate: <test_db_create_strategy>
	      url: jdbc:postgresql://<host>:<port>/<database>
	  production:
	    dataSource:
        dbCreate: <prod_db_create_strategy>
	      url: jdbc:postgresql://<host>:<port>/<database>
	      properties:
	         jmxEnabled: true
	         initialSize: 5
	         maxActive: 50
	         minIdle: 5
	         maxIdle: 25
	         maxWait: 10000
	         maxAge: 600000
	         timeBetweenEvictionRunsMillis: 5000
	         minEvictableIdleTimeMillis: 60000
	         validationQuery: SELECT 1
	         validationQueryTimeout: 3
	         validationInterval: 15000
	         testOnBorrow: true
	         testWhileIdle: true
	         testOnReturn: false
	         jdbcInterceptors: ConnectionState
	         defaultTransactionIsolation: 2 # TRANSACTION_READ_COMMITTED
	```

5. Add the following dependency to `build.gradle` for use of Spring Security REST

	```gradle
	// Spring Security
	compile group: 'org.grails.plugins', name: 'spring-security-rest', version: '3.0.0'
	```

6. Now, execute `grails compile` and then `grails s2-quickstart com.mysecurerest User Privilege` to generate user and privilege domain classes and their proper configuration inside `application.groovy`.

7. Next, set `application.groovy` as such:
	
```groovy
grails {
    plugin {
        springsecurity {
            rest {
                login {
                    active = true
                    endpointUrl = <log_in_endpoint>
                    failureStatusCode = <failure_status_code>
                    useJsonCredentials = true
                    usernamePropertyName = 'username'
                    passwordPropertyName = 'password'
                }

                token {
                    storage {
                        jwt {
                            secret = <32 char long string>
                            expiration = 86400
                        }
                    }

                    validation {
                        active = true
                        headerName = <token_validation_header_name>
                        endpointUrl = <token_validation_endpoint>
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
                userDomainClassName = <user_domain_qualified_class_name>
                authorityJoinClassName = <user_authority_join_qualified_class_name>
            }

            authority { className = <authority_domain_qualified_class_name> }

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
```
