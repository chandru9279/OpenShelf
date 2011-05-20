package openshelf

import com.sun.image.codec.jpeg.JPEGEncodeParam
import com.sun.image.codec.jpeg.JPEGCodec
import com.sun.image.codec.jpeg.JPEGImageEncoder
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.Image

class Utils {

  void saveImageAsJpeg(Image img, String s) {
    BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    graphics.drawImage(img, null, null);
    FileOutputStream out = null;

    try {
      out = new FileOutputStream(s);
    } catch (java.io.FileNotFoundException io) {
      System.out.println("File Not Found");
    }

    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
    param.setQuality(1.0f, false);
    encoder.setJPEGEncodeParam(param);

    try {
      encoder.encode(image);
      out.close();
    } catch (java.io.IOException io) {
      System.out.println("IOException");
    }
  }

}
