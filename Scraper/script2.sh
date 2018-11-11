#!/usr/bin/env bash
java Soar2JSON > collegescheduler.html
node fetchdata.js
rm -f 'collegescheduler.html'
