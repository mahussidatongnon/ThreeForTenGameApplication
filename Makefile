# Chemins des modules
CORE_MODULE=core
AIPLAYER_MODULE=aiplayer

# Commande Gradle
GRADLE_CMD=./gradlew

# Par défaut : build + compose up
all: build docker-compose-up

# Build des JARs avec Gradle
build:
	$(GRADLE_CMD) :$(CORE_MODULE):bootJar :$(AIPLAYER_MODULE):bootJar

# Build des images Docker
docker-build:
	docker build -f $(CORE_MODULE)/Dockerfile -t threeforten-core .
	docker build -f $(AIPLAYER_MODULE)/Dockerfile -t threeforten-aiplayer .

# Lancer les conteneurs
docker-compose-up:
	docker-compose up --build

# Arrêter les conteneurs
down:
	docker-compose down

# Nettoyage complet (volumes, images optionnellement)
clean:
	$(GRADLE_CMD) clean
	docker-compose down -v --remove-orphans
	docker image prune -f

.PHONY: all build docker-build docker-compose-up down clean