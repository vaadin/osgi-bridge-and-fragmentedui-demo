# osgi-bridge-and-fragmentedui-demo #

This is a demo that:
1. shows how to run Vaadin application in OSGi container, with Vaadin artifacts deployed to the container as OSGi bundles and the application itself being OSGi bundle, more specifically Web Application Bundle (WAB)
2. provides a thin, simple example on how to have other bundles in the container contribute to the Vaadin application UI

This is only a proof of concept. In particular the demo doesn't show any best practices as for now, it's only purpose is to prove that running mutli-bundle OSGi-Vaadin applications is possible. Also, there are things that are known not to work.

## Setup ##
What you need to run the demo:
0. Use latest Java 1.8 from Oracle
1. Download and install latest Karaf 4.x (at the moment this is 4.0.0.M1)
2. (Optional, but recomended) For convenience, install `webconsole` feature in Karaf:
        # }> cd $KARAF_HOME
        # }> bin/karaf
        karaf }> feature:install webconsole
This will ease deployment of OSGi bundles to Karaf

3. Install `war` feature in Karaf
        karaf }> feature:install war

  This will install, if not yet installed:  
  * Jetty
  * Support for simple HTTP
  * Support for WAR files and Web Application Bundles

4. Deploy **patched** Vaadin core bundles.

  As Vaadin at the moment is distributed as a set of JAR files, we need to first gather them all, to be able to deploy all of them on Karaf as OSGi bundles. Also, as of version 7.3.3 vaadin-shared.jar need patching `MANIFEST.MF` because of faulty `Import-Package` statement. This will be fixed in 7.3.4 (see http://dev.vaadin.com/ticket/14618)
  1. Getting all jars  
    My way was to create a completely new maven project based on Vaadin archetype build it, and find all the Vaadin bundles in `WEB-INF/lib` of resulting web application. I copied those jars to a temporary folder

  2. Patching OSGi manifests  
    You need to patch META-INF/MANIFEST.MF files in vaadin-server and vaadin-shared bundles. Look for `Import-Package` statement and in this statement change `org.json;version="0.0.20131108.vaadin1"` to `org.json;version="0.0.20080701"`. Do this for both vaadin-server and vaadin-shared. Also, put the patched files to your local maven repository, so the build process can see the correct import statements.

  3. Deploy Vaadin bundles to Karaf.  
    This can be done in two ways (and is not specific to Vaadin bundles, all the bundles can be deployed in those two ways described here):
    * copy Vaadin bundles to `$KARAF_HOME\deploy`. Karaf will hot-deploy dropped files

    or (only available if you installed `webconsole` feature)

    * Open karaf webconsole: http://localhost:8181/system/console/bundles. By default username = password = karaf. Click `Install/Update…` button, choose Vaadin bundles to upload, click `Install or update`. There's no need to start the bundles.

5. Build and deploy demo bundles
  The demo consists of two maven projects:  
  * example – this is OSGi bridge and Vaadin WAB application
  * uifragment1 – this is bundle that contributes a UI fragment to the example application  

  First, you need to build and install example project to your local maven repository, as uifragment1 uses it as a maven dependency:  

        # }> cd $DEMO_ROOT/example
        # }> mvn clean install

  Then, you can build uifragment1 project:  

        # }> cd $DEMO_ROOT/uifragment1
        # }> mvn clean package

  Package is enough to get a deployable bundle

  Then deploy both example and uifragment1 bundles on Karaf.  
  Finally, you can start the demo. First, start example bundle (click on play button in bundle listing in web console, or use `start` command in terminal console). Open the web browser and go to http://localhost:8181/vaadin-osgi. You will see the main application window. On the left side there's navigation bar, initially empty. When you start `uifragment1` bundle a button will appear in this navigation bar. After clicking on that button, a view contributed by uiframgent1 will be displayed in the larger part of the application window. Stopping the `uifragment1` bundle will remove the view and button.

## Things known to be broken ##
1. Push. Push won't work because Karaf by default uses Jetty 9.0.7 and Atmosphere support for push used by Vaadin requires at least 9.1.x.
2. Theme compilation. This is caused by sac.jar not being deployable as OSGi bundle (no OSGi metadata in `META-INF/MANIFEST.MF` file)
