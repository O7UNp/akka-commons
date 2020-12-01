package dev.xethh.libs.toolkits.akka.commons

import java.sql.{Connection, ResultSet}

import scala.language.implicitConversions

package object jdbcExtension {
  object ext{
    implicit def conn2Connection(connection:Connection): ConnectionExtension =new ConnectionExtension(connection)
    implicit def rs2RsUtils(rs:ResultSet): ResultSetUtils =new ResultSetUtils(rs)
  }

  import java.sql.{Connection, PreparedStatement, ResultSet}

  class ResultSetUtils(resultSet: ResultSet) {
    def nextRecord():ResultSet = {
      resultSet.next()
      resultSet
    }
    def to[T](rs: ResultSet=>T): T = {
      rs(resultSet)
    }
    def update(rs:ResultSet=>Unit): Boolean ={
      rs(resultSet)
      resultSet.rowUpdated()
    }
    def nextAsOption[T](rs: ResultSet=>T): Option[T] = {
      if(resultSet.next()){
        Some(rs(resultSet))
      }
      else{
        None
      }
    }
    def nextAs[T](rs: ResultSet=>T): T = {
      nextRecord()
      rs(resultSet)
    }
    def allTo[T](rs: ResultSet => T): Iterator[T] = {
      new Iterator[T]() {
        override def hasNext: Boolean = resultSet.next()
        override def next(): T = {
          rs(resultSet)
        }
      }
    }

  }

  class ConnectionExtension(connection: Connection){
    def prepareNoParam(sql:String) :PreparedStatement={
      val stmt = connection.prepareStatement(sql)
      stmt
    }
    def prepare(sql:String)(stmtProcess:PreparedStatement=>Unit) :PreparedStatement={
      val stmt = prepareNoParam(sql)
      stmtProcess(stmt)
      stmt
    }

    def run(process: Connection=>Unit): Unit ={
      process(connection)
      connection.close()
    }
    def call[X](process: Connection=>X): X ={
      val rs = process(connection)
      connection.close()
      rs
    }

  }
}
