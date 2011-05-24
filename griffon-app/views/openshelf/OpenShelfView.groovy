package openshelf

import java.awt.GridLayout
import java.awt.FlowLayout
import javax.swing.BoxLayout
import groovy.swing.impl.TableLayout
import java.awt.Color

actions {
  action(id: 'captureAction',
          name: 'Capture',
          closure: controller.idling)
}

application(title: 'OpenShelf',
        pack: true,
        locationByPlatform: true,
        iconImage: imageIcon('/griffon-icon-48x48.png').image,
        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image]) {

  panel(border: emptyBorder(10)) {
    borderLayout()

    panel(visible: true) {
      boxLayout(axis: BoxLayout.Y_AXIS)
      widget(controller.player.visualComponent)
      captureButton = button captureAction
      panel(layout: new GridLayout(3, 3)) {
        label(text: 'Welcome ', enabled: true)
        employee = textArea(columns: 20,rows:3, id: 'employee', text: bind('employee', target: model),lineWrap:true)
        label(text: 'You have got the book ')
        book = textArea(columns: 20,rows:3, id: 'book', text: bind('book', target: model),lineWrap:true)
        label(text: 'and you are here to ')
        action = textArea(columns: 20, rows:3 ,id: 'action', text: bind('action', target: model),lineWrap:true)

        //employee = textField(columns: 20, id: 'employee', text: bind('employee', target: model))
        //book = textField(columns: 20, id: 'book', text: bind('book', target: model))
      }
    }
  }

}