package openshelf

import java.awt.Color

actions {
    action(id: 'captureAction',
        name: 'Capture',
        closure: controller.idling)
}

application(title: 'OpenShelf',
        //size: [520,480],
        pack: true,
        //location: [50,50],
        locationByPlatform: true,
        iconImage: imageIcon('/griffon-icon-48x48.png').image,
        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                imageIcon('/griffon-icon-32x32.png').image,
                imageIcon('/griffon-icon-16x16.png').image]) {

  panel(border: emptyBorder(10) ) {
    borderLayout()
    panel(visible:true) {
      widget(controller.player.visualComponent)
      button captureAction
      employee=textField(columns:20, id:'employee',text:bind('employee',target:model))
      book=textField(columns:20, id:'book',text:bind('book',target:model))
      action=textField(columns:20, id:'action',text:bind('action',target:model))
      errorMessage=textField(columns:20, id:'errorMessage',text:bind('errorMessage',target:model))
    }
  }

}