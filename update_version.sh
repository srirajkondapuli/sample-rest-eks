#!/usr/bin/env sh
VERSION=`cat version.txt`
sed -i.bak "s/^version = .*/version = \"${VERSION}\"/" build.gradle.kts
