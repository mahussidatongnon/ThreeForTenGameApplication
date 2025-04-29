#!/bin/sh
set -e

QVAL_DIR="/qValues/"
BASE_URL="https://jeffdevtest44.s3.eu-west-3.amazonaws.com/ia_kotlin"

# Liste des fichiers à télécharger
#FILES="5Best.jsonl 6Best.jsonl 7Best.jsonl"
FILES="5Best.jsonl"

mkdir -p "$QVAL_DIR"

for FILE in $FILES; do
  DEST="$QVAL_DIR/$FILE"
  echo "📁 Checking $FILE..."
  curl --progress-bar -z "$DEST" -L "$BASE_URL/$FILE" -o "$DEST"
done


echo "✅ All Q-values downloaded to $QVAL_DIR"
