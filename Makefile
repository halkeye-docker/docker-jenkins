all:
	docker build --build-arg JENKINS_VERSION=$(shell curl -qLs https://updates.jenkins.io/stable/latestCore.txt) -t halkeye/jenkins:latest .
