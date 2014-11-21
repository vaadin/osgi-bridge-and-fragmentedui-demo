# osgi-bridge-and-fragmentedui-demo #

This is a demo that:
1. shows how to run Vaadin application in OSGi container, with Vaadin artifacts deployed to the container as OSGi bundles and the application itself being OSGi bundle, more specifically Web Application Bundle (WAB)
2. provides a thin, simple example on how to have other bundles in the container contribute to the Vaadin application UI

This is only a proof of concept. In particular the demo doesn't show any best practices as for now, it's only purpose is to prove that running mutli-bundle OSGi-Vaadin applications is possible. Also, there are things that are known not to work.

## Setup ##
What you need to run the demo:
0. Use latest Java 1.8 from Oracle
1. [Download](http://karaf.apache.org/index/community/download.html) and install latest Karaf 4.x (at the moment this is 4.0.0.M1)
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

4. Deploy Vaadin core bundles.

  1. Getting Vaadin
    [Download vaadin-all-7.x.x.zip](https://vaadin.com/download) (use 7.3.5 or newer).

  2. Deploy Vaadin to Karaf.

    You will want to deploy the following bundles:
    * vaadin-client-compiled-7.3.5.jar
    * vaadin-server-7.3.5.jar
    * vaadin-shared-7.3.5.jar 
    * vaadin-themes-7.3.5.jar
    * lib/jsoup-1.6.3.jar
    * lib/json-0.0.20080701.jar
    * lib/guava-16.0.1.vaadin1.jar
    * lib/streamhtmlparser-jsilver-0.0.10.vaadin1.jar
    * lib/flute-1.3.0.gg2.jar

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

  Then deploy both uifragment1/target/uifragment1-0.0.1.jar and example/target/example-0.0.1.war to Karaf in the same way as described above. 
  Finally, you can start the demo. First, start example bundle (click on play button in bundle listing in web console, or use `start` command in terminal console). Open the web browser and go to http://localhost:8181/vaadin-osgi. You will see the main application window. On the left side there's navigation bar, initially empty. When you start `uifragment1` bundle a button will appear in this navigation bar. After clicking on that button, a view contributed by uiframgent1 will be displayed in the larger part of the application window. Stopping the `uifragment1` bundle will remove the view and button.

## Things known to be broken ##
1. Push. Push won't work because Karaf by default uses Jetty 9.0.7 and Atmosphere support for push used by Vaadin requires at least 9.1.x.
2. Theme compilation. This is caused by sac.jar not being deployable as OSGi bundle (no OSGi metadata in `META-INF/MANIFEST.MF` file)
