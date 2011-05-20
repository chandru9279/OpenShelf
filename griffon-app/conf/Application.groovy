application {
    title = 'OpenShelf'
    startupGroups = ['OpenShelf']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "OpenShelf"
    'OpenShelf' {
        model      = 'openshelf.OpenShelfModel'
        view       = 'openshelf.OpenShelfView'
        controller = 'openshelf.OpenShelfController'
    }

}
