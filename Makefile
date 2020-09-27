root=$(shell pwd)
img=jslc

clean:
	@docker image ls | grep $(img) >/dev/null && docker image rm $(img)

build:
	@docker image ls | grep $(img) >/dev/null || docker build -t $(img) .

ssh: build
	@docker run -ti --rm -v $(root):/app:delegated $(img) bash

run: build
	@docker run --rm $(img)

install:
	@mvn clean install

report:
	@mvn clean package site

ci:
	@mvn -P ci -e verify
