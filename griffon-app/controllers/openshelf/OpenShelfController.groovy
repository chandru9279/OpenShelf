package openshelf

import javax.media.control.FrameGrabbingControl
import javax.media.util.BufferToImage
import javax.media.format.VideoFormat

import javax.media.CaptureDeviceManager
import javax.media.Manager
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.LuminanceSource
import com.google.zxing.BinaryBitmap
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.Reader;
import com.google.zxing.MultiFormatReader
import com.google.zxing.Result
import com.google.gson.Gson

class OpenShelfController {

  def view
  def model
  def player
  def employee
  def book

  OpenShelfController() {
    def cameraName = "vfw:Microsoft WDM Image Capture (Win32):0";
    def device = CaptureDeviceManager.getDevice(cameraName);
    def locator = device.getLocator();
    player = Manager.createRealizedPlayer(locator);
    player.start();
  }

  void mvcGroupInit(Map args) {
    println 'mvc initialized..........'
    //view.captureButton.doClick()
  }

  def reactedTime = System.currentTimeMillis()

  def idling = {

    while (true) {

      FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl")
      def frameBuffer = fgc.grabFrame()
      def bufferToImageConverter = new BufferToImage((VideoFormat) frameBuffer.getFormat())
      def img = bufferToImageConverter.createImage(frameBuffer)

      try {
        LuminanceSource source = new BufferedImageLuminanceSource(img)
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source))
        Reader reader = new MultiFormatReader()
        Result result = reader.decode(bitmap)

        if (((System.currentTimeMillis() - reactedTime) / 1000) > 30)
          resetForNextTranscation()

        react(result.text)


      } catch (Exception e) {

      } finally {
        Thread.sleep(300)
      }
    }
  }


  def react = {
    String incoming ->
    reactedTime = System.currentTimeMillis();
    java.awt.Toolkit.getDefaultToolkit().beep()
    println incoming;

    try {
      def parsed = [:]
      parse(incoming, parsed);
      //routines[parsed.key](model, parsed.value)

    }
    catch (exception) {
      exception.printStackTrace()
    }
  }


  def parse(String incoming, Map parsed) {
    //incoming = incoming.replace("[", "").replace("]", "").trim();
    //if (!incoming.contains(":")) throw new Exception("Unknown QR Code, Can't parse $incoming")
    //def keyValue = incoming.split(":")
    //if (keyValue.length != 2) throw new Exception("Unknown QR Code, Can't parse $incoming")
    Gson gson = new Gson();
    if (incoming.contains("name")) {
      employee = gson.fromJson(incoming, Employee.class)
      updateEmployeeView()
    } else if (incoming.contains("title")) {
      book = gson.fromJson(incoming, Book.class)
      updateBookView()
    }
    if (employee != null && book != null) {

      withSql {sql ->
        updateBookList(sql, book)
        updateEmployeeList(sql, employee)
        createBorrowTransaction(sql, book, employee)
        Thread.sleep(2000)
        resetForNextTranscation()
      }

    }

  }

  def updateBookView() {
    view.book.text = book?.title;
  }

  def updateEmployeeView() {
    view.employee.text = employee?.name
  }

  def resetForNextTranscation() {
    resetModel();
    clearView()
  }

  private def resetModel() {
    employee = null;
    book = null
  }

  private def clearView() {
    view.action.text = ""
    view.book.text = ""
    view.employee.text = ""
  }

  private def updateEmployeeList(sql, employee) {
    def employeeRow = sql.firstRow("Select * from employees where employeeId=?", [employee.id])
    if (employeeRow == null) {
      def employees = sql.dataSet("employees")
      employees.add(employeeId: employee.id, name: employee.name)
    }
  }

  private def updateBookList(sql, book) {
    def bookRow = sql.firstRow("Select * from books where isbn=? and copyId=?", [book.isbn, book.copyId])
    if (bookRow == null) {
      def books = sql.dataSet("books")
      books.add(isbn: book.isbn, copyid: book.copyId, title: book.title)
    }
  }

  private def createBorrowTransaction(sql, book, employee) {
    def borrowRow = sql.firstRow("Select * from borrow where isbn=? and copyId=? and employeeId=?", [book.isbn, book.copyId, employee.id])
    def borrow = sql.dataSet("borrow")
    if (borrowRow == null) {
      borrow.add(employeeId: employee.id, isbn: book.isbn, copyId: book.copyId, status: "CO")
      updateActionStatus("Renew it");
    } else if (borrowRow.status.equals("CO")) {
      sql.execute("delete from borrow where isbn=? and copyId=? and employeeId=?", [book.isbn, book.copyId, employee.id])
      updateActionStatus("Return it");
    }
  }

  def updateActionStatus(String actionStatus) {
    view.action.text = actionStatus;
  }

  /*def routines = [
          "bid": {
            OpenShelfModel model, String value ->
            model.book = value;
          },
          "eid": {
            OpenShelfModel model, String value ->
            model.employee = value;
          }
  ]*/
}

