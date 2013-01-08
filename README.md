# Evidence
This repository contains sourecode for application evidence.

## Build process
Checkout mvp vaadin addon framework
> git clone git://github.com/mbocek/org.vaadin.mvp.git

Build and deploy it
> mvn clean install

Checkout evidence source code base
> git clone git://github.com/mbocek/evidence.git

Build database (database module)
>  mvn clean process-resources liquibase:update

Build application and run it (application module)
> mvn clean install jetty:run

Build application and enable remote debugging
> mvnDebug clean install jetty:run


