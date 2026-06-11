#!/bin/bash
# Uso: ./commit.sh "mensagem do commit"

TOKEN="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"
USER="mmdorosario07"
REPO="projeto-fim-semestre"
BRANCH="main"

git add .
git commit -m "$1"
git push https://$USER:$TOKEN@github.com/$USER/$REPO.git $BRANCH
