#!/bin/bash
# Uso: ./commit.sh "mensagem do commit"

TOKEN="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"
USER="mmdorosario07"
REPO="projeto-fim-semestre"
BRANCH="main"

git add .
git commit -m "$1"
git push https://$USER:$TOKEN@github.com/$USER/$REPO.git $BRANCH
