import org.junit.Test
import com.google.gson.Gson
import openshelf.Book
import static junit.framework.Assert.assertEquals
import openshelf.Employee

class GsonTests {

  @Test
  public void testBasicGson(){
    Gson gson = new Gson()
    def json = gson.toJson(new Book(title: "The Pragmatic Programmer", isbn: "8131722422", copyId:747))
    println json
    Book book = gson.fromJson(json, Book.class)
    assertEquals("8131722422", book.isbn)
    assertEquals("747", book.copyId)
    assertEquals("The Pragmatic Programmer", book.title)
  }
  @Test
  public void testEmployeeGson(){
    Gson gson = new Gson()
    def json = gson.toJson(new Employee(name: "Prakash K", id: "12829"))
    println json
    Employee employee = gson.fromJson(json, Employee.class)
    assertEquals("12829", employee.id)
    assertEquals("Prakash K", employee.name)
  }

  @Test
  public void testEmployeeGson1(){
    Gson gson = new Gson()
    def json = gson.toJson(new Employee(name: "Chandrasekar T", id: "12811"))
    println json
    Employee employee = gson.fromJson(json, Employee.class)
    assertEquals("12829", employee.id)
    assertEquals("Prakash K", employee.name)
  }

}
