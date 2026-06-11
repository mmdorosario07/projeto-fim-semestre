#!/bin/bash
# Uso: ./commit.sh "mensagem do commit"

TOKEN="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"
USER="mmdorosario07"
REPO="projeto-fim-semestre"
BRANCH="main"

git add .
git commit -m "$1"
git push https://$USER:$TOKEN@github.com/$USER/$REPO.git $BRANCH
