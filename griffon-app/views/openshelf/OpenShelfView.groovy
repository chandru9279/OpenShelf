package openshelf

import javax.media.CaptureDeviceManager
import javax.media.Manager
import java.awt.Component
import java.awt.BorderLayout
import java.awt.Button
import java.awt.Dimension

application(title: 'OpenShelf',
        //size: [520,480],
        pack: true,
        //location: [50,50],
        locationByPlatform: true,
        iconImage: imageIcon('/griffon-icon-48x48.png').image,
        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image]) {

  panel(border: emptyBorder(6)) {
    borderLayout()
    panel(visible:true) {
      widget(getCameraComponent())
    }

  }

}

def getCameraComponent() {
  def cameraName = "vfw:Microsoft WDM Image Capture (Win32):0";
  def device = CaptureDeviceManager.getDevice(cameraName);
  def locator = device.getLocator();
  player = Manager.createRealizedPlayer(locator);
  player.start();
  def component = player.getVisualComponent()
  print component
  component
}