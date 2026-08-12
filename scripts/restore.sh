#!/bin/bash
set -e
if [ -z "$1" ]; then
  echo "Usage: $0 <backup_file.sql>"
  exit 1
fi
echo "Restoring $1 to PostgreSQL..."
cat "$1" | docker exec -i project_echo-postgres-1 psql -U echo project_echo
echo "Restore complete!"
