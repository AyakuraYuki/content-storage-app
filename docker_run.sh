#!/bin/sh
local_storage_path=/Users/$(whoami)/Library/Containers/Application/content_storage_app/data
host_port=8888

docker run --name csa -d -p ${host_port}:8888 -v ${local_storage_path}:/data/content_storage_app/data csa:latest
