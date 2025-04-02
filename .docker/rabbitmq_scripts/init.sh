#!/bin/bash
set -e

# Crée un nouvel utilisateur
rabbitmqctl add_user myuser mypassword

# Attribue des permissions à l'utilisateur
rabbitmqctl set_permissions -p / myuser ".*" ".*" ".*"

# Active les plugins nécessaires
rabbitmq-plugins enable rabbitmq_stomp
rabbitmq-plugins enable rabbitmq_management

# Assure que l'utilisateur admin a les bonnes permissions
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"

# Lancer RabbitMQ
exec "$@"
