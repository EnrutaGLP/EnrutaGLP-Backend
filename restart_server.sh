echo Deploying the app
echo Getting PID in case it exists
server_pid=$(ps aux | grep java | awk '{print $2}' | head -n 1)
n_lines=$(ps aux | grep java | wc -l)
if [ $n_lines == 2 ];
then
	echo Killing server with its pid $server_pid
	kill -9 $server_pid
	echo Removing nohup out file
	rm nohup.out
	echo Removing jar file
	rm EnrutaGLPBackend-0.0.1-SNAPSHOT.jar
fi
echo Downloading jar file named EnrutaGLPBackend-0.0.1-SNAPSHOT.jar located in google drive
gshell download EnrutaGLPBackend-0.0.1-SNAPSHOT.jar
nohup java -jar EnrutaGLPBackend-0.0.1-SNAPSHOT.jar &
