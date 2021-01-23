.DEFAULT_GOAL := build

NAME = jenkins
TAGNAME = halkeye/$(NAME)
VERSION = $(shell git log -n 1 --pretty=format:'%h')

build: ## Build docker image
	docker build -t $(TAGNAME):$(VERSION) .

push: ## push to docker hub
	docker push $(TAGNAME):$(VERSION)

run:
	docker run --rm -it --name $(NAME) -p 3000:8080 $(TAGNAME):$(VERSION)

bash:
	docker run --rm -it --name $(NAME) --entrypoint="" $(TAGNAME):$(VERSION) sh


.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
