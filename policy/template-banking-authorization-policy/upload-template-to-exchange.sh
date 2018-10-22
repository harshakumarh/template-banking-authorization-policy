mvn prepare-package deploy:deploy-file@upload-template -f ./pom.xml -Pdeploy-to-exchange -DskipTests
