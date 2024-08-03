.DEFAULT_COAL := build

.PHONY: build run

build:
	./gradlew build

run:
	@if [ -z "$(OUTPUT)" ]; then \
		echo "出力先のパス($OUTPUT)が指定されていません"; \
		exit 1; \
	fi

	@if [ "$(OUTPUT)" -ne "*.ppm" ]; then \
		echo "ファイルの拡張子は.ppmのみが許可されています"; \
		exit 1; \
	fi

	./gradlew run --quiet > $(OUTPUT)

