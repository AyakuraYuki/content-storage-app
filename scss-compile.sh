#!/usr/bin/env bash
scss ./src/main/resources/static/css/application.scss:./src/main/resources/static/css/application.css --style compressed
rm -rf ./.sass-cache/
