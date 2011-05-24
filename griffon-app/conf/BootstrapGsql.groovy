import groovy.sql.Sql

class BootstrapGsql {
    def init = { Sql sql ->
      def books = sql.dataSet("Books");
      books.add(title:"The Pragmatic Programmer",isbn:"8131722422",copyId:"747")
    }

    def destroy = { Sql sql ->
    }
} 
