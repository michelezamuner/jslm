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
	@mvn install

test:
	@mvn -e test jacoco:report

integration:
	@mvn verify

report:
	@mvn site

ci:
	@mvn -P ci -e clean verify