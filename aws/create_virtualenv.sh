#!/usr/bin/env bash

# pip uninstall virtualenv
# pip install virtualenv --user
python3 -m venv venv || python -m venv venv
source .venv/bin/activate
pip install -r requirements.txt