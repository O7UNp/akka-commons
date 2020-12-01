## Maven
```xml
        <dependency>
            <groupId>dev.xethh.libs.toolkits.akka</groupId>
            <artifactId>commons</artifactId>
            <version>1.0.1</version>
        </dependency>

```

## JDBC Extension


### JDBCConnection extension
```scala
import dev.xethh.libs.toolkits.akka.commons.jdbcExtension.ext._
import java.sql.Connection
val conn:Connection 

// call method return a value from sub scope of connection with closing connection after result occurred
val countOfRecord = conn.call{_=>    
          conn.prepareNoParam("select count(*) from Table") // Prepare statement with no parameter
              .executeQuery() // execute query to obtain result set
              .nextAs{rs=>    // nextAs pass result set in lambda as rs and get the result
                rs.getLong(1)
              }
      }

// run method run jdbc operation from sub scope with closing connection before leave
conn.run{ _=>
  // prepare method will crate prepared statement and provide sub scope for fill in parameter
  conn.prepare("Update Table set value=? where field=?"){stmt=>
    stmt.setString(1, "value")
    stmt.setString(2, "field")
  }.execute()
}
```