# Bike Escape #

Mobile app for city bike races. 
API - REST services.

### BUILD: ###

<pre>
./gradlew build
</pre>

### DEPLOY ###

Copy to server (docker, docker-compose and java required):
* deploy
* create_env.sh
* docker-compose.yml
* build/libs/api.jar

<pre>
scp -r deploy/ sebastian@xx.xx.xx.xx:/home/sebastian/bike_escape_deploy &&
scp -r docker-compose.yml sebastian@xx.xx.xx.xx:/home/sebastian/bike_escape_deploy &&
scp -r build/libs/api.jar sebastian@xx.xx.xx.xx:/home/sebastian/bike_escape_deploy
</pre>

<pre>
scp -r deploy/ docker-compose.yml -r build/libs/api.jar sebastian@xx.xx.xx.xx:/home/sebastian/bike_escape_deploy
</pre>

<pre>
docker-compose up
chmod +x create_folders.sh
./create_folders.sh
java -jar api.jar
</pre>
