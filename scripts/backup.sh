#!/bin/bash
set -e
TIMESTAMP=$(date +%s)
echo "Backing up PostgreSQL to backup_${TIMESTAMP}.sql..."
docker exec project_echo-postgres-1 pg_dump -U echo project_echo > "backup_${TIMESTAMP}.sql"
echo "Backup complete!"
