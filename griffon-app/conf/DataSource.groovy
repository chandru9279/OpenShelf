dataSource {
    pooled = false
    driverClassName = "com.mysql.jdbc.Driver"
    username = "prakash"
    password = "password"
    tokenizeddl = true // set this to true if using MySQL or any other
                        // RDBMS that requires execution of DDL statements
                        // on separate calls
}
pool {
    maxWait = 60000
    maxIdle = 5
    maxActive = 8
}
environments {
    development {
        dataSource {
            dbCreate = "create" // one of ['create', 'skip']
            url = "jdbc:mysql://localhost:3306/openshelf"
        }
    }
    test {
        dataSource {
            dbCreate = "create"
            url = "jdbc:mysql://localhost:3306/openshelf"
        }
    }
    production {
        dataSource {
            dbCreate = "skip"
            url = "jdbc:mysql://localhost:3306/openshelf"
        }
    }
}
