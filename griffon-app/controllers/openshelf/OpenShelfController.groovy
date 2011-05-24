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
        //println result.text
        react(result.text)

      } catch (Exception e) {

      } finally {
        Thread.sleep(300)
      }
    }
  }


  def react = {
    String incoming ->
    java.awt.Toolkit.getDefaultToolkit().beep()
    println incoming;

    try {
      def parsed = [:]
      parse(incoming, parsed);
      //routines[parsed.key](model, parsed.value)
      view.employee.text = employee?.name
      view.book.text = book?.title;
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
    } else if (incoming.contains("title")) {
      book = gson.fromJson(incoming, Book.class)
    }
    if (employee != null && book != null) {

      /*def borrow = Borrow.findByEmployeeAndBook(employee, book)
borrow ? view.action.text = "Renew it" : "Return it"*/
    }

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

