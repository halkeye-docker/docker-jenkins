.DEFAULT_GOAL := help

build: ## Build docker image
	docker build --build-arg JENKINS_VERSION=$(shell curl -qLs https://updates.jenkins.io/stable/latestCore.txt) -t halkeye/jenkins:latest .

push: ## push to docker hub
	docker push halkeye/jenkins:latest

run:
	docker run -it -p 3000:8080 halkeye/jenkins:latest

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
