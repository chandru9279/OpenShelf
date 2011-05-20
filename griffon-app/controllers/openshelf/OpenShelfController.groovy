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

class OpenShelfController {

  def view
  def model
  def player

  OpenShelfController() {
    def cameraName = "vfw:Microsoft WDM Image Capture (Win32):0";
    def device = CaptureDeviceManager.getDevice(cameraName);
    def locator = device.getLocator();
    player = Manager.createRealizedPlayer(locator);
    player.start();
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
        react(result.text)

      } catch (Exception e) {

      } finally {
      }
    }
  }


  def react = {
    String incoming ->
    println incoming;

    try {
      def parsed = [:]
      parse(incoming, parsed);
      routines[parsed.key](model, parsed.value)
      view.employee.text = model.employee
      view.book.text = model.book
    }
    catch (exception) {
      exception.printStackTrace()
    }
  }


  def parse(String incoming, Map parsed) {
    incoming = incoming.replace("[", "").replace("]", "").trim();
    if (!incoming.contains(":")) throw new Exception("Unknown QR Code, Can't parse $incoming")
    def keyValue = incoming.split(":")
    if (keyValue.length != 2) throw new Exception("Unknown QR Code, Can't parse $incoming")
    parsed['key'] = keyValue[0]
    parsed['value'] = keyValue[1]
  }

  def routines = [
          "bid": {
            OpenShelfModel model, String value ->
            model.book = value;
          },
          "eid": {
            OpenShelfModel model, String value ->
            model.employee = value;
          }
  ]
}

