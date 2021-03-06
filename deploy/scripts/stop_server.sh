#!/bin/bash

SERVER_NAME=dietstory-game-server

if [ "$(docker ps -aq -f name=${SERVER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=running -f name=${SERVER_NAME})" ]; then
        # stop existing server
        docker stop ${SERVER_NAME}
    fi

    # Remove container
    docker rm ${SERVER_NAME}
fi