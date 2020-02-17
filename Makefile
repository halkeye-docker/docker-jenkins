.DEFAULT_GOAL := build

NAME = jenkins
TAGNAME = halkeye/$(NAME)
VERSION = latest

build: ## Build docker image
	docker build -t $(TAGNAME):$(VERSION) .

push: ## push to docker hub
	docker push $(TAGNAME):$(VERSION)

run:
	docker run -it --name $(NAME) -p 3000:8080 $(TAGNAME):$(VERSION)

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
