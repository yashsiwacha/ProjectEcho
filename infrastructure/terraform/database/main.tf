# Database Security Group
resource "aws_security_group" "db" {
  name        = "${var.name_prefix}-db-sg"
  description = "Security group for RDS PostgreSQL"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    cidr_blocks     = ["10.0.0.0/16"] # Should be restricted to ECS subnets in prod
  }
}

# RDS PostgreSQL Instance
resource "aws_db_instance" "postgres" {
  identifier           = "${var.name_prefix}-postgres"
  engine               = "postgres"
  engine_version       = "16"
  instance_class       = "db.t4g.micro"
  allocated_storage    = 20
  
  db_name              = "projectecho"
  username             = "postgres"
  password             = var.db_password
  
  db_subnet_group_name   = aws_db_subnet_group.default.name
  vpc_security_group_ids = [aws_security_group.db.id]
  
  skip_final_snapshot    = true # Set to false in prod
  publicly_accessible    = false
}

resource "aws_db_subnet_group" "default" {
  name       = "${var.name_prefix}-db-subnet-group"
  subnet_ids = var.database_subnets
}

# Redis Security Group
resource "aws_security_group" "redis" {
  name        = "${var.name_prefix}-redis-sg"
  description = "Security group for ElastiCache Redis"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    cidr_blocks     = ["10.0.0.0/16"]
  }
}

# ElastiCache Redis Cluster
resource "aws_elasticache_cluster" "redis" {
  cluster_id           = "${var.name_prefix}-redis"
  engine               = "redis"
  node_type            = "cache.t4g.micro"
  num_cache_nodes      = 1
  parameter_group_name = "default.redis7"
  engine_version       = "7.1"
  port                 = 6379
  
  subnet_group_name    = aws_elasticache_subnet_group.default.name
  security_group_ids   = [aws_security_group.redis.id]
}

resource "aws_elasticache_subnet_group" "default" {
  name       = "${var.name_prefix}-redis-subnet-group"
  subnet_ids = var.database_subnets
}
