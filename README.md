To build and run the Docker image:

Save the Dockerfile in the root of your Maven project.

Open a terminal in the same directory as the Dockerfile.

Build the Docker image:
docker build -t test .


docker run -p 8080:8080 test
